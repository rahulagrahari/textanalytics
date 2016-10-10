package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * @author Simone Papandrea
 *
 */
class OpenNLPTokenizer extends Tokenizer {

	private static final String MODEL = "models/openNLP/en-token.bin";
	private TokenizerModel mTokenizerModel;

	OpenNLPTokenizer(String modelFile) {

		super(modelFile);

	}

	@Override
	public String[] tokenize(String sentence) {

		return new TokenizerME(mTokenizerModel).tokenize(sentence);
	}

	@Override
	public void load() throws Exception {

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(this.getModel());
			mTokenizerModel = new TokenizerModel(inputStream);
	
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
