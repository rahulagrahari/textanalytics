package it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
class SimpleSplitter extends Unit implements Splitter {

	private final static String MODEL= "\r\n|\r|\n";
	
	SimpleSplitter(String model) {

		super(model);

	}

	public String[] split(String sentence) {

		return sentence.split(this.getModel());
	}

	
	@Override
	public void load() throws Exception {

	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}

}
