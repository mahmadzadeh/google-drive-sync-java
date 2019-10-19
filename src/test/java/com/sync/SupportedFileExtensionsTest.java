package com.sync;

import com.sync.SupportedFileExtensions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SupportedFileExtensionsTest {

    @Test
    public void fromFileName() {


        HashMap<String, SupportedFileExtensions> testVals = new HashMap<String, SupportedFileExtensions>() {{
            put( "image.gif", SupportedFileExtensions.gif );
            put( "image.jpeg", SupportedFileExtensions.jpeg );
            put( "image.png", SupportedFileExtensions.png );
            put( "file.txt", SupportedFileExtensions.txt );
            put( "file.ods", SupportedFileExtensions.ods );
        }};

        testVals.keySet()
                .stream().forEach( k -> assertEnumEquals( k, testVals.get( k ) ) );

    }

    private void assertEnumEquals( String k, SupportedFileExtensions supportedFileExtensions ) {

        assertEquals( SupportedFileExtensions.fromFileName( k ), Optional.of( supportedFileExtensions ) );
    }
}