package it.si3p.supwsd.modules.preprocessing.units.lemmatizer;

/**
 * @author papandrea
 *
 */
public enum LemmatizerType {

	JWNL("jwnl"),
	STANFORD("stanford"),
	TREE_TAGGER("tree_tagger"),
	SIMPLE("simple"),
	OPEN_NLP("open_nlp");
	
	private final String mValue;
	
	private LemmatizerType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}

