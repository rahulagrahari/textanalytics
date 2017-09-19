package it.si3p.supwsd.modules.parser.xml.senseval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.modules.parser.xml.semeval7.SemEval7HandlerFast;

/**
 * @author papandrea
 *
 */
public class SensEvalHandlerFast extends SemEval7HandlerFast {

	private final Map<String, String> mSATids;
	private final List<String> mSATS;
	private final Pattern mBPattern= Pattern.compile("'([sm]|re)");
	private final Pattern mXPattern = Pattern.compile("%");
	private String mIDSAT,mHead="",mSAT="";
	private  final Map<String,Lexel>mLexels;
	
	public SensEvalHandlerFast() {

		mSATids = new HashMap<String, String>();
		mSATS = new ArrayList<String>();
		mLexels = new HashMap<String,Lexel>();
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) {

		SensEvalTag tag;

		tag = SensEvalTag.valueOf(name.toUpperCase());

		switch (tag) {

		case HEAD:

			String sats;
			
			mHead="";
			mInstanceID  = attributes.getValue(SensEvalAttribute.ID.name().toLowerCase());			
			mSATS.clear();
			sats = attributes.getValue(SensEvalAttribute.SATS.name().toLowerCase());

			if (sats != null)
				mSATS.addAll(Arrays.asList(sats.split("\\s")));
			
			break;

		case SAT:

			mSAT="";
			mIDSAT = attributes.getValue(SensEvalAttribute.ID.name().toLowerCase());
			break;
			
		default:
			break;
		}

		this.push(tag);
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {

		SensEvalTag tag = SensEvalTag.valueOf(name.toUpperCase());

		switch (tag) {

		case TEXT:

			mSATS.clear();
			mSATids.clear();
			mLexels.clear();
			addAnnotation();
			notifyAnnotations();
			break;

		case HEAD:
			
			String instance = "";
			Lexel lexel;
			
			for (String sat : mSATS) 
				if (mSATids.containsKey(sat))				
					instance += mSATids.get(sat) + "_";
					
			mHead=mHead.trim();
			
			if (this.mBPattern.matcher(mHead).matches())
				mHead= "be";
			
			if (this.mXPattern.matcher(mHead).matches())
				mHead= "percent";
		
			instance+=mHead;		
			addWord(formatAnnotation(mHead));
			lexel=addInstance(formatInstance(instance));			
			mLexels.put(mInstanceID,lexel);		
			
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
	public void characters(char ch[], int start, int length) {

		switch ((SensEvalTag) this.get()) {

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

		Lexel lexel = mLexels.get(mInstanceID);

		lexel.setName(lexel + "_" + word);
	}
}