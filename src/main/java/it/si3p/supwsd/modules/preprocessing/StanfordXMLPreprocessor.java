package it.si3p.supwsd.modules.preprocessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyRelation;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author papandrea
 *
 */
public class StanfordXMLPreprocessor extends StanfordPreprocessor {

	private final ConcurrentHashMap<String, List<Integer>> mIndex;

	public StanfordXMLPreprocessor(boolean split, boolean pos, boolean lemma, boolean depparse) {

		super(split, pos, lemma, depparse, true);

		mIndex = new ConcurrentHashMap<String, List<Integer>>();
	}

	@Override
	String init(Annotation annotation) {

		Pattern pattern;
		Matcher matcher;
		int count = 0, length;
		List<Integer> indexes;

		pattern = Pattern.compile(Pattern.quote(Annotation.ANNOTATION_TAG));
		matcher = pattern.matcher(annotation.getText());
		indexes = new ArrayList<Integer>();
		length = Annotation.ANNOTATION_TAG.length();

		while (matcher.find()) {

			indexes.add(matcher.start() - count);
			count += length;
		}

		mIndex.put(annotation.getID(), indexes);

		return matcher.replaceAll("");
	}

	@Override
	void annote(Annotation annotation, List<CoreMap> sentences) {

		List<CoreLabel> labels;
		CoreLabel label;
		CoreMap sentence;
		SemanticGraph graph;
		String tokens[][], tags[][], lemmas[][], words[];
		DependencyTree[] dtree;
		Iterator<Lexel> lexels;
		List<Integer> indexes;
		Lexel lexel = null;
		String id;
		int size, length, position;

		size = sentences.size();
		tokens = new String[size][];
		tags = new String[size][];
		lemmas = new String[size][];
		dtree = new DependencyTree[size];
		lexels = annotation.iterator();
		id = annotation.getID();
		indexes = mIndex.get(id);

		for (int i = 0; i < size; i++) {

			sentence = sentences.get(i);
			labels = sentence.get(TokensAnnotation.class);
			length = labels.size();
			words = new String[length];
			tags[i] = new String[length];
			lemmas[i] = new String[length];

			for (int j = 0; j < length; j++) {

				label = labels.get(j);
				words[j] = label.get(TextAnnotation.class);
				tags[i][j] = label.get(PartOfSpeechAnnotation.class);
				lemmas[i][j] = label.get(LemmaAnnotation.class);
				position = label.beginPosition();

				if (!indexes.isEmpty() && position > indexes.get(0)) {
										
					lexel.setOffset(j - lexel.getTokenIndex() - 1);
					lexel = null;
					indexes.remove(0);
				}

				if (!indexes.isEmpty() && position == indexes.get(0)) {
										
					lexel = lexels.next();
					lexel.setIndexes(i, j);
					indexes.remove(0);
				}
			}

			if (lexel != null)
				lexel.setOffset(length - lexel.getTokenIndex() - 1);

			tokens[i] = words;
			graph = sentence.get(BasicDependenciesAnnotation.class);

			if (graph != null)
				dtree[i] = getDependencyTree(graph);
		}

		annotation.annote(tokens, mPOS ? tags : null, mLemma ? lemmas : null, mDepParse ? dtree : null);
		mIndex.remove(id);
	}

	private DependencyTree getDependencyTree(SemanticGraph graph) {

		DependencyTree dtree = null;
		IndexedWord dep;

		dtree = new DependencyTree();

		for (IndexedWord vertex : graph.vertexSet()) {

			if (vertex.tag() != null) {

				for (SemanticGraphEdge edge : graph.getOutEdgesSorted(vertex)) {

					dep = edge.getDependent();
					dtree.add(vertex.index(), vertex.value(), vertex.tag(), dep.index(), dep.value(), dep.tag(),
							edge.getRelation().getShortName().equals("nsubjpass") ? DependencyRelation.PASSIVE
									: DependencyRelation.ACTIVE);
				}
			}
		}

		return dtree;
	}
}