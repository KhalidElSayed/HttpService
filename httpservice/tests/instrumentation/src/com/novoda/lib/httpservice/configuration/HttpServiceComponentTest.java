
package com.novoda.lib.httpservice.configuration;

import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.processor.BasicOAuthProcessor;

import android.test.MoreAsserts;

public class HttpServiceComponentTest extends XmlTestCase {

    public void testParsingStandardXML() throws Exception {
        HttpServiceComponent cp = HttpServiceComponent.fromXml(getXmlPullParser(R.xml.stdconfig),
                getInstrumentation().getTargetContext());
        assertNotNull(cp);
        assertEquals(BasicOAuthProcessor.class.getCanonicalName(), cp.processors.get(0).name);
    }
}
