package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

/**
 * @author Simone Papandrea
 *
 */
public enum TokenizerType {

	OPEN_NLP("open_nlp"),
	PENN_TREE_BANK("penn_tree_bank"),
	STANFORD("stanford"),
	SIMPLE("simple");
	
	private final String mValue;
	
	private TokenizerType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}
