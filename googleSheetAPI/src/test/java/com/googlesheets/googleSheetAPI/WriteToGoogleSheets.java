package com.googlesheets.googleSheetAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class WriteToGoogleSheets {

	static Sheets.Spreadsheets spreadsheets;
	static GoogleCredential credential;
	static final String existingSpreadSheetID = "1KxQR7tnftVTu_uN08bSd4y3cGUYBqoi_Rd76MwKT9AE";
	static final String credentialPath = "./decent-micron-307707-e0dd7d6f13d8.json";

	public static void main(String[] args) throws IOException, GeneralSecurityException, InterruptedException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyyyy HH.mm.ss");
		Date date = new Date();
		String dateFormatted = formatter.format(date);
		
		
		  //Create Sheet 
		new WriteToGoogleSheets().createSheetAndColumn(dateFormatted);
		  
		  //Enter Data 
		Long i = (long) 0; while (i < 5) {
		  
		  new WriteToGoogleSheets().writeGoogleSheets(dateFormatted, "Test", i, i +
		  10); i++; Thread.sleep(1000); }
		 
		
		//Retrieve Entered Data based on the range
		List<List<Object>> dataOP = new WriteToGoogleSheets().getSpreadsheetValues(existingSpreadSheetID,"A1:C6");
		for(List<Object> ab : dataOP)
		{
			System.out.println(ab.iterator().next());
		}
	}

	public void createSheetAndColumn(String sheetName) throws IOException, GeneralSecurityException {
		if (!sheetName.equals("")) {
			authorize(credentialPath);
			getSpreadsheetInstance();
			createNewSheet(existingSpreadSheetID, sheetName);
			List<Object> innerList = new ArrayList<Object>();
			innerList.add("Row 1");
			//writeSheet(innerList, sheetName + "!A1");
			List<Object> innerList2 = new ArrayList<Object>();
			innerList2.add("Column1");
			innerList2.add("Column2");
			innerList2.add("Column3");
			writeSheet(innerList2, sheetName + "!A1");
		}
	}

	public void writeGoogleSheets(String sheetName, String pageName, Long domLoad, Long pageLoad) throws IOException {
		int nextRow = getRows(sheetName) + 1;
		List<Object> innerList2 = new ArrayList<Object>();
		innerList2.add(pageName);
		innerList2.add(domLoad);
		innerList2.add(pageLoad);
		writeSheet(innerList2, sheetName + "!A" + nextRow);
	}

	public int getRows(String sheetName) throws IOException {

		ValueRange result = spreadsheets.values().get(existingSpreadSheetID, sheetName).execute();
		int numRows = result.getValues() != null ? result.getValues().size() : 0;
		System.out.printf("%d rows retrieved.\n", numRows);
		return numRows;
	}

	public void authorize(String credentialPath) throws IOException {
		credential = GoogleCredential.fromStream(Files.newInputStream(Paths.get(credentialPath)))
				.createScoped(Arrays.asList(SheetsScopes.DRIVE));
	}

	public void getSpreadsheetInstance() throws GeneralSecurityException, IOException {
		spreadsheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), credential).setApplicationName("My Project 79975").build()
				.spreadsheets();
	}

	public List<List<Object>> getSpreadsheetValues(String sheetID, String range) throws IOException, InterruptedException {
		/*
		 * return spreadsheets.values().get(sheetID, range).setMajorDimension("ROWS")
		 * .setValueRenderOption("UNFORMATTED_VALUE").setDateTimeRenderOption(
		 * "FORMATTED_STRING").execute() .getValues();
		 */
		ValueRange response = spreadsheets.values()
                .get(sheetID, range)
                .execute();
		Thread.sleep(12);
        return response.getValues();
		/*
		 * if (values == null || values.isEmpty()) {
		 * System.out.println("No data found."); } else {
		 * System.out.println("Name, Major"); for (List row : values) { // Print columns
		 * A and E, which correspond to indices 0 and 4. System.out.printf("%s, %s\n",
		 * row.get(0), row.get(4)); } }
		 */
	
	}

	public void createNewSheet(String existingSpreadSheetID, String newsheetTitle) throws IOException {
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

	public void writeSheet(List<Object> inputData, String sheetAndRange) throws IOException {

		List<List<Object>> values = Arrays.asList(inputData);
		ValueRange body = new ValueRange().setValues(values);
		UpdateValuesResponse result = spreadsheets.values().update(existingSpreadSheetID, sheetAndRange, body)
				.setValueInputOption("USER_ENTERED").execute();
		System.out.printf("%d cells updated.\n", result.getUpdatedCells());
	}

}
