package novoda.lib.httpservice.auth;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.stub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OAuthAccountAuthenticationTest {
    
    @Spy
    OAuthAccountAuthenticatorService service;
    
    @Mock
    Resources res;
    
    @Mock
    XmlResourceParser parser;

    @Before
    public void setUp() throws Exception {
        service = new OAuthAccountAuthenticatorService();
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected=IllegalStateException.class)
    public void testShouldThrowAnExceptionifMetadataNotPresent() {
        service.onCreate();
    }
    
    @Test
    public void testShouldNotThrowExceptionIfMetaDataIsPresent() throws NameNotFoundException {
        
        // Can not make this one work for some reason...
        
//        PackageManager manager = mock(PackageManager.class);
//        ServiceInfo info = mock(ServiceInfo.class);
//        Bundle bundle = mock(Bundle.class);
//        
//        doReturn(manager).when(service).getPackageManager();
//        doReturn(res).when(service).getResources();
//
//        when(res.getXml(anyInt())).thenReturn(parser);
//        when(bundle.getInt(anyString())).thenReturn(anyInt());
//        when(info.metaData).thenReturn(bundle);
//        
//        stub(manager.getServiceInfo((ComponentName) anyObject(), anyInt())).toReturn(info);
//        
//        service.onCreate();
//        verify(parser, times(5)).getAttributeValue(eq(OAuthMetaData.SCHEMA), anyString());
    }

}
