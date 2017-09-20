package it.si3p.supwsd.modules.extraction.extractors.we;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.Status;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @author papandrea
 *
 */
public class WEMemoryManagment {

	private static final String MEM_DIR = "we" + File.separator + "memory";
	private static final int BLOCK_SIZE = 100;
	private final WEVirtualMemory mWEVirtualMemory;
	private final Set<String> mDefaults;
	private CacheManager mCacheManager;
	private Cache<String, double[]> mCache;
	private double[] UNK;

	public WEMemoryManagment(String vectorsFile, String separator) {

		this.mWEVirtualMemory = new WEVirtualMemory(vectorsFile, MEM_DIR, separator);
		this.mDefaults = Collections.synchronizedSet(new HashSet<String>());
	}

	public void load(String vocabFile, float cacheSize) throws IOException {

		int size = (int) (cacheSize * createVirtualMem(vocabFile, cacheSize));

		this.mCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.withCache("preConfigured", CacheConfigurationBuilder
						.newCacheConfigurationBuilder(String.class, double[].class, ResourcePoolsBuilder.heap(size))
						.build())
				.build(true);

		this.mCache = mCacheManager.getCache("preConfigured", String.class, double[].class);
		this.UNK = mWEVirtualMemory.swap(mCache, size);

		if (this.UNK == null)
			throw new IOException("Vector <UNK> not found!");
	}

	private int createVirtualMem(String vocabFile, float cacheSize) throws IOException {

		new File(MEM_DIR).mkdirs();
		
		if (cacheSize < 1) {

			if (vocabFile == null)
				throw new IllegalArgumentException("WE Vocab file cannot be null");

			if (!mWEVirtualMemory.check())
				mWEVirtualMemory.create(vocabFile, BLOCK_SIZE);

			mWEVirtualMemory.load();
		}

		return mWEVirtualMemory.getMaxSize();
	}

	public double[] get(String word) {

		double[] wordEmbedding = UNK;

		wordEmbedding = this.mCache.get(word);

		if (wordEmbedding == null) {

			if (!this.mDefaults.contains(word)) {

				if (mWEVirtualMemory.isReady()) {

					try {
						wordEmbedding = mWEVirtualMemory.get(word);

					} catch (IOException e) {

					}
				}

				if (wordEmbedding != null)
					this.mCache.put(word, wordEmbedding);
				else
					this.mDefaults.add(word);
			}

			if (wordEmbedding == null)
				wordEmbedding = UNK;
		}

		return wordEmbedding;
	}

	public int getMemSize() {

		return this.UNK.length;
	}

	public void close() {

		if (this.mCacheManager != null) {

			if (this.mCacheManager.getStatus() == Status.AVAILABLE)
				this.mCache.clear();

			this.mCacheManager.close();
		}

		this.mCache = null;
		this.mDefaults.clear();
	}
}