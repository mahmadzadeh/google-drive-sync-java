package com.sync;

import java.io.File;
import java.util.Collection;

public interface LocalFileSystem {

    Collection<File> listFilesInDir(File dir);
}
