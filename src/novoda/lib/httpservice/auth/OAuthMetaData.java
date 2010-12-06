
package novoda.lib.httpservice.auth;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.os.Parcel;
import android.os.Parcelable;

public class OAuthMetaData implements Parcelable {

    private static final String OAUTH_DATA = "oauth-data";

    public static String SCHEMA = "http://schemas.novoda.com/lib/httpservice";

    public static String CONSUMER_KEY = "consumerKey";

    public static String CONSUMER_SECRET = "consumerSecret";

    public static String REQUEST_TOKEN_URL = "requestTokenURL";

    public static String ACCESS_TOKEN_URL= "accessTokenURL";

    public static String AUTHORIZE_URL = "authorizeURL";

    String consumerKey = "";

    String consumerSecret = "";

    String urls;

    private OAuthMetaData() {
        // no constructor user fromXML
    }

    public static OAuthMetaData fromXml(XmlResourceParser xrp) throws XmlPullParserException,
            IOException {

        OAuthMetaData metaData = new OAuthMetaData();
        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
            if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                String s = xrp.getName();
                if (s.equals(OAUTH_DATA)) {
                    metaData.consumerKey = xrp.getAttributeValue(SCHEMA, "consumerKey");
                    metaData.consumerSecret = xrp.getAttributeValue(SCHEMA, "consumerSecret");
                }
            } else if (xrp.getEventType() == XmlResourceParser.END_TAG) {
                ;
            } else if (xrp.getEventType() == XmlResourceParser.TEXT) {
                ;
            }
            xrp.next();
        }
        xrp.close();
        return metaData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
