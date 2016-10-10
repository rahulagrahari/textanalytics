package it.uniroma1.lcl.supWSD.data;


/**
 * @author Simone Papandrea
 *
 */
public class Token{

	private final String mWord;
	private final String mPOS;
	private final String mLemma;

	public Token(String word,String pos,String lemma) {

		this.mWord = word;
		this.mPOS = pos;
		this.mLemma = lemma!=null?lemma.toLowerCase():lemma;
		
	}
	
	public String getWord() {

		return mWord;
	}

	public String getPOS() {

		return mPOS;
	}

	public String getLemma() {

		return mLemma;
	}

}
