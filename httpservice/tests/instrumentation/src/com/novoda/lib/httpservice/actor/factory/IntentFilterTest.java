
package com.novoda.lib.httpservice.actor.factory;

import com.novoda.lib.httpservice.tests.R;

import org.xmlpull.v1.XmlPullParser;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.XmlResourceParser;
import android.test.InstrumentationTestCase;
import android.test.MoreAsserts;
import android.util.Log;

import java.util.Iterator;

public class IntentFilterTest extends InstrumentationTestCase {

    public void testParsingXMLAutomatically() throws Exception {

        XmlResourceParser xml = getInstrumentation().getContext().getResources()
                .getXml(R.xml.intentfiltertest);

        final IntentFilter filter = new IntentFilter();

        int type;
        while ((type = xml.next()) != XmlPullParser.END_DOCUMENT && type != XmlPullParser.START_TAG) {
        }

        final String nodeName = xml.getName();

        if (!"intent-filter".equals(nodeName)) {
            throw new RuntimeException();
        }

        filter.readFromXml(xml);
        MoreAsserts.assertContentsInAnyOrder(new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return filter.actionsIterator();
            }
        }, "testAction");

        Intent intent = new Intent();
        intent.setAction("testAction");
        assertTrue(filter.matchAction(intent.getAction()));
    }
}
