package it.si3p.supwsd.inventory;

import java.io.IOException;

/**
 * @author papandrea
 *
 */
public class SenseInventoryFactory {

	private static SenseInventoryFactory instance;

	private SenseInventoryFactory() {

	}

	public static SenseInventoryFactory getInstance() {

		if (instance == null)
			instance = new SenseInventoryFactory();

		return instance;
	}

	public SenseInventory getSenseInventory(SenseInventoryType senseInventoryType,String dict) throws IOException {

		SenseInventory senseInventory = null;

		if (senseInventoryType != null) {
			
			switch (senseInventoryType) {

			case BABELNET:

				senseInventory = new BabelNetInventory();
				break;

			default:
				
				if(dict==null || dict.isEmpty())
					throw new IllegalArgumentException("Wordnet dict cannot be null or empty");
				
				senseInventory = new WordNetInventory(dict);
				break;
			}
		}
		
		return senseInventory;
	}
}
