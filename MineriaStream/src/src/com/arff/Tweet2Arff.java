package src.com.arff;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import com.models.Tweet;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Tweet2Arff {
	
	// Members
	private StringToWordVector mFilter;
	private NGramTokenizer mTokenizer;
	
	private Stopwords mStopwords;
	
	public Tweet2Arff(){
		// Set the tokenizer 
		// This tokenizer is able to recognize n-grams, that is, sequences of tokens.
		// the methods void setNGramMaxSize(int value) and void setNGramMinSize(int value)
		// to define the size of the n-grams as unigrams.
		this.mTokenizer = new NGramTokenizer(); 
		this.mTokenizer.setNGramMinSize(3); 
		this.mTokenizer.setNGramMaxSize(10); 
		this.mTokenizer.setDelimiters("\\W");
		
		// Set the filter
		// Any filter has to make reference to a dataset, which is the 
		// inputInstances dataset in this case, as done with the filter.setInputFormat(inputInstances) call.
		this.mFilter = new StringToWordVector();
		this.mFilter.setTokenizer(this.mTokenizer); 
		this.mFilter.setWordsToKeep(1000000); 
		this.mFilter.setDoNotOperateOnPerClassBasis(true); 
		this.mFilter.setLowerCaseTokens(true);
		this.mFilter.setStopwords(new File("res/stopwords"));
		this.mFilter.setUseStoplist(true);
	}
	
    public void saveTweets(
		  Collection<Tweet> pTweets,
		  Collection<String> pTopics,
		  String pFileName) throws Exception {
    
    	Instances input_instances;
    	Instances output_instances;
    	
		// 1. set up attributes
    	FastVector atts;
		atts = new FastVector();
		
		for(String topic : pTopics){
			// - numeric attribute
			atts.addElement(new Attribute(topic));
		}

		// 2. create Instances object
		input_instances = new Instances("Twitter", atts, 0);

		// 3. fill with data
		for(Tweet tweet : pTweets) {
			makeInstanceFromTweet(tweet, input_instances);
		}
				
		this.mFilter.setInputFormat(input_instances); 
		
    	// Filter the input instances into the output ones 
		output_instances = Filter.useFilter(input_instances, this.mFilter);
    
		// 4. output data
		System.out.println(output_instances);
		
		// 5. Save data to ARFF file
		ArffSaver saver = new ArffSaver();
		saver.setInstances(output_instances);
		saver.setFile(new File("./data/" + pFileName));
		saver.writeBatch();    
    }
    
    public Collection<String> getTopicsFromTweets(Collection<Tweet> pTweets){
    	HashSet<String> topics = new HashSet<String>();
    	
    	// Foreach Tweet extract the topics.
		for(Tweet tweet : pTweets) {
			for(String topic : tweet.getContent().split("[\\W]")) {
				if(!topic.isEmpty()){
					
					if(this.mStopwords == null || !this.mStopwords.contains(topic)){
						topics.add(topic);
					}
					
				}
			}
		}
    	
    	return topics;
    }
    
    /**
     * Method that converts a text message into an instance.
     */
    private void makeInstanceFromTweet(Tweet pTweet, Instances pData) {

      // Create instance of length two.
      double[] tfs = new double[pData.numAttributes()];
      
      for(int i = 0; i <  pData.numAttributes(); i++){
    	  Attribute att = pData.attribute(i);
    	  String topic = att.name();
    	  
    	  tfs[i] = pTweet.getContent().split(topic, -1).length - 1;
      }
	
      // Give instance access to attribute information from the dataset.
      pData.add(new Instance(1.0, tfs));
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

	/**
	 * @return the stopwords
	 */
	public Stopwords getStopwords() {
		return mStopwords;
	}

	/**
	 * @param pStopwords the stopwords to set
	 */
	public void setStopwords(Stopwords pStopwords) {
		this.mStopwords = pStopwords;
	}
}
