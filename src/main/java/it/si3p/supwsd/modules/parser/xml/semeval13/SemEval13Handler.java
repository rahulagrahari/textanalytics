package it.si3p.supwsd.modules.parser.xml.semeval13;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import it.si3p.supwsd.modules.parser.xml.semeval7.SemEval7Attribute;
import it.si3p.supwsd.modules.parser.xml.semeval7.SemEval7Handler;

/**
 * @author papandrea
 *
 */
public class SemEval13Handler extends SemEval7Handler {
	
	private String mInstance,mWF,mLemma,mPOS;
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) {

		SemEval13Tag tag;

		tag = SemEval13Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case INSTANCE:

			mInstance="";
			mID= attributes.getValue(SemEval13Attribute.ID.name().toLowerCase());
			mLemma=attributes.getValue(SemEval7Attribute.LEMMA.name().toLowerCase());
			mPOS=attributes.getValue(SemEval7Attribute.POS.name().toLowerCase());
			break;

		case WF:
			mWF="";
			break;
			
		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval13Tag tag = SemEval13Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case TEXT:

			notifyAnnotations();
			break;
			
		case SENTENCE:

			addAnnotation();
			break;

		case INSTANCE:
			
			if(mPOS==null)
				mPOS="n";
			
			addWord(formatAnnotation(mInstance));
			addInstance(formatInstance(mLemma)+"."+mPOS);
			break;
			
		case WF:
			
			this.addWord(mWF.trim()+" ");
			break;
			
		default:
			break;
		}

		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) {
		
		switch ((SemEval13Tag)this.get()) {

		case WF:

			mWF+= new String(ch, start, length).replaceAll("[\r\n]", " ");
			break;

		case INSTANCE:
			
			mInstance+=new String(ch, start, length).replaceAll("[\r\n]", " ");
			break;
			
		default:
			break;
		}
	}

}
