package it.si3p.supwsd.inventory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import it.si3p.supwsd.data.POSMap.TAG;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSynset;
import it.uniroma1.lcl.babelnet.BabelSynsetComparator;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.jlt.util.Language;

/**
 * @author papandrea
 *
 */
class BabelNetInventory implements SenseInventory {

	private final BabelNet mBabelNet;

	BabelNetInventory() throws IOException {

		mBabelNet = BabelNet.getInstance();
	}

	@Override
	public String getSense(String lemma, TAG pos) {

		String sense = null;
		List<BabelSynset> synsets;

		try {
			synsets = mBabelNet.getSynsets(lemma, Language.EN,getPOS(pos));
			Collections.sort(synsets, new BabelSynsetComparator(lemma));

			if (!synsets.isEmpty())
				sense = synsets.get(0).getId().getID();

		} catch (IOException e) {
	
		}

		return sense;
	}

	private BabelPOS getPOS(TAG pos) {

		BabelPOS POS;

		switch (pos) {

		case n:
			POS = BabelPOS.NOUN;
			break;
			
		case v:
			POS = BabelPOS.VERB;
			break;
			
		case a:
			POS = BabelPOS.ADJECTIVE;
			break;
			
		case r:
			POS = BabelPOS.ADVERB;
			break;
			
		default:
			POS = BabelPOS.NOUN;
			break;
		}

		return POS;
	}

	public void dispose() {

	}

	@Override
	public void close() {

	}
}
