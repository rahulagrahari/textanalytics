package it.si3p.supwsd.modules.extraction.extractors.we.comp;

import java.util.Map;

/**
 * @author papandrea
 *
 */
public class WEVocabComparator extends WEFreqComparator {

	public WEVocabComparator(Map<String, Integer> vocab,String def)  {

		super(null,vocab, def);
	}

	@Override
	public int compare(String arg0, String arg1) {

		int f0=0,f1=0;
		
		if(mVocab.containsKey(arg0))
			f0=mVocab.get(arg0);

		if(mVocab.containsKey(arg1))
			f1=mVocab.get(arg1);
		
		return f1-f0;
	}

}
