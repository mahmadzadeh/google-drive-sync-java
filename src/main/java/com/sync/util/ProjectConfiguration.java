package com.sync.util;

import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class ProjectConfiguration {

    public static final String CREDENTIAL_FILE_PATH = "credential.file.path";
    private final Properties props;

    private final String path;

    public ProjectConfiguration(String pathToFile) {
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
}
