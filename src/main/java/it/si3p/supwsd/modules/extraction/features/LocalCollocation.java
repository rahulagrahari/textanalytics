package it.si3p.supwsd.modules.extraction.features;

import java.util.Objects;

/**
 * @author papandrea
 *
 */
public class LocalCollocation extends POSTag{

	private static final String KEY_PREFIX="C";
	private static final String DEFAULT="!DEF!";
	private final int mJ;

	
	public LocalCollocation(int i, int j, String value) {
		
		super(KEY_PREFIX+i+j,i,value.toLowerCase());

		this.mJ=j;
	}

	@Override
	public int hashCode(){
		
		return Objects.hash(this.getJ());
	}
	
	@Override 
	public boolean equals(Object arg){
		
		if(arg instanceof LocalCollocation){
			
			LocalCollocation featureType=(LocalCollocation) arg;
			
			return  this.getJ()==featureType.getJ();
		}
		
		return false;
	}

	
	final int getJ() {
		
		return this.mJ;
	}

	@Override
	public String getDefaultValue() {

		return DEFAULT;
	}
	

}
