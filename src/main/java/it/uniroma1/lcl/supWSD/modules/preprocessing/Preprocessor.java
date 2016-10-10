package it.uniroma1.lcl.supWSD.modules.preprocessing;

import it.uniroma1.lcl.supWSD.data.Annotation;

/**
 * @author Simone Papandrea
 *
 */
public interface Preprocessor {

	public void load() throws Exception;
	public void unload();
	public void execute(Annotation annotation);

}
