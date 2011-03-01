package com.novoda.lib.httpservice.tester.service;

import com.novoda.lib.httpservice.HttpService;

public abstract class BaseHttpService extends HttpService {
	
	private static final String DEFAULT_XML = "actors.xml";
	private String configFileName;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		configFileName = getCustomFileName();
		if(configFileName == null) {
			configFileName = DEFAULT_XML;
		}
		
		//TODO read the file and instantiate the actors
	}

	private String getCustomFileName() {
		//TODO
//		ServiceInfo info = getPackageManager().getServiceInfo(new ComponentName(this, 
//				SimpleHttpService.class), PackageManager.GET_INTENT_FILTERS|PackageManager.GET_META_DATA);
//		
//		Log.e("XXXX handlerName : " + info.metaData.getString("handlerName"));
		// TODO Auto-generated method stub
		return null;
	}
	
}
