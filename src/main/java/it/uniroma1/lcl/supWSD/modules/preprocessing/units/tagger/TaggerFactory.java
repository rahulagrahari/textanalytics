package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger;

/**
 * @author Simone Papandrea
 *
 */
public class TaggerFactory {

	private static TaggerFactory instance;

	private TaggerFactory() {

	}

	public static TaggerFactory getInstance() {

		if (instance == null)
			instance = new TaggerFactory();

		return instance;
	}

	public Tagger getTagger(TaggerType type, String model) {

		Tagger tagger = null;

		if (type != null) {
		
			switch (type) {

			case STANFORD:
				tagger = new StanfordTagger(model);
				break;

			case OPEN_NLP:
				tagger = new OpenNLPTagger(model);
				break;

			default:
				tagger = new SimpleTagger(model);
			}
		}
		
		return tagger;
	}
}
