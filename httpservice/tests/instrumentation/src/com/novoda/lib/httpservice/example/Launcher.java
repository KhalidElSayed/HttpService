
package com.novoda.lib.httpservice.example;

import com.novoda.lib.httpservice.example.auth.AuthenticationService;
import com.novoda.lib.httpservice.utils.Log;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.Smoke;

import java.io.IOException;

@Smoke
public class Launcher extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        Runtime.getRuntime().exec("setprop log.tag.httpservice VERBOSE");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-provider VERBOSE");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-bus VERBOSE");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-con VERBOSE");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        Runtime.getRuntime().exec("setprop log.tag.httpservice INFO");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-provider INFO");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-bus INFO");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-con INFO");
        super.tearDown();
    }

    @LargeTest
    @Smoke
    public void testTwitterTrend() throws Exception {
        Intent intent = new Intent("GET", Uri.parse("http://api.twitter.com/1/trends.json"));

        intent.setClass(getInstrumentation().getContext(),
                com.novoda.lib.httpservice.HttpService.class);

        assertNotNull(getInstrumentation().getContext().startService(intent));
        Thread.sleep(3000);
    }

    public void testOAuthProcessor() throws Exception {
        Intent intent = new Intent("GET", Uri.parse("http://api.twitter.com/1/trends.json"));

        intent.setClass(getInstrumentation().getContext(),
                com.novoda.lib.httpservice.HttpService.class);

        assertNotNull(getInstrumentation().getContext().startService(intent));
        Thread.sleep(3000);

        AccountManager accountManager = AccountManager.get(getInstrumentation().getContext());
        String token = accountManager.blockingGetAuthToken(AuthenticationService.TEST_ACCOUNT,
                "test", true);
        Log.i("TEST" + token);
    }

}
