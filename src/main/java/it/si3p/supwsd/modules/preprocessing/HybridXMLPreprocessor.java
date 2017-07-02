package it.si3p.supwsd.modules.preprocessing;

import java.util.ArrayList;
import java.util.Iterator;
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