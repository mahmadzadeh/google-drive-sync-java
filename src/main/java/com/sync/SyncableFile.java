package com.sync;

import java.io.File;

public class SyncableFile {

    private final File file;
    private final SupportedFileExtension extension;

    public SyncableFile( File file, SupportedFileExtension extension ) {
        this.file = file;
        this.extension = extension;
    }

    public File getFile() {
        return file;
    }

    public SupportedFileExtension getExtension() {
        return extension;
    }

}
