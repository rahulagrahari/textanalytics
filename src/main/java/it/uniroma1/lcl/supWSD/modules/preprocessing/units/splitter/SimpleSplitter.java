package it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter;

/**
 * @author Simone Papandrea
 *
 */
class SimpleSplitter extends Splitter {

	private final static String MODEL= "\r\n|\r|\n";
	
	SimpleSplitter(String model) {

		super(model);

	}

	@Override
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
