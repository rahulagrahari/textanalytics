package it.uniroma1.lcl.supWSD.modules.parser.xml.semeval13;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval7.SemEval7Attributes;
import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval7.SemEval7Handler;

/**
 * @author Simone Papandrea
 *
 */
public class SemEval13Handler extends SemEval7Handler {
	
	private String mInstance,mWF,mLemma,mPOS;
	
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		SemEval13Tags tag;

		tag = SemEval13Tags.valueOf(name.toUpperCase());

		switch (tag) {

		case INSTANCE:

			mInstance="";
			mID= attributes.getValue(SemEval13Attributes.ID.name().toLowerCase());
			mLemma=attributes.getValue(SemEval7Attributes.LEMMA.name().toLowerCase());
			mPOS=attributes.getValue(SemEval7Attributes.POS.name().toLowerCase());
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

		SemEval13Tags tag = SemEval13Tags.valueOf(name.toUpperCase());

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
			
			addWord(Annotation.ANNOTATION_TAG + mInstance.trim().replaceAll("[\\s\\-]", "_")+Annotation.ANNOTATION_TAG+" ");
			addInstance(mLemma.trim().replaceAll("[\\s\\-]", "_").toLowerCase()+"."+mPOS);
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
	public void characters(char ch[], int start, int length) throws SAXException {
		
		switch ((SemEval13Tags)this.get()) {

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
