package it.si3p.supwsd.modules.preprocessing.units.splitter;

import java.util.List;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import it.si3p.supwsd.modules.preprocessing.units.Unit;

/**
 * @author papandrea
 *
 */
class StanfordSplitter extends Unit implements Splitter {

	 StanfordSplitter(String modelFile) {

		super(modelFile);

	}

	
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
	public void load()  {

	}

	@Override
	public String getDefaultModel() {

		return null;
	}

}
