package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
class SimpleTokenizer extends Unit implements Tokenizer {

	private final static String MODEL = "[ \t\n\r\f]+";

	SimpleTokenizer(String separator) {

		super(separator);
	}

	
	public String[] tokenize(String sentence) {

		return sentence.split(MODEL);
	}

	@Override
	public void load() throws Exception {
	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}
}
