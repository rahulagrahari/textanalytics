package it.uniroma1.lcl.supWSD.modules.classification.classifiers;

/**
 * @author Simone Papandrea
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
