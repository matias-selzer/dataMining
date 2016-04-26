package src.com.arff;

import java.util.ArrayList;

import com.models.Tweet;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterMiner implements StatusListener{

	private ArrayList<Tweet> mTweets;
	
	public TwitterMiner() {
		this.mTweets = new ArrayList<Tweet>();
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    
	    cb.setDebugEnabled(true)
	      .setOAuthConsumerKey("eBTp6EMtomlH82TIwXUm7rX4h")
	      .setOAuthConsumerSecret("l5iC52danwX2cpDYKoaB8UBM346JnxZW3qDpHX4zKmGEvSVrka")
	      .setOAuthAccessToken("2388407179-r6FijmkiB0VDUU1B6v44Uw2SToh1p8KC7tyVc9F")
	      .setOAuthAccessTokenSecret("jAElhTX3NkjXMGSOfOCYWSGQB2BfkFAtzw11dMpQ5CvKC");

	    TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    
	    FilterQuery fq = new FilterQuery();
	    
        String keywords[] = {"macri"};

        fq.track(keywords);

        twitterStream.addListener(this);
        twitterStream.filter(fq);
        
        try {
        	
        	while(this.mTweets.size() < 5){
		        Thread.sleep(1000);
        	}

	        twitterStream.shutdown();

	        Tweet2Arff tweet2arff = new Tweet2Arff();
	        
	        tweet2arff.saveTweets(
	        		this.mTweets,
	        		"Twitter",
	        		"pepe.arff");
	        
        }catch(Exception ex){
        }
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
