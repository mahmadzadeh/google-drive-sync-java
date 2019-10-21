package com.sync;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RemoteFileSystemImpl implements RemoteFileSystem {

    public static final String MIME_TYPE_PARAM = "mimeType";
    public static final String GOOGLE_FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    public static final String TRASHED_FALSE = "trashed = false";

    private final Drive remoteFs;

    public RemoteFileSystemImpl(Drive remoteFs) {
        this.remoteFs = remoteFs;
    }

    @Override
    public List<File> listFolders() {
        FileList result;
        try {
            result = remoteFs.files().list()
                    .setQ(MIME_TYPE_PARAM + " = '" + GOOGLE_FOLDER_MIME_TYPE + "' and " + TRASHED_FALSE)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        } catch (IOException e) {
            throw new RemoteFolderListException("Unable to list folders on remote file system  ", e);
        }

        return result.getFiles();

    }

    @Override
    public void deleteFolder(String folderId) {
        try {
            remoteFs.files().delete(folderId)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            throw new RemoteFileDeletionException("Exception while deleting folder : " + folderId, e);
        }

        return;
    }

    @Override
    public File createFolder(String folderName) {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType(GOOGLE_FOLDER_MIME_TYPE);

        File file;
        try {
            file = remoteFs.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
        } catch (IOException e) {
            throw new RemoteFileCreationException("Exception while creating remote folder: " + folderName, e);
        }

        return file;
    }

    @Override
    public File createFile(File fileMetaData, FileContent content) {
        File file;

        try {
            file = remoteFs.files().create(fileMetaData, content)
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            throw new RemoteFileCreationException("Exception while uploading: " + fileMetaData.getName()
                    + " with mime type " + content.getType(), e);
        }

        return file;
    }

    @Override
    public Optional<File> getParentFolder(String driveDirName) {

        return this.listFolders().stream().filter(f -> f.getName().equals(driveDirName))
                .findFirst();
    }


}
