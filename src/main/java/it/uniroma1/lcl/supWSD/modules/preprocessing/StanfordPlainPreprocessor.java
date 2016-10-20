package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyRelation;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author Simone Papandrea
 *
 */
public class StanfordPlainPreprocessor extends StanfordPreprocessor {

	private DependencyParser mParser;

	public StanfordPlainPreprocessor(boolean split, boolean pos, boolean lemma, boolean depparse) {

		super(split, pos, lemma, depparse, false);

	}

	@Override
	public void load() {

		super.load();

		if (mDepParse)
			mParser = DependencyParser.loadFromModelFile(DependencyParser.DEFAULT_MODEL);

	}

	@Override
	String init(Annotation annotation) {

		return annotation.getText();
	}

	@Override
	void annote(Annotation annotation, List<CoreMap> sentences) {

		List<CoreLabel> labels;
		CoreLabel label;
		CoreMap sentence;
		DependencyTree[] dtree;
		List<Lexel> lexels;
		CompoundProcessor cProcessor;
		List<TaggedWord> tagged;
		GrammaticalStructure gs;
		String tokens[][], tags[][], lemmas[][], words[];
		int size, length;

		size = sentences.size();
		tokens = new String[size][];
		tags = new String[size][];
		lemmas = new String[size][];
		dtree = new DependencyTree[size];

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
			}

			tokens[i] = words;
		}

		cProcessor = new CompoundProcessor(annotation.getID());
		lexels = cProcessor.getCompounds(tokens, lemmas, tags);
		annotation.getLexels().addAll(lexels);

		if (mDepParse) {

			for (int i = 0; i < tokens.length; i++) {

				tagged = new ArrayList<TaggedWord>();

				for (int j = 0; j < tokens[i].length; j++)
					tagged.add(new TaggedWord(lemmas[i][j], tags[i][j]));

				gs = mParser.predict(tagged);

				if (gs != null)
					dtree[i] = getDependencyTree(gs);
			}
		}

		annotation.annote(tokens, mPOS ? tags : null, mLemma ? lemmas : null, mDepParse ? dtree : null);
	}

	private DependencyTree getDependencyTree(GrammaticalStructure gs) {

		DependencyTree dtree = null;
		IndexedWord vertex, dep;

		dtree = new DependencyTree();

		for (TypedDependency dependency : gs.typedDependencies()) {

			vertex = dependency.gov();

			if (vertex.tag() != null) {

				dep = dependency.dep();
				dtree.add(vertex.index(), vertex.value(), vertex.tag(), dep.index(), dep.value(), dep.tag(),
						dependency.reln().getShortName().equals("nsubjpass") ? DependencyRelation.PASSIVE
								: DependencyRelation.ACTIVE);
			}
		}

		return dtree;
	}
}