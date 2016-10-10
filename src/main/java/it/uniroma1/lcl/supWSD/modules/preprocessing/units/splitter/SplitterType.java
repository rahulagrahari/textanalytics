package it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter;

/**
 * @author Simone Papandrea
 *
 */
public enum SplitterType {

	OPEN_NLP("open_nlp"),
	STANFORD("stanford"),
	SIMPLE("simple");
	
	private final String mValue;
	
	private SplitterType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}
