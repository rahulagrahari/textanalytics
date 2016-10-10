package it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.comp;

import java.util.Comparator;

/**
 * @author Simone Papandrea
 *
 */
public class WEAlphaComparator implements Comparator<String> {

	protected String mSeparator;
	
	public WEAlphaComparator(String separator){
		
		this.mSeparator=separator;
	}
		@Override
		public int compare(String arg0, String arg1) {

			return arg0.substring(0, arg0.indexOf(mSeparator)).compareTo(arg1.substring(0, arg1.indexOf(mSeparator)));

		}

	}