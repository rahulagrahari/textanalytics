package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.collections4.map.LRUMap;
import it.uniroma1.lcl.supWSD.data.POSMap;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

/**
 * @author Simone Papandrea
 *
 */
class JWNLLemmatizer extends Unit implements Lemmatizer {

	private static final int DEFAULT_CACHE_SIZE = 10000;
	private static final String CONFIG = "resources/wndictionary/prop.xml";
	private Map<String, String> mCache;
	private final Pattern mLegalPattern;
	private final Pattern mMultiWordsPattern;
	private final Pattern mEfficentPattern;
	private Dictionary mDictionary;
	private boolean mInit = true;

	JWNLLemmatizer(String configFile) {

		this(configFile, DEFAULT_CACHE_SIZE);
	}

	protected JWNLLemmatizer(String modelFile, int cacheSize) {

		super(modelFile);

		this.mCache = Collections.synchronizedMap(new LRUMap<String, String>(cacheSize));
		this.mLegalPattern = Pattern.compile("^[a-z\\-_ \\.\\/']*$");
		this.mMultiWordsPattern = Pattern.compile("[\\- _]");
		this.mEfficentPattern = Pattern.compile("(^[\\- _]|[\\- _]{2}|[\\- _]$)");
	}

	@Override
	public void load() throws Exception {

		mInit = !JWNL.isInitialized();

		if (mInit) {

			InputStream inputStream = null;

			try {
				inputStream = new FileInputStream(this.getDefaultModel());
				JWNL.initialize(inputStream);

			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
					}
				}
			}
		}

		mDictionary = Dictionary.getInstance();
	}

	public String[] lemmatize(String[] words, String[] POSTags) {

		String lemmas[];
		int length;

		length = words.length;
		lemmas = new String[length];

		for (int i = 0; i < length; i++)
			lemmas[i] = getLemma(words[i], POSTags[i]);

		return lemmas;
	}

	private String getLemma(String word, String tag) {

		String lemma, key, subword, temp;
		String tokens[];
		POS POS;
		int length, l;

		word = word.trim().toLowerCase();
		lemma = word;

		if (!word.isEmpty() && mLegalPattern.matcher(word).find() && !mEfficentPattern.matcher(word).find()) {

			POS = getPOS(tag);
			key = word + tag;
			temp = this.mCache.get(key);

			if (temp != null)

				lemma = temp;

			else {

				try {

					lemma = getBaseForm(word, POS);

					if (!lemma.equals(word)) {

						tokens = this.mMultiWordsPattern.split(word);
						length = tokens.length;

						if (length > 1) {

							tokens: for (int i = 0; i < length; i++) {

								subword = "";
								l = i == 0 ? length - 1 : length;

								for (int j = i; j < l; j++) {

									subword += tokens[i];

									if (lemma.equals(subword) || lemma.equals(getBaseForm(subword, POS))) {
										lemma = word;
										break tokens;
									}

									subword += " ";
								}
							}
						}
					}

				} catch (Exception e) {

				}

				lemma = lemma.replace(" ", "_");
				mCache.put(key, lemma);
			}
		}

		return lemma;
	}

	@SuppressWarnings("unchecked")
	private String getBaseForm(String word, POS POS) throws JWNLException {

		String lemma= word, l;
		List<String> indexWords;
		int lemmas;

		synchronized (this) {

			indexWords = mDictionary.getMorphologicalProcessor().lookupAllBaseForms(POS, word);
		}

		lemmas = indexWords.size();

		if (lemmas > 0) {

			lemma = indexWords.get(0);

			for (int i = 1; i < lemmas; i++) {

				l = indexWords.get(i);

				if (l.equals(word)) {
					lemma = l;
					break;
				}
			}
		}

		return lemma;
	}

	private POS getPOS(String pos) {

		POS POSTag;
		POSMap.TAG tag;

		tag = POSMap.getInstance().getPOS(pos);

		switch (tag) {

		case n:
			POSTag = POS.NOUN;
			break;

		case v:
			POSTag = POS.VERB;
			break;

		case a:
			POSTag = POS.ADJECTIVE;
			break;

		case r:
			POSTag = POS.ADVERB;
			break;

		default:
			POSTag = POS.NOUN;
			break;
		}

		return POSTag;
	}

	@Override
	public void unload() {

		this.mCache.clear();

		if (mDictionary != null)
			mDictionary.close();

		if (mInit) {
			Dictionary.uninstall();
			JWNL.shutdown();
		}
	}

	@Override
	public String getDefaultModel() {

		return CONFIG;
	}

}
