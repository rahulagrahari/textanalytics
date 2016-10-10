package it.uniroma1.lcl.supWSD.inventory;

import java.io.IOException;
import java.net.URL;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import it.uniroma1.lcl.supWSD.data.POSMap.TAG;

/**
 * @author Simone Papandrea
 *
 */
class WordNetInventory implements SenseInventory {

	private final IDictionary mDictionary;
	private static final String[] SEPARATORS = { "_", "-", " ", "" };
	private static final String MULTIWORD_SEPARATOR = "_";

	WordNetInventory(String dict) throws IOException {

		URL url = new URL("file", null, dict);

		this.mDictionary = new Dictionary(url);
		this.mDictionary.open();
	}

	@Override
	public String getSense(String lemma, TAG pos) {

		String sense = null, word;
		IIndexWord indexWord = null;
		POS POS;
		boolean multi;
		int i = 0;

		word = lemma;
		multi = lemma.contains(MULTIWORD_SEPARATOR);
		POS = getPOS(pos);

		do {

			word = lemma.replace(MULTIWORD_SEPARATOR, SEPARATORS[i++]);
			indexWord = mDictionary.getIndexWord(word, POS);

			if (indexWord != null)
				sense = mDictionary.getWord(indexWord.getWordIDs().get(0)).getSenseKey().toString();

		} while (sense == null && multi && i < SEPARATORS.length);

		return sense;
	}

	private POS getPOS(TAG pos) {

		POS ps = null;

		switch (pos) {

		case n:
			ps = POS.NOUN;
			break;

		case v:
			ps = POS.VERB;
			break;

		case a:
			ps = POS.ADJECTIVE;
			break;

		case r:
			ps = POS.ADVERB;
			break;

		default:
			ps = POS.NOUN;
			break;
		}

		return ps;
	}

	@Override
	public void close() {
		this.mDictionary.close();
	}
}
