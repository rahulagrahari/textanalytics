package it.si3p.supwsd.modules.classification.classifiers;

/**
 * @author papandrea
 *
 */
public enum ClassifierType {

	LIBLINEAR("liblinear"),
	LIBSVM("libsvm");
	
	private final String mValue;
	
	private ClassifierType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}
