package it.si3p.supwsd.modules.preprocessing.units.splitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import it.si3p.supwsd.modules.preprocessing.units.Unit;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * @author papandrea
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
	public void load() throws IOException {

		try (InputStream inputStream = new FileInputStream(this.getModel())){

			this.mSentenceModel= new SentenceModel(inputStream);
		}
	}

	@Override
	public String getDefaultModel() {

		return MODEL;
	}

}
