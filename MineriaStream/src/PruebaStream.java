import java.io.IOException;
import java.util.Collection;

import com.mining.TwitterMiner;
import com.mining.TwitterMiner.CompletionCondition;
import com.mining.TwitterMiningListener;
import com.models.Tweet;

import src.com.arff.Tweet2Arff;
import twitter4j.*;

public class PruebaStream {

	public final static Tweet2Arff tweet2arff = new Tweet2Arff();	
	
	public static void main(String[] args) 
			throws TwitterException, IOException {
		
        TwitterMiner miner = new TwitterMiner();
        
        miner.setCompletionCondition(
        		CompletionCondition.COUNT,
        		5);
        
        miner.addKeywords(new String[]{
			"macri",
		});
        
        miner.addLanguages(new String[]{
        	TwitterMiner.LANGUAGE_SPANISH
        });
        
        miner.addListener(new TwitterMiningListener() {
			@Override
			public void onMiningStart() { }
			
			@Override
			public void onMiningComplete(Collection<Tweet> pTweets) {
				save(pTweets);				
			}
		});

        try {
        save(tweet2arff.readTweets("./data/Twitter_data.arff"));
        } catch(Exception ex){ }
         
        /*
        try{
        	miner.start();	    
        } catch(Exception ex){
        	System.err.println(ex.getMessage());
        }
        */
	}
	
	
	public static void save(Collection<Tweet> pTweets) {
		try {
				        
	        tweet2arff.saveTweets(
	        		pTweets,
	        		"Twitter",
	        		"pepe.arff");
		} catch(Exception ex) { 
			System.err.println(ex.getMessage());
		}
	}
}
