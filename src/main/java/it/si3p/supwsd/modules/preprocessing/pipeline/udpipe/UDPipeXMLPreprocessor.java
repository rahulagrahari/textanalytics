package it.si3p.supwsd.modules.preprocessing.pipeline.udpipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;

/**
 * @author papandrea
 *
 */
public class UDPipeXMLPreprocessor extends UDPipePreprocessor {

	private final ConcurrentHashMap<String, List<Integer>> mIndex;

	public UDPipeXMLPreprocessor(String model) {

		super(model);

		mIndex = new ConcurrentHashMap<String, List<Integer>>();
	}

	@Override
	String init(Annotation annotation) {

		Pattern pattern;
		Matcher matcher;
		int count = 0, length;
		List<Integer> indexes;

		pattern = Pattern.compile(Pattern.quote(Annotation.ANNOTATION_TAG));
		matcher = pattern.matcher(annotation.getText().replaceAll("\\s", ""));
		indexes = new ArrayList<Integer>();
		length = Annotation.ANNOTATION_TAG.length();

		while (matcher.find()) {

			indexes.add(matcher.start() - count);
			count += length;
		}

		mIndex.put(annotation.getID(), indexes);

		return annotation.getText().replaceAll(Annotation.ANNOTATION_TAG, "");
	}

	@Override
	void annote(Annotation annotation, String processed) {

		List<String[]> tags, lemmas, words;
		String[] tokens;
		int i = -1, j = 0, skip = 0;
		String[] lines;
		List<Integer> indexes;
		Lexel lexel = null;
		String id, temp = "", word, position;
		Iterator<Lexel> lexels;
		List<String> tagList, lemmaList, wordList;

		words = new ArrayList<String[]>();
		tags = new ArrayList<String[]>();
		lemmas = new ArrayList<String[]>();
		id = annotation.getID();
		indexes = mIndex.get(id);
		lexels = annotation.iterator();
		tagList = new ArrayList<String>();
		lemmaList = new ArrayList<String>();
		wordList = new ArrayList<String>();

		lines = (processed + "\n# sent_id").split("\n");

		for (String line : lines) {

			if (line.startsWith("# sent_id")) {

				if (lexel != null) {
					lexel.setOffset(j - lexel.getTokenIndex() - 1);
					indexes.remove(0);				
					lexel = null;
				}

				if (j > 0) {
					words.add(wordList.toArray(new String[wordList.size()]));
					tags.add(tagList.toArray(new String[tagList.size()]));
					lemmas.add(lemmaList.toArray(new String[lemmaList.size()]));
					wordList.clear();
					tagList.clear();
					lemmaList.clear();
				}

				i++;
				j = 0;

			} else {

				tokens = line.split("\t");

				if (tokens.length > 1) {

					while (!indexes.isEmpty() && temp.length() == indexes.get(0)) {

						if (lexel == null) {

							lexel = lexels.next();
							lexel.setIndexes(i, j);							
						
						} else {
							lexel.setOffset(j - lexel.getTokenIndex() - 1);
							lexel = null;										
						}

						indexes.remove(0);
					}

					position = tokens[0];
					word = tokens[1];

					if (!position.contains("-")) {

						wordList.add(word);
						tagList.add(tokens[4]);
						lemmaList.add(tokens[2]);
						j++;

						if (Integer.valueOf(position) > skip)
							temp += word;
					} else {

						temp += word;
						skip = Integer.valueOf(position.substring(position.lastIndexOf('-') + 1));
					}
				}
			}
		}

		mIndex.remove(id);
		annotation.annote(words.toArray(new String[words.size()][]), tags.toArray(new String[tags.size()][]),
				lemmas.toArray(new String[lemmas.size()][]), null);
	}
}