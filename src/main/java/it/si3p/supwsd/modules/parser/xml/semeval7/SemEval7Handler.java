package it.si3p.supwsd.modules.parser.xml.semeval7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.modules.parser.xml.XMLHandler;

/**
 * @author papandrea
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
	public void startElement(String uri, String localName, String name, Attributes attributes) {

		SemEval7Tag tag;

		tag = SemEval7Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case INSTANCE:

			mInstance="";
			mID = attributes.getValue(SemEval7Attribute.ID.name().toLowerCase());
			mLemma=attributes.getValue(SemEval7Attribute.LEMMA.name().toLowerCase());
			mPOS=attributes.getValue(SemEval7Attribute.POS.name().toLowerCase());
			break;

		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SemEval7Tag tag = SemEval7Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case TEXT:

			notifyAnnotations();
			break;
			
		case SENTENCE:

			addAnnotation();
			break;

		case INSTANCE:
			
			addWord(formatAnnotation(mInstance));
			addInstance(formatInstance(mLemma)+"."+mPOS);
			break;

		default:
			break;
		}

		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) {

		String word;

		switch ((SemEval7Tag) this.get()) {

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


	protected String formatInstance(String lemma){
		
		return lemma.trim().replaceAll("[\\s\\-]", "_").toLowerCase();
	}
	
	protected final void notifyAnnotations() throws SAXException {
		
		try {
			mAnnotationListener.notifyAnnotations(mAnnotations);
		} catch (Exception e) {
			throw new SAXException(e);
		}
		
		mAnnotations.clear();
	}

	protected final void addAnnotation() {

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
