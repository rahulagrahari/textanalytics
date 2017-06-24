package it.si3p.supwsd.modules.preprocessing.units.tokenizer;

import it.si3p.supwsd.modules.preprocessing.units.Unit;

/**
 * @author papandrea
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
	public void load()  {
	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}
}
