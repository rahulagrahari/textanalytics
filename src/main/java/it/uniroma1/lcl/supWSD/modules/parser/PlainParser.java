package it.uniroma1.lcl.supWSD.modules.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import it.uniroma1.lcl.supWSD.data.Annotation;

/**
 * @author Simone Papandrea
 *
 */
public class PlainParser extends Parser {


	@Override
	public void parse(String file) throws Exception {

		BufferedReader reader = null;
		List<Annotation> annotations;
		Annotation annotation;
		String line;
		int id=0;
		
		try {

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				
			while ((line = reader.readLine()) != null) {

				annotations = new ArrayList<Annotation>();
				annotation = new Annotation(id++,line.trim());
				annotations.add(annotation);
				this.mParserListener.annotationsReady(annotations);
			}

		} finally {
			
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
	}

}
