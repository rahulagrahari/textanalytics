package it.si3p.supwsd.modules.preprocessing;

import java.util.List;
import it.si3p.supwsd.modules.preprocessing.units.tokenizer.Tokenizer;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.DependencyParser;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;
import it.si3p.supwsd.modules.preprocessing.units.lemmatizer.Lemmatizer;
import it.si3p.supwsd.modules.preprocessing.units.splitter.Splitter;
import it.si3p.supwsd.modules.preprocessing.units.tagger.Tagger;

/**
 * @author papandrea
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