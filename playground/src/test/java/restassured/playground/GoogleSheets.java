package restassured.playground;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheets {

	static Sheets.Spreadsheets spreadsheets;
	static GoogleCredential credential;
	static final String existingSpreadSheetID = "1KxQR7tnftVTu_uN08bSd4y3cGUYBqoi_Rd76MwKT9AE";
	static final String credentialPath = "./decent-micron-307707-e0dd7d6f13d8.json";
	static ValueRange response;
	static String testDataSheetName = "TestData";
	static List<List<Object>> values ;

	public void createSheetAndColumn(String sheetName, List<Object> columnNames) throws IOException, GeneralSecurityException {
		if (!sheetName.equals("")) {
			
			createNewSheet(existingSpreadSheetID, sheetName);
			writeSheet(columnNames, sheetName + "!A1");
		}
	}

	public void writeDataGoogleSheets(String sheetName,List<Object> data) throws IOException {
		int nextRow = getRows(sheetName) + 1;
		writeSheet(data, sheetName + "!A" + nextRow);
	}

	public static int getRows(String sheetName) throws IOException {
		
		List<List<Object>> values = spreadsheets.values().get(existingSpreadSheetID, sheetName)
                .execute().getValues();
		int numRows = values != null ? values.size() : 0;
		System.out.printf("%d rows retrieved. in '"+testDataSheetName+"'\n", numRows);
		return numRows;
	}
	
	public static int getRows() throws IOException {
		
		List<List<Object>> values = spreadsheets.values().get(existingSpreadSheetID, testDataSheetName)
                .execute().getValues();
		int numRows = values != null ? values.size() : 0;
		System.out.printf("%d rows retrieved. in '"+testDataSheetName+"'\n", numRows);
		return numRows;
	}
	
	public static void getSpreadSheetValues(String sheetName) throws IOException
	{
		values = spreadsheets.values().get(existingSpreadSheetID, sheetName)
                .execute().getValues();
	}
	
	public static void getSpreadSheetValues() throws IOException
	{
		values = spreadsheets.values().get(existingSpreadSheetID, testDataSheetName)
                .execute().getValues();
	}
	
	
	public static String getCellContent(int rownum,int colnum) throws IOException {
		return values.get(rownum).get(colnum).toString();
	}
	
	
	public static int getLastColumnNum() throws IOException,Exception
	{
			
		return values.get(0).size();

	}

	public static void authorize() throws IOException {
		credential = GoogleCredential.fromStream(Files.newInputStream(Paths.get(credentialPath)))
				.createScoped(Arrays.asList(SheetsScopes.DRIVE));
	}

	public static void getSpreadsheetInstance() throws GeneralSecurityException, IOException {
		spreadsheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), credential).setApplicationName("My Project 79975").build()
				.spreadsheets();
	}

	public static void getSpreadsheetValuesFromRange(String sheetID, String range) throws IOException, InterruptedException {

		values = spreadsheets.values()
                .get(sheetID, range)
                .execute().getValues();
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
