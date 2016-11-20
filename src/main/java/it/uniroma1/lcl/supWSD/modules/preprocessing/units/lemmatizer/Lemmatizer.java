package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;


/**
 * @author Simone Papandrea
 *
 */
public interface Lemmatizer {


	public String[] lemmatize(String[] words,String[] POS) ;
	

}
