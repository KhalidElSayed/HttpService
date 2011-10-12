package com.novoda.httpservice.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.ResultReceiver;

import com.novoda.httpservice.exception.ProviderException;
import com.novoda.httpservice.exception.RequestException;
import com.novoda.httpservice.handler.HasHandlers;
import com.novoda.httpservice.handler.RequestHandler;
import com.novoda.httpservice.processor.HasProcessors;
import com.novoda.httpservice.processor.Processor;

public class EventBus implements HasHandlers, HasProcessors {

    public static final String DEFAULT_KEY = "default";

    public static final int SUCCESS = 200;

    public static final int ERROR = 500;

    private List<RequestHandler> handlers = new ArrayList<RequestHandler>();

    private List<Processor> processors = new ArrayList<Processor>();

    private IntentRegistry intentRegistry;

    public EventBus(IntentRegistry intentRegistry) {
        if (intentRegistry == null) {
            throw new ProviderException("IntentRegistry is null");
        }
        this.intentRegistry = intentRegistry;
    }

    @Override
    public void add(RequestHandler handler) {
        add(handlers, handler);
    }

    @Override
    public void remove(RequestHandler handler) {
        remove(handlers, handler);
    }

    @Override
    public void add(Processor processor) {
        add(processors, processor);
    }

    @Override
    public void remove(Processor processor) {
        remove(processors, processor);
    }

    public void fireOnThrowable(IntentWrapper intentWrapper, Throwable t) {
        fireOnThrowableOnReceiver(intentWrapper, t);
        List<IntentWrapper> intents = intentRegistry.getSimilarIntents(intentWrapper);
        if (intents != null && !intents.isEmpty()) {
            for (IntentWrapper similarIntent : intents) {
                fireOnThrowableOnReceiver(similarIntent, t);
            }
        }
        intentRegistry.onConsumed(intentWrapper);
        for (RequestHandler handler : handlers) {
            if (handler.match(intentWrapper)) {
                handler.onThrowable(intentWrapper, t);
            }
        }
    }

    private void fireOnThrowableOnReceiver(IntentWrapper intentWrapper, Throwable t) {
        if (intentWrapper == null) {
            return;
        }
        ResultReceiver receiver = intentWrapper.getResultReceiver();
        if (receiver != null) {
            receiver.send(ERROR, null);
        }
        receiver = intentWrapper.getEndResultReceiver();
        if (receiver != null) {
            receiver.send(ERROR, null);
        }
    }

    public void fireOnContentReceived(Response response) {
        IntentWrapper intentWrapper = response.getIntentWrapper();
        if (intentWrapper != null) {
            ResultReceiver receiver = intentWrapper.getResultReceiver();
            if (receiver != null) {
                try {
                    Bundle b = new Bundle();
                    b.putString(IntentWrapper.SIMPLE_BUNDLE_RESULT,
                            getContentAsString(response.getHttpResponse()));
                    receiver.send(response.getStatusCode(), b);
                } catch (Throwable t) {
                    receiver.send(response.getStatusCode(), null);
                }
            }
        }
        for (RequestHandler handler : handlers) {
            if (handler.match(intentWrapper)) {
                handler.onContentReceived(intentWrapper, response);
            }
        }
    }

    public void fireOnContentConsumed(IntentWrapper intentWrapper) {
        if (intentWrapper != null) {
            List<IntentWrapper> intents = new ArrayList<IntentWrapper>(
                    intentRegistry.getSimilarIntents(intentWrapper));
            if (intents != null && !intents.isEmpty()) {
                for (IntentWrapper similarIntent : intents) {
                    sendResultConsumedReceiver(similarIntent);
                }
            }
            sendResultConsumedReceiver(intentWrapper);
        }
        for (RequestHandler handler : handlers) {
            if (handler.match(intentWrapper)) {
                handler.onContentConsumed(intentWrapper);
            }
        }
    }

    private void sendResultConsumedReceiver(IntentWrapper intentWrapper) {
        ResultReceiver receiver = intentWrapper.getEndResultReceiver();
        if (receiver != null) {
            try {
                receiver.send(SUCCESS, null);
            } catch (Throwable t) {
                receiver.send(ERROR, null);
            }
        }
        intentRegistry.onConsumed(intentWrapper);
    }

    public void fireOnPreProcess(IntentWrapper intentWrapper, HttpRequest httpRequest,
            HttpContext context) {
        for (Processor processor : processors) {
            if (processor.match(intentWrapper)) {
                try {
                    processor.process(httpRequest, context);
                } catch (Exception e) {
                    throw new RequestException("Exception preprocessing content", e);
                }
            }
        }
    }

    public void fireOnPostProcess(IntentWrapper intentWrapper, HttpResponse response,
            HttpContext context) {
        for (ListIterator<Processor> iterator = processors.listIterator(processors.size()); iterator
                .hasPrevious();) {
            final Processor processor = iterator.previous();
            if (processor.match(intentWrapper)) {
                try {
                    processor.process(response, context);
                } catch (Exception e) {
                    throw new RequestException("Exception preprocessing content", e);
                }
            }
        }
    }

    private String getContentAsString(HttpResponse httpResponse) {
        HttpEntity entity = null;
        try {
            entity = httpResponse.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new RequestException("Exception converting entity to string", e);
        } finally {
            try {
                entity.consumeContent();
            } catch (Exception e) {
                throw new RequestException("Exception consuming content", e);
            }
        }
    }

    private static final <T> void remove(List<T> ts, T t) {
        if (t == null) {
            return;
        }
        if (ts.contains(t)) {
            ts.remove(t);
        }
    }

    private static final <T> void add(List<T> ts, T t) {
        if (t == null) {
            return;
        }
        if (ts.contains(t)) {
            return;
        }
        ts.add(t);
    }

}
