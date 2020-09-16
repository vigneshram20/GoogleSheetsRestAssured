package com.googlesheets.googleSheetAPI;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Data;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets;
import com.google.api.services.sheets.v4.SheetsRequestInitializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FromInternet {

	static Sheets.Spreadsheets spreadsheets;
	static GoogleCredential credential;
	static final String existingSpreadSheetID = "1vIKnhokR94w6cur7PTf4I_y45xXwTDesuGHJixHk_M0";

	public static void main(String[] args) throws IOException, GeneralSecurityException {

		authorize("C:\\Users\\vigneshram_s\\Downloads\\selenium-project-289514-438b4a0157eb.json");
		getSpreadsheetInstance();
		//createNewSheet(existingSpreadSheetID, "Hi12");
		//writevaluesinSheetType1(existingSpreadSheetID);		
		//writeSomething();
		//getRows();
		System.out.println("AA");

	}

	public static void authorize(String credentialPath) throws IOException {
		credential = GoogleCredential.fromStream(Files.newInputStream(Paths.get(credentialPath)))
				.createScoped(Arrays.asList(SheetsScopes.DRIVE));
	}

	public static void getSpreadsheetInstance() throws GeneralSecurityException, IOException {
		spreadsheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), credential).setApplicationName("A").build().spreadsheets();
	}

	public static List<List<Object>> getSpreadsheetValues(String sheetID, String range) throws IOException {
		return spreadsheets.values().get(sheetID, range).setMajorDimension("ROWS")
				.setValueRenderOption("UNFORMATTED_VALUE").setDateTimeRenderOption("FORMATTED_STRING").execute()
				.getValues();
	}

	public static void createNewSheet(String existingSpreadSheetID, String newsheetTitle) throws IOException {
		// Create a new AddSheetRequest
		AddSheetRequest addSheetRequest = new AddSheetRequest();
		SheetProperties sheetProperties = new SheetProperties();
		sheetProperties.setIndex(0);

		// Add the sheetName to the sheetProperties
		addSheetRequest.setProperties(sheetProperties);
		addSheetRequest.setProperties(sheetProperties.setTitle(newsheetTitle));

		// Create batchUpdateSpreadsheetRequest
		BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();

		// Create requestList and set it on the batchUpdateSpreadsheetRequest
		List<Request> requestsList = new ArrayList<Request>();
		batchUpdateSpreadsheetRequest.setRequests(requestsList);

		// Create a new request with containing the addSheetRequest and add it to the
		// requestList
		Request request = new Request();
		request.setAddSheet(addSheetRequest);
		requestsList.add(request);

		// Add the requestList to the batchUpdateSpreadsheetRequest
		batchUpdateSpreadsheetRequest.setRequests(requestsList);

		// Call the sheets API to execute the batchUpdate
		spreadsheets.batchUpdate(existingSpreadSheetID, batchUpdateSpreadsheetRequest).execute();
	}

	public static void writevaluesinSheetType1(String existingSpreadSheetID) throws IOException {

		Request req = new Request();
		List<Request> requests = new ArrayList<Request>();
		requests.add(req);
		
		ExtendedValue extendedValue = new ExtendedValue();
		extendedValue.setStringValue("Hello World");
		
		CellData cellData = new CellData();
		cellData.setUserEnteredValue(extendedValue);
		
		List<CellData> values = new ArrayList<CellData>();
		values.add(cellData);
		
		RowData data = new RowData();
		data.setValues(values);

		GridCoordinate grids = new GridCoordinate();
		grids.setSheetId(0);
		grids.setRowIndex(1);
		grids.setColumnIndex(1);
		
		UpdateCellsRequest updateCellReq = new UpdateCellsRequest();
		updateCellReq.setStart(grids);
		updateCellReq.setFields("userEnteredValue,userEnteredFormat.backgroundColor");
		req.setUpdateCells(updateCellReq);
		

		List<RowData> rowData = new ArrayList<RowData>();
		rowData.add(data);
		updateCellReq.setRows(rowData);
		
		requests.add(req);

		BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest().
				setRequests(requests);
		spreadsheets.batchUpdate(existingSpreadSheetID, batchUpdateRequest).execute();

	}
	
	public static void writeSomething(List<Object> inputData) throws IOException {

		/*List<Object> innerList = new ArrayList<Object>();
		innerList.add("aaaaaaaaa");
	*/
		List<List<Object>> values = Arrays.asList(inputData);
		ValueRange body = new ValueRange()
		        .setValues(values);
		UpdateValuesResponse result =
		        spreadsheets.values().update(existingSpreadSheetID, "Hi1!A1", body)
		                .setValueInputOption("RAW")
		                .execute();
		System.out.printf("%d cells updated.", result.getUpdatedCells());

	   
	}
	
	public static void getRows() throws IOException
	{
		ValueRange result = spreadsheets.values().get(existingSpreadSheetID, "Sheet1").execute();
		int numRows = result.getValues() != null ? result.getValues().size() : 0;
		System.out.printf("%d rows retrieved.", numRows);
	}
}
