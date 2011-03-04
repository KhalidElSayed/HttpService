package com.novoda.lib.httpservice.config;

public class ConfigManager {

	private Config config;
	
	public Config init() {
		if(config == null) {
			config = new ManualConfig(); 
		}
		return config;
	}
}
