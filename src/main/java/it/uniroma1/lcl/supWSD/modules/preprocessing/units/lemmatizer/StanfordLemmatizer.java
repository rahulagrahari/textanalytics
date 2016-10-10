package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;

import java.util.List;

import edu.stanford.nlp.process.Morphology;

/**
 * @author Simone Papandrea
 *
 */
class StanfordLemmatizer extends Lemmatizer {

	StanfordLemmatizer(String configFile) {

		super(configFile);
	}

	@Override
	public void load() throws Exception {

	}

	@Override
	public String[] lemmatize(List<String> words, String[] POSTags) {

		String lemmas[];
		int length;

		length = words.size();
		lemmas = new String[length];

		for (int i = 0; i < length; i++)
			lemmas[i] = getLemma(words.get(i), POSTags[i]);

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
