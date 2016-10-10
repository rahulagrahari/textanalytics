package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer.Tokenizer;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.DependencyParser;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer.Lemmatizer;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter.Splitter;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger.Tagger;

/**
 * @author Simone Papandrea
 *
 */
public class HybridPreprocessor implements Preprocessor {

	private final Splitter mSplitter;
	private final Tokenizer mTokenizer;
	private final Tagger mTagger;
	private final Lemmatizer mLemmatizer;
	private final DependencyParser mDependencyParser;

	public HybridPreprocessor(Splitter splitter, Tokenizer tokenizer, Tagger tagger, Lemmatizer lemmatizer,
			DependencyParser dependencyParser) {

		this.mSplitter = splitter;
		this.mTokenizer = tokenizer;
		this.mTagger = tagger;
		this.mLemmatizer = lemmatizer;
		this.mDependencyParser = dependencyParser;
	}

	@Override
	public final void execute(Annotation annotation) {

		String[] sentences;
		String[][] words, lemmas = null, POS = null;
		Vector<List<String>> tokens = null;
		DependencyTree[] dependencies = null;
		List<String> sentence;
		Iterator<Lexel> lexels;
		String word, head = "";
		final int index;
		final String tag = Annotation.ANNOTATION_TAG;
		boolean start = false;

		index = tag.length();
		sentences = split(annotation.getText());
		words = tokenize(sentences);

		if (words != null) {

			tokens = new Vector<List<String>>();
			lexels = annotation.iterator();

			for (int i = 0; i < words.length; i++) {

				sentence = new ArrayList<String>();

				for (int j = 0; j < words[i].length; j++) {

					word = words[i][j];

					if (word.startsWith(tag) && !start) {

						head = "";
						start = true;
						lexels.next().set(i, sentence.size());
						word = word.substring(index);
					}

					if (word.endsWith(tag)) {
						start = false;
						word = head + word.substring(0, word.length() - index);
					}

					if (start)
						head += word;
					else
						sentence.add(word);
				}

				tokens.add(sentence);
			}

			POS = POSTag(tokens);

			if (POS != null)
				lemmas = lemmatize(tokens, POS);

			if (lemmas != null)
				dependencies = parseDependencies(lemmas, POS);

			annotation.annote(tokens, POS, lemmas, dependencies);

		} else
			annotation.annote(sentences);
	}

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

	protected final String[][] POSTag(Vector<List<String>> tokens) {

		String[][] tags = null;
		int length;

		if (mTagger != null) {

			length = tokens.size();
			tags = new String[length][];

			for (int i = 0; i < length; i++)
				tags[i] = POSTag(tokens.get(i));
		}

		return tags;
	}

	protected final String[][] lemmatize(Vector<List<String>> words, String[][] POSTags) {

		String lemmas[][];
		int length;

		length = words.size();
		lemmas = new String[length][];

		for (int i = 0; i < length; i++)
			lemmas[i] = lemmatize(words.get(i), POSTags[i]);

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
				threads.add(service.submit(mSplitter));

			if (mTokenizer != null)
				threads.add(service.submit(mTokenizer));

			if (mTagger != null)
				threads.add(service.submit(mTagger));

			if (mLemmatizer != null)
				threads.add(service.submit(mLemmatizer));

			if (mDependencyParser != null)
				threads.add(service.submit(mDependencyParser));

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

	protected String[] lemmatize(List<String> words, String[] POS) {

		return mLemmatizer.lemmatize(words, POS);
	}

	protected DependencyTree parseDependencies(String[] lemmas, String[] POS) {

		return mDependencyParser.parse(lemmas, POS);
	}

	@Override
	public void unload() {

		if (mSplitter != null)
			mSplitter.unload();

		if (mTokenizer != null)
			mTokenizer.unload();

		if (mTagger != null)
			mTagger.unload();

		if (mLemmatizer != null)
			mLemmatizer.unload();

		if (mDependencyParser != null)
			mDependencyParser.unload();
	}
}