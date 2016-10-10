package it.uniroma1.lcl.supWSD.modules.parser.xml.semeval15;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval7.SemEval7Attributes;
import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval7.SemEval7Handler;

/**
 * @author Simone Papandrea
 *
 */
public class SemEval15Handler extends SemEval7Handler {
	
	private String mWF,mLemma,mPOS;
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		SemEval15Tags tag;

		tag = SemEval15Tags.valueOf(name.toUpperCase());

		switch (tag) {

		case WF:

			mLemma=attributes.getValue(SemEval15Attributes.LEMMA.name().toLowerCase());
			mPOS=attributes.getValue(SemEval7Attributes.POS.name().toLowerCase());
			mID = attributes.getValue(SemEval15Attributes.ID.name().toLowerCase());
			break;
			
		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval15Tags tag = SemEval15Tags.valueOf(name.toUpperCase());

		switch (tag) {

		case SENTENCE:

			this.notifyAnnotations();
			break;
			
		case WF:
			
			this.addWord(mWF.trim()+" ");
			
			if(mLemma!=null)
				addInstance(mLemma.trim().replaceAll("[\\s\\-]", "_").toLowerCase()+"."+mPOS);
			break;
			
		default:
			break;
		}

		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		
		switch ((SemEval15Tags)this.get()) {

		case WF:

			mWF+= new String(ch, start, length).replaceAll("[\r\n]", " ");
			break;

		default:
			break;
		}
	}

}
