package it.uniroma1.lcl.supWSD.modules.classification;

import java.io.IOException;
import it.uniroma1.lcl.supWSD.modules.classification.classifiers.Classifier;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTrain;

/**
 * @author Simone Papandrea
 *
 */
public class Learner {

	private final Classifier<?, ?> mClassifier;

	public Learner(Classifier<?, ?> classifier) throws IOException {

		this.mClassifier = classifier;
	}

	public final void train(AmbiguityTrain ambiguity) {

		Object model;
		String lexel;

		try {

			lexel=ambiguity.getLexel();
			model = mClassifier.train(ambiguity);
			Serializer.writeStatistic(ambiguity);
			Serializer.writeModel(lexel, model);

		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {

			ambiguity.dispose();
		}
	}
}