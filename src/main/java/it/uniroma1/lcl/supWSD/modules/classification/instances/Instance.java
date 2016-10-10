package it.uniroma1.lcl.supWSD.modules.classification.instances;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;

/**
 * @author Simone Papandrea
 *
 */
public class Instance implements Comparable<Instance> {

	private final String mID;
	private final String mWord;
	private final SortedSet<Feature> mFeatures;
	private final SortedSet<String> mSenses;
	
	public Instance(String id,String word,Collection<Feature> features,SortedSet<String>  senses) {

		this.mID=id;
		this.mWord=word;
		this.mFeatures=new TreeSet<Feature>(features);
		this.mSenses=senses;
	}

	public String getID() {

		return this.mID;
	}

	public String getWord() {

		return this.mWord;
	}
	
	public SortedSet<Feature> getFeatures() {

		return this.mFeatures;
	}	

	public SortedSet<String> getSenses(){
		
		return this.mSenses;
	}

	@Override
	public int compareTo(Instance o) {
	
		return this.getID().compareTo(o.getID());
	}

	@Override
	public boolean equals(Object o){
		
		boolean result=false;
		
		if(o instanceof Instance){
			
			Instance instance=(Instance) o;
			
			result=this.getID().equals(instance.getID());
		}
		
		return result;
	}
	
	@Override
	public int hashCode(){
		
		return this.getID().hashCode();
	}
}
