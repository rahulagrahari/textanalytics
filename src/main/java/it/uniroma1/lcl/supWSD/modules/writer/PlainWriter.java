package it.uniroma1.lcl.supWSD.modules.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTest;
import it.uniroma1.lcl.supWSD.modules.classification.instances.Instance;
import it.uniroma1.lcl.supWSD.modules.classification.scorer.Result;

/**
 * @author Simone Papandrea
 *
 */
class PlainWriter extends Writer {

	private static final String TAG = "x";
	private static final String FILE_NAME = "plain";
	
	@Override
	public void write(Map<AmbiguityTest, TreeSet<Result>>tests) throws IOException {

		TreeSet<Instance> instances;
		TreeSet<Result> results;
		Iterator<Result> iterator;
		Result result;
		String line = "", word;

		instances = new TreeSet<Instance>();
		results = new TreeSet<Result>();

		for (Entry<AmbiguityTest, TreeSet<Result>> e : tests.entrySet()) {

			instances.addAll(e.getKey().getInstances());
			results.addAll(e.getValue());
		}

		iterator = results.iterator();

		for (Instance instance : instances) {

			word = instance.getWord();
			result = iterator.next();
			line += "<" + TAG;
			
			if (result.hasLegalAnswer())
				line += " " + String.join(" ", result.getAnswers());

			line += ">" + word + "</" + TAG + "> ";
		}

		writeResult(line.trim());
	}

	private void writeResult(String result) throws IOException {

		final String filename;
		BufferedWriter writer = null;

		filename = getDir()+ File.separator+ FILE_NAME + ".result";

		try {

			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), ENCODING));
			writer.write(result);
			writer.newLine();

		} finally {

			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
				}
		}
	}
}