package it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser;


import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author Simone Papandrea
 *
 */
public interface DependencyParser{


	public abstract DependencyTree parse(String[]lemmas,String[] POS) ;
}