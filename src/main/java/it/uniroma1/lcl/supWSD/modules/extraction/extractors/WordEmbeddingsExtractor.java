package it.uniroma1.lcl.supWSD.modules.extraction.extractors;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.data.Token;
import it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.WEMemoryManagment;
import it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.strategy.WEIntegrationStrategy;
import it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.strategy.WEStrategy;
import it.uniroma1.lcl.supWSD.modules.extraction.extractors.we.strategy.WEStrategyInstance;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;
import it.uniroma1.lcl.supWSD.modules.extraction.features.WordEmbedding;

/**
 * @author Simone Papandrea
 *
 */
public class WordEmbeddingsExtractor extends FeatureExtractor {

	private static final String DEFAULT_SEPARATOR = " ";
	private final WEIntegrationStrategy mStrategy;
	private final String mVocabFile;
	private final float mCacheSize;
	private final int mWindowSize;
	private final WEMemoryManagment mWEMemoryManagment;

	public WordEmbeddingsExtractor(WEStrategy strategy, int windowSize, String vectorsFile, String vocabFile,
			float cacheSize)  {

		super(0);

		this.mStrategy = WEStrategyInstance.getInstance().getIntegrationStrategy(strategy, windowSize);
		this.mWindowSize = windowSize;
		this.mVocabFile = vocabFile;
		this.mCacheSize = cacheSize;
		this.mWEMemoryManagment = new WEMemoryManagment(vectorsFile,DEFAULT_SEPARATOR);
	}

	@Override
	public Collection<Feature> extract(Lexel lexel, Annotation annotation) {

		Vector<Feature> features;
		Map<Integer, double[]> wordEmbeddings;
		Token[] tokens;
		String word;
		double value, wordEmbedding[];
		int tokenID, min = 0, max;

		tokenID = lexel.getTokenIndex();
		tokens = annotation.getTokens(lexel);
		max = tokens.length - 1;

		if (mWindowSize > -1) {

			min = Math.max(min, tokenID - this.mWindowSize);
			max = Math.min(max, tokenID + this.mWindowSize);
		}

		features = new Vector<Feature>();
		wordEmbeddings = new HashMap<Integer, double[]>();

		for (int k = min; k <= max; k++) {

			word = tokens[k].getWord().toLowerCase();
			wordEmbedding = this.mWEMemoryManagment.get(word);
			wordEmbeddings.put(k - tokenID, wordEmbedding);
		}

		for (int i = 0; i < this.mWEMemoryManagment.getMemSize(); i++) {

			value = 0;

			for (Entry<Integer, double[]> entry : wordEmbeddings.entrySet())
				value += mStrategy.coefficent(entry.getKey()) * entry.getValue()[i];

			features.add(new WordEmbedding(i, value));
		}

		wordEmbeddings.clear();

		return features;
	}

	@Override
	public void load() throws IOException {

		this.mWEMemoryManagment.load(mVocabFile, mCacheSize);
	}

	@Override
	public void unload() {

		this.mWEMemoryManagment.close();
	}

	@Override
	public Class<? extends Feature> getFeatureClass() {

		return WordEmbedding.class;
	}

}
