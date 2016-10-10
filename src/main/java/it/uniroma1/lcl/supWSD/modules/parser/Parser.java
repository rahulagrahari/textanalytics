package it.uniroma1.lcl.supWSD.modules.parser;

import java.util.List;

import it.uniroma1.lcl.supWSD.data.Annotation;

/**
 * @author Simone Papandrea
 *
 */
public abstract class Parser  {

	protected ParserListener mParserListener;
	

	public abstract void parse(String annotationsFile)throws Exception;


	public final void setParserListener(ParserListener parserListener) {

		this.mParserListener = parserListener;
	}

	public interface ParserListener {

		public void annotationsReady(List<Annotation> annotations) throws Exception;
	}


}
