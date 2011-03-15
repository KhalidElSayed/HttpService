
package com.novoda.lib.httpservice.actor.factory;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.LoggingActor;
import com.novoda.lib.httpservice.tests.R;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.test.MoreAsserts;

public class XmlActorFactoryTest extends InstrumentationTestCase {

    public static Intent GET_EXAMPLE = new Intent("GET", Uri.parse("http://www.example.com"));

    public void testParsingBasicXML() throws Exception {
        XmlResourceParser xml = getInstrumentation().getContext().getResources()
                .getXml(R.xml.xmlactor);
        XmlActorFactory factory = new XmlActorFactory(xml, getInstrumentation().getTargetContext());
        Actor actor = factory.getActor(GET_EXAMPLE, null);
        MoreAsserts.assertAssignableFrom(LoggingActor.class, actor);
    }
}
