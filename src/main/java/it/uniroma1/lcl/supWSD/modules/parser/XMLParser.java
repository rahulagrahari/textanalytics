package it.uniroma1.lcl.supWSD.modules.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.modules.parser.xml.XMLHandler;

/**
 * @author Simone Papandrea
 *
 */
public class XMLParser extends Parser implements AnnotationListener {

	private SAXParser mSAXParser;
	private XMLHandler mHandler;

	protected XMLParser(XMLHandler handler) throws ParserConfigurationException, SAXException {

		SAXParserFactory factory = SAXParserFactory.newInstance();

		mSAXParser = factory.newSAXParser();
		mHandler = handler;
		mHandler.setHandlerListener(this);
	}

	@Override
	public void parse(String file) throws SAXException, IOException  {

		InputStream inputStream;

		inputStream = new FileInputStream(file);
		mSAXParser.parse(inputStream, mHandler);
	}

	@Override
	public void notifyAnnotations(List<Annotation> mAnnotations) throws SAXException  {

		try {
			this.mParserListener.annotationsReady(mAnnotations);
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}

}
