import java.io.IOException;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class PruebaStream {

	public static void main(String[] args) throws TwitterException, IOException{
		
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	      .setOAuthConsumerKey("eBTp6EMtomlH82TIwXUm7rX4h")
	      .setOAuthConsumerSecret("l5iC52danwX2cpDYKoaB8UBM346JnxZW3qDpHX4zKmGEvSVrka")
	      .setOAuthAccessToken("2388407179-r6FijmkiB0VDUU1B6v44Uw2SToh1p8KC7tyVc9F")
	      .setOAuthAccessTokenSecret("jAElhTX3NkjXMGSOfOCYWSGQB2BfkFAtzw11dMpQ5CvKC");

	    TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    
	    StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatus(Status status) {
                User user = status.getUser();
                
                // gets Username
                String username = status.getUser().getScreenName();
                System.out.println(username);
                String profileLocation = user.getLocation();
                System.out.println(profileLocation);
                long tweetId = status.getId(); 
                System.out.println(tweetId);
                String content = status.getText();
                System.out.println(content +"\n");
                

            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}

        };
        FilterQuery fq = new FilterQuery();
    
        String keywords[] = {"cold"};

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq); 
	    
	}
	
	

}
