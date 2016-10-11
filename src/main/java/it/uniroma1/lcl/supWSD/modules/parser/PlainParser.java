package it.uniroma1.lcl.supWSD.modules.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer.Tokenizer;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer.TokenizerFactory;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer.TokenizerType;

/**
 * @author Simone Papandrea
 *
 */
public class PlainParser extends Parser {

	private int mNgram = 1;

	public void setNgram(int ngram) {

		this.mNgram = ngram;
	}

	@Override
	public void parse(String file) throws Exception {

		BufferedReader reader = null;
		List<Annotation> annotations;
		Annotation annotation;
		String line;
		int count = 0;
		Tokenizer tokenizer = null; 
		
		try {

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			tokenizer=TokenizerFactory.getInstance().getTokenizer(TokenizerType.STANFORD,null);
			tokenizer.load();
				
			while ((line = reader.readLine()) != null) {

				annotations = new ArrayList<Annotation>();
				annotation = parseLine(tokenizer.tokenize(line),count);
				annotations.add(annotation);
				count += annotation.getLexelsCount();
				this.mParserListener.annotationsReady(annotations);
			}

		} finally {

			if(tokenizer!=null)
				tokenizer.unload();
			
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}

	}

	private Annotation parseLine(String tokens[], int id) {

		Annotation annotation;
		String sentence="", word;
		int length, min;
		List<Lexel> lexels;
	
		length = tokens.length;
		lexels = new ArrayList<Lexel>();

		for (int i = 0; i < length; i++) {

			min = Math.min(mNgram, length - i);
			word = "";

			for (int j = 0; j < min; j++) {

				word += tokens[i + j];
				lexels.add(new Lexel(String.format("%010d", id++), word));
				word += "_";
			}

			sentence += Annotation.ANNOTATION_TAG + tokens[i] + Annotation.ANNOTATION_TAG+" ";
		}

		annotation = new Annotation(sentence.trim());
		annotation.addLexels(lexels);

		return annotation;
	}

}
