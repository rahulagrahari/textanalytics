package it.si3p.supwsd.modules.preprocessing;

import it.si3p.supwsd.data.Annotation;

/**
 * @author papandrea
 *
 */
public interface Preprocessor {

	public void load() throws Exception;
	public void unload();
	public void execute(Annotation annotation);

}
