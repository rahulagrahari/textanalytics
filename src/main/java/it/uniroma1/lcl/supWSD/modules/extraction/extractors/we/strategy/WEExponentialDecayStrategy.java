package it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.strategy;

/**
 * @author Simone Papandrea
 *
 */
class WEExponentialDecayStrategy extends WEIntegrationStrategy {

	private final double mAlpha;
	
	protected WEExponentialDecayStrategy(int window) {
		
		super(window);
		
		mAlpha = 1 - Math.pow(0.1, 1 / (window * 1.0));
	}


	@Override
	public double coefficent(int val) {

		return val==0?1:Math.pow(1 - mAlpha, Math.abs(val) - 1);
	}


}
