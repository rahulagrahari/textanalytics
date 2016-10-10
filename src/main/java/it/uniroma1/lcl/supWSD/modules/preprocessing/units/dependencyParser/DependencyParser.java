package it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser;

import java.util.concurrent.Callable;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author Simone Papandrea
 *
 */
public abstract class DependencyParser  extends Unit implements Callable<Void>{

	DependencyParser(String modelPath) {
		
		super(modelPath);
		
	}

	public abstract DependencyTree parse(String[]lemmas,String[] POS) ;
}