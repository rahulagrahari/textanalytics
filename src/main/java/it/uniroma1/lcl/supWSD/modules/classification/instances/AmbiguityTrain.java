package it.uniroma1.lcl.supWSD.modules.classification.instances;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;

/**
 * @author Simone Papandrea
 *
 */
public class AmbiguityTrain extends Ambiguity {

	private transient Map<Class<? extends Feature>, HashSet<String>> mTypes;

	public AmbiguityTrain(String lexel) {

		super(lexel);

		this.mTypes = new HashMap<Class<? extends Feature>, HashSet<String>>();
	}

	@Override
	protected int addFeature(Feature feature) {

		int index;

		index = setStatistic(feature);
		addClass(feature);

		return index;
	}

	private int setStatistic(Feature feature) {

		TermsVector termsVector;
		String key, def;
		int index;

		index = getFeaturesCount();
		key = feature.getKey();
		termsVector = this.mStatistic.get(key);

		if (termsVector == null) {

			termsVector = new TermsVector();
			this.mStatistic.put(key, termsVector);

			def = feature.getDefaultValue();

			if (def != null)
				index = termsVector.set(def, index + 1);
		}

		return termsVector.set(feature.getName(), index + 1);
	}

	private void addClass(Feature feature) {

		Class<? extends Feature> type;
		HashSet<String> keys;

		type = feature.getClass();
		keys = mTypes.get(type);

		if (keys == null) {
			keys = new HashSet<String>();
			mTypes.put(type, keys);
		}

		keys.add(feature.getKey());
	}

	public Set<String> getFeatureKeys(Class<? extends Feature> featureType) {

		return this.mTypes.get(featureType);
	}

	public Set<Class<? extends Feature>> getTypes() {

		return this.mTypes.keySet();
	}

	@Override
	public void dispose() {

		super.dispose();
	
		this.mTypes.clear();
	}

	public void cutoff(Class<? extends Feature> type, int cutoff) {

		Iterator<Term> vectorIter;

		TermsVector termsVector;
		Term term;

		for (String key : getFeatureKeys(type)) {

			termsVector = getFeatureStatistic(key);
			vectorIter = termsVector.iterator();

			while (vectorIter.hasNext()) {

				term = vectorIter.next();

				if (term.getFrequency() <= cutoff)
					vectorIter.remove();
			}
		}
	}
}