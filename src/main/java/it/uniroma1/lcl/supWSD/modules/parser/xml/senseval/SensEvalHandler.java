package it.uniroma1.lcl.supWSD.modules.parser.xml.senseval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.parser.xml.semeval7.SemEval7Handler;

/**
 * @author Simone Papandrea
 *
 */
public class SensEvalHandler extends SemEval7Handler {

	private final Map<String, String> mSATids;
	private final List<String> mSATS;
	private final Pattern mBPattern= Pattern.compile("'([sm]|re)");
	private final Pattern mXPattern = Pattern.compile("%");
	private String mIDSAT,mHead="",mSAT="";
	
	public SensEvalHandler() {

		mSATids = new HashMap<String, String>();
		mSATS = new ArrayList<String>();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		SensEvalTags tag;

		tag = SensEvalTags.valueOf(name.toUpperCase());

		switch (tag) {

		case HEAD:

			String sats;
			
			mHead="";
			mID = attributes.getValue(SensEvalAttributes.ID.name().toLowerCase());			
			mSATS.clear();
			sats = attributes.getValue(SensEvalAttributes.SATS.name().toLowerCase());

			if (sats != null)
				mSATS.addAll(Arrays.asList(sats.split("\\s")));
			
			break;

		case SAT:

			mSAT="";
			mIDSAT = attributes.getValue(SensEvalAttributes.ID.name().toLowerCase());
			break;
			
		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SensEvalTags tag = SensEvalTags.valueOf(name.toUpperCase());

		switch (tag) {

		case TEXT:

			mSATS.clear();
			mSATids.clear();
			addAnnotation();
			notifyAnnotations();
			break;

		case HEAD:
			
			String instance = "";
			
			for (String sat : mSATS) 
				if (mSATids.containsKey(sat))				
					instance += mSATids.get(sat) + "_";
					
			mHead=mHead.trim();
			
			if (this.mBPattern.matcher(mHead).matches())
				mHead= "be";
			
			if (this.mXPattern.matcher(mHead).matches())
				mHead= "percent";
		
			instance+=mHead;
			addInstance(instance.replaceAll("[\\s\\-]", "_").toLowerCase());
			addWord(Annotation.ANNOTATION_TAG + mHead+Annotation.ANNOTATION_TAG+" ");
			break;

		case SAT:

			mSAT=mSAT.trim();
			
			if (mSATS.contains(mIDSAT))
				addSAT(mSAT);
			else
				mSATids.put(mIDSAT, mSAT);
			
			addWord(mSAT+" ");
			break;
		default:
			break;
		}

		this.pop();
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

		switch ((SensEvalTags) this.get()) {

		case TEXT:
			
			addWord(new String(ch, start, length).replaceAll("[\r\n]", " ").replaceAll("(^(0|\\*[^ ]*)| (0|\\*[^ ]*) ?)", " "));
			break;

		case HEAD:
			
			mHead+= new String(ch, start, length).replaceAll("[\r\n]", " ");	
			break;

		case SAT:

			mSAT+= new String(ch, start, length).replaceAll("[\r\n]", " ");	
			break;

		default:
			break;
		}
	}

	protected final void addSAT(String word) {

		Lexel lexel = mLexels.get(mID);

		lexel.setName(lexel + "_" + word);
	}
}