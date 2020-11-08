package com.facebook.testing.screenshot.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Nullable;

class SingleTestRunArtifactsManager {

  private final String mTestRunId;
  private final File mRootDir;
  private File mCurrentTestRunReportsDirectory;

  public SingleTestRunArtifactsManager(String testRunId, File rootDir) {
    mTestRunId = testRunId;
    mRootDir = rootDir;
  }

  public void recordFile(String fileName, byte[] content) throws IOException {
    File reportsDirectory = getOrCreateCurrentTestRunReportsDirectory();
    try (FileOutputStream recordedFile = new FileOutputStream(new File(reportsDirectory, fileName))) {
      recordedFile.write(content);
    }
  }

  @Nullable
  public File readFile(String fileName) {
    File requestedFile = new File(getOrCreateCurrentTestRunReportsDirectory(), fileName);
    if (requestedFile.isFile()) {
      return requestedFile;
    } else {
      return null;
    }
  }

  private File getOrCreateCurrentTestRunReportsDirectory() {
    if (mCurrentTestRunReportsDirectory == null) {
      mCurrentTestRunReportsDirectory = new File(mRootDir, mTestRunId);
      if (!mCurrentTestRunReportsDirectory.mkdir() && !mCurrentTestRunReportsDirectory.isDirectory()) {
        throw new IllegalStateException("Unable to create a directory to store reports.");
      }
    }
    return mCurrentTestRunReportsDirectory;
  }
}
