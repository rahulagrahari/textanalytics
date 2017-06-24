package it.si3p.supwsd.modules.classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTest;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTrain;
import it.si3p.supwsd.modules.classification.instances.TermsVector;
import it.si3p.supwsd.modules.extraction.features.Feature;
import opennlp.tools.ml.maxent.GISModel;
import opennlp.tools.ml.maxent.io.BinaryGISModelWriter;

/**
 * @author papandrea
 *
 */
public class Serializer {

	private final static String MODELS_DIR = "models";
	private final static String STATS_DIR = "stat";
	private static String DIRECTORY = ".";
	protected static final String ENCODING = "ISO8859-1";
	private static final String DEFAULT_SEPARATOR = "\t";

	public static Object readModel(String lexel) throws IOException, ClassNotFoundException {

		Object model = null;
		final String filename;

		filename = DIRECTORY + File.separator + MODELS_DIR + File.separator + lexel + ".model.gz";

		try (ObjectInputStream stream= new ObjectInputStream(new GZIPInputStream(new FileInputStream(filename)))){
			
			model = stream.readObject();
		}
		
		return model;
	}

	public static AmbiguityTest readStatistic(String lexel) throws IOException {

		AmbiguityTest ambiguityTest = null;
		String line, senses[], tokens[];
		String key, filename;

		filename = DIRECTORY + File.separator + STATS_DIR + File.separator + lexel + ".stat.gz";

		try(BufferedReader reader = new BufferedReader(
					new InputStreamReader(new GZIPInputStream(new FileInputStream(filename)), ENCODING))){

			line = reader.readLine();
			senses = line.split(DEFAULT_SEPARATOR);
			ambiguityTest = new AmbiguityTest(lexel, senses);

			while ((line = reader.readLine()) != null) {

				tokens = line.split(DEFAULT_SEPARATOR);
				key = tokens[0];

				for (int i = 1; i < tokens.length; i += 2)
					ambiguityTest.setStatistic(key, tokens[i], Integer.valueOf(tokens[i + 1]));
			}
		}

		return ambiguityTest;
	}

	public static void writeStatistic(AmbiguityTrain ambiguity) throws IOException {

		TermsVector vector;
		String line;
		Set<String> keys;
		final String filename;

		filename = DIRECTORY + File.separator + STATS_DIR + File.separator + ambiguity.getLexel() + ".stat.gz";

		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(filename)), ENCODING))){

			writer.write(String.join(DEFAULT_SEPARATOR, ambiguity.getSenses()));
			writer.newLine();

			for (Class<? extends Feature> type : ambiguity.getTypes()) {

				keys = ambiguity.getFeatureKeys(type);

				if (keys != null) {

					for (String featureKey : keys) {

						writer.write(featureKey);
						writer.write(DEFAULT_SEPARATOR);
						vector = ambiguity.getFeatureStatistic(featureKey);
						line = "";

						for (String key : vector.getKeys())
							line += key + DEFAULT_SEPARATOR + String.valueOf(vector.get(key).getIndex())
									+ DEFAULT_SEPARATOR;

						writer.write(line.trim());
						writer.newLine();
					}
				}
			}
		}
	}

	public static void writeModel(String lexel, Object model) throws IOException {

		final String filename = DIRECTORY + File.separator + MODELS_DIR + File.separator + lexel + ".model.gz";
		
		try(GZIPOutputStream gzipStream = new GZIPOutputStream(new FileOutputStream(filename))) {

			if (GISModel.class.isInstance(model)) {

				try(DataOutputStream stream = new DataOutputStream(gzipStream)){
				
					BinaryGISModelWriter writer = new BinaryGISModelWriter((GISModel) model, stream);

					try {
						writer.persist();
					} finally {
						writer.close();
					}
				}	
			}
			else {

				try(ObjectOutputStream stream = new ObjectOutputStream(gzipStream);) {
					stream.writeObject(model);
				}
			}
		}
	}

	public static void setDirectory(String dir) {

		DIRECTORY = dir;

		new File(dir + File.separator + MODELS_DIR).mkdirs();
		new File(dir + File.separator + STATS_DIR).mkdirs();
	}

	public static boolean modelExists(String lexel) {

		boolean exist;
		final Path path;

		try {
			path = Paths.get(DIRECTORY, MODELS_DIR, lexel + ".model.gz");
			exist = Files.exists(path);
		
		} catch (InvalidPathException e) {
			
			exist = false;
		}

		return exist;
	}

}
