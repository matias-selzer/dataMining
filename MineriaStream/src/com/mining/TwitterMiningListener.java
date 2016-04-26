package com.mining;

import java.util.Collection;

import com.models.Tweet;

public interface TwitterMiningListener {
	
	public void onMiningComplete(Collection<Tweet> pTweets);
	public void onMiningStart();
	
}
