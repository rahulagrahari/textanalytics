package it.si3p.supwsd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import it.si3p.supwsd.config.Config;
import it.si3p.supwsd.inventory.SenseInventory;
import it.si3p.supwsd.inventory.SenseInventoryFactory;
import it.si3p.supwsd.mns.MNS;
import it.si3p.supwsd.mns.MNSFactory;
import it.si3p.supwsd.modules.Tester;
import it.si3p.supwsd.modules.Trainer;
import it.si3p.supwsd.modules.classification.Serializer;
import it.si3p.supwsd.modules.classification.classifiers.Classifier;
import it.si3p.supwsd.modules.classification.classifiers.ClassifierFactory;
import it.si3p.supwsd.modules.parser.Parser;
import it.si3p.supwsd.modules.parser.ParserFactory;
import it.si3p.supwsd.modules.parser.ParserType;
import it.si3p.supwsd.modules.preprocessing.HybridXMLPreprocessor;
import it.si3p.supwsd.modules.preprocessing.HybridPlainPreprocessor;
import it.si3p.supwsd.modules.preprocessing.HybridPreprocessor;
import it.si3p.supwsd.modules.preprocessing.Preprocessor;
import it.si3p.supwsd.modules.preprocessing.StanfordPlainPreprocessor;
import it.si3p.supwsd.modules.preprocessing.StanfordPreprocessor;
import it.si3p.supwsd.modules.preprocessing.StanfordXMLPreprocessor;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.DependencyParserFactory;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.DependencyParserType;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.DependencyParser;
import it.si3p.supwsd.modules.preprocessing.units.lemmatizer.Lemmatizer;
import it.si3p.supwsd.modules.preprocessing.units.lemmatizer.LemmatizerFactory;
import it.si3p.supwsd.modules.preprocessing.units.lemmatizer.LemmatizerType;
import it.si3p.supwsd.modules.preprocessing.units.splitter.Splitter;
import it.si3p.supwsd.modules.preprocessing.units.splitter.SplitterFactory;
import it.si3p.supwsd.modules.preprocessing.units.splitter.SplitterType;
import it.si3p.supwsd.modules.preprocessing.units.tagger.Tagger;
import it.si3p.supwsd.modules.preprocessing.units.tagger.TaggerFactory;
import it.si3p.supwsd.modules.preprocessing.units.tagger.TaggerType;
import it.si3p.supwsd.modules.preprocessing.units.tokenizer.Tokenizer;
import it.si3p.supwsd.modules.preprocessing.units.tokenizer.TokenizerFactory;
import it.si3p.supwsd.modules.preprocessing.units.tokenizer.TokenizerType;
import it.si3p.supwsd.modules.writer.Writer;
import it.si3p.supwsd.modules.writer.WriterFactory;

/**
 * @author papandrea
 *
 */
public class SupWSD {

	public static void train(String conf, String corpus, String keys) throws Exception {

		Trainer trainer;
		Config config;
		ParserType parserType;
		Parser parser;
		Classifier<?, ?> classifier;
		Preprocessor preprocessor;
		Map<String, SortedSet<String>> senses = null;

		senses = readSenses(keys);
		config = Config.load(conf);
		Serializer.setDirectory(config.getWorkingDir());
		Writer.setDirectory(config.getWorkingDir());
		parserType = config.getParserType();
		parser = ParserFactory.getInstance().getParser(parserType);
		classifier = ClassifierFactory.getInstance().getClassifier(config.getClassifierType());
		preprocessor = getPreprocessor(config.getSplitterType(), config.getTokenizerType(), config.getTaggerType(),
				config.getLemmatizerType(), config.getDParserType(), config.getSplitterModel(),
				config.getTokenizerModel(), config.getTaggerModel(), config.getLemmatizerModel(),
				config.getDParserModel(), parserType);

		trainer = new Trainer(parser, preprocessor, config.getFeatureExtractors(), classifier, senses);
		trainer.execute(corpus);
	}

	public static void test(String conf, String corpus, String keys) throws Exception  {

		Tester tester;
		Config config;
		ParserType parserType;
		Parser parser;
		MNS mns;
		Preprocessor preprocessor;
		Classifier<?, ?> classifier;
		Writer writer;
		SenseInventory senseInventory;
		Map<String, SortedSet<String>> senses = null;

		if (keys != null)
			senses = readSenses(keys);

		config = Config.load(conf);
		Serializer.setDirectory(config.getWorkingDir());
		Writer.setDirectory(config.getWorkingDir());
		parserType = config.getParserType();
		parser = ParserFactory.getInstance().getParser(parserType);
		mns = MNSFactory.getInstance().getMNS(config.getParserType(), config.getMNS());
		preprocessor = getPreprocessor(config.getSplitterType(), config.getTokenizerType(), config.getTaggerType(),
				config.getLemmatizerType(), config.getDParserType(), config.getSplitterModel(),
				config.getTokenizerModel(), config.getTaggerModel(), config.getLemmatizerModel(),
				config.getDParserModel(), parserType);
		classifier = ClassifierFactory.getInstance().getClassifier(config.getClassifierType());
		writer = WriterFactory.getInstance().getWriter(config.getWriterType());
		senseInventory = SenseInventoryFactory.getInstance().getSenseInventory(config.getSenseInventory(),
				config.getDict());

		tester = new Tester(parser, mns, preprocessor, config.getFeatureExtractors(), classifier, writer, senses,
				senseInventory);
		tester.execute(corpus);
	}

	private static Preprocessor getPreprocessor(SplitterType splitterType, TokenizerType tokenizerType,
			TaggerType taggerType, LemmatizerType lemmatizerType, DependencyParserType dependencyParserType,
			String splitterModel, String tokenizerModel, String taggerModel, String lemmatizerModel,
			String dParserModel, ParserType parserType) {

		Preprocessor preprocessor;
		boolean split, pos, lemma, depparse;

		split = splitterType != null;
		pos = taggerType != null;
		lemma = lemmatizerType != null;
		depparse = dependencyParserType != null;

		if ((tokenizerType != null && tokenizerType.equals(TokenizerType.STANFORD))
				&& (!split || (splitterType.equals(SplitterType.STANFORD)))
				&& (!pos || (split && taggerType.equals(TaggerType.STANFORD)))
				&& (!lemma || (pos && lemmatizerType.equals(LemmatizerType.STANFORD)))
				&& (!depparse || (pos && dependencyParserType.equals(DependencyParserType.STANFORD)))) {

			preprocessor = getStanfordPreprocessor(split,pos,lemma,depparse,taggerModel,  dParserModel,
					parserType);
		} else {

			preprocessor = getHybridPreprocessor(splitterType, tokenizerType, taggerType, lemmatizerType,
					dependencyParserType, splitterModel, tokenizerModel, taggerModel, lemmatizerModel, dParserModel,
					parserType);
		}

		return preprocessor;
	}

	private static StanfordPreprocessor getStanfordPreprocessor(boolean split, boolean pos, boolean lemma,
			boolean depparse, String taggerModel, String dParserModel, ParserType parserType) {

		StanfordPreprocessor preprocessor;

		if (parserType.equals(ParserType.PLAIN))
			preprocessor = new StanfordPlainPreprocessor(split, pos, lemma, depparse);
		else
			preprocessor = new StanfordXMLPreprocessor(split, pos, lemma, depparse);

		if (taggerModel != null)
			preprocessor.setPOSModel(taggerModel);
		
		if (dParserModel != null)
			preprocessor.setDepparseSModel(dParserModel);

		return preprocessor;
	}

	private static HybridPreprocessor getHybridPreprocessor(SplitterType splitterType, TokenizerType tokenizerType,
			TaggerType taggerType, LemmatizerType lemmatizerType, DependencyParserType dependencyParserType,
			String splitterModel, String tokenizerModel, String taggerModel, String lemmatizerModel,
			String dParserModel, ParserType parserType) {

		HybridPreprocessor preprocessor;
		Splitter splitter;
		Tokenizer tokenizer;
		Tagger tagger;
		Lemmatizer lemmatizer;
		DependencyParser dependencyParser;

		splitter = SplitterFactory.getInstance().getSplitter(splitterType, splitterModel);
		tokenizer = TokenizerFactory.getInstance().getTokenizer(tokenizerType, tokenizerModel);
		tagger = TaggerFactory.getInstance().getTagger(taggerType, taggerModel);

		if (taggerType.equals(lemmatizerType) && tagger instanceof Lemmatizer)
			lemmatizer = (Lemmatizer) tagger;
		else
			lemmatizer = LemmatizerFactory.getInstance().getLemmatizer(lemmatizerType, lemmatizerModel);

		dependencyParser = DependencyParserFactory.getInstance().getDependecyParser(dependencyParserType, dParserModel);

		if (parserType.equals(ParserType.PLAIN))
			preprocessor = new HybridPlainPreprocessor(splitter, tokenizer, tagger, lemmatizer, dependencyParser);
		else
			preprocessor = new HybridXMLPreprocessor(splitter, tokenizer, tagger, lemmatizer, dependencyParser);

		return preprocessor;
	}

	private static Map<String, SortedSet<String>> readSenses(String keysFile) throws  IOException  {

		Map<String, SortedSet<String>> keys;
		final String regex = "\\s|\\t|\\n|\\r|\\f";
		String line, sense, tokens[];
		SortedSet<String> senses;

		keys = new HashMap<String, SortedSet<String>>();

		try (BufferedReader keyReader = new BufferedReader(new InputStreamReader(new FileInputStream(keysFile)))){

			while ((line = keyReader.readLine()) != null) {

				tokens = line.split(regex);
				senses = new TreeSet<String>();

				for (int i = 2; i < tokens.length; i++) {

					sense = tokens[i];

					if (sense.equals("!!"))
						break;

					senses.add(sense);
				}

				keys.put(tokens[1], senses);
			}

		}

		return keys;
	}
}
