package it.si3p.supwsd.modules.preprocessing.units.dependencyParser;

/**
 * @author papandrea
 *
 */
public class DependencyParserFactory {

	private static DependencyParserFactory instance;

	private DependencyParserFactory() {

	}

	public static DependencyParserFactory getInstance() {

		if (instance == null)
			instance = new DependencyParserFactory();

		return instance;
	}

	public DependencyParser getDependecyParser(DependencyParserType type, String model) {

		DependencyParser dependencyParser = null;

		if (type != null) {
		
			switch (type) {

			case STANFORD:
				dependencyParser = new StanfordDependencyParser(model);
				break;

			}
		}
		
		return dependencyParser;
	}
}
