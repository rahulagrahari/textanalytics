package it.uniroma1.lcl.supWSD.modules.extraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.extraction.extractors.FeatureExtractor;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;

/**
 * @author Simone Papandrea
 *
 */
public class Extractor {

	private final FeatureExtractor[] mFeatureExtractors;

	public Extractor(FeatureExtractor[] featureExtractors) {

		this.mFeatureExtractors = featureExtractors;
	}

	public void load() throws Exception {

		ExecutorService service;
		List<Future<Void>> featureTasks;

		service = Executors.newFixedThreadPool(mFeatureExtractors.length);
		featureTasks = new ArrayList<Future<Void>>();

		try {

			for (FeatureExtractor extractor : this.mFeatureExtractors)

				featureTasks.add(service.submit(new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						extractor.load();
						return null;
					}
				}));

			for (Future<Void> future : featureTasks)
				future.get();

		} finally {

			service.shutdownNow();
			service.awaitTermination(5, TimeUnit.SECONDS);
		}
	}

	public void unload() {

		for (FeatureExtractor extractor : this.mFeatureExtractors)
			extractor.unload();
	}

	public Vector<Feature> extract(Lexel lexel, Annotation annotation) throws InterruptedException, ExecutionException {

		Vector<Feature> features;
		Collection<Feature> feature;

		features = new Vector<Feature>();

		for (FeatureExtractor extractor : this.mFeatureExtractors) {

			feature = extractor.extract(lexel, annotation);

			if (feature != null)
				features.addAll(feature);
		}

		return features;
	}

	public FeatureExtractor[] getFeatureExtractors() {

		return this.mFeatureExtractors;
	}

}
