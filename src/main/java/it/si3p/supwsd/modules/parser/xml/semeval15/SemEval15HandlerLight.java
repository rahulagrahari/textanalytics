package it.si3p.supwsd.modules.parser.xml.semeval15;

import org.xml.sax.SAXException;

/**
 * @author papandrea
 *
 */
public class SemEval15HandlerLight extends SemEval15Handler{
	

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval15Tag tag = SemEval15Tag.valueOf(name.toUpperCase());

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
