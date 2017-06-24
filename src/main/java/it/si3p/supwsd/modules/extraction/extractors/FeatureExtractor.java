package it.si3p.supwsd.modules.extraction.extractors;

import java.io.IOException;
import java.util.Collection;

import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.modules.extraction.features.Feature;

/**
 * @author papandrea
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

	public void load() throws IOException{
	}

	public void unload() {
	}

	public abstract Class<? extends Feature> getFeatureClass();

	public abstract Collection<Feature> extract(Lexel lexel, Annotation annotation);
}
