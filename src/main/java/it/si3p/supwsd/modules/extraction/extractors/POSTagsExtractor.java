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
		int id, min, max, length,offset,index;
		
		tokens = annotation.getTokens(lexel);
		length = tokens.length;
		id = lexel.getTokenIndex();
		offset=lexel.getOffset();
		min = id - mLeftPOS;
		max = id + mRightPOS;
		features = new Vector<Feature>(max - min + 1);
		
		for (int i = min; i <= max; i++) {

			index=i>id?i+offset:i;
			
			if (index > -1 && index < length) 
							
				POS = tokens[index].getPOS();				
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
