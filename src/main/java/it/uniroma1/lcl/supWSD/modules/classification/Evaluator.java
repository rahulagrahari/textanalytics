package it.uniroma1.lcl.supWSD.modules.classification;

import java.io.IOException;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import it.uniroma1.lcl.supWSD.data.POSMap.TAG;
import it.uniroma1.lcl.supWSD.inventory.SenseInventory;
import it.uniroma1.lcl.supWSD.modules.classification.classifiers.Classifier;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTest;
import it.uniroma1.lcl.supWSD.modules.classification.instances.Instance;
import it.uniroma1.lcl.supWSD.modules.classification.scorer.Result;
import it.uniroma1.lcl.supWSD.modules.classification.scorer.Score;

/**
 * @author Simone Papandrea
 *
 */
public class Evaluator {

	private final SenseInventory mSenseInventory;
	private final Classifier<?, ?> mClassifier;
	private final Score mScore;

	public Evaluator(Classifier<?, ?> classifier, SenseInventory resource,int total) throws IOException {

		this.mClassifier = classifier;
		this.mSenseInventory = resource;
		this.mScore = new Score(total);
	}

	public Collection<Result> evaluate(AmbiguityTest ambiguity)  {

		Collection<Result> results=null;
		String sense=null,lexel;
		String[] classes;
		int index;
		
		lexel=ambiguity.getLexel();		
		classes = ambiguity.getClasses();
	
		if (classes==null) {

			if (mSenseInventory != null){
			
				index=lexel.lastIndexOf(".");				
				sense = mSenseInventory.getSense(lexel.substring(0,index),TAG.valueOf(lexel.substring(index+1)));
			}
			
			if(sense==null)
				sense = Result.SENSE_UNK;
			
			classes = new String[] { sense };
		}

		try {

			if (classes.length == 1)
				results = getResult(ambiguity, classes[0]);
			else
				results = mClassifier.evaluate(ambiguity, Serializer.readModel(lexel), classes[0]);
						
			mScore.add(ambiguity,results);

		} catch (Exception e) {
		
		}
		finally{
			
			ambiguity.dispose();
		}
		
		return results;
	}

	private final SortedSet<Result> getResult(AmbiguityTest ambiguity, String cls) {

		SortedSet<Result> results = new TreeSet<Result>();

		for (Instance instance : ambiguity.getInstances())
			results.add(new Result(instance.getWord(), instance.getID(), instance.getSenses(), cls));

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