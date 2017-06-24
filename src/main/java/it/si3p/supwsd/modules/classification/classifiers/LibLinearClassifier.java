package it.si3p.supwsd.modules.classification.classifiers;

import java.util.SortedSet;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTrain;
import it.si3p.supwsd.modules.classification.instances.Instance;
import it.si3p.supwsd.modules.extraction.features.Feature;

/**
 * @author papandrea
 *
 */
class LibLinearClassifier extends Classifier<Model,FeatureNode> {

	private final Parameter mParameter;
	private final double mBias=-1;

	LibLinearClassifier() {

		this.mParameter = new Parameter(SolverType.L2R_L2LOSS_SVC_DUAL, 1, Double.POSITIVE_INFINITY);
	}

	@Override
	public Object train(AmbiguityTrain ambiguity) {

		Problem problem;
		FeatureNode[] nodes;
		int i = 0;
		int examples;

		examples = ambiguity.getExamplesCount();
		problem = new Problem();
		problem.l = examples;
		problem.x = new FeatureNode[examples][];
		problem.y = new double[examples];
		
		for (Instance instance : ambiguity.getInstances()) {

			for (String sense : instance.getSenses()) {
				
				nodes = getFeatureNodes(instance.getFeatures());
				problem.x[i] = nodes;
				problem.y[i] = ambiguity.getClass(sense);
				i++;				
			}
		}

		problem.n = ambiguity.getFeaturesCount();
		problem.bias=mBias;
		this.mParameter.setEps(0.1);
	
		return Linear.train(problem, mParameter);
	}

	@Override
	protected double[] predict(Model model, FeatureNode[] featuresNodes) {

		double[] temp,probs;
		int classes,label;
		double sum = 0;
		
		classes = model.getNrClass();
		temp = new double[classes];
		Linear.predictValues(model, featuresNodes, temp);

		for (int i = 0; i < classes; i++) {
			temp[i] = 1 / (1 + Math.exp(-temp[i]));
			sum += temp[i];			
		}

		probs=new double[classes];
		
		for(int i=0;i<classes;i++){
			
			label = model.getLabels()[i];
			
			if (label > 0)
				probs[label-1]=temp[i]/sum;
		}
		
		return probs;
	}

	@Override
	protected  FeatureNode[] getFeatureNodes(SortedSet<Feature> features) {

		FeatureNode[] vector;
		int length,index,i=0;

		length = features.size();
		vector = new FeatureNode[length];
	
		for (Feature feature:features) {

			index=feature.getIndex();			
			vector[i++] = new FeatureNode(index, feature.getValue());
		}
		
		return vector;
	}


}
