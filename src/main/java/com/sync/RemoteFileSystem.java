package com.sync;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;

import java.util.Optional;

public interface RemoteFileSystem {

    java.util.List<File> listFolders();

    void deleteFolder(String folderId);

    File createFolder(String folderName);

    File createFile(File fileMetaData, FileContent content);

    Optional<File> getParentFolder(String driveDirName);
}
