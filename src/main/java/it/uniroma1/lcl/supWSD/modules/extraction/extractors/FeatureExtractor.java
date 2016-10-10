package it.uniroma1.lcl.supWSD.modules.extraction.extractors;

import java.util.Collection;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;

/**
 * @author Simone Papandrea
 *
 */
public abstract class FeatureExtractor {

	private final int mCutOff;

	public FeatureExtractor(int cutoff) {

		this.mCutOff = cutoff;
	}

	public final int getCutOff() {

		return this.mCutOff;
	}

	public void load() throws Exception {
	}

	public void unload() {
	}

	public abstract Class<? extends Feature> getFeatureClass();

	public abstract Collection<Feature> extract(Lexel lexel, Annotation annotation);
}
