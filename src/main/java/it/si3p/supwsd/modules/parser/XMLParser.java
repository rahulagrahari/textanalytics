package it.si3p.supwsd.modules.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.modules.parser.xml.XMLHandler;

/**
 * @author papandrea
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

		mSAXParser.parse(new FileInputStream(file), mHandler);
	}

	@Override
	public void notifyAnnotations(List<Annotation> annotations) throws SAXException  {

		try {
			notifyAnnotations(annotations,null) ;
		
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}

	@Override
	public void notifyAnnotations(List<Annotation> annotations,String instance) throws SAXException  {

		try {
			this.mParserListener.annotationsReady(annotations,instance);
		
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}
}
