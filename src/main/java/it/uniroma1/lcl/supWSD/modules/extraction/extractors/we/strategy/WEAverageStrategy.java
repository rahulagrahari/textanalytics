package it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.strategy;

/**
 * @author Simone Papandrea
 *
 */
class WEAverageStrategy extends WEIntegrationStrategy{

	protected WEAverageStrategy(int window) {
		
		super(window);
	}

	@Override
	public double coefficent(int val) {
		
		int window=getWindow();
		
		return Math.pow(window * 1.0, -1.0);
	}

}
