package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;

import java.util.concurrent.Callable;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
public abstract class Lemmatizer  extends Unit implements Callable<Void>{

	Lemmatizer(String modelPath) {
		super(modelPath);
		
	}

	public abstract String[] lemmatize(String[] words,String[] POS) ;
	

}
