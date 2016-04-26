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

public class Tweet2Arff {
	
	// Members
	private StringToWordVector mFilter;
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
	}
	
    public void saveTweets(
		  Collection<Tweet> pTweets,
		  String pFileName,
		  String pRelationshipName) throws Exception {
    
    	Instances input_instances;
    	Instances output_instances;
    	
    	FastVector atts = new FastVector(2);
        atts.addElement(new Attribute("filename", (FastVector) null));
        atts.addElement(new Attribute("contents", (FastVector) null));

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
    
		// 5. Save data to ARFF file
		ArffSaver saver = new ArffSaver();
		saver.setInstances(output_instances);
		saver.setFile(new File("./data/" + pFileName));
		saver.writeBatch();    
    }
    
    /**
     * Method that converts a tweet into an instance.
     */
    private void makeInstanceFromTweet(Tweet pTweet, Instances pData) {

    	double[] newInst = new double[2];
    	
        newInst[0] = (double)pData.attribute(0).addStringValue(pTweet.getUserName());
        newInst[1] = (double)pData.attribute(1).addStringValue(pTweet.getContent());
        
        pData.add(new Instance(1.0, newInst));
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
