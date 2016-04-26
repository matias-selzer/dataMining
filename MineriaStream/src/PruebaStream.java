import java.io.IOException;
import java.util.Collection;

import com.mining.TwitterMiner;
import com.mining.TwitterMiner.CompletionCondition;
import com.mining.TwitterMiningListener;
import com.models.Tweet;

import src.com.arff.Tweet2Arff;
import twitter4j.*;

public class PruebaStream {

	public static void main(String[] args) 
			throws TwitterException, IOException {
		
        TwitterMiner miner = new TwitterMiner();
        
        miner.setCompletionCondition(CompletionCondition.COUNT);
        miner.setCompletionValue(100);
        
        miner.addKeywords(new String[]{
			"boca",
		});
        
        miner.addLanguages(new String[]{
        	TwitterMiner.LANGUAGE_SPANISH
        });
        
        miner.addListener(new TwitterMiningListener() {
			
			@Override
			public void onMiningStart() { }
			
			@Override
			public void onMiningComplete(Collection<Tweet> pTweets) {
				try {
					Tweet2Arff tweet2arff = new Tweet2Arff();		        
			        tweet2arff.saveTweets(
			        		pTweets,
			        		"Twitter",
			        		"pepe.arff");
				} catch(Exception ex) {
				}
				
			}
		});
        
        miner.start();	    
	}
}
