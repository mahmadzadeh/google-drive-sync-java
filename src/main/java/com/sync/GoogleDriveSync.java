package com.sync;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GoogleDriveSync {

    private final Drive remoteFs;
    private final LocalFileSystem localFs;

    public GoogleDriveSync(Drive remoteFs, LocalFileSystem localFs) {

        this.remoteFs = remoteFs;
        this.localFs = localFs;
    }

    public Set<SyncableFile> getLocalFilesToSync(String syncDir) {
        return localFs.listFilesInDir(syncDir)
                .stream().
                        map(f -> {
                            Optional<SupportedFileExtensions> fileExtensions = SupportedFileExtensions.fromFileName(f.getName());
                            return fileExtensions.isPresent()
                                    ? new com.sync.SyncableFile(f, fileExtensions.get())
                                    : null;
                        })
                .filter(it -> it != null)
                .collect(Collectors.toSet());

    }

    public void dropExistingRemoteFolder(String remoteFolderName) throws IOException {
        getParentDir(remoteFolderName)
                .ifPresent(f -> deleteFolder(f.getId()));
    }

    public File createFreshRemoteFolder(String remoteFolderName)  {
        return createFolder( remoteFolderName );
    }

    public void sync(Set<SyncableFile> fileSet, File remoteFolder) {

        fileSet.stream().forEach( file -> {

            File metaData = new File();
            metaData.setName( file.getFile().getName() );
            metaData.setParents( Collections.singletonList( remoteFolder.getId() ) );

            FileContent content = new FileContent( file.getExtension().getMimeType(), file.getFile() );

            File savedFile = createFile( metaData, content );

            log( savedFile );
        } );
    }

    private void log(File savedFile) {
        System.out.println(savedFile);
    }

    private Optional<File> getParentDir(String driveDirName) {

        FileList result = null;
        try {
            result = remoteFs.files().list()
                    .setQ("mimeType = 'application/vnd.google-apps.folder' and trashed = false")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File f : result.getFiles()) {
            if (f.getName().equals(driveDirName)) {
                return Optional.of(f);
            }
        }

        return Optional.empty();
    }

    private void deleteFolder(String id) {

        try {
            remoteFs.files().delete(id)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            throw new RemoteFileCreationException("Exception while uploading: " + id, e);
        }

        return;
    }

    private File createFolder( String removeDriveDirName ) {
        File fileMetadata = new File();
        fileMetadata.setName( removeDriveDirName );
        fileMetadata.setMimeType( "application/vnd.google-apps.folder" );

        File file;
        try {
            file = remoteFs.files().create( fileMetadata )
                    .setFields( "id" )
                    .execute();
        } catch ( IOException e ) {
            throw new RemoteFileCreationException( "Exception while creating remote folder: " + removeDriveDirName, e );
        }

        return file;
    }

    private  File createFile( File fileMetaData, FileContent content )  {

        File file;

        try {
            file = remoteFs.files().create( fileMetaData, content )
                    .setFields( "id, parents" )
                    .execute();
        } catch ( IOException e ) {
            throw new RemoteFileCreationException("Exception while uploading: " + fileMetaData.getName()
                    + " with mime type " + content.getType() , e);
        }

        return file;
    }

}
