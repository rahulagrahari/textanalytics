package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyRelation;

/**
 * @author Simone Papandrea
 *
 */
public class StanfordPreprocessor implements Preprocessor {

	private StanfordCoreNLP mPipeline;
	private final String mAnnotators;
	private final boolean mPOS;
	private final boolean mLemma;
	private final boolean mDepParse;

	public StanfordPreprocessor(boolean split, boolean pos, boolean lemma, boolean depparse) {

		String annotators;

		mPOS = pos;
		mLemma = lemma;
		mDepParse = depparse;

		annotators = "tokenize";

		if (split)
			annotators += ",ssplit";

		if (pos)
			annotators += ",pos";

		if (lemma)
			annotators += ",lemma";

		if (depparse)
			annotators += ",depparse";

		this.mAnnotators = annotators;
	}

	@Override
	public void load() {

		Properties props = new Properties();
		props.setProperty("annotators", mAnnotators);
		props.setProperty("tokenize.options", "normalizeParentheses=false,normalizeOtherBrackets=false,untokenizable=allKeep,escapeForwardSlashAsterisk=false,ptb3Escaping=false");
		
		this.mPipeline = new StanfordCoreNLP(props);
	}

	@Override
	public void unload() {

	}

	@Override
	public void execute(Annotation annotation) {

		edu.stanford.nlp.pipeline.Annotation document;
		Pattern pattern;
		Matcher matcher;
		HashSet<Integer> indexes;
		int count = 0;

		indexes = new HashSet<Integer>();
		pattern = Pattern.compile(Annotation.ANNOTATION_TAG);
		matcher = pattern.matcher(annotation.getText());

		while (matcher.find()) {

			indexes.add(matcher.start() - count);
			count += Annotation.ANNOTATION_TAG.length() * 2;
			matcher.find();
		}

		document = new edu.stanford.nlp.pipeline.Annotation(matcher.replaceAll(""));
		mPipeline.annotate(document);
		annote(annotation, document.get(SentencesAnnotation.class), indexes);
	}

	private void annote(Annotation annotation, List<CoreMap> sentences, HashSet<Integer> indexes) {

		List<CoreLabel> labels;
		List<String> words;
		Vector<List<String>> tokens;
		CoreLabel label;
		CoreMap sentence;
		Iterator<Lexel> lexels;
		SemanticGraph graph;
		String tags[][], lemmas[][];
		DependencyTree[] dtree;
		int size, length;

		lexels = annotation.iterator();
		size = sentences.size();
		tokens = new Vector<List<String>>(size);
		tags = new String[size][];
		lemmas = new String[size][];
		dtree = new DependencyTree[size];

		for (int i = 0; i < size; i++) {

			sentence = sentences.get(i);
			labels = sentence.get(TokensAnnotation.class);
			length = labels.size();
			words = new ArrayList<String>(length);
			tags[i] = new String[length];
			lemmas[i] = new String[length];

			for (int j = 0; j < length; j++) {

				label = labels.get(j);
				words.add(label.get(TextAnnotation.class));
				tags[i][j] = label.get(PartOfSpeechAnnotation.class);
				lemmas[i][j] = label.get(LemmaAnnotation.class);

				if (indexes.contains(label.beginPosition()))
					lexels.next().set(i, j);
			}

			tokens.add(words);
			graph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);

			if (graph != null)
				dtree[i] = getDependencyTree(graph);
		}

		annotation.annote(tokens, mPOS ? tags : null, mLemma ? lemmas : null, mDepParse ? dtree : null);
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
							edge.getRelation().getShortName().equals("nsubjpass") ? DependencyRelation.PASSIVE : DependencyRelation.ACTIVE);
				}
			}
		}

		return dtree;
	}
}