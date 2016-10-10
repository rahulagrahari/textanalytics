package it.uniroma1.lcl.supWSD.modules.classification.instances;

/**
 * @author Simone Papandrea
 *
 */
public class Term {

	private int mIndex;
	private int mFrequency;

	public Term(int index) {

		this.mIndex =index;
		this.mFrequency = 0;
	}

	void occurence() {

		this.mFrequency++;
	}

	public int getIndex() {

		return this.mIndex;
	}

	public void setIndex(int index) {

		this.mIndex=index;
	}
	
	public int getFrequency() {

		return this.mFrequency;
	}

}
