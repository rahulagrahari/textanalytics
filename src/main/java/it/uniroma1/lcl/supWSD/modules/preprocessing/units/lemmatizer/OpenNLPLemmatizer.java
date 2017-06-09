package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;


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
	public void load() throws Exception {
		
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(this.getModel());
			mLemmatizerModel = new LemmatizerModel(inputStream);
		} 

		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	protected String getDefaultModel() {
		
		return MODEL;
	}

}
