package it.si3p.supwsd.modules;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import it.si3p.supwsd.modules.classification.Learner;
import it.si3p.supwsd.modules.classification.classifiers.Classifier;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTrain;
import it.si3p.supwsd.modules.extraction.extractors.FeatureExtractor;
import it.si3p.supwsd.modules.parser.Parser;
import it.si3p.supwsd.modules.preprocessing.Preprocessor;

/**
 * @author papandrea
 *
 */
public class Trainer extends Analyzer<AmbiguityTrain> {

	private final Learner mLearner;

	public Trainer(Parser parser, Preprocessor preprocessor, FeatureExtractor[] featureExtractors, Classifier<?, ?> sc,
			Map<String,SortedSet<String> > senses){
		super(parser, preprocessor, featureExtractors, senses);

		this.mLearner = new Learner(sc);
	}

	@Override
	protected void classify(Collection<AmbiguityTrain> ambiguities) throws IOException {

		for (AmbiguityTrain ambiguity : ambiguities) {
			
			cutoff(ambiguity);
			mLearner.train(ambiguity);		
		}
	}

	@Override
	protected AmbiguityTrain getAmbiguity(String lexel) {

		return new AmbiguityTrain(lexel);

	}

	private void cutoff(AmbiguityTrain ambiguityTrain) {

		int cutoff;

		for (FeatureExtractor fe : this.mExtractor.getFeatureExtractors()) {

			cutoff = fe.getCutOff();

			if (cutoff > 0)
				ambiguityTrain.cutoff(fe.getFeatureClass(), cutoff);
		}
	}
}