package it.uniroma1.lcl.supWSD.modules.parser.xml.semeval7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.parser.xml.XMLHandler;

/**
 * @author Simone Papandrea
 *
 */
public class SemEval7Handler extends XMLHandler {

	private int mIndex=0;
	protected String mID;
	private String mSentence = "",mLemma,mPOS;
	protected final Map<String,Lexel>mLexels;
	private final List<Annotation>mAnnotations;
	private String mInstance="";
	
	public SemEval7Handler() {

		mLexels = new HashMap<String,Lexel>();
		mAnnotations=new ArrayList<Annotation>();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		SemEval7Tags tag;

		tag = SemEval7Tags.valueOf(name.toUpperCase());

		switch (tag) {

		case INSTANCE:

			mInstance="";
			mID = attributes.getValue(SemEval7Attributes.ID.name().toLowerCase());
			mLemma=attributes.getValue(SemEval7Attributes.LEMMA.name().toLowerCase());
			mPOS=attributes.getValue(SemEval7Attributes.POS.name().toLowerCase());
			break;

		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval7Tags tag = SemEval7Tags.valueOf(name.toUpperCase());

		switch (tag) {

		case TEXT:

			notifyAnnotations();
			break;
			
		case SENTENCE:

			addAnnotation();
			break;

		case INSTANCE:
			
			addWord(Annotation.ANNOTATION_TAG + mInstance.trim().replaceAll("[\\s\\-]", "_")+Annotation.ANNOTATION_TAG+" ");
			addInstance(mLemma.trim().replaceAll("[\\s\\-]", "_").toLowerCase()+"."+mPOS);
			break;

		default:
			break;
		}

		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		String word;

		switch ((SemEval7Tags) this.get()) {

		case SENTENCE:

			word = new String(ch, start, length).replaceAll("[\r\n]", " ");
			addWord(word);
			break;

		case INSTANCE:
			
			word = new String(ch, start, length).replaceAll("[\r\n]", " ");
			mInstance+=word;
			break;
	
		default:
			break;
		}
	}

	protected final void addWord(String word) {

		if (!word.isEmpty()) 
			mSentence += word;		
	}
	
	protected void addInstance(String instance) {

		if (!instance.isEmpty())
			mLexels.put(mID,new Lexel(mID, instance));	
	}

	protected final void notifyAnnotations() throws SAXException {
		
		try {
			mAnnotationListener.notifyAnnotations(mAnnotations);
		} catch (Exception e) {
			throw new SAXException(e);
		}
		
		mAnnotations.clear();
	}

	protected final void addAnnotation() throws SAXException {

		Annotation annotation;

		annotation=new Annotation(mIndex,mSentence.trim());
		
		for(Lexel lexel:mLexels.values())
			annotation.addLexel(lexel);
		
		mAnnotations.add(annotation);
		mLexels.clear();
		mSentence = "";
		mIndex++;
	}

}
