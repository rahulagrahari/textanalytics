package it.si3p.supwsd.modules.preprocessing.units.splitter;

/**
 * @author papandrea
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
