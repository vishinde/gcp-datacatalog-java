package com.datacatalog.com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

//import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.datacatalog.v1.DataCatalogClient;
import com.google.cloud.datacatalog.v1.DataCatalogSettings;
import com.google.cloud.datacatalog.v1.SearchCatalogRequest;
import com.google.cloud.datacatalog.v1.SearchCatalogRequest.Scope;
import com.google.cloud.datacatalog.v1.LookupEntryRequest;
import com.google.cloud.datacatalog.v1.Entry;

public class App 
{
    public static void main( String[] args )
    {
        /*
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/path/to/credentials.json"));
        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();
        */

        try
        {
            GoogleCredentials mycreds = ServiceAccountCredentials.fromStream(new FileInputStream("/home/vivekshinde/my-spanking-new-project-6fb3f295b8e7.json"));

            DataCatalogSettings settings = DataCatalogSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(mycreds)).build();
            DataCatalogClient client = DataCatalogClient.create(settings);
            
        String projectId = "my-spanking-new-project";
        String datasetId = "bq_sme_dataset";

    // Get an entry by the resource name from the source Google Cloud Platform service.
    String linkedResource =
        String.format("//bigquery.googleapis.com/projects/%s/datasets/%s", projectId, datasetId);
    LookupEntryRequest request =
        LookupEntryRequest.newBuilder().setLinkedResource(linkedResource).build();

        Entry entry = client.lookupEntry(request);
        System.out.printf("Entry name: %s\n", entry.getName());

        }
        catch(IOException ex)
        {
            //test
        }
    }
}
