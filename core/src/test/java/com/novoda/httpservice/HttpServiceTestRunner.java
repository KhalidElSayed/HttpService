package com.novoda.httpservice;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.runners.model.InitializationError;

public class HttpServiceTestRunner extends RobolectricTestRunner {

    public HttpServiceTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass, "../../");
    }

}
