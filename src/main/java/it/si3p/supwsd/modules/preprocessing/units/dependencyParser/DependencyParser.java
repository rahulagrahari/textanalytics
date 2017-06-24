package it.si3p.supwsd.modules.preprocessing.units.dependencyParser;


import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author papandrea
 *
 */
public interface DependencyParser{


	public abstract DependencyTree parse(String[]lemmas,String[] POS) ;
}