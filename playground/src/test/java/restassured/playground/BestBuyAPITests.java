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

	@Test(dataProvider = "dataProviderForIterations")
	public void getProducts(Hashtable<String , String> data, Method m) throws IOException, GeneralSecurityException {
		
		Response response=	given().log().all()
				.get(data.get("RequestURI"));

		//Asserting status code
		response.then().statusCode(200);
		
		List<Object> outputData =Arrays.asList(m.getName(),data.get("RequestURI"),response.statusCode(),"Passed");
		
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
