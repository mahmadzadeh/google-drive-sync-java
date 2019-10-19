package com.sync;

import java.io.File;

public class SyncableFile {

    private final File file;
    private final SupportedFileExtensions extension;

    public SyncableFile( File file, SupportedFileExtensions extension ) {
        this.file = file;
        this.extension = extension;
    }

    public File getFile() {
        return file;
    }

    public SupportedFileExtensions getExtension() {
        return extension;
    }

}
