package com.googlesheets.googleSheetAPI;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
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
	
	public static void main(String[] args) throws IOException, GeneralSecurityException
	{
		GoogleCredential credential = GoogleCredential.fromStream(Files.newInputStream(Paths.get("C:\\Users\\91720\\Downloads\\secret.json")))
				.createScoped(Arrays.asList(SheetsScopes.DRIVE));
		
		Sheets.Spreadsheets spreadsheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("A")
                .build()
                .spreadsheets();
		
		List<List<Object>> sheet = spreadsheets.values().get("1-i_zvIBXbDCbqElhh-o-GcmsXTHByyidzQ_cX6rOOiY", "Sheet1!a1:a2")
                .setMajorDimension("ROWS")
                .setValueRenderOption("UNFORMATTED_VALUE")
                .setDateTimeRenderOption("FORMATTED_STRING")
                .execute()
                .getValues();  

		for (List row : sheet) {

            // Print columns A and E, which correspond to indices 0 and 4.

           // System.out.printf("%s, %s\n", row.get(0));
			System.out.println("AA");

          }
		
		 //Set sheet name
        //Can be any string, I chose to set it to the account name
        //String sheetName = mCredential.getSelectedAccountName();

        //Create a new AddSheetRequest
        AddSheetRequest addSheetRequest = new AddSheetRequest();
        SheetProperties sheetProperties = new SheetProperties();

        //Add the sheetName to the sheetProperties
        addSheetRequest.setProperties(sheetProperties);
        addSheetRequest.setProperties(sheetProperties.setTitle("Hi"));

        //Create batchUpdateSpreadsheetRequest
        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();

        //Create requestList and set it on the batchUpdateSpreadsheetRequest
        List<Request> requestsList = new ArrayList<Request>();
        batchUpdateSpreadsheetRequest.setRequests(requestsList);

        //Create a new request with containing the addSheetRequest and add it to the requestList
        Request request = new Request();
        request.setAddSheet(addSheetRequest);
        requestsList.add(request);

        //Add the requestList to the batchUpdateSpreadsheetRequest
        batchUpdateSpreadsheetRequest.setRequests(requestsList);

        //Call the sheets API to execute the batchUpdate
        spreadsheets.batchUpdate("1-i_zvIBXbDCbqElhh-o-GcmsXTHByyidzQ_cX6rOOiY",batchUpdateSpreadsheetRequest).execute();
		
		/*// The spreadsheet to apply the updates to.
	    String spreadsheetId = "1-i_zvIBXbDCbqElhh-o-GcmsXTHByyidzQ_cX6rOOiY"; // TODO: Update placeholder value.

	    // A list of updates to apply to the spreadsheet.
	    // Requests will be applied in the order they are specified.
	    // If any request is not valid, no requests will be applied.
	    List<Request> requests = new ArrayList<Request>(); // TODO: Update placeholder value.
	    
	    Request req = new Request();
	    req.getAddSheet();
	    req.setAddSheet(addSheet)

	    requests.add(req);
	    //requests.add("")
	    // TODO: Assign values to desired fields of `requestBody`:
	    BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
	    requestBody.setRequests(requests);

	    Sheets.Spreadsheets.BatchUpdate request =
	    		spreadsheets.batchUpdate(spreadsheetId, requestBody);*/
		
		/*Spreadsheet requestBody = new Spreadsheet();
	    Sheets.Spreadsheets.Create request = spreadsheets.create(requestBody);

	    Spreadsheet response = request.execute();*/
		
		/*Spreadsheet spreadSheet = new Spreadsheet().setProperties(
			      new SpreadsheetProperties().setTitle("My Spreadsheet"));
			    //Spreadsheet result = sheetsService.spreadsheets().create(spreadSheet).execute();
		spreadsheets.create(spreadSheet).execute();*/
		
		//System.out.println(spreadsheets.get);
			        
			    //assertThat(spreadsheets1.getSpreadsheetId()).isNotNull();  
            // Print columns A and E, which correspond to indices 0 and 4.
		System.out.println("AA");
		
	}

}
