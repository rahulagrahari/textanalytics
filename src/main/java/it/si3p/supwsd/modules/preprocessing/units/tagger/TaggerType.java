package it.si3p.supwsd.modules.preprocessing.units.tagger;

/**
 * @author papandrea
 *
 */
public enum TaggerType {

	OPEN_NLP("open_nlp"),
	STANFORD("stanford"),
	SIMPLE("simple"),
	TREE_TAGGER("tree_tagger");
	
	private final String mValue;
	
	private TaggerType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}
