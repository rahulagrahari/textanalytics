package it.si3p.supwsd.modules.preprocessing.units.tagger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import it.si3p.supwsd.modules.preprocessing.units.Unit;
import it.si3p.supwsd.modules.preprocessing.units.lemmatizer.Lemmatizer;

/**
 * @author papandrea
 *
 */
public class TreeTagger extends Unit implements Tagger, Lemmatizer {

	private static final String MODEL = "models/TreeTagger/lib/english-utf8.par";
	private static final String HOME= "models/TreeTagger";
	private TreeTaggerWrapper<String> mTreeTagger;
	private Map <String,String[]>mLemmas;
	private int mIndex;
	
	public TreeTagger(String modelPath) {
	
		super(modelPath);
		
		mLemmas=new  HashMap<String,String[]>();
	}

	public String[] lemmatize(String[] words, String[] POS) {

		String[] lemmas;
		String key;
		
		key=String.join("", words);
		lemmas= mLemmas.get(key);
		mLemmas.remove(key);
		
		return lemmas;
	}
	
	public synchronized String[] tag(List<String> words) {

		String[] tags,lemmas;
		int size;
		
		size=words.size();
		tags=new String[size];
		lemmas=new String[size];
		mIndex=0;
		
		mTreeTagger.setHandler(new TokenHandler<String>() {
			
			public void token(String token, String pos, String lemma) {
				
				tags[mIndex]=pos;
				lemmas[mIndex]=lemma;
				mIndex++;			
			}
		});

		try {
			mTreeTagger.process(words);
		} catch (IOException | TreeTaggerException e) {
		}
		
		mLemmas.put(String.join("", words),lemmas);
		
		return tags;
	}

	@Override
	public void load() throws IOException {

		System.setProperty("treetagger.home",HOME);
		
		mTreeTagger = new TreeTaggerWrapper<String>();
		mTreeTagger.setModel(this.getModel());
	}

	@Override
	public void unload() {

		mLemmas.clear();
	
		if (mTreeTagger != null)
			mTreeTagger.destroy();
	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}
}