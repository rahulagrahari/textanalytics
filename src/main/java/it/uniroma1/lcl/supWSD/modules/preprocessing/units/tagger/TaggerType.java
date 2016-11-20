package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger;

/**
 * @author Simone Papandrea
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
