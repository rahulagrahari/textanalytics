package it.si3p.supwsd.modules.preprocessing.units.lemmatizer;

/**
 * @author papandrea
 *
 */
public class LemmatizerFactory {

	private static LemmatizerFactory instance;

	private LemmatizerFactory() {

	}

	public static LemmatizerFactory getInstance() {

		if (instance == null)
			instance = new LemmatizerFactory();

		return instance;
	}

	public Lemmatizer getLemmatizer(LemmatizerType type, String model) {

		Lemmatizer lemmatizer = null;

		if (type != null) {
		
			switch (type) {

			case STANFORD:
				lemmatizer = new StanfordLemmatizer(model);
				break;

			case JWNL:
				lemmatizer = new JWNLLemmatizer(model);
				break;

			case OPEN_NLP:
				lemmatizer = new OpenNLPLemmatizer(model);
				break;
				
			default:
				lemmatizer = new SimpleLemmatizer(model);
			}
		}

		return lemmatizer;
	}
}
