package com.sync;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GoogleDriveSync {

    private final LocalFileSystem localFs;
    private final RemoteFileSystem remoteFs;

    public GoogleDriveSync(LocalFileSystem localFs, RemoteFileSystem remoteFs) {

        this.localFs = localFs;
        this.remoteFs = remoteFs;
    }

    public Set<SyncableFile> getLocalFilesToSync(java.io.File syncDir) {
        return localFs.listFilesInDir(syncDir)
                .stream().
                        map(f -> {
                            Optional<SupportedFileExtension> fileExtensions = SupportedFileExtension.fromFileName(f.getName());
                            return fileExtensions.isPresent()
                                    ? new com.sync.SyncableFile(f, fileExtensions.get())
                                    : null;
                        })
                .filter(it -> it != null)
                .collect(Collectors.toSet());

    }

    public void dropExistingRemoteFolder(String remoteFolderName) {
        remoteFs.getParentFolder(remoteFolderName)
                .ifPresent(f -> remoteFs.deleteFolder(f.getId()));
    }

    public File createFreshRemoteFolder(String remoteFolderName)  {
        return remoteFs.createFolder( remoteFolderName );
    }

    public void sync(java.io.File localFolder, File remoteFolder) {
        getLocalFilesToSync(localFolder).stream().forEach( file -> syncToRemoteFolder(remoteFolder, file));
    }

    public void sync(Set<SyncableFile> fileSet, File remoteFolder) {
        fileSet.stream().forEach( file -> syncToRemoteFolder(remoteFolder, file));
    }

    private void syncToRemoteFolder(File remoteFolder, SyncableFile file) {
        File metaData = new File();
        metaData.setName(file.getFile().getName());
        metaData.setParents(Collections.singletonList(remoteFolder.getId()));

        FileContent content = new FileContent(file.getExtension().getMimeType(), file.getFile());

        File savedFile = remoteFs.createFile(metaData, content);

        log(savedFile);
    }

    private void log(File savedFile) {
        System.out.println(savedFile);
    }

}
