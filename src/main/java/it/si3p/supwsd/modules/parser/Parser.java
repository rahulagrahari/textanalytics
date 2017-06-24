package it.si3p.supwsd.modules.parser;

import java.util.List;
import it.si3p.supwsd.data.Annotation;

/**
 * @author papandrea
 *
 */
public abstract class Parser  {

	protected ParserListener mParserListener;
	

	public abstract void parse(String annotationsFile)throws Exception;


	public final void setParserListener(ParserListener parserListener) {

		this.mParserListener = parserListener;
	}

	public interface ParserListener {

		public void annotationsReady(List<Annotation> annotations) throws Exception ;
	}


}
