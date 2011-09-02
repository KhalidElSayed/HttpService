package com.novoda.httpservice.provider;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;
import com.novoda.httpservice.HttpServiceTestRunner;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Ignore
@RunWith(HttpServiceTestRunner.class)
public class EventBusTestConcurrency {

    private IntentRegistry intentRegistry;

    private IntentWrapper intentWrapper;

    private Map<String, List<IntentWrapper>> registry = Collections
            .synchronizedMap(new HashMap<String, List<IntentWrapper>>());

    @Before
    public void setupIntentRegistry() {

        List<IntentWrapper> l = new ArrayList<IntentWrapper>();
        l.add(new IntentWrapper(new Intent()));
        l.add(new IntentWrapper(new Intent()));
        l.add(new IntentWrapper(new Intent()));
        l.add(new IntentWrapper(new Intent()));
        l.add(new IntentWrapper(new Intent()));
        l.add(new IntentWrapper(new Intent()));
        l.add(new IntentWrapper(new Intent()));
        registry.put("something", l);

        intentRegistry = mock(IntentRegistry.class);
        intentWrapper = mock(IntentWrapper.class);
        when(intentRegistry.getSimilarIntents(any(IntentWrapper.class))).thenReturn(registry.get("something"));
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        bus = new EventBus(intentRegistry);
        bus.fireOnContentConsumed(null);

        runMultiThread();
        assertTrue(true);
    }

    private void runMultiThread() throws InterruptedException {
        int i = 0;
        while (i < 1000) {
            CountDownLatch latch = new CountDownLatch(5);
            new RunMultiTimes(latch).start();
            new RunMultiTimes(latch).start();
            new RunMultiTimes(latch).start();
            new RunMultiTimes(latch).start();
            new RunMultiTimes(latch).start();
            latch.await();
            ++i;
        }
    }

    private class RunMultiTimes extends Thread {
        private CountDownLatch latch;

        public RunMultiTimes(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            bus.fireOnContentConsumed(intentWrapper);
            latch.countDown();
            super.run();
        }
    }

    private EventBus bus;
}
