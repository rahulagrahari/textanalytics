package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

/**
 * @author Simone Papandrea
 *
 */
class SimpleTokenizer extends Tokenizer {

	private final static String MODEL = "[ \t\n\r\f]+";

	SimpleTokenizer(String separator) {

		super(separator);
	}

	
	@Override
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
