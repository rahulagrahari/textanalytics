package it.si3p.supwsd.modules.extraction.features;

import java.util.Objects;

/**
 * @author papandrea
 *
 */
public class SurroundingWord extends Feature{
	
	private static final String DEFAULT=null;
	private static final String KEY_PREFIX="SW_";
	
	
	public SurroundingWord(String value) {
		
		super(KEY_PREFIX+value,"1");
		
	}

	
	@Override
	public int hashCode(){
		
		return Objects.hash(this.getKey());
	}

	@Override 
	public boolean equals(Object arg){
		
		if(arg instanceof SurroundingWord){
			
			SurroundingWord feature=(SurroundingWord) arg;
			
			return this.getKey().equals(feature.getKey());
		}
		
		return false;
	}
	
	
	@Override
	public String getDefaultValue() {
		
		return DEFAULT;
	}
	

}
