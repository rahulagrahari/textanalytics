package it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter;

/**
 * @author Simone Papandrea
 *
 */
public class SplitterFactory {

	private static SplitterFactory instance;

	private SplitterFactory() {

	}

	public static SplitterFactory getInstance() {

		if (instance == null)
			instance = new SplitterFactory();

		return instance;
	}

	public Splitter getSplitter(SplitterType type, String model) {

		Splitter splitter = null;

		if (type != null) {
		
			switch (type) {

			case STANFORD:
				splitter = new StanfordSplitter(model);
				break;

			case OPEN_NLP:
				splitter = new OpenNLPSplitter(model);
				break;

			default:
				splitter = new SimpleSplitter(model);

			}
		}
		
		return splitter;
	}
}
