package it.uniroma1.lcl.supWSD.modules.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTest;
import it.uniroma1.lcl.supWSD.modules.classification.scorer.Result;

/**
 * @author Simone Papandrea
 *
 */
class PlainWriter extends Writer {

	private static final String TAG = "x";
	private static final String FILE_NAME = "plain";

	@Override
	public void write(Map<AmbiguityTest, TreeSet<Result>> tests) throws IOException {

		final String filename;
		BufferedWriter writer = null;
		Map<String, TreeSet<Result>> map;
		TreeSet<Result> results;
		String line, id;

		filename = getDir() + File.separator + FILE_NAME + ".result";

		try {

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), ENCODING));
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

			for (TreeSet<Result> r : map.values()) {

				line = "";

				for (Result result:r) {

					line += "<" + TAG;

					if (result.hasLegalAnswer())
						line += " " + String.join(" ", result.getAnswers());

					line += ">" + result.getWord() + "</" + TAG + "> ";
				}

				writer.write(line.trim());
				writer.newLine();
			}

		} finally {

			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
				}
		}
	}
}