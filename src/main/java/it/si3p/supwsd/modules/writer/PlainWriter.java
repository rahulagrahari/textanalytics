package it.si3p.supwsd.modules.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import it.si3p.supwsd.data.POSMap;
import it.si3p.supwsd.data.POSMap.TAG;
import it.si3p.supwsd.data.Token;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTest;
import it.si3p.supwsd.modules.classification.scorer.Result;

/**
 * @author papandrea
 *
 */
class PlainWriter extends Writer {

	private static final String FILE_NAME = "plain";

	@Override
	public void write(Map<AmbiguityTest, TreeSet<Result>> tests) throws IOException {

		final String filename;
		Map<String, TreeSet<Result>> map;
		TreeSet<Result> results;
		String line, id;
		TAG pos;
		Token token;
		 POSMap posMap;
		 
		filename = getDir() + File.separator + FILE_NAME + ".result";

		try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), ENCODING))) {

			map = new TreeMap<String, TreeSet<Result>>();
				 
			for (Entry<AmbiguityTest, TreeSet<Result>> e : tests.entrySet()) {

				for (Result result : e.getValue()) {

					id = result.getID();
					id = id.substring(0, id.indexOf("."));
					results = map.get(id);

					if (results == null) {

						results = new TreeSet<Result>();
						map.put(id,results);
					}

					results.add(result);
				}
			}
			
			posMap = POSMap.getInstance();

			for (TreeSet<Result> r : map.values()) {

				line = "";

				for (Result result:r) {

					token=result.getToken();
					pos=posMap.getPOS(token.getPOS());
					line += "<" + pos;

					if (result.hasLegalAnswer())
						line += " " + String.join(" ", result.getAnswers());

					
					line += ">" + token.getWord() + "</" + pos+ "> ";
				}

				writer.write(line.trim());
				writer.newLine();
			}
		}
	}
}