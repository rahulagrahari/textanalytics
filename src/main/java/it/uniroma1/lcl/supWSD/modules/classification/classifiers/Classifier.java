package it.uniroma1.lcl.supWSD.modules.classification.classifiers;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTest;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTrain;
import it.uniroma1.lcl.supWSD.modules.classification.instances.Instance;
import it.uniroma1.lcl.supWSD.modules.classification.scorer.Result;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;

/**
 * @author Simone Papandrea
 *
 */
public abstract class Classifier<T,V> {

	public abstract Object train(AmbiguityTrain ambiguity);
	protected abstract double[] predict(T model, V[] featuresNodes);
	protected abstract V[] getFeatureNodes(SortedSet<Feature> features);
	
	@SuppressWarnings("unchecked")
	public final Collection<Result> evaluate(AmbiguityTest ambiguity,Object model,String cls) {

		SortedSet<Result> results;
		V[] featureNode;
		SortedSet<Feature> features;
		Result result;
		double[] probs;

		results=new TreeSet<Result>();
	
		for (Instance instance : ambiguity.getInstances()) {

			features=instance.getFeatures();
			
			if(!features.isEmpty()){
				
				featureNode = getFeatureNodes(features);
				probs=predict((T) model, featureNode);
				result=new Result(instance.getWord(),instance.getID(), instance.getSenses(), ambiguity.getClasses(),probs);
			}
			else
				result=new Result(instance.getWord(),instance.getID(), instance.getSenses(),cls);
			
			results.add(result);
		}
		
		return results;
	}


}
