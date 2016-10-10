package it.uniroma1.lcl.supWSD.modules.extraction.filters;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Simone Papandrea
 *
 */
public class StopWordsFilter implements Filter {

	private final HashSet<String> mFilters;
	
	public StopWordsFilter(String[] filters){
	
		this.mFilters=new HashSet<String>();
		this.mFilters.addAll(Arrays.asList(filters));
	}
	
	@Override
	public boolean filter(String word) {
		
		return !mFilters.contains(word.toLowerCase());
	}

	
}
