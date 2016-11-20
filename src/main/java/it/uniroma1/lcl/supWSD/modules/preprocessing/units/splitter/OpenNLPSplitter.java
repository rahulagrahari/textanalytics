package it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * @author Simone Papandrea
 *
 */
class OpenNLPSplitter extends Unit implements Splitter {

	private static final String MODEL = "models/openNLP/en-sent.bin";
	private SentenceModel mSentenceModel;

	OpenNLPSplitter(String modelFile) {

		super(modelFile);

	}

	public String[] split(String sentence) {

		SentenceDetectorME sentenceDetector;

		sentenceDetector = new SentenceDetectorME(mSentenceModel);

		return sentenceDetector.sentDetect(sentence);
	}

	@Override
	public void load() throws Exception {

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(this.getModel());
			this.mSentenceModel= new SentenceModel(inputStream);

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
