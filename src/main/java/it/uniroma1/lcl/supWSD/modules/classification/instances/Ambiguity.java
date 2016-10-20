package it.uniroma1.lcl.supWSD.modules.classification.instances;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;

/**
 * @author Simone Papandrea
 *
 */
public abstract class Ambiguity {

	private final List<Instance> mInstances;
	protected Map<String, TermsVector> mStatistic;
	protected final List<String> mSenses;
	private int mExamplesCount = 0;
	protected int mFeaturesCount = 0;
	private final String mLexel;

	public Ambiguity(String lexel) {

		this.mLexel = lexel;
		this.mInstances = new ArrayList<Instance>();
		this.mStatistic = new HashMap<String, TermsVector>();
		this.mSenses = new ArrayList<String>();

	}

	public String getLexel() {

		return mLexel;
	}

	public final void add(String id, String word, Collection<Feature> features, SortedSet<String> senses) {

		int index;
		Iterator<Feature> featureIter;
		Feature feature;

		featureIter = features.iterator();

		while (featureIter.hasNext()) {

			feature = featureIter.next();
			index = addFeature(feature);

			if (index > -1) {

				mFeaturesCount = Math.max(mFeaturesCount, index);
				feature.setIndex(index);

			} else
				featureIter.remove();
		}

		if (senses != null) {
			for (String sense : senses)
				if (!mSenses.contains(sense))
					mSenses.add(sense);

			mExamplesCount += senses.size();
		}

		mInstances.add(new Instance(id, word, features, senses));

	}

	protected abstract int addFeature(Feature feature);

	public List<String> getSenses() {

		return this.mSenses;
	}

	public int getClass(String sense) {

		return this.mSenses.indexOf(sense) + 1;
	}

	public List<Instance> getInstances() {

		return this.mInstances;
	}

	public int getExamplesCount() {

		return this.mExamplesCount;
	}

	public int getFeaturesCount() {

		return this.mFeaturesCount;
	}

	public Map<String, TermsVector> getStatistic() {

		return this.mStatistic;
	}

	public TermsVector getFeatureStatistic(String featureKey) {

		return this.mStatistic.get(featureKey);
	}

	public Set<String> getFeatureKeys() {

		return this.mStatistic.keySet();
	}

	public void dispose() {

		this.mStatistic.clear();
		this.mInstances.clear();
		this.mSenses.clear();
	}

	@Override
	public boolean equals(Object arg0) {

		if (arg0 instanceof AmbiguityTest) {

			AmbiguityTest ambiguity = (AmbiguityTest) arg0;

			return this.getLexel().equals(ambiguity.getLexel());
		}

		return false;
	}

	@Override
	public int hashCode() {

		return this.getLexel().hashCode();
	}

	@Override
	public String toString() {

		return this.getLexel();
	}
}
