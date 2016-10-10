package it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter;

import java.util.concurrent.Callable;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
public abstract class Splitter  extends Unit implements Callable<Void>{

	Splitter(String modelPath) {
		super(modelPath);
		
	}

	public abstract String[] split(String sentence) ;
}
