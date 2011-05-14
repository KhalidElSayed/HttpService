
package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileActor extends Actor implements ResumableActor {

    public static final String DOWNLOAD_DIRECTORY_PATH_EXTRA = "downloadDirectoryPath";

    public static final String FILE_NAME_EXTRA = "fileName";

    private RandomAccessFile file;

    @Override
    public void onPreprocess(HttpUriRequest method, HttpContext context) {
        super.onPreprocess(method, context);
    }

    @Override
    public void onResponseReceived(HttpResponse httpResponse) {
        try {
            httpResponse.getEntity().writeTo(new FileOutputStream(getFile()));
            Intent intent = new Intent();
            intent.setAction("com.novoda.lib.httpservice.action.DOWNLOAD_COMPLETE");
            intent.putExtra(DOWNLOAD_DIRECTORY_PATH_EXTRA, getFile().getAbsolutePath());
            getHttpContext().sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            Intent intent = getIntent();
            e.printStackTrace();
            intent.setAction("com.novoda.lib.httpservice.action.DOWNLOAD_FAILED");
            getHttpContext().sendBroadcast(intent);
        } catch (IOException e) {
            Intent intent = getIntent();
            e.printStackTrace();
            intent.setAction("com.novoda.lib.httpservice.action.DOWNLOAD_FAILED");
            getHttpContext().sendBroadcast(intent);
        }
        super.onResponseReceived(httpResponse);
    }

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

    protected File getFile() {
        String file = getIntent().getStringExtra(DOWNLOAD_DIRECTORY_PATH_EXTRA);
        if (Log.infoLoggingEnabled()) {
            Log.i("Downloading " + getIntent().getDataString() + " to " + file);
        }
        File fileFile = new File(file);
        fileFile.mkdirs();
        return new File(fileFile, getIntent().getData().getLastPathSegment());
    }
}
