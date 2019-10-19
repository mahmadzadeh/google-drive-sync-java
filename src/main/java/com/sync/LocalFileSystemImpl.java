package com.sync;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class LocalFileSystemImpl implements LocalFileSystem {

    @Override
    public Collection<File> listFilesInDir(File dir) {

        if (!dir.isDirectory()) {
            return Collections.emptySet();
        }

        return Arrays.stream(dir.listFiles()).collect(Collectors.toSet());
    }
}
