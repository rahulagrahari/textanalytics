package it.si3p.supwsd.modules.classification;

import java.io.IOException;
import it.si3p.supwsd.modules.classification.classifiers.Classifier;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTrain;

/**
 * @author papandrea
 *
 */
public class Learner {

	private final Classifier<?, ?> mClassifier;

	public Learner(Classifier<?, ?> classifier){

		this.mClassifier = classifier;
	}

	public final void train(AmbiguityTrain ambiguity) throws IOException {

		Object model;
		String lexel;

		try {

			lexel=ambiguity.getLexel();
			model = mClassifier.train(ambiguity);
			Serializer.writeStatistic(ambiguity);
			Serializer.writeModel(lexel, model);

		}finally {

			ambiguity.dispose();
		}
	}
}