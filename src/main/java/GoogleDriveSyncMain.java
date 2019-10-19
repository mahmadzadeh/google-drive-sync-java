import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.sync.*;
import com.sync.util.ProjectConfiguration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GoogleDriveSyncMain {
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(
            DriveScopes.DRIVE_FILE,
            DriveScopes.DRIVE_METADATA,
            DriveScopes.DRIVE);

    public static final String DRIVE_DIR_NAME = "home_computer";
    public static final String SYNC_DIR = "/home/mahmadzadeh/Documents/synced_google_drive";


    public static void main(String... args) throws IOException, GeneralSecurityException {

        if (args.length == 0) {
            System.out.println("Invalid usage. GoogleDriveSyncMain [PATH-TO-CONFIGURATION-FILE]");
            System.exit(-1);
        }

        /**
         * MySync
         * .instance(Properties)
         * .checkRequirements()
         * .initializeDrive()
         * .showWhatWillBeSynced()
         * .sync()
         *
         */
        ProjectConfiguration config = new ProjectConfiguration(args[0]);

        String credsFilePath = config.getCredentialFilePath().orElseThrow(() -> new IllegalArgumentException("Invalid configuration file." +
                " Unable to get credential file path from configuration file located  " + config.getConfigFilePath()));

        java.io.File credentialFile = new java.io.File(credsFilePath);

        if (!credentialFile.exists()) {
            System.out.println("Invalid credential file path given. File " + credentialFile.getAbsolutePath() + " " +
                    "does not exist. Aborting...");
            System.exit(-1);
        }

        //Build a new authorized API client service.
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        ServerBuilderContract serverBuilderContract = new ServerBuilderContractImpl();

        Drive service = serverBuilderContract.createInstance(httpTransport, serverBuilderContract.getCredentials(httpTransport, credentialFile));

        GoogleDriveSync mySync = new GoogleDriveSync(service, new LocalFileSystemImpl());

        Set<SyncableFile> fileSet = mySync.getLocalFilesToSync(SYNC_DIR);

        mySync.dropExistingRemoteFolder(DRIVE_DIR_NAME);

        File remoteFolder = mySync.createFreshRemoteFolder(DRIVE_DIR_NAME);

        mySync.sync(fileSet, remoteFolder);

    }


    private static void log(File savedFile) {
        System.out.println(">>> saved file " + savedFile.getName() + " under " + savedFile.getParents());
    }

    private static void log(String savedFile) {
        System.out.println(">>> " + savedFile);
    }

}