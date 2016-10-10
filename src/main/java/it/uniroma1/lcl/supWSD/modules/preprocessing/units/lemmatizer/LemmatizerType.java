package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;

/**
 * @author Simone Papandrea
 *
 */
public enum LemmatizerType {

	JWNL("jwnl"),
	STANFORD("stanford"),
	SIMPLE("simple");
	
	private final String mValue;
	
	private LemmatizerType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}

