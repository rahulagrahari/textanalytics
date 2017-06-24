package it.si3p.supwsd.modules.preprocessing.units.lemmatizer;


/**
 * @author papandrea
 *
 */
public interface Lemmatizer {


	public String[] lemmatize(String[] words,String[] POS) ;
	

}
