package it.si3p.supwsd.modules.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import it.si3p.supwsd.data.Annotation;

/**
 * @author papandrea
 *
 */
public class PlainParser extends Parser {


	@Override
	public void parse(String file) throws Exception {

		List<Annotation> annotations;
		Annotation annotation;
		String line;
		int id=0;
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
				
			while ((line = reader.readLine()) != null) {

				annotations = new ArrayList<Annotation>();
				annotation = new Annotation(id++,line.trim());
				annotations.add(annotation);
				this.mParserListener.annotationsReady(annotations);
			}

		}
	}
}