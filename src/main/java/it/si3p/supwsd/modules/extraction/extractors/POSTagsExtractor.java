package it.si3p.supwsd.modules.extraction.extractors;

import java.util.Collection;
import java.util.Vector;

import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.data.Token;
import it.si3p.supwsd.modules.extraction.features.Feature;
import it.si3p.supwsd.modules.extraction.features.POSTag;

/**
 * @author papandrea
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
		double val;
		
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

			val=1-(Math.abs(i-id)/(length*1.0));
			features.add(new POSTag(i - id, POS,val));
		}

		return features;
	}


	@Override
	public Class<? extends Feature> getFeatureClass() {

		return POSTag.class;
	}

}
