package it.si3p.supwsd.inventory;

/**
 * @author papandrea
 *
 */
public enum SenseInventoryType {

	WORDNET("wordnet"),
	BABELNET("babelnet");
	
private final String mValue;
	
	private SenseInventoryType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}
