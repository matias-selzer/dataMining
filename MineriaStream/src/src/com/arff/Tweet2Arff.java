package src.com.arff;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import com.models.Tweet;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.stemmers.SnowballStemmer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Tweet2Arff {
	
	public static int ATTR_POS_ID		 	 = 0;
	public static int ATTR_POS_USER_NAME 	 = 1;
	public static int ATTR_POS_USER_LOCATION = 2;
	public static int ATTR_POS_CONTENT 		 = 3;
	
	public static int ATTR_COUNT 			 = 4;
	
	// Members
	private StringToWordVector mFilter;
	private Normalize mNormalizeFilter;
	private NGramTokenizer mTokenizer;
	
	public Tweet2Arff(){
		// Set the tokenizer 
		// This tokenizer is able to recognize n-grams, that is, sequences of tokens.
		// the methods void setNGramMaxSize(int value) and void setNGramMinSize(int value)
		// to define the size of the n-grams as unigrams.
		this.mTokenizer = new NGramTokenizer(); 
		this.mTokenizer.setNGramMinSize(1); 
		this.mTokenizer.setNGramMaxSize(1); 
		this.mTokenizer.setDelimiters("\\W");
		
		// Set the filter
		// Any filter has to make reference to a dataset, which is the 
		// inputInstances dataset in this case, as done with the filter.setInputFormat(inputInstances) call.
		this.mFilter = new StringToWordVector();
		this.mFilter.setTokenizer(this.mTokenizer); 
		this.mFilter.setWordsToKeep(1000000); 
		this.mFilter.setDoNotOperateOnPerClassBasis(true); 
		this.mFilter.setLowerCaseTokens(true);
		this.mFilter.setStopwords(new File("res/stop-words/stop-words_spanish_1"));
		this.mFilter.setUseStoplist(true);
		
		this.mFilter.setStemmer(new SnowballStemmer());
		
		this.mNormalizeFilter = new Normalize();
	}
	
	public Collection<Tweet> readTweets(String pFilePath) throws Exception{
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
	    Instances data = makeInstancesFromFile(new File(pFilePath));
	    for(int i = 0; i < data.numInstances(); i++) {
	    	tweets.add(makeTweetFromInstance(data.instance(i)));
	    }
	    
		return tweets;		
	}
	
    public void saveTweets(
		  Collection<Tweet> pTweets,
		  String pFileName,
		  String pRelationshipName) throws Exception {
    
    	Instances input_instances;
    	Instances output_instances;
    	Instances normalized_output_instances;
    	
    	FastVector atts = new FastVector(ATTR_COUNT);
        atts.insertElementAt(new Attribute("id"), ATTR_POS_ID);
        atts.insertElementAt(new Attribute("username", (FastVector) null), ATTR_POS_USER_NAME);
        atts.insertElementAt(new Attribute("user_location", (FastVector) null), ATTR_POS_USER_LOCATION);
        atts.insertElementAt(new Attribute("content", (FastVector) null), ATTR_POS_CONTENT);
        

        // 2. create Instances object
        input_instances = new Instances(pRelationshipName, atts, 0);
        
		// 3. fill with data
		for(Tweet tweet : pTweets) {
			makeInstanceFromTweet(tweet, input_instances);
		}
		
		// 4. Apply the filter to the input twitter.
    	// Filter the input instances into the output ones.
		this.mFilter.setInputFormat(input_instances); 
		output_instances = Filter.useFilter(input_instances, this.mFilter);
		
		this.mNormalizeFilter.setInputFormat(output_instances);
		normalized_output_instances = Filter.useFilter(output_instances, this.mNormalizeFilter); 
		
		// 4. output data
		System.out.println(normalized_output_instances);

		// 5. Save data to ARFF file
		ArffSaver saver = new ArffSaver();
		
		// Save the TF-IDF instance.
		saver.setInstances(normalized_output_instances);
		saver.setFile(new File("./data/" + pFileName + ".arff"));
		saver.writeBatch();    
		
		// Save the input data.
		saver.setInstances(input_instances);
		saver.setFile(new File("./data/" + pFileName + "_data.arff"));
		saver.writeBatch();
    }
    
    /**
     * Method that converts a tweet into an instance.
     */
    private void makeInstanceFromTweet(Tweet pTweet, Instances pData) {

    	double[] newInst = new double[ATTR_COUNT];
    	
    	newInst[ATTR_POS_ID]		 	=  pTweet.getId();
        newInst[ATTR_POS_USER_NAME] 	= (double)pData.attribute(ATTR_POS_USER_NAME).addStringValue(pTweet.getUserName());
        newInst[ATTR_POS_USER_LOCATION] = (double)pData.attribute(ATTR_POS_USER_LOCATION).addStringValue(pTweet.getUserLocation());
        newInst[ATTR_POS_CONTENT] 		= (double)pData.attribute(ATTR_POS_CONTENT).addStringValue(pTweet.getContent());
        
        
        pData.add(new Instance(1.0, newInst));
    }

    private Tweet makeTweetFromInstance(Instance pInstance){
    	Tweet tweet = new Tweet();
    	
    	tweet.setId((long)pInstance.value(ATTR_POS_ID));
    	tweet.setUserName(pInstance.stringValue(ATTR_POS_USER_NAME));
    	tweet.setContent(pInstance.stringValue(ATTR_POS_CONTENT));
    	tweet.setUserLocation(pInstance.stringValue(ATTR_POS_USER_LOCATION));
    	
    	return tweet;
    }
    
    private Instances makeInstancesFromFile(File pFile) throws Exception{
    	
        InputStreamReader is = new InputStreamReader(new FileInputStream(pFile));
        Instances instances	 = new Instances(is);  
        is.close();
        
        return instances;
    }
    
	/**
	 * @return the filter
	 */
	public StringToWordVector getFilter() {
		return mFilter;
	}

	/**
	 * @param pFilter the filter to set
	 */
	public void setFilter(StringToWordVector pFilter) {
		this.mFilter = pFilter;
	}
}
