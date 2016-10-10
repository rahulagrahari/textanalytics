package it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter;

import java.util.List;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

/**
 * @author Simone Papandrea
 *
 */
class StanfordSplitter extends Splitter {

	 StanfordSplitter(String modelFile) {

		super(modelFile);

	}

	@Override
	public String[] split(String sentence) {

		String[] sentences;
		Document doc;
		List<Sentence> sents;
		int size;

		doc = new Document(sentence);
		sents = doc.sentences();
		size = sents.size();
		sentences = new String[size];

		for (int i = 0; i < size; i++)
			sentences[i] = sents.get(i).text();

		return sentences;
	}

	@Override
	public void load() throws Exception {

	}

	@Override
	public String getDefaultModel() {

		return null;
	}

}
