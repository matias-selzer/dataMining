package com.models;

public class TwitterCredential {
	
	public static final TwitterCredential MatiCredential = new TwitterCredential(
			"eBTp6EMtomlH82TIwXUm7rX4h", 
			"l5iC52danwX2cpDYKoaB8UBM346JnxZW3qDpHX4zKmGEvSVrka", 
			"2388407179-r6FijmkiB0VDUU1B6v44Uw2SToh1p8KC7tyVc9F", 
			"jAElhTX3NkjXMGSOfOCYWSGQB2BfkFAtzw11dMpQ5CvKC");
	
	
	private String mConsumerKey;
	private String mConsumerSecret;
	private String mAccessToken;
	private String mAccessTokenSecret;
	
	public TwitterCredential(){
	}
	
	public TwitterCredential(
			String pConsumerKey,
			String pConsumerSecret,
			String pAccessToken,
			String pAccessTokenSecret){
		this.mConsumerKey 		= pConsumerKey;
		this.mConsumerSecret    = pConsumerSecret;
		this.mAccessToken       = pAccessToken;
		this.mAccessTokenSecret = pAccessTokenSecret;
	}
	
	/**
	 * @return the consumerKey
	 */
	public String getConsumerKey() {
		return mConsumerKey;
	}
	
	/**
	 * @param pConsumerKey the consumerKey to set
	 */
	public void setConsumerKey(String pConsumerKey) {
		this.mConsumerKey = pConsumerKey;
	}
	
	/**
	 * @return the consumerSecret
	 */
	public String getConsumerSecret() {
		return mConsumerSecret;
	}
	
	/**
	 * @param pConsumerSecret the consumerSecret to set
	 */
	public void setConsumerSecret(String pConsumerSecret) {
		this.mConsumerSecret = pConsumerSecret;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return mAccessToken;
	}

	/**
	 * @param pAccessToken the accessToken to set
	 */
	public void setAccessToken(String pAccessToken) {
		this.mAccessToken = pAccessToken;
	}

	/**
	 * @return the accessTokenSecret
	 */
	public String getAccessTokenSecret() {
		return mAccessTokenSecret;
	}

	/**
	 * @param pAccessTokenSecret the accessTokenSecret to set
	 */
	public void setAccessTokenSecret(String pAccessTokenSecret) {
		this.mAccessTokenSecret = pAccessTokenSecret;
	}
	
	
	
}
