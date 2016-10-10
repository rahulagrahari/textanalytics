package it.uniroma1.lcl.supWSD.modules.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTest;
import it.uniroma1.lcl.supWSD.modules.classification.scorer.Result;

/**
 * @author Simone Papandrea
 *
 */
class AllWriter extends Writer {

	@Override
	public void write(Map<AmbiguityTest, TreeSet<Result>> tests) throws IOException {

		final String filename;
		BufferedWriter writer = null;

		filename = getDir() + File.separator + "results.result";
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), ENCODING));

		try {

			for (Entry<AmbiguityTest, TreeSet<Result>> e : tests.entrySet())
				
				for (Result result : e.getValue()) {

					writer.write(result.toString());
					writer.newLine();
				}

		} finally {

			if (writer != null)
				try {
					writer.close();
				} catch (IOException ex) {
				}
		}
	}
}
