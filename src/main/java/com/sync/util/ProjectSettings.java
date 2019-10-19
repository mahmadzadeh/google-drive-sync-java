package com.sync.util;

import org.apache.commons.lang3.Validate;

import java.io.File;

public class ProjectSettings {

    private File credentialFilePath;
    private File localFolderToSync;
    private String remoteFolderName;

    private ProjectSettings() {
    }

    public static ProjectSettings INSTANCE() {
        return new ProjectSettings();
    }

    public ProjectSettings withCredentialFilePath(String path) {
        credentialFilePath = getFileOrException(path, "credential file path given is invalid " + path);
        return this;
    }

    public ProjectSettings withLocalFolderToSync(String path) {
        localFolderToSync = getFileOrException(path, "local folder to sync has invalid path " + path);
        return this;
    }

    public ProjectSettings withRemoteFolderName(String path) {
        Validate.notBlank(path, "invalid remote folder name given " + path);
        remoteFolderName = path;
        return this;
    }

    public File getCredentialFilePath() {
        return credentialFilePath;
    }

    public File getLocalFolderToSync() {
        return localFolderToSync;
    }

    public String getRemoteFolderName() {
        return remoteFolderName;
    }

    private File getFileOrException(String path, String errMsg) {
        File f = new File(path);
        if (!f.exists()) {
            throw new IllegalArgumentException(errMsg);
        }

        return f;
    }
}
