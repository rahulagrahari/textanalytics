package it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.strategy;

/**
 * @author Simone Papandrea
 *
 */
class WEFractionalDecayStrategy extends WEIntegrationStrategy{

	protected WEFractionalDecayStrategy(int window) {
		
		super(window);
	}

	@Override
	public double coefficent(int val) {
		
		int window=getWindow();
		
		return ((window + (val > 0 ? -val: val) + 1) * 1.0) / (window * 1.0);
	}

}
