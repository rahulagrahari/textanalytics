package it.si3p.supwsd.mns;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.data.POSMap;
import it.si3p.supwsd.data.POSMap.TAG;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;


/**
 * @author papandrea
 *
 */
class SensEvalMNS extends MNS{

	private Dictionary mDictionary;
	private final Pattern mLegalPattern = Pattern.compile("^[a-z\\-_ \\.\\/']*$");
	private final Pattern mMultiWordsPattern = Pattern.compile("[\\- _]");
	private final Pattern mEfficentPattern = Pattern.compile("(^[\\- _]|[\\- _]{2}|[\\- _]$)");
	private boolean mInit;
	
	SensEvalMNS(String file) throws IOException{
		
		super(file);		
	}

	@Override
	public void load() throws Exception {
		
		if(this.isEnabled()){
		
			super.load();
		}
		
		mInit = !JWNL.isInitialized();

		if (mInit) {
			
			try(InputStream inputStream = new FileInputStream("resources/wndictionary/prop.xml");) {
				
				JWNL.initialize(inputStream);
			}
		}

		mDictionary = Dictionary.getInstance();
	}
	
	@Override
	public List<String> resolve(Lexel lexel,String pos) {
		
		List<String> names;
		String word,name;
		
		names=super.get(lexel.getID());
		
		if(names==null){
			
			word=lexel.toString();
			name = getLexel(word, pos, true);

			if (name == null)
				name = getLexel(word, pos, false);
			
			names=Arrays.asList(new String[]{name});
		}
		
		return names;
	}
		
	private String getLexel(String word, String pos, boolean deep) {

		String lexel = null, lemma;
		POS POS;
		TAG tag;

		tag = POSMap.getInstance().getPOS(pos);
		POS = getPOS(tag);
		lemma = word;

		try {

			lemma = getLemma(word, POS, deep);

			if (hasSense(lemma, POS))
				lexel = lemma + "." + tag;

			else {

				for (TAG t : POSMap.TAG.values()) {

					if (!t.equals(tag)) {
						POS = getPOS(t);
						lemma = getLemma(lemma, POS, deep);

						if (hasSense(lemma, POS)) {
							lexel = lemma + "." + t;
							break;
						}
					}
				}
			}

		} catch (JWNLException e) {
		}

		if (lexel == null && !deep)
			lexel = lemma + "." + tag;

		return lexel;
	}

	private String getLemma(String word, POS POS, boolean deep) throws JWNLException {

		String lemma, subword;
		String tokens[];
		int length, l;

		word = word.trim().toLowerCase();
		lemma = word;

		if (!word.isEmpty() && mLegalPattern.matcher(word).find() && !mEfficentPattern.matcher(word).find()) {

			lemma = getBaseForm(word, POS);

			if (!lemma.equals(word) && deep) {

				tokens = this.mMultiWordsPattern.split(word);
				length = tokens.length;

				if (length > 1) {

					tokens: for (int i = 0; i < length; i++) {

						subword = "";
						l = i == 0 ? length - 1 : length;

						for (int j = i; j < l; j++) {

							subword += tokens[j];

							if (lemma.equals(subword) || lemma.equals(getBaseForm(subword, POS))) {
								lemma = word;
								break tokens;
							}

							subword += " ";
						}
					}
				}
			}

			lemma = lemma.replace(" ", "_");
		}

		return lemma;
	}

	@SuppressWarnings("unchecked")
	private String getBaseForm(String word, POS POS) throws JWNLException {

		String lemma, l;
		List<String> indexWords;
		int lemmas;

		lemma = word;
		indexWords = mDictionary.getMorphologicalProcessor().lookupAllBaseForms(POS, word);
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

	private boolean hasSense(String lemma, POS pos) throws JWNLException {

		IndexWord indexWord;

		indexWord = mDictionary.lookupIndexWord(pos, lemma);

		return indexWord != null && indexWord.getLemma().replace(' ', '_').equals(lemma);
	}

	private POS getPOS(POSMap.TAG tag) {

		POS POSTag;

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

		if(mDictionary!=null)
		mDictionary.close();
		
		if(mInit){
			Dictionary.uninstall();
			JWNL.shutdown();
		}
	}
}
