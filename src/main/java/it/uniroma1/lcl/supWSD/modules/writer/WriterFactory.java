package it.uniroma1.lcl.supWSD.modules.writer;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * @author Simone Papandrea
 *
 */
public class WriterFactory {

	private static WriterFactory instance;

	private WriterFactory() {

	}

	public static WriterFactory getInstance() {

		if (instance == null)
			instance = new WriterFactory();

		return instance;
	}

	public Writer getWriter(WriterType WriterType) throws ParserConfigurationException, SAXException {

		Writer writer = null;

		switch (WriterType) {

		case PLAIN:
			writer = new PlainWriter();
			break;

		case SINGLE:
			writer = new SingleWriter();
			break;
			
		default:
			writer = new AllWriter();
			break;
		}

		return writer;
	}
}
