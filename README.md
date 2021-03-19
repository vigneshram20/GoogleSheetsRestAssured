#Steps to create google developer service account  

1.Navigate to https://console.developers.google.com/ 

2.Create a New Project 

3.Navigate to Credentials 

4.Create New Service Account 

5.Download the client secret.json and place in JAVA project root folder 

#Steps to create and share the spreadsheet to the service account 

1.Create a spreadsheet in our primary account. 

2.Share the spreadsheet with the service account we just created 

3.Copy the id of the spreadsheet from the URL. 

#Google API Dependency JARS 

<dependency> 

    <groupId>com.google.api-client</groupId> 

    <artifactId>google-api-client</artifactId> 

    <version>1.23.0</version> 

</dependency> 

<dependency> 

    <groupId>com.google.oauth-client</groupId> 

    <artifactId>google-oauth-client-jetty</artifactId> 

    <version>1.23.0</version> 

</dependency> 

<dependency> 

    <groupId>com.google.apis</groupId> 

    <artifactId>google-api-services-sheets</artifactId> 

    <version>v4-rev516-1.23.0</version> 

</dependency> 

 

#Google sheets API authorization flow 

1.Getting Credential instance by passing client secret and client id 

2.Getting Spreadsheet instance by passing Credential instance along with Project Name 

3.Create New Sheet using BatchUpdate method with the help of spreadsheetID 

4.Retrieve or Insert values as Range using the Spreadsheet instance. 

 
 

 
