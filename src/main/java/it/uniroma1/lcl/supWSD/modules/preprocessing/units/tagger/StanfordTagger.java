package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger;

import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Simone Papandrea
 *
 */
class StanfordTagger extends Tagger {

	private static final String MODEL = "edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger";
	private MaxentTagger mTagger;
	
	StanfordTagger(String modelFile) {

		super(modelFile);
	}

	@Override
	public String[] tag(List<String> words) {

		String[]tags;
		String[] tokens;
		String pos;
		int length;
		
		tags=new String[words.size()];
		pos=mTagger.tagTokenizedString(String.join(" ",words));
		tokens = pos.split(" ");
		length=tokens.length;
		tags=new String[length];
		
		for(int i=0;i<length;i++)
			tags[i]=tokens[i].split("_")[1];
			
		return tags;
	}

	@Override
	public void load() throws Exception {

		mTagger = new MaxentTagger(this.getModel());
	}

	@Override
	public String getDefaultModel() {

		return MODEL;
	}

}
