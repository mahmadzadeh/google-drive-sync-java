import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.sync.*;
import com.sync.util.Configuration;
import com.sync.util.ProjectSettings;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleDriveSyncMain {

    public static void main(String... args) throws IOException, GeneralSecurityException {

        if (args.length == 0) {
            log("Invalid usage. GoogleDriveSyncMain [ABSOLUTE-PATH-TO-CONFIGURATION-FILE]");
            System.exit(-1);
        }

        ProjectSettings settings = new Configuration(args[0]).convertToSettings();

        ServerBuilderContract serverBuilderContract = new ServerBuilderContractImpl();
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = serverBuilderContract.createInstance(httpTransport, serverBuilderContract.getCredentials(httpTransport,
                settings.getCredentialFilePath()), settings.getAppName());

        GoogleDriveSync mySync = new GoogleDriveSync(new LocalFileSystemImpl(), new RemoteFileSystemImpl(service));

        mySync.dropExistingRemoteFolder(settings.getRemoteFolderName());

        mySync.sync(settings.getLocalFolderToSync(), mySync.createFreshRemoteFolder(settings.getRemoteFolderName()));

    }

    private static void log(String msg) {
        System.out.println(">>> " + msg);
    }

}