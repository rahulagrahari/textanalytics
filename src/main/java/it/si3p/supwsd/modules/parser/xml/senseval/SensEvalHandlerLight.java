package it.si3p.supwsd.modules.parser.xml.senseval;


import org.xml.sax.SAXException;


/**
 * @author papandrea
 *
 */
public class SensEvalHandlerLight extends SensEvalHandler {

	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SensEvalTag tag = SensEvalTag.valueOf(name.toUpperCase());

		switch (tag) {

		case CORPUS:
			this.pop();
			break;
			
		case TEXT:

			super.endElement(uri, localName, name);
			notifyAnnotations();
			this.pop();
			break;

		default:
			super.endElement(uri, localName, name);
			break;
		}
	}
}