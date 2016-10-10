package it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.comp;

import java.io.IOException;
import java.util.Map;

/**
 * @author Simone Papandrea
 *
 */
public class WEFreqComparator extends WEAlphaComparator {

	protected final Map<String, Integer> mVocab;
	private final String mDefault;
	
	public WEFreqComparator(String separator,Map<String, Integer> vocab,String def) throws IOException {

		super(separator);
		
		this.mVocab = vocab;
		this.mDefault=def;
	}

	@Override
	public int compare(String arg0, String arg1) {

		String w0, w1;
		int f0 = 0, f1 = 0;

		w0 = arg0.substring(0, arg0.indexOf(mSeparator));
		w1 = arg1.substring(0, arg1.indexOf(mSeparator));

		if (this.mVocab.containsKey(w0))
			f0 = this.mVocab.get(w0);		
		else if(w0.equals(mDefault))
			f0=Integer.MAX_VALUE;
		
		if (this.mVocab.containsKey(w1))
			f1 = this.mVocab.get(w1);		
		else if(w1.equals(mDefault))
			f1=Integer.MAX_VALUE;
		
		return f1 - f0;
	}

}
