package it.si3p.supwsd.modules.extraction.extractors.we;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ehcache.Cache;
import it.si3p.supwsd.modules.extraction.extractors.we.comp.WEAlphaComparator;
import it.si3p.supwsd.modules.extraction.extractors.we.comp.WEFreqComparator;
import it.si3p.supwsd.modules.extraction.extractors.we.comp.WEVocabComparator;

/**
 * @author papandrea
 *
 */
public class WEVirtualMemory {

	private static final String DEF = "<UNK>";
	private static final String CACHE_FILE = "cache.we";
	private static final String KEYS_FILE = "keys.we";
	private static final String VECTORS_DIR = "vectors";
	private final String mVectorsDir;
	private final String mKeysFile;
	private final String mCacheFile;
	private final String mVectorSeparator;
	private final List<String> mKeys;
	private boolean mReady = false;
	private String mMemFile;

	public WEVirtualMemory(String vectorsFile, String memDir, String separator) {

		this.mVectorsDir = memDir + File.separator + VECTORS_DIR + File.separator;
		this.mCacheFile = memDir + File.separator + CACHE_FILE;
		this.mKeysFile = memDir + File.separator + KEYS_FILE;
		this.mVectorSeparator = separator;
		this.mKeys = new ArrayList<String>();
		this.mMemFile = vectorsFile;
	}

	public boolean check() {

		return Files.exists(Paths.get(mKeysFile)) && Files.exists(Paths.get(mCacheFile));
	}

	public void load() throws IOException {

		this.mKeys.addAll(WEFileUtils.readLines(mKeysFile));
		this.mMemFile = mCacheFile;
		this.mReady = true;
	}

	public void create(String vocabFile, int blockSize) throws IOException {

		File file;
		Map<String, Integer> vocab;
		Path path;

		path = Paths.get(mVectorsDir);

		if (!Files.exists(path))
			path.toFile().mkdir();

		file = WEFileUtils.sortFile(mMemFile, "alpha", new WEAlphaComparator(mVectorSeparator));
		WEFileUtils.splitFile(file, mKeysFile, mVectorsDir, mVectorSeparator, blockSize);
		file.delete();
		vocab = loadVocab(vocabFile);
		WEFileUtils.sortFilesDir(mVectorsDir, mVectorSeparator, new WEVocabComparator(vocab, DEF));
		WEFileUtils.sortFile(mMemFile, mCacheFile, new WEFreqComparator(mVectorSeparator, vocab, DEF));
		vocab.clear();
	}

	public int getMaxSize() throws IOException {

		return WEFileUtils.getCountLines(mMemFile);
	}

	public boolean isReady() {

		return this.mReady;
	}

	public double[] swap(Cache<String, double[]> cache, int cacheSize) throws IOException {

		String line, word;
		String[] vals;
		double[] vector = null, unk = null;
		int length = 0;

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(mMemFile)))) {

			while ((cacheSize != 0 || unk == null) && (line = reader.readLine()) != null) {

				vals = line.split(mVectorSeparator);
				length = vals.length - 1;
				word = vals[0];
				vector = new double[length];

				for (int j = 0; j < length; j++)
					vector[j] = Double.valueOf(vals[j + 1]);

				if (word.equals(DEF))
					unk = vector;

				else if (cacheSize > 0) {

					cache.put(word, vector);
					cacheSize--;
				}
			}
		}

		return unk;
	}

	public double[] get(String word) throws IOException {

		double[] wordEmbedding = null;
		String line, key;
		String[] vals;
		int length, index;

		index = Collections.binarySearch(this.mKeys, word);

		if (index < 0)
			index = -index - 1;

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(mVectorsDir + index)))) {

			while ((line = reader.readLine()) != null && wordEmbedding == null) {

				vals = line.split(mVectorSeparator);
				key = vals[0];

				if (key.equals(word)) {

					length = vals.length;
					word = vals[0];
					wordEmbedding = new double[length - 1];

					for (int j = 1; j < length; j++)
						wordEmbedding[j - 1] = Double.valueOf(vals[j]);
				}
			}
		}

		return wordEmbedding;
	}

	private Map<String, Integer> loadVocab(String vocabFile) throws IOException {

		HashMap<String, Integer> mVocab;
		BufferedReader reader;
		String line, vals[];

		reader = new BufferedReader(new InputStreamReader(new FileInputStream(vocabFile)));
		mVocab = new HashMap<String, Integer>();

		while ((line = reader.readLine()) != null) {

			vals = line.split(mVectorSeparator);
			mVocab.put(vals[0], Integer.valueOf(vals[1]));
		}

		reader.close();

		return mVocab;
	}

}
