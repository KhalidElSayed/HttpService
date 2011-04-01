
package com.novoda.lib.httpservice.actor;

import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class FileActor extends Actor implements ResumableActor {

    private RandomAccessFile file;

    @Override
    public void onCreate(Bundle bundle) {
        try {
            file = new RandomAccessFile(getFile(), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate(bundle);
    }

    @Override
    public void onBytesReceived(byte[] bytes) throws IOException {
        file.seek(length());
        file.write(bytes);
    }

    @Override
    public void onAllBytesReceived() {
        try {
            file.close();
        } catch (IOException e) {
            throw new FileActorException();
        }
    }

    @Override
    public long length() {
        try {
            return file.length();
        } catch (IOException e) {
            throw new FileActorException();
        }
    }

    protected abstract File getFile();
}
