package src.com.arff;

import java.io.File;
import java.util.Collection;

import com.models.Tweet;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Tweet2Arff2 {
	
	// Members
	private StringToWordVector mFilter;
	private NGramTokenizer mTokenizer;
	
	public Tweet2Arff2(){
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
		  String pFileName) throws Exception {
    
    	Instances input_instances;
    	Instances output_instances;
    	
		// 1. set up attributes
    	FastVector atts = new FastVector(2);
		
    	// Add attribute for holding messages.
    	atts.addElement(new Attribute("Message", (FastVector)null));
    	
    	// Add class attribute.
        FastVector classValues = new FastVector(2);
        classValues.addElement("miss");
        classValues.addElement("hit");
    	atts.addElement(new Attribute("Class", classValues));
        
		// 2. create Instances object
		// Create dataset with initial capacity of 100, and set index of class.
		input_instances = new Instances("Twitter", atts, 100);
		input_instances.setClassIndex(input_instances.numAttributes() - 1);
	    
		// 3. fill with data
		for(Tweet tweet : pTweets) {
			Instance instance = makeInstanceFromTweet(tweet, input_instances);
			
		    // Set class value for instance.
		    instance.setClassValue(Long.toString(tweet.getId()));

		    // Add instance to training data.
		    input_instances.add(instance);
		    
		    System.out.println(instance);
		}
				
		// 4. output data
		System.out.println(input_instances);
				
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
    
    /**
     * Method that converts a text message into an instance.
     */
    private Instance makeInstanceFromTweet(Tweet pTweet, Instances pData) {

      // Create instance of length two.
      Instance instance = new Instance(2);

      // Set value for message attribute
      Attribute messageAtt = pData.attribute("Message");
      instance.setValue(messageAtt, messageAtt.addStringValue(pTweet.getContent()));

      // Give instance access to attribute information from the dataset.
      instance.setDataset(pData);
      return instance;
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
