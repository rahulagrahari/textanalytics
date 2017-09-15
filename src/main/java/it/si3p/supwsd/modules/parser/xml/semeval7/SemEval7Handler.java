package it.si3p.supwsd.modules.parser.xml.semeval7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private int mIndex = 0;
	protected String mID;
	private String mLemma, mPOS;
	protected final Map<Lexel, String> mSentences;
	private final Map<String, List<Annotation>> mAnnotations;
	private String mInstance = "";
	private String mSentence = "";

	public SemEval7Handler() {

		mAnnotations = new HashMap<String, List<Annotation>>();
		mSentences = new HashMap<Lexel, String>();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) {

		SemEval7Tag tag;

		tag = SemEval7Tag.valueOf(name.toUpperCase());

		switch (tag) {

		case INSTANCE:

			mInstance = "";
			mID = attributes.getValue(SemEval7Attribute.ID.name().toLowerCase());
			mLemma = attributes.getValue(SemEval7Attribute.LEMMA.name().toLowerCase());
			mPOS = attributes.getValue(SemEval7Attribute.POS.name().toLowerCase());
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

		case CORPUS:

			notifyAnnotations();
			break;

		case SENTENCE:

			addAnnotation();
			break;

		case INSTANCE:

			addInstance(mInstance, formatInstance(mLemma) + "." + mPOS);
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
			mInstance += word;
			break;

		default:
			break;
		}
	}

	protected final void addWord(String word) {

		word=word.trim();
		
		if (!word.isEmpty()) {

			for (Entry<Lexel, String> entry : mSentences.entrySet())
				mSentences.put(entry.getKey(), entry.getValue() + word+" ");

			mSentence += word+" ";
		}
	}

	protected Lexel addInstance(String word, String instance) {

		String sentence=mSentence;
		Lexel lexel = null;
		
		addWord(word);	
		
		if (!instance.isEmpty()) {	
			lexel=new Lexel(mID, instance);
			mSentences.put(lexel, sentence + formatAnnotation(word)+" ");
		}
		
		return lexel;
	}

	protected String formatInstance(String lemma) {

		return lemma.trim().replaceAll("[\\s\\-]", "_").toLowerCase();
	}

	protected final void notifyAnnotations() throws SAXException {

		try {

			for (List<Annotation> annotations : mAnnotations.values())
				mAnnotationListener.notifyAnnotations(annotations);
		} catch (Exception e) {
			throw new SAXException(e);
		}

		mAnnotations.clear();
	}

	protected final void addAnnotation() {

		List<Annotation> annotations;
		Annotation annotation;
		String name, sentence;
		Lexel lexel;

		for (Entry<Lexel, String> entry : mSentences.entrySet()) {

			sentence = entry.getValue().trim();
			lexel = entry.getKey();			
			name = lexel.toString();
			annotations = mAnnotations.get(name);

			if (annotations == null) {

				annotations = new ArrayList<Annotation>();
				mAnnotations.put(name, annotations);
			}

			annotation = new Annotation(mIndex, sentence);
			annotation.addLexel(lexel);
			annotations.add(annotation);
			mIndex++;
		}

		mSentences.clear();
		mSentence = "";
	}

}
