package it.si3p.supwsd.modules.extraction.extractors.we.strategy;

/**
 * @author papandrea
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
