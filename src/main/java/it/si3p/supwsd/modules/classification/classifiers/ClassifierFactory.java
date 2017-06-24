package it.si3p.supwsd.modules.classification.classifiers;


/**
 * @author papandrea
 *
 */
public class ClassifierFactory {

	private static ClassifierFactory instance;

	private ClassifierFactory() {

	}

	public static ClassifierFactory getInstance() {

		if (instance == null)
			instance = new ClassifierFactory();

		return instance;
	}

	public Classifier<?,?> getClassifier(ClassifierType classifierType){
		
		Classifier<?,?> classifier = null;

		switch (classifierType) {

		case LIBSVM:
			classifier = new LibSVMClassifier();
			break;
			
		default:
			classifier = new LibLinearClassifier();
		}
		
		return classifier;
	}
}
