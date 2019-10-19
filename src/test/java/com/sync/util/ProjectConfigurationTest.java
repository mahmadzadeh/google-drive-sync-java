package com.sync.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class ProjectConfigurationTest {

    private final String VALID_CONFIG_PROPERTIES = "src/test/resources/config.properties";
    private ProjectConfiguration sut;

    @Before
    public void setUp() {
        sut = new ProjectConfiguration(VALID_CONFIG_PROPERTIES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyPathToPropertyThenThrowRuntime() {
        sut = new ProjectConfiguration("");
    }

    @Test(expected = InvalidConfigurationFileException.class)
    public void givenInvalidPathToPropertyThenThrowRuntime() {
        sut = new ProjectConfiguration("~/file.json");
    }

    @Test
    public void givenInvalidPropertyThenGetPropertyReturnsOptionalEmpty() {

        assertEquals(Optional.empty(),
                sut.getProperty("invalid.property"));
    }

    @Test
    public void givenValidPropertyThenGetPropertyReturnsIt() {
        assertEquals(Optional.of("value"),sut.getProperty("key"));
    }


    @Test
    public void canGetCredentialsJsonFilePath() {
        assertEquals(Optional.of("/usr/local/test"), sut.getCredentialFilePath());
    }



}