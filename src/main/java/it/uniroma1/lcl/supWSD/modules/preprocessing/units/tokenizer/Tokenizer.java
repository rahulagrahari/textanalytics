package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

import java.util.concurrent.Callable;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
 public abstract class Tokenizer  extends Unit implements Callable<Void>{

	Tokenizer(String modelPath) {
		super(modelPath);		
	}

	public abstract String[] tokenize(String sentence) ;
}
