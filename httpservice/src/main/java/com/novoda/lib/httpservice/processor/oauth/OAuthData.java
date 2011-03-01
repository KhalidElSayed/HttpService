package com.novoda.lib.httpservice.processor.oauth;


public interface OAuthData {

    /**
     * Gets the token key.
     * 
     * @return the token key
     */
    String getTokenKey();

    /**
     * Gets the token secret.
     * 
     * @return the token secret
     */
    String getTokenSecret();

    /**
     * Gets the consumer key.
     * 
     * @return the consumer key
     */
    String getConsumerKey();

    /**
     * Gets the consumer secret.
     * 
     * @return the consumer secret
     */
    String getConsumerSecret();
}
