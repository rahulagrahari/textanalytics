package it.si3p.supwsd.modules.classification;

import java.io.IOException;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import it.si3p.supwsd.data.POSMap;
import it.si3p.supwsd.inventory.SenseInventory;
import it.si3p.supwsd.modules.classification.classifiers.Classifier;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTest;
import it.si3p.supwsd.modules.classification.instances.Instance;
import it.si3p.supwsd.modules.classification.scorer.Result;
import it.si3p.supwsd.modules.classification.scorer.Score;

/**
 * @author papandrea
 *
 */
public class Evaluator {

	private final SenseInventory mSenseInventory;
	private final Classifier<?, ?> mClassifier;
	private final Score mScore;
	private final POSMap mPOSMap;
	
	public Evaluator(Classifier<?, ?> classifier, SenseInventory resource, int total) {

		this.mClassifier = classifier;
		this.mSenseInventory = resource;
		this.mScore = new Score(total);
		this.mPOSMap=POSMap.getInstance();
	}

	public Collection<Result> evaluate(AmbiguityTest ambiguity) throws ClassNotFoundException, IOException {

		Collection<Result> results = null;
		String sense = null, lexel;
		String[] classes;
		int index;

		lexel = ambiguity.getLexel();
		classes = ambiguity.getClasses();
		
		if (classes == null) {

			if (mSenseInventory != null) {

				index = lexel.lastIndexOf(".");
				sense = mSenseInventory.getSense(lexel.substring(0, index),mPOSMap.getPOS(lexel.substring(index + 1)));
			}

			if (sense == null)
				sense = Result.SENSE_UNK;

			classes = new String[] { sense };
		}

		try {
			
			if (classes.length == 1)
				results = getResult(ambiguity, classes[0]);
			else
				results = mClassifier.evaluate(ambiguity, Serializer.readModel(lexel), classes[0]);

			mScore.add(ambiguity, results);
			
		} finally {
			
			ambiguity.dispose();
		}

		return results;
	}

	private final SortedSet<Result> getResult(AmbiguityTest ambiguity, String cls) {

		SortedSet<Result> results = new TreeSet<Result>();

		for (Instance instance : ambiguity.getInstances())
			results.add(new Result(instance.getID(), instance.getSenses(), cls, instance.getToken()));

		return results;
	}

	public Score getScore() {

		return mScore;
	}

	public void closeResource() {

		if (this.mSenseInventory != null)
			this.mSenseInventory.close();
	}
}