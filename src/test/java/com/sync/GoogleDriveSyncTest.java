package com.sync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoogleDriveSyncTest {

    @Mock
    private LocalFileSystem mockLocalFs;

    @Mock
    private RemoteFileSystem mockRemoteFs;

    private GoogleDriveSync googleDriveSync;

    @Before
    public void setUp() {
        googleDriveSync = new GoogleDriveSync(mockLocalFs, mockRemoteFs);
    }

    @Test
    public void givenLocalDirToSyncWithNoFilesThenGetLocalFilesToSyncReturnsEmptySetOfFiles() {
        when(mockLocalFs.listFilesInDir(any(File.class))).thenReturn(Collections.emptyList());

        Set<SyncableFile> actual = googleDriveSync.getLocalFilesToSync(new File("."));

        assertEquals(Collections.emptySet(), actual);
    }

    @Test
    public void givenNonEmptyLocalDirToSyncWhenAllFilesUnknownExtensionThenGetLocalFilesToSyncReturnsEmptySetOfFiles() {

        List<File> allInvalidFiles = Arrays.asList(
                new File("file.invalid"),
                new File("file.popo"),
                new File("file.bogus"),
                new File("file.rad")
        );

        when(mockLocalFs.listFilesInDir(any(File.class))).thenReturn(allInvalidFiles);

        Set<SyncableFile> actual = googleDriveSync.getLocalFilesToSync(new File("."));

        assertEquals(Collections.emptySet(), actual);
    }


    @Test
    public void givenNonEmptyLocalDirWhenMixOfUnknownAndKnownExtensionThenGetLocalFilesToSyncReturnsSetOfFiles() {

        List<File> twoValidFiles = Arrays.asList(
                new File("file.invalid"),
                new File("file.popo"),
                new File("file.txt"),
                new File("file.jpeg")
        );

        when(mockLocalFs.listFilesInDir(any(File.class))).thenReturn(twoValidFiles);

        Set<SyncableFile> actual = googleDriveSync.getLocalFilesToSync(new File("."));

        assertEquals(2, actual.size());
    }

    /**
     * Only include files of mapped extension @{@link SupportedFileExtension}
     */
    @Test
    public void getLocalFilesToSyncSunnyDay() {

        List<File> expectedFiles = Arrays.asList(
                new File("file.gif"),
                new File("file.doc"),
                new File("file.txt"),
                new File("file.png")
        );

        when(mockLocalFs.listFilesInDir(any(File.class))).thenReturn(expectedFiles);

        Set<SyncableFile> actual = googleDriveSync.getLocalFilesToSync(new File("."));

        assertEquals(expectedFiles.size(), actual.size());
    }

}