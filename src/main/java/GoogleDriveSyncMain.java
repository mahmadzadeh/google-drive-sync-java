import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.sync.*;
import com.sync.util.Configuration;
import com.sync.util.ProjectSettings;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

public class GoogleDriveSyncMain {

    public static void main(String... args) throws IOException, GeneralSecurityException {

        if (args.length == 0) {
            log("Invalid usage. GoogleDriveSyncMain [PATH-TO-CONFIGURATION-FILE]");
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

        ProjectSettings settings = new Configuration(args[0]).convertToSettings();

        //Build a new authorized API client service.
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        ServerBuilderContract serverBuilderContract = new ServerBuilderContractImpl();
        Drive service = serverBuilderContract.createInstance(httpTransport, serverBuilderContract.getCredentials(httpTransport,
                settings.getCredentialFilePath()), settings.getAppName());

        GoogleDriveSync mySync = new GoogleDriveSync(service, new LocalFileSystemImpl());

        Set<SyncableFile> fileSet = mySync.getLocalFilesToSync(settings.getLocalFolderToSync());

        mySync.dropExistingRemoteFolder(settings.getRemoteFolderName());

        mySync.sync(fileSet, mySync.createFreshRemoteFolder(settings.getRemoteFolderName()));

    }

    private static void log(String msg) {
        System.out.println(">>> " + msg);
    }

}