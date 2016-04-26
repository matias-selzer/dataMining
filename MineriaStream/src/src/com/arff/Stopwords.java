package src.com.arff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

public class Stopwords {
	
	private HashSet<String> mStoplist;
	
	public Stopwords(String pFilepath){
		this.mStoplist = new HashSet<String>();
		
		File file = new File(pFilepath);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(!line.startsWith("#")){
		    		this.mStoplist.add(line);
		    	}
		    }
		} catch (Exception e) {
		}
	}
	
	public boolean contains(String word){
		return this.mStoplist.contains(word);
	}
}
