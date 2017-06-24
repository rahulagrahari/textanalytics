package it.si3p.supwsd.modules.classification.instances;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import it.si3p.supwsd.data.Token;
import it.si3p.supwsd.modules.extraction.features.Feature;

/**
 * @author papandrea
 *
 */
public class Instance implements Comparable<Instance> {

	private final String mID;
	private final Token mToken;
	private final SortedSet<Feature> mFeatures;
	private final SortedSet<String> mSenses;
	
	public Instance(String id,Collection<Feature> features,SortedSet<String>  senses, Token token) {

		this.mID=id;
		this.mToken=token;
		this.mFeatures=new TreeSet<Feature>(features);
		this.mSenses=senses;
	}

	public String getID() {

		return this.mID;
	}

	public Token getToken() {

		return this.mToken;
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
