package com.datacatalog.com;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.datacatalog.v1.DataCatalogClient;
import com.google.cloud.datacatalog.v1.DataCatalogSettings;
import com.google.cloud.datacatalog.v1.Entry;
import com.google.cloud.datacatalog.v1.LookupEntryRequest;

import java.io.FileInputStream;
import java.util.logging.Logger;

public class App {

    static Logger LOGGER = Logger.getLogger(App.class.getName());
    static String CREDENTIALS;
    static String PROJECT_ID;
    static String DATASET_ID;

    static {
        CREDENTIALS = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (CREDENTIALS == null) {
            CREDENTIALS = System.getProperty("credentials", "./credential.json");
        }

        PROJECT_ID = System.getProperty("project_id", "my-spanking-new-project");
        DATASET_ID = System.getProperty("dataset_id", "bq_sme_dataset");
    }

    /**
     * This application takes advantage of the following System properties. Pass them in using the -D flag.
     *  credentials: This represents the credentials JSON file. By default, it will try and load from the environment
     *  variable GOOGLE_APPLICATION_CREDENTIALS
     *  project_id: The project ID with the dataset you want to query
     *  dataset_id: The ID of the BQ dataset
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info(String.format("Credential: %s", CREDENTIALS));
        LOGGER.info(String.format("Project ID: %s", PROJECT_ID));
        LOGGER.info(String.format("Dataset ID: %s", DATASET_ID));

        // By default, credentials are usually stored in the environment variable
        // GOOGLE_APPLICATION_CREDENTIALS on GCP VM resources. This example loads an explicitly
        // defined file. Always use the JSON file option when storing credentials.
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(CREDENTIALS));
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
        DataCatalogSettings settings = DataCatalogSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        DataCatalogClient client = DataCatalogClient.create(settings);

        // Get an entry by the resource name from the source Google Cloud Platform service.
        String linkedResource =
                String.format("//bigquery.googleapis.com/projects/%s/datasets/%s", PROJECT_ID, DATASET_ID);

        LookupEntryRequest request =
                LookupEntryRequest.newBuilder().setLinkedResource(linkedResource).build();

        Entry entry = client.lookupEntry(request);

        LOGGER.info(String.format("Entry name: %s", entry.getName()));
    }
}
