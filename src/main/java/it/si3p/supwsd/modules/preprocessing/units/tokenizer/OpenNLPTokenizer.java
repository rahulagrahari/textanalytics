package it.si3p.supwsd.modules.preprocessing.units.tokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import it.si3p.supwsd.modules.preprocessing.units.Unit;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * @author papandrea
 *
 */
class OpenNLPTokenizer extends Unit implements Tokenizer {

	private static final String MODEL = "models/openNLP/en-token.bin";
	private TokenizerModel mTokenizerModel;

	OpenNLPTokenizer(String modelFile) {

		super(modelFile);

	}


	public String[] tokenize(String sentence) {

		return new TokenizerME(mTokenizerModel).tokenize(sentence);
	}

	@Override
	public void load() throws IOException {

		try(InputStream inputStream = new FileInputStream(this.getModel())) {
		
			mTokenizerModel = new TokenizerModel(inputStream);	
		} 
	}

	@Override
	public String getDefaultModel() {

		return MODEL;
	}

}
