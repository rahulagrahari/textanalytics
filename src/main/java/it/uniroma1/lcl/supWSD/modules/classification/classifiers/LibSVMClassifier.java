package it.uniroma1.lcl.supWSD.modules.classification.classifiers;


import java.util.SortedSet;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTrain;
import it.uniroma1.lcl.supWSD.modules.classification.instances.Instance;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

/**
 * @author Simone Papandrea
 *
 */
 class LibSVMClassifier extends Classifier<svm_model, svm_node> {

	private svm_parameter mParameter = new svm_parameter();

	LibSVMClassifier() {

		this.mParameter = new svm_parameter();
		this.mParameter.svm_type = svm_parameter.C_SVC;
		this.mParameter.kernel_type = svm_parameter.RBF;
		this.mParameter.degree = 3;
		this.mParameter.gamma = 0;
		this.mParameter.coef0 = 0;
		this.mParameter.nu = 0.5;
		this.mParameter.cache_size = 100;
		this.mParameter.C = 1;
		this.mParameter.eps = 1e-3;
		this.mParameter.p = 0.1;
		this.mParameter.shrinking = 1;
		this.mParameter.probability = 0;
		this.mParameter.nr_weight = 0;
		this.mParameter.weight_label = new int[0];
		this.mParameter.weight = new double[0];
	}

	@Override
	public Object train(AmbiguityTrain ambiguity) {

		svm_problem problem;
		svm_node[] nodes;
		int i = 0;
		int examples;

		examples = ambiguity.getExamplesCount();
		problem = new svm_problem();
		problem.l = examples;
		problem.x = new svm_node[examples][];
		problem.y = new double[examples];

		for (Instance instance : ambiguity.getInstances()) {

			for (String sense : instance.getSenses()) {

				nodes = getFeatureNodes(instance.getFeatures());
				problem.x[i] = nodes;
				problem.y[i] = ambiguity.getClass(sense);
				i++;
			}
		}

		return libsvm.svm.svm_train(problem, mParameter);
	}

	@Override
	protected double[] predict(svm_model model, svm_node[] svmNode) {

		double[] temp, probs;
		int classes, label;
		int[] labels;

		classes = svm.svm_get_nr_class(model);
		temp = new double[classes];
		svm.svm_predict_probability(model, svmNode, temp);
		labels = new int[classes];
		svm.svm_get_labels(model, labels);
		probs = new double[classes];

		for (int i = 0; i < classes; i++) {

			label = labels[i];

			if (label > 0)
				probs[label - 1] = temp[i];
		}

		return probs;
	}

	@Override
	protected svm_node[] getFeatureNodes(SortedSet<Feature> features) {

		svm_node[] vector;
		svm_node node;
		int length, index, i = 0;

		length = features.size();
		vector = new svm_node[length];

		for (Feature feature : features) {

			index = feature.getIndex();
			node = new svm_node();
			node.index = index;
			node.value = feature.getValue();
			vector[i++] = node;

		}

		return vector;
	}

}