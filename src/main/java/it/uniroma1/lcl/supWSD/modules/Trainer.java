package it.uniroma1.lcl.supWSD.modules;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import it.uniroma1.lcl.supWSD.modules.classification.Learner;
import it.uniroma1.lcl.supWSD.modules.classification.classifiers.Classifier;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTrain;
import it.uniroma1.lcl.supWSD.modules.extraction.extractors.FeatureExtractor;
import it.uniroma1.lcl.supWSD.modules.parser.Parser;
import it.uniroma1.lcl.supWSD.modules.preprocessing.Preprocessor;
import net.didion.jwnl.JWNLException;
import opennlp.tools.util.InvalidFormatException;

/**
 * @author Simone Papandrea
 *
 */
public class Trainer extends Analyzer<AmbiguityTrain> {

	private final Learner mLearner;

	public Trainer(Parser parser, Preprocessor preprocessor, FeatureExtractor[] featureExtractors, Classifier<?, ?> sc,
			Map<String,SortedSet<String> > senses)
			throws InvalidFormatException, IOException, ParserConfigurationException, SAXException, JWNLException {

		super(parser, preprocessor, featureExtractors, senses);

		this.mLearner = new Learner(sc);
	}

	@Override
	protected void classify(Collection<AmbiguityTrain> ambiguities) {

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