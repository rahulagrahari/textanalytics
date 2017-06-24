package it.si3p.supwsd.modules.extraction.features;

/**
 * @author papandrea
 *
 */
public abstract class Feature implements Comparable<Feature>{

	private final String mName;
	private final double mValue;
	private final String mKey;
	private int mIndex=-1;
	
	
	public Feature(String key,String name) {

		this(key,name,1);
	}
	
	public Feature(String key,String name,double value) {

		this.mKey=key;
		this.mName = name;
		this.mValue=value;
	}


	public final String getName(){
		
		return this.mName;
	}

	public final int getIndex(){
		
		return this.mIndex;
	}
	
	public final void setIndex(int index){
		
		this.mIndex=index;
	}

	public final double getValue(){
		
		return this.mValue;
	}
	
	public final String getKey() {
	
		return mKey;
	}

	@Override
	public final int compareTo(Feature arg0) {
		
		return Integer.compare(this.getIndex(),arg0.getIndex());
	}
	
	
	public abstract String getDefaultValue();
	
}
