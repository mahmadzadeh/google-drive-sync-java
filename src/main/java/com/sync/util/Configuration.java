package com.sync.util;

import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class Configuration {

    public static final String CREDENTIAL_FILE_PATH = "credential.file.path";
    public static final String LOCAL_SYNC_FOLDER = "local.sync.folder";
    public static final String REMOTE_FOLDER_NAME = "remote.sync.folder";

    private final Properties props;

    private final String path;

    public Configuration(String pathToFile) {
        Validate.notBlank(pathToFile, "path to config properties file can not be blank. " +
                "Provided path " + pathToFile);

        props = new Properties();

        try (FileReader reader = new FileReader(new File(pathToFile))) {
            props.load(reader);
        } catch (IOException e) {
            throw new InvalidConfigurationFileException("Invalid configuration properties given in path " + pathToFile, e);
        }

        path = pathToFile;
    }

    public String getConfigFilePath() {
        return path;
    }

    public Optional<String> getProperty(String property) {
        return Optional.ofNullable(this.props.getProperty(property));
    }

    public Optional<String> getCredentialFilePath() {
        return getProperty(CREDENTIAL_FILE_PATH);
    }

    public Optional<String> getLocalFolderToSync() {
        return getProperty(LOCAL_SYNC_FOLDER);
    }

    public Optional<String> getRemoteFolder() {
        return getProperty(REMOTE_FOLDER_NAME);
    }

    public ProjectSettings convertToSettings() {
        String credsFilePath = getCredentialFilePath().orElse("");
        String localFolderToSync = getLocalFolderToSync().orElse("");
        String remoteFilePath = getRemoteFolder().orElse("");

        return ProjectSettings
                .INSTANCE()
                .withCredentialFilePath(credsFilePath)
                .withLocalFolderToSync(localFolderToSync)
                .withRemoteFolderName(remoteFilePath);
    }
}
