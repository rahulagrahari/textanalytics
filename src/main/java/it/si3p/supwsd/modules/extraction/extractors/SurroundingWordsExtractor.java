package it.si3p.supwsd.modules.extraction.extractors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.data.Token;
import it.si3p.supwsd.modules.extraction.features.Feature;
import it.si3p.supwsd.modules.extraction.features.SurroundingWord;
import it.si3p.supwsd.modules.extraction.filters.StopWordsAlphaFilter;

/**
 * @author papandrea
 *
 */
public class SurroundingWordsExtractor extends FeatureExtractor {

	private final int mWindowSize;
	private final SurroundingStopWordsFilter mStopWordsFilter;

	public SurroundingWordsExtractor(int cutoff,int windowSize,String stopWordsFile) throws IOException {

		super(cutoff);
		
		this.mWindowSize=windowSize;
			
		String[] stopWords=null;
		
		if(stopWordsFile!=null)
			stopWords= readStopWords(stopWordsFile);
		
		this.mStopWordsFilter = new SurroundingStopWordsFilter(stopWords);
	}

	
	@Override
	public Collection<Feature> extract(Lexel lexel,Annotation annotation) {

		HashSet<Feature> features;
		Token[] tokens;
		List<Token[]> sentences;
		Token token;
		Feature feature;
		int sentenceID, tokenID;
		int min = 0, max;

		sentences = annotation.getTokenSentences();
		tokenID = lexel.getTokenIndex();
		sentenceID = lexel.getSentenceIndex();
		max = sentences.size() - 1;

		if (mWindowSize > -1){
			
			min = Math.max(min, sentenceID - mWindowSize);
			max = Math.min(max, sentenceID + mWindowSize);
		}
		
		features = new HashSet<Feature>();

		for (int i = min; i <= max; i++) {

			tokens = annotation.getTokens(i);

			for (int j = 0; j < tokens.length; j++) {

				token = tokens[j];
				feature=new SurroundingWord(token.getLemma());
				
				if ((i != sentenceID || j != tokenID) && !features.contains(feature) && mStopWordsFilter.filter(token.getWord()))
					features.add(feature);		
			}
		}
		
		return features;
	}

	private static String[] readStopWords(String stopWordsFiles) throws IOException {

		List<String> stopWords;
		String line;
		
		stopWords = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsFiles)))){

			while ((line = reader.readLine()) != null)
				stopWords.add(line);
		}

		return stopWords.toArray(new String[stopWords.size()]);
	}
	
	private static class SurroundingStopWordsFilter extends StopWordsAlphaFilter {

		private static final String[] DEFAULT_FILTERS = { "a", "about", "above", "across", "after", "afterwards",
				"again", "against", "albeit", "all", "almost", "alone", "along", "already", "also", "although",
				"always", "among", "amongst", "an", "and", "another", "any", "anyhow", "anyone", "anything", "anywhere",
				"are", "around", "as", "at", "b", "be", "became", "because", "become", "becomes", "becoming", "been",
				"before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "both",
				"but", "by", "c", "can", "cannot", "co", "could", "d", "down", "during", "e", "each", "eg", "either",
				"else", "elsewhere", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere",
				"except", "f", "few", "for", "former", "formerly", "from", "further", "g", "h", "had", "has", "have",
				"he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him",
				"himself", "his", "how", "however", "i", "ie", "if", "in", "inc", "indeed", "into", "is", "it", "its",
				"itself", "j", "k", "l", "latter", "latterly", "least", "less", "ltd", "m", "many", "may", "me",
				"meanwhile", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "n",
				"namely", "neither", "never", "nevertheless", "next", "no", "nobody", "none", "noone", "nor", "not",
				"nothing", "now", "nowhere", "o", "of", "off", "often", "on", "once", "one", "only", "onto", "or",
				"other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "p", "per", "perhaps",
				"q", "r", "rather", "s", "same", "seem", "seemed", "seeming", "seems", "several", "she", "should",
				"since", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still",
				"such", "t", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there",
				"thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "this", "those",
				"though", "through", "throughout", "thru", "thus", "to", "together", "too", "toward", "towards", "u",
				"under", "until", "up", "upon", "v", "very", "via", "w", "was", "we", "well", "were", "what",
				"whatever", "whatsoever", "when", "whence", "whenever", "whensoever", "where", "whereafter", "whereas",
				"whereat", "whereby", "wherefrom", "wherein", "whereinto", "whereof", "whereon", "whereto", "whereunto",
				"whereupon", "wherever", "wherewith", "whether", "which", "whichever", "whichsoever", "while", "whilst",
				"whither", "who", "whoever", "whole", "whom", "whomever", "whomsoever", "whose", "whosoever", "why",
				"will", "with", "within", "without", "would", "x", "yet", "you", "your", "yours", "yourself",
				"yourselves", "z", "say", "says", "said", "do", "n't", "'ve", "'d", "'m", "'s", "'re", "'ll", "-lrb-",
				"-rrb-", "-lsb-", "-rsb-", "-lcb-", "-rcb-" };

		public SurroundingStopWordsFilter(String[] filters) {
			super(filters==null?DEFAULT_FILTERS:filters);
		}
		
	}

	@Override
	public Class<? extends Feature> getFeatureClass() {
	
		return SurroundingWord.class;
	}

}
