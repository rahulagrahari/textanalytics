package it.si3p.supwsd.modules.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTest;
import it.si3p.supwsd.modules.classification.scorer.Result;

/**
 * @author papandrea
 *
 */
class AllWriter extends Writer {

	@Override
	public void write(Map<AmbiguityTest, TreeSet<Result>> tests) throws IOException {

		final String filename;

		filename = getDir() + File.separator + "results.result";
	
		try (BufferedWriter	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), ENCODING))){

			for (Entry<AmbiguityTest, TreeSet<Result>> e : tests.entrySet()){
				
				for (Result result : e.getValue()) {

					writer.write(result.toString());
					writer.newLine();
				}
			}
		}
	}
}
