package com.sync;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class LocalFileSystemImpl implements LocalFileSystem {

    @Override
    public Collection<File> listFilesInDir(String dir) {

        java.io.File direc = new java.io.File(dir);

        if (!direc.isDirectory()) {
            return Collections.emptySet();
        }

        return Arrays.stream(direc.listFiles()).collect(Collectors.toSet());
    }
}
