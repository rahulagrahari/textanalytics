package it.si3p.supwsd.modules.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import it.si3p.supwsd.data.Annotation;

/**
 * @author papandrea
 *
 */
public class PlainParser extends Parser {


	@Override
	public void parse(String file) throws Exception {

		Annotation annotation;
		String line;
		int id=0;
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
				
			while ((line = reader.readLine()) != null) {

				annotation = new Annotation(String.valueOf(id++),line.trim());
				this.mParserListener.annotationsReady(Arrays.asList(annotation),null);
			}

		}
	}
}