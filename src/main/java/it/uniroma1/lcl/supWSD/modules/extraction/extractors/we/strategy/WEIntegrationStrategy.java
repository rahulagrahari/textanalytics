package it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.strategy;

/**
 * @author Simone Papandrea
 *
 */
public abstract class WEIntegrationStrategy {

	private final int mWindow;

	protected WEIntegrationStrategy(int window) {
		
		this.mWindow=window;
	}

	public abstract double coefficent(int val);

	int getWindow() {

		return this.mWindow;
	}
}
