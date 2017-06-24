package it.si3p.supwsd.modules.preprocessing.units.dependencyParser;

/**
 * @author papandrea
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

