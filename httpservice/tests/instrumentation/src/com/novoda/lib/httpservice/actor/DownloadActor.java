
package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.utils.Log;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;

public class DownloadActor extends Actor {

    private Messenger messenger = new Messenger(this);
    
    @Override
    public void handleMessage(Message msg) {
        Log.i(" in actor " + msg);
        super.handleMessage(msg);
    }

    @Override
    public void onCreate(Bundle bundle) {
        ResultReceiver v = getIntent().getParcelableExtra("test");
        v.send(0, null);
        Messenger msg = getIntent().getParcelableExtra("handler");
        try {
            Message message = Message.obtain();
            message.replyTo = messenger;
            message.what = 111111111;
            msg.send(message);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onCreate(bundle);
    }
}
