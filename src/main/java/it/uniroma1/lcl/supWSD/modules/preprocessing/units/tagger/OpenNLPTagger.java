package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * @author Simone Papandrea
 *
 */
class OpenNLPTagger extends Unit implements Tagger {

	private static final String MODEL = "models/openNLP/en-pos-maxent.bin";
	private POSModel mPOSModel;
	
	OpenNLPTagger(String modelFile) {

		super(modelFile);

	}

	public String[] tag(List<String> words) {
					
		return new POSTaggerME(mPOSModel).tag(words.toArray(new String[words.size()]));
		
	}

	@Override
	public void load() throws Exception {

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(this.getModel());
			mPOSModel = new POSModel(inputStream);
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
	public String getDefaultModel() {

		return MODEL;
	}

}
