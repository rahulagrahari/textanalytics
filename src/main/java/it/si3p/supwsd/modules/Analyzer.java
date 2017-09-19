package it.si3p.supwsd.modules;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.data.Token;
import it.si3p.supwsd.modules.classification.instances.Ambiguity;
import it.si3p.supwsd.modules.extraction.Extractor;
import it.si3p.supwsd.modules.extraction.extractors.FeatureExtractor;
import it.si3p.supwsd.modules.extraction.features.Feature;
import it.si3p.supwsd.modules.parser.Parser;
import it.si3p.supwsd.modules.parser.Parser.ParserListener;
import it.si3p.supwsd.modules.preprocessing.Preprocessor;

/**
 * @author papandrea
 *
 */
public abstract class Analyzer<T extends Ambiguity> implements ParserListener {

	private static final int EXECUTOR_SIZE = 16;
	private final Parser mParser;
	private final Preprocessor mPreprocessor;
	protected final Extractor mExtractor;
	protected final Map<String, SortedSet<String>> mSenses;

	public Analyzer(Parser parser, Preprocessor preprocessor, FeatureExtractor[] featureExtractors,
			Map<String, SortedSet<String>> senses) {

		this.mParser = parser;
		this.mParser.setParserListener(this);
		this.mPreprocessor = preprocessor;
		this.mExtractor = new Extractor(featureExtractors);
		this.mSenses = senses;
	}

	public final void execute(String annotationsFile) throws Exception {

		try {

			init();
			this.mParser.parse(annotationsFile);

		} finally {
			finalyze();
		}
	}

	protected void init() throws Exception {

		this.mPreprocessor.load();
		this.mExtractor.load();

	}

	protected void finalyze() {

		this.mExtractor.unload();
		this.mPreprocessor.unload();

	}

	@Override
	public void annotationsReady(List<Annotation> annotations, String instance) throws Exception {

		CompletionService<SentenceThread> service;
		ExecutorService executor;
		Map<String, T> ambiguities;
		Annotation annotation;
		SentenceThread thread;
		Token token = null;
		T ambiguity;
		String id;
		List<String> names;
		SortedSet<String> senses;
		int threads = 0, size, slots, count;
		final int queue = EXECUTOR_SIZE * 2;

		executor = Executors.newFixedThreadPool(EXECUTOR_SIZE);

		try {

			service = new ExecutorCompletionService<SentenceThread>(executor);
			ambiguities = new HashMap<String, T>();
			size = annotations.size();
			count = 0;

			do {

				slots = Math.min(queue - threads, size - count);
				threads += slots;

				for (int j = 0; j < slots; j++) {

					annotation = annotations.get(j + count);
					thread = instance == null ? new SentenceThread(annotation)
							: new SentenceThreadInstance(annotation, instance);
					service.submit(thread);
				}

				count += slots;
				slots = Math.min(threads, EXECUTOR_SIZE);
				threads -= slots;

				while (slots-- > 0) {

					thread = service.take().get();
					annotation = thread.mAnnotation;

					for (Lexel lexel : thread.mFeatures.keySet()) {

						token = annotation.getToken(lexel);
						names = getModelName(lexel, token);
						id = lexel.getID();
						senses = getSenses(id);

						// if (senses == null)
							// throw new Exception("No sense for instance id "+id);
						
						for (String name : names) {

							if (ambiguities.containsKey(name))
								ambiguity = ambiguities.get(name);

							else {
								ambiguity = getAmbiguity(name);
								ambiguities.put(name, ambiguity);
							}

							ambiguity.add(id, token, thread.mFeatures.get(lexel), senses);
						}
					}
				}

			} while (threads > 0);

			classify(ambiguities.values());

		} finally {

			executor.shutdownNow();

			try {
				executor.awaitTermination(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {

			}
		}
	}

	protected List<String> getModelName(Lexel lexel, Token token) {

		return Arrays.asList(new String[] { lexel.toString() });
	}

	protected final SortedSet<String> getSenses(String id) {

		return mSenses == null ? null : mSenses.get(id);
	}

	protected final int getSensesCount() {

		return mSenses == null ? 0 : mSenses.size();
	}

	private class SentenceThread implements Callable<SentenceThread> {

		private final Annotation mAnnotation;
		private final Map<Lexel, Vector<Feature>> mFeatures;

		SentenceThread(Annotation annotation) {

			this.mAnnotation = annotation;
			this.mFeatures = new HashMap<Lexel, Vector<Feature>>();
		}

		@Override
		public SentenceThread call() {

			if (!mAnnotation.isAnnotated())
				mPreprocessor.execute(mAnnotation);

			for (Lexel lexel : mAnnotation)
				extract(lexel);

			return this;
		}

		protected void extract(Lexel lexel) {

			Vector<Feature> features;

			features = mExtractor.extract(lexel, mAnnotation);
			mFeatures.put(lexel, features);
		}
	}

	private class SentenceThreadInstance extends SentenceThread {

		private final String mInstance;

		SentenceThreadInstance(Annotation annotation, String instance) {

			super(annotation);
			this.mInstance = instance;
		}

		@Override
		protected void extract(Lexel lexel) {

			if (lexel.toString().equals(mInstance)) {
				super.extract(lexel);
			}
		}
	}

	protected abstract T getAmbiguity(String lexel);

	protected abstract void classify(Collection<T> ambiguities) throws Exception;

}
