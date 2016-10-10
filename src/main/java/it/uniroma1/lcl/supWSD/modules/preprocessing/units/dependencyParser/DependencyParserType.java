package it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser;

/**
 * @author Simone Papandrea
 *
 */
public enum DependencyParserType {

	STANFORD("stanford");
	
	private final String mValue;
	
	private DependencyParserType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}

