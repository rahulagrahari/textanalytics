package it.si3p.supwsd.modules.parser.xml.semeval15;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import it.si3p.supwsd.modules.parser.xml.semeval7.SemEval7Attribute;
import it.si3p.supwsd.modules.parser.xml.semeval7.SemEval7Handler;

/**
 * @author papandrea
 *
 */
public class SemEval15Handler extends SemEval7Handler {
	
	private String mWF,mLemma,mPOS;
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) {

		SemEval15Tag tag;

		tag = SemEval15Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case WF:

			mWF="";
			mLemma=attributes.getValue(SemEval15Attribute.LEMMA.name().toLowerCase());
			mPOS=attributes.getValue(SemEval7Attribute.POS.name().toLowerCase());
			mID = attributes.getValue(SemEval15Attribute.ID.name().toLowerCase());
			break;
			
		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval15Tag tag = SemEval15Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case CORPUS:

			notifyAnnotations();
			break;
			
		case SENTENCE:

			addAnnotation();
			break;
			
		case WF:
							
			if(mLemma!=null)
				addInstance(mWF,formatInstance(mLemma)+"."+mPOS);
			else
				addWord(mWF);
			
			break;
			
		default:
			break;
		}

		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) {
		
		switch ((SemEval15Tag)this.get()) {

		case WF:

			mWF+= new String(ch, start, length).replaceAll("[\r\n]", " ");
			break;

		default:
			break;
		}
	}

}
