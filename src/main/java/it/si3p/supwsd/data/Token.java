package it.si3p.supwsd.data;


/**
 * @author papandrea
 *
 */
public class Token{

	private final String mWord;
	private final String mPOS;
	private final String mLemma;

	public Token(String word,String pos,String lemma) {

		this.mWord = word;
		this.mPOS = pos;
		this.mLemma = lemma==null?null:lemma.toLowerCase();
		
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
