package com.sync;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class RemoteDriveBuilderContractImpl implements RemoteDriveBuilderContract {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private final NetHttpTransport httpTransport;

    public RemoteDriveBuilderContractImpl(NetHttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(
            DriveScopes.DRIVE_FILE,
            DriveScopes.DRIVE_METADATA,
            DriveScopes.DRIVE);

    @Override
    public Credential getCredentials(File credsFilePath)  {
        return getCredes(this.httpTransport, credsFilePath);
    }

    @Override
    public Drive createInstance(Credential credential, String applicationName) {

        Drive service = new Drive.Builder( this.httpTransport, JSON_FACTORY, credential )
                .setApplicationName(applicationName)
                .build();

        return service;
    }

    private Credential getCredes(final NetHttpTransport HTTP_TRANSPORT, File credsFilePath)  {

        GoogleClientSecrets clientSecrets;

        try (InputStreamReader in = new FileReader(credsFilePath)) {

            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, in);

        } catch (IOException e) {

            throw new ClientSecretReadException("Unable to read credential file given in path "
                    + credsFilePath, e);
        }

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = null;

        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES )
                    .setDataStoreFactory( getDataStore() )
                    .setAccessType( "offline" )
                    .build();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort( 8888 ).build();
        return getUserCredential(flow, receiver);
    }

    private Credential getUserCredential(GoogleAuthorizationCodeFlow flow, LocalServerReceiver receiver) {
        try {
            return new AuthorizationCodeInstalledApp( flow, receiver ).authorize( "user" );
        } catch (IOException e) {
            throw new AuthorizationException("", e);
        }
    }

    private FileDataStoreFactory getDataStore() {

        try {
            return new FileDataStoreFactory( new java.io.File( TOKENS_DIRECTORY_PATH ) );
        } catch ( IOException e ) {
            throw new TokenStoreException("", e);
        }
    }
}
