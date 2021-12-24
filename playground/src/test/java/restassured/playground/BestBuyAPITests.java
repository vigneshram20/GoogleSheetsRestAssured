package restassured.playground;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class BestBuyAPITests extends BaseTest{
	
	/**
	 * Prerequisite :
	 * 
	 ------BEST BUY API PLAYGROUND:
	 
	 1) Clone BestBuy API Playground from the below github URI 
	      https://github.com/BestBuy/api-playground
	 2) Install NodeJS v4 and above in your system
	 3) Navigate to the api-playground project folder and execute the below commands in command prompt
	 4)   cd api-playground
		  npm install
		  npm start
	 5) Now your api playground will be started running in the http://localhost:3030
	 
	 -----STEPS TO CREATE GOOGLE DEVELOPER SERVICE ACCOUNT:
	 
	 1) Navigate to https://console.developers.google.com/
	 2)  Create a New Project
	 3) Navigate to Credentials
     4) Create New Service Account
     5) Download the client secret.json and place in JAVA project root folder
     
     -----STEPS TO CREATE AND SHARE THE SPREADSHEET TO THE SERVICE ACCOUNT:
     
     1) Create a spreadsheet in our primary account.(your own account)
     2) Share the spreadsheet with the service account we just created (service account id we have created in the previous step)
     3) Copy the id of the spreadsheet from the URL.
     
     
	 
	 */

	@Test(dataProvider = "dataProviderForIterations")
	public void getProducts(Hashtable<String , String> data, Method m) throws IOException, GeneralSecurityException {
		
		Response response=	given().log().all()
				.get(data.get("EndPoint"));

		//Asserting status code
		response.then().statusCode(200);
		
		List<Object> outputData =Arrays.asList(m.getName(),data.get("EndPoint"),response.statusCode(),"Passed");
		
		new GoogleSheets().writeDataGoogleSheets(dateFormatted,outputData);
		
	}
	
	@DataProvider(name="dataProviderForIterations",parallel=false)
	public static Object[][] supplyDataForIterations(Method m) throws Exception{
		return getDataForDataprovider(m.getName());
		
		
		
	}

	private static Object[][] getDataForDataprovider(String testcasename) throws Exception {
		GoogleSheets.getSpreadSheetValues();
		int totalcolumns=GoogleSheets.getLastColumnNum();
		ArrayList<Integer> rowscount=getNumberofIterationsForATestCase(testcasename);
		Object[][] b=new Object[rowscount.size()][1];
		Hashtable<String,String> table =null;
		for(int i=1;i<=rowscount.size();i++) {
			table=new Hashtable<String,String>();
			for(int j=0;j<totalcolumns;j++){
				table.put(GoogleSheets.getCellContent(0, j), GoogleSheets.getCellContent(rowscount.get(i-1), j));
				b[i-1][0]=table;
			}
		}
		return b;
	}

	private static ArrayList<Integer> getNumberofIterationsForATestCase(String testcasename) throws IOException {
		ArrayList<Integer> a=new ArrayList<Integer>();
		int totalRows = GoogleSheets.getRows();
		for(int i=1;i < totalRows;i++) {
			String cellContent = GoogleSheets.getCellContent(i, 0);
			if(testcasename.equalsIgnoreCase(cellContent)) {
				String IsExecutable = GoogleSheets.getCellContent(i, 1);
				if(IsExecutable.equalsIgnoreCase("Yes")) {
					a.add(i);
				}
			}
		}

		return a;
	}
	


}
