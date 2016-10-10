package it.uniroma1.lcl.supWSD.modules.extraction.filters;

import java.util.regex.Pattern;

/**
 * @author Simone Papandrea
 *
 */
public class StopWordsAlphaFilter extends StopWordsFilter {

	protected final Pattern mAlphaPattern;
	
	public StopWordsAlphaFilter(String[] filters) {
		
		super(filters);
		
		this.mAlphaPattern = Pattern.compile("[a-zA-Z]");
	}

	@Override
	public boolean filter(String word) {
		
		boolean filtered=super.filter(word);
		
		if(filtered)
			filtered=this.mAlphaPattern.matcher(word).find();
		
		return filtered;
	}

	
}
