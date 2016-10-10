package it.uniroma1.lcl.supWSD.inventory;

/**
 * @author Simone Papandrea
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
