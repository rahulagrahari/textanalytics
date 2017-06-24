package it.si3p.supwsd.modules.preprocessing.units.tagger;

import java.util.List;

/**
 * @author papandrea
 *
 */
public interface Tagger {


	public abstract String[] tag(List<String> words) ;
}
