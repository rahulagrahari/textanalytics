package it.uniroma1.lcl.supWSD.modules.parser;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import it.uniroma1.lcl.supWSD.modules.parser.xml.lexical.LexicalHandler;
import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval13.SemEval13Handler;
import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval15.SemEval15Handler;
import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval7.SemEval7Handler;
import it.uniroma1.lcl.supWSD.modules.parser.xml.senseval.SensEvalHandler;

/**
 * @author Simone Papandrea
 *
 */
public class ParserFactory {

	private static ParserFactory instance;

	private ParserFactory() {

	}

	public static ParserFactory getInstance() {

		if (instance == null)
			instance = new ParserFactory();

		return instance;
	}

	public Parser getParser(ParserType parserType) throws ParserConfigurationException, SAXException {

		Parser parser = null;

		switch (parserType) {

		case SENSEVAL:

			parser = new XMLParser(new SensEvalHandler());
			break;

		case SEMEVAL7:

			parser = new XMLParser(new SemEval7Handler());
			break;
			
		case SEMEVAL13:

			parser = new XMLParser(new SemEval13Handler());
			break;

		case SEMEVAL15:

			parser = new XMLParser(new SemEval15Handler());
			break;

		case LEXICAL:
			parser = new XMLParser(new LexicalHandler());
			break;
			
		case PLAIN:
			
			parser=new PlainParser();
			break;
		}
		
		return parser;
	}
}
