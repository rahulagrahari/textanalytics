package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer.Tokenizer;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.DependencyParser;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer.Lemmatizer;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter.Splitter;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger.Tagger;

/**
 * @author Simone Papandrea
 *
 */
abstract class HybridPreprocessor implements Preprocessor {

	private final Splitter mSplitter;
	private final Tokenizer mTokenizer;
	private final Tagger mTagger;
	private final Lemmatizer mLemmatizer;
	private final DependencyParser mDependencyParser;
	
	HybridPreprocessor(Splitter splitter, Tokenizer tokenizer, Tagger tagger, Lemmatizer lemmatizer,
			DependencyParser dependencyParser) {

		this.mSplitter = splitter;
		this.mTokenizer = tokenizer;
		this.mTagger = tagger;
		this.mLemmatizer = lemmatizer;
		this.mDependencyParser = dependencyParser;
	}

	@Override
	public abstract void execute(Annotation annotation);

	
	protected final String[][] tokenize(String[] sentences) {

		String[][] tokens = null;
		int length;

		if (mTokenizer != null) {

			length = sentences.length;
			tokens = new String[length][];

			for (int i = 0; i < length; i++)
				tokens[i] = tokenize(sentences[i]);
		}

		return tokens;
	}

	protected final String[][] POSTag(String[][] tokens) {

		String[][] tags = null;
		int length;

		if (mTagger != null) {

			length = tokens.length;
			tags = new String[length][];

			for (int i = 0; i < length; i++)
				tags[i] = POSTag(Arrays.asList(tokens[i]));
		}

		return tags;
	}
	
	protected final String[][] lemmatize(String[][] words, String[][] POSTags) {

		String lemmas[][];
		int length;

		length = words.length;
		lemmas = new String[length][];

		for (int i = 0; i < length; i++)
			lemmas[i] = lemmatize(words[i], POSTags[i]);

		return lemmas;
	}

	protected final DependencyTree[] parseDependencies(String[][] lemmas, String[][] POS) {

		DependencyTree[] trees = null;
		int length;

		if (mDependencyParser != null) {

			length = lemmas.length;
			trees = new DependencyTree[length];

			for (int i = 0; i < length; i++)
				trees[i] = parseDependencies(lemmas[i], POS[i]);
		}

		return trees;
	}

	@Override
	public void load() throws InterruptedException, ExecutionException {

		List<Future<Void>> threads;
		ExecutorService service;

		service = Executors.newFixedThreadPool(5);
		threads = new ArrayList<Future<Void>>(5);

		try {

			if (mSplitter != null)
				threads.add(service.submit((Unit)mSplitter));

			if (mTokenizer != null)
				threads.add(service.submit((Unit)mTokenizer));

			if (mTagger != null)
				threads.add(service.submit((Unit)mTagger));

			if (mLemmatizer != null)
				threads.add(service.submit((Unit)mLemmatizer));

			if (mDependencyParser != null)
				threads.add(service.submit((Unit)mDependencyParser));

			for (Future<Void> thread : threads)
				thread.get();

		} finally {

			service.shutdownNow();
			service.awaitTermination(5, TimeUnit.SECONDS);
		}
	}

	protected String[] split(String sentence) {

		String[] sentences;

		if (mSplitter != null)
			sentences = mSplitter.split(sentence);
		else
			sentences = new String[] { sentence };

		return sentences;
	}

	protected String[] tokenize(String sentence) {

		return mTokenizer.tokenize(sentence);
	}

	protected String[] POSTag(List<String> tokens) {

		return mTagger.tag(tokens);
	}

	protected String[] lemmatize(String[] words, String[] POS) {

		return mLemmatizer.lemmatize(words, POS);
	}

	protected DependencyTree parseDependencies(String[] lemmas, String[] POS) {

		return mDependencyParser.parse(lemmas, POS);
	}

	@Override
	public void unload() {

		if (mSplitter != null)
			((Unit)mSplitter).unload();

		if (mTokenizer != null)
			((Unit)mTokenizer).unload();

		if (mTagger != null)
			((Unit)mTagger).unload();

		if (mLemmatizer != null)
			((Unit)mLemmatizer).unload();

		if (mDependencyParser != null)
			((Unit)mDependencyParser).unload();
	}
	

}