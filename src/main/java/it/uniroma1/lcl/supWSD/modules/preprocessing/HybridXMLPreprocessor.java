package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.ArrayList;
import java.util.Iterator;
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
public class HybridXMLPreprocessor extends HybridPreprocessor {

	
	public HybridXMLPreprocessor(Splitter splitter, Tokenizer tokenizer, Tagger tagger, Lemmatizer lemmatizer,
			DependencyParser dependencyParser) {

		super(splitter,tokenizer,tagger,lemmatizer,dependencyParser);
	}

	@Override
	public final void execute(Annotation annotation) {

		String[] sentences;
		String[][] lemmas = null, POS = null,tokens=null,words;
		DependencyTree[] dependencies = null;
				
		sentences = split(annotation.getText());
		words = tokenize(sentences);

		if (words != null){
			
			tokens =getTokens(words, annotation.iterator());
			POS = POSTag(tokens);

			if (POS != null)
				lemmas = lemmatize(tokens, POS);
			
			if (lemmas != null)
				dependencies = parseDependencies(lemmas, POS);

			annotation.annote(tokens, POS, lemmas, dependencies);
		}
		 else
			annotation.annote(sentences);
	}
	
	private String[][] getTokens(String[][] words,Iterator<Lexel> iterator) {

		String[][] tokens = null;
		List<String> sentence;
		String word, head = "";
		String[] temp;
		final int index,length;
		final String tag = Annotation.ANNOTATION_TAG;
		boolean start = false;

		index = tag.length();
		length=words.length;
		tokens = new String[length][];

		for (int i = 0; i < length; i++) {

			sentence = new ArrayList<String>();
			temp=words[i];
				
			for (int j = 0; j < temp.length; j++) {

				word =temp[j];

				if (word.startsWith(tag) && !start) {

					head = "";
					start = true;
					iterator.next().set(i, sentence.size());
					word = word.substring(index);
				}

				if (word.endsWith(tag)) {
					start = false;
					word = head + word.substring(0, word.length() - index);
				}

				if (start)
					head += word;
				else
					sentence.add(word);
			}

			tokens[i]=sentence.toArray(new String[sentence.size()]);
		}

		return tokens;
	}
	
	
}