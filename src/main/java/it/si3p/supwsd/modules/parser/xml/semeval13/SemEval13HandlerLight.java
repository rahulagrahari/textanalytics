package it.si3p.supwsd.modules.parser.xml.semeval13;

import org.xml.sax.SAXException;

/**
 * @author papandrea
 *
 */
public class SemEval13HandlerLight extends SemEval13Handler {
	

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval13Tag tag = SemEval13Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case CORPUS:
			this.pop();
			break;
			
		case TEXT:
			notifyAnnotations();
			this.pop();
			break;
				
		default:
			super.endElement(uri, localName, name);
			break;
		}
	}
}
