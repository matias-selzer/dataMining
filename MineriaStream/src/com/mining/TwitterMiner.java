package com.mining;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.models.Tweet;
import com.models.TwitterCredential;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterMiner implements StatusListener{

	public enum CompletionCondition {
		TIME_OUT,
		COUNT,
	};
	
	public static final String LANGUAGE_ENGLISH = "en";
	public static final String LANGUAGE_SPANISH = "es";
	public static final String LANGUAGE_ITALIAN = "it";
	
	private HashSet<TwitterMiningListener> mListeners;
	private HashSet<String> mKeywords;
	private HashSet<String> mLanguages;
	private ArrayList<Tweet> mTweets;
	
	private ConfigurationBuilder mConfig;
	
	private CompletionCondition mCompletionCondition;
	
	private long mCompletionValue; 
	
	public TwitterMiner() {
		this.mCompletionCondition = CompletionCondition.COUNT;
		this.mCompletionValue	  = 1;
		
		this.mListeners  = new HashSet<TwitterMiningListener>();
		this.mTweets     = new ArrayList<Tweet>();
		this.mKeywords   = new HashSet<String>();
		this.mLanguages  = new HashSet<String>();
		this.mConfig     = new ConfigurationBuilder();
	    
		this.mConfig.setDebugEnabled(true)
				    .setOAuthConsumerKey(TwitterCredential.MatiCredential.getConsumerKey())
				    .setOAuthConsumerSecret(TwitterCredential.MatiCredential.getConsumerSecret())
				    .setOAuthAccessToken(TwitterCredential.MatiCredential.getAccessToken())
				    .setOAuthAccessTokenSecret(TwitterCredential.MatiCredential.getAccessTokenSecret());
	}
	
	/**
	 * Starts a new streaming session on twitter.
	 * Uses the keywords to filter the tweets.
	 */
	public void start() {
		
		// Create the filter
		FilterQuery fq 	  = new FilterQuery();
		
        fq.track(this.mKeywords.toArray(new String[this.mKeywords.size()]));
        fq.language(this.mLanguages.toArray(new String[this.mLanguages.size()]));
        
		// Create a new twitter stream
	    TwitterStream twitterStream = new TwitterStreamFactory(this.mConfig.build()).getInstance();

        twitterStream.addListener(this);
        twitterStream.filter(fq);
        
        try {
        	
        	// Notify the listeners that a streaming is about to start.
        	notifyStart();
        	
        	// Wait the condition to complete.
        	switch (this.mCompletionCondition) {
			case COUNT:
				while(this.mCompletionValue > this.mTweets.size()){
					Thread.sleep(1000);
				}
				break;

			case TIME_OUT:
				Thread.sleep(this.mCompletionValue);
				break;
			}
        	
        	// Close the stream.
	        twitterStream.shutdown();
	        
	        // Notify the listeners that the streaming is complete.
	        notifyComplete();
	        
        }catch(Exception ex){ }
	}
	
	public void addListener(TwitterMiningListener listener){
		synchronized (this.mListeners) {
			this.mListeners.add(listener);
		}
	}
	
	public void removeListener(TwitterMiningListener listener){
		synchronized (this.mListeners) {
			this.mListeners.remove(listener);
		}
	}
	
	/**
	 * @return the completionCondition
	 */
	public CompletionCondition getCompletionCondition() {
		return mCompletionCondition;
	}

	/**
	 * @param pCompletionCondition the completionCondition to set
	 */
	public void setCompletionCondition(CompletionCondition pCompletionCondition) {
		this.mCompletionCondition = pCompletionCondition;
	}

	/**
	 * @return the completionValue
	 */
	public long getCompletionValue() {
		return mCompletionValue;
	}

	/**
	 * @param pCompletionValue the completionValue to set
	 */
	public void setCompletionValue(long pCompletionValue) {
		this.mCompletionValue = pCompletionValue;
	}

	public void notifyStart() {
		synchronized (this.mListeners) {
			for(TwitterMiningListener listener : this.mListeners){
				listener.onMiningStart();
			}
		}
	}
	
	public void notifyComplete() {
		synchronized (this.mListeners) {
			for(TwitterMiningListener listener : this.mListeners){
				listener.onMiningComplete(this.mTweets);
			}
		}
	}
	
	public void addKeywords(String[] pKeywords){
		for(String keyword : pKeywords){
			this.mKeywords.add(keyword);
		}
	}
	
	public void addKeywords(Collection<String> pKeywords){
		this.mKeywords.addAll(pKeywords);
	}
	
	public void addKeyword(String pKeyword){
		this.mKeywords.add(pKeyword);
	}
	
	public void removeKeyword(String pKeyword){
		this.mKeywords.remove(pKeyword);
	}
	
	public void addLanguages(String[] pLanguages){
		for(String language : pLanguages){
			this.mLanguages.add(language);
		}
	}
	
	public void addLanguages(Collection<String> pLanguages){
		this.mLanguages.addAll(pLanguages);
	}
	
	public void addLanguage(String pLanguage){
		this.mLanguages.add(pLanguage);
	}
	
	public void removeLanguage(String pLanguage){
		this.mLanguages.remove(pLanguage);
	}
	
	@Override
    public void onException(Exception arg0) 
    { }

    @Override
    public void onDeletionNotice(StatusDeletionNotice arg0)
    { }

    @Override
    public void onScrubGeo(long arg0, long arg1)
    { }

    @Override
    public void onStatus(Status pStatus)  {
    	synchronized (this) {
    		this.mTweets.add(new Tweet(pStatus));	
		}
    }

    @Override
    public void onTrackLimitationNotice(int arg0) 
    { }

	@Override
	public void onStallWarning(StallWarning arg0) 
	{ }
	
}
