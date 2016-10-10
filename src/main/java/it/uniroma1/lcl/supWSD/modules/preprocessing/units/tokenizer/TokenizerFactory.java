package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

/**
 * @author Simone Papandrea
 *
 */
public class TokenizerFactory {

	private static TokenizerFactory instance;

	private TokenizerFactory() {

	}

	public static TokenizerFactory getInstance() {

		if (instance == null)
			instance = new TokenizerFactory();

		return instance;
	}

	public Tokenizer getTokenizer(TokenizerType type, String model) {

		Tokenizer tokenizer = null;

		if (type != null) {
			
			switch (type) {

			case OPEN_NLP:
				tokenizer = new OpenNLPTokenizer(model);
				break;

			case STANFORD:
				tokenizer = new StanfordTokenizer(model);
				break;

			case PENN_TREE_BANK:
				tokenizer = new PennTreeBankTokenizer();
				break;

			default:
				tokenizer = new SimpleTokenizer(model);
			}
		}

		return tokenizer;
	}
}
