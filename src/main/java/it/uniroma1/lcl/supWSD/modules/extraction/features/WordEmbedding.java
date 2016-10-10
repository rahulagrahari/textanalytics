package it.uniroma1.lcl.supWSD.modules.extraction.features;

import java.util.Objects;

/**
 * @author Simone Papandrea
 *
 */
public class WordEmbedding extends Feature{

	private static final String DEFAULT=null;
	private static final String KEY_PREFIX="WE_";

	public WordEmbedding(int index,double value) {
		
		super(KEY_PREFIX+index,String.valueOf(index),value);
		
	}

	
	@Override
	public int hashCode(){
		
		return Objects.hash(this.getKey());
	}

	@Override 
	public boolean equals(Object arg){
		
		if(arg instanceof WordEmbedding){
			
			WordEmbedding feature=(WordEmbedding) arg;
			
			return this.getKey().equals(feature.getKey());
		}
		
		return false;
	}
	
	

	@Override
	public String getDefaultValue() {
		
		return DEFAULT;
	}
	

}
