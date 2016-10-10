package it.uniroma1.lcl.supWSD.modules.extraction.extractors;

import java.util.Collection;
import java.util.Vector;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.data.Token;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;
import it.uniroma1.lcl.supWSD.modules.extraction.features.POSTag;

/**
 * @author Simone Papandrea
 *
 */
public class POSTagsExtractor extends FeatureExtractor {

	private static final String DEFAULT = "NULL";
	private final int mLeftPOS;
	private final int mRightPOS;

	public POSTagsExtractor(int cutoff) {

		this(cutoff, 3, 3);
	}

	public POSTagsExtractor(int cutoff, int leftPOS, int rightPOS) {

		super(cutoff);
		this.mLeftPOS = leftPOS;
		this.mRightPOS = rightPOS;

	}

	@Override
	public Collection<Feature> extract(Lexel lexel, Annotation annotation) {

		Vector<Feature> features;
		Token[] tokens;
		String POS;
		int id, min, max, length;

		tokens = annotation.getTokens(lexel);
		length = tokens.length;
		id = lexel.getTokenIndex();
		min = id - mLeftPOS;
		max = id + mRightPOS;
		features = new Vector<Feature>(max - min + 1);

		for (int i = min; i <= max; i++) {

			if (i > -1 && i < length)
				POS = tokens[i].getPOS();
			else
				POS = DEFAULT;

			features.add(new POSTag(i - id, POS));
		}

		return features;
	}


	@Override
	public Class<? extends Feature> getFeatureClass() {

		return POSTag.class;
	}

}
