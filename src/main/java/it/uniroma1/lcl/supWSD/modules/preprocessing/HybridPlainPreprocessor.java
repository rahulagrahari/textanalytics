package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.List;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer.Tokenizer;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.DependencyParser;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer.Lemmatizer;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.splitter.Splitter;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger.Tagger;

/**
 * @author Simone Papandrea
 *
 */
public class HybridPlainPreprocessor extends HybridPreprocessor {

	public HybridPlainPreprocessor(Splitter splitter, Tokenizer tokenizer, Tagger tagger, Lemmatizer lemmatizer,
			DependencyParser dependencyParser) {

		super(splitter, tokenizer, tagger, lemmatizer, dependencyParser);

	}
	
	@Override
	public final void execute(Annotation annotation) {

		String[] sentences;
		String[][] lemmas = null, POS = null,words;
		DependencyTree[] dependencies = null;
		List<Lexel> lexels;
		CompoundProcessor cProcessor;
		
		sentences = split(annotation.getText());
		words = tokenize(sentences);

		if (words != null){
			
			POS = POSTag(words);

			if (POS != null)
				lemmas = lemmatize(words, POS);

			cProcessor = new CompoundProcessor(annotation.getID());
			lexels = cProcessor.getCompounds(words, lemmas, POS);
			annotation.getLexels().addAll(lexels);
			
			if (lemmas != null)
				dependencies = parseDependencies(lemmas, POS);

			annotation.annote(words, POS, lemmas, dependencies);
		}
		 else
			annotation.annote(sentences);
	}	
}