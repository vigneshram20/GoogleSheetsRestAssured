Steps to create google developer service account  

Navigate to https://console.developers.google.com/ 

Create a New Project 

Navigate to Credentials 

Create New Service Account 

Download the client secret.json and place in JAVA project root folder 

Steps to create and share the spreadsheet to the service account 

Create a spreadsheet in our primary account. 

Share the spreadsheet with the service account we just created 

Copy the id of the spreadsheet from the URL. 

Google API Dependency JARS 

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

 

Google sheets API authorization flow 

Getting Credential instance by passing client secret and client id 

Getting Spreadsheet instance by passing Credential instance along with Project Name 

Create New Sheet using BatchUpdate method with the help of spreadsheetID 

Retrieve or Insert values as Range using the Spreadsheet instance. 

 
 

 
