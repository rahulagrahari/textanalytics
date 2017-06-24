package it.si3p.supwsd.modules.preprocessing.units.splitter;

import it.si3p.supwsd.modules.preprocessing.units.Unit;

/**
 * @author papandrea
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
	public void load()  {

	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}

}
