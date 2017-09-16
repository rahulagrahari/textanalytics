package it.si3p.supwsd.modules.parser.xml.semeval7;


import org.xml.sax.SAXException;


/**
 * @author papandrea
 *
 */
public class SemEval7HandlerLight extends SemEval7Handler {


	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval7Tag tag = SemEval7Tag.valueOf(name.toUpperCase());

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
