
package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class FileActor extends Actor implements ResumableActor {

    public static final String DOWNLOAD_COMPLETE = "com.novoda.lib.httpservice.action.DOWNLOAD_COMPLETE";

    public static final String DOWNLOAD_FAILED = "com.novoda.lib.httpservice.action.DOWNLOAD_FAILED";

    public static final String DOWNLOAD_DIRECTORY_PATH_EXTRA = "downloadDirectoryPath";

    public static final String FILE_NAME_EXTRA = "fileName";

    public static final String WRITE_TO = "writeToUri";

    public static final String EXCEPTION_MESSAGE_EXTRA = "exception";

    public static final String ERROR_TYPE_EXTRA = "errorType";

    public static final String CANCELLABLE_EXTRA = "cancellable";

    private RandomAccessFile file;

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Log.i(" ===================== ee =========================== ");
//            Thread.currentThread().interrupt();
//            try {
//                this.finalize();
//            } catch (Throwable e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            FileActor.this.sendEmptyMessage(ON_DESTROY);
//            super.handleMessage(msg);
//        }
//    };

//    private Messenger cancel = new Messenger(handler);

    @Override
    public void onPreprocess(HttpUriRequest method, HttpContext context) {
        super.onPreprocess(method, context);
    }

    @Override
    public void onResponseReceived(HttpResponse httpResponse) {
        if (Log.infoLoggingEnabled()) {
            Log.i("Downloading " + getIntent().getDataString() + " to "
                    + getIntent().getStringExtra(DOWNLOAD_DIRECTORY_PATH_EXTRA));
        }
        try {
//            if (getIntent().hasExtra(CANCELLABLE_EXTRA)) {
//                registerForCancel();
//            }
            httpResponse.getEntity().writeTo(getOutputStream());
            broadcastFinishedDownload();
        } catch (FileNotFoundException e) {
            broadcastDownloadFailed(e);
        } catch (IOException e) {
            broadcastDownloadFailed(e);
        }
        super.onResponseReceived(httpResponse);
    }

//    private void registerForCancel() {
//        Messenger receiver = getIntent().getParcelableExtra(CANCELLABLE_EXTRA);
//        Message msg = Message.obtain();
//        msg.replyTo = cancel;
//        try {
//            receiver.send(msg);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }

    private void broadcastDownloadFailed(Exception e) {
        Intent intent = getIntent();
        intent.setAction(DOWNLOAD_FAILED);
        intent.putExtra(EXCEPTION_MESSAGE_EXTRA, e.getMessage());
        intent.setComponent(null);
        broadcast(intent);
        if (Log.errorLoggingEnabled()) {
            Log.e("Download failed for " + intent.getDataString(), e);
            e.printStackTrace();
        }
    }

    private void broadcast(Intent intent) {
        if (Log.infoLoggingEnabled()) {
            Log.i("Broadcasting " + intent);
        }
        getHttpContext().sendBroadcast(intent);
    }

    private OutputStream getOutputStream() throws FileNotFoundException {
        if (getIntent().hasExtra(WRITE_TO)) {
            Uri writeTo = getIntent().getParcelableExtra(WRITE_TO);
            return getHttpContext().getContentResolver().openOutputStream(writeTo);
        }
        return new FileOutputStream(getFile());
    }

    private void broadcastFinishedDownload() {
        Intent intent = getIntent();
        Uri uri = null;
        if (getIntent().hasExtra(WRITE_TO)) {
            uri = getIntent().getParcelableExtra(WRITE_TO);
        } else {
            uri = new Uri.Builder().scheme(ContentResolver.SCHEME_FILE)
                    .appendEncodedPath(getFile().getAbsolutePath()).build();
            intent.putExtra(DOWNLOAD_DIRECTORY_PATH_EXTRA, getFile().getAbsolutePath());
        }
        intent.setAction(DOWNLOAD_COMPLETE);
        intent.setData(uri);
        intent.setComponent(null);
        broadcast(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        // try {
        // // file = new RandomAccessFile(getFile(), "rw");
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // }
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
        File fileFile = new File(file);
        fileFile.mkdirs();
        return new File(fileFile, getIntent().getData().getLastPathSegment());
    }
}
