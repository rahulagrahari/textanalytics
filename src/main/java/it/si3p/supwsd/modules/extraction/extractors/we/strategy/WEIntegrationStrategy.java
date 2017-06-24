package it.si3p.supwsd.modules.extraction.extractors.we.strategy;

/**
 * @author papandrea
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
