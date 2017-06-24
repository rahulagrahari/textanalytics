package it.si3p.supwsd.modules.preprocessing.units.splitter;

/**
 * @author papandrea
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
