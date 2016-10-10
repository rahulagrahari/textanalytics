package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger;

import java.util.List;
import java.util.concurrent.Callable;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
public abstract class Tagger  extends Unit implements Callable<Void>{

	Tagger(String modelPath) {
		super(modelPath);
		
	}

	public abstract String[] tag(List<String> words) ;
}
