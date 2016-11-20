package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;

import edu.stanford.nlp.process.Morphology;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
class StanfordLemmatizer extends Unit implements Lemmatizer {

	StanfordLemmatizer(String configFile) {

		super(configFile);
	}

	@Override
	public void load() throws Exception {

	}

	public String[] lemmatize(String[] words, String[] POSTags) {

		String lemmas[];
		int length;

		length = words.length;
		lemmas = new String[length];

		for (int i = 0; i < length; i++)
			lemmas[i] = getLemma(words[i], POSTags[i]);

		return lemmas;
	}

	private String getLemma(String word, String tag) {

		String lemma;

		word = word.trim().toLowerCase();
		lemma = word;

		if (!word.isEmpty())
			lemma=new Morphology().lemma(word, tag, true);

		return lemma;
	}

	@Override
	public void unload() {

	}

	@Override
	public String getDefaultModel() {

		return null;
	}

}
