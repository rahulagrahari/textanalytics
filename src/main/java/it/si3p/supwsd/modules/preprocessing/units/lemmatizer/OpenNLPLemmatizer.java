package it.si3p.supwsd.modules.preprocessing.units.lemmatizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import it.si3p.supwsd.modules.preprocessing.units.Unit;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;

/**
 * @author papandrea
 *
 */
public class OpenNLPLemmatizer extends Unit implements Lemmatizer {

	private static final String MODEL = "models/openNLP/en-lemmatizer.bin";
	private LemmatizerModel mLemmatizerModel;
	
	public OpenNLPLemmatizer(String modelPath) {
		super(modelPath);
	}

	@Override
	public String[] lemmatize(String[] words, String[] POS) {
		
		return new LemmatizerME( mLemmatizerModel).lemmatize(words,POS);
	}


	@Override
	public void load() throws IOException {
		
		try(InputStream inputStream = new FileInputStream(this.getModel())){
		
			mLemmatizerModel = new LemmatizerModel(inputStream);
		} 
	}

	@Override
	protected String getDefaultModel() {
		
		return MODEL;
	}
}