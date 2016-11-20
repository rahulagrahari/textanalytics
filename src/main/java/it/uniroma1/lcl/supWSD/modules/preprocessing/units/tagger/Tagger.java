package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger;

import java.util.List;

/**
 * @author Simone Papandrea
 *
 */
public interface Tagger {


	public abstract String[] tag(List<String> words) ;
}
