package com.sync;

import org.junit.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SupportedFileExtensionTest {

    @Test
    public void fromFileName() {


        HashMap<String, SupportedFileExtension> testVals = new HashMap<String, SupportedFileExtension>() {{
            put( "image.gif", SupportedFileExtension.gif );
            put( "image.jpeg", SupportedFileExtension.jpeg );
            put( "image.png", SupportedFileExtension.png );
            put( "file.txt", SupportedFileExtension.txt );
            put( "file.ods", SupportedFileExtension.ods );
        }};

        testVals.keySet()
                .stream().forEach( k -> assertEnumEquals( k, testVals.get( k ) ) );

    }

    private void assertEnumEquals( String k, SupportedFileExtension supportedFileExtensions ) {

        assertEquals( SupportedFileExtension.fromFileName( k ), Optional.of( supportedFileExtensions ) );
    }
}