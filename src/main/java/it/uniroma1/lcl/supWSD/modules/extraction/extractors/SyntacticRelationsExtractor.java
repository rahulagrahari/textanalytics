package it.uniroma1.lcl.supWSD.modules.extraction.extractors;

import java.util.Collection;
import java.util.Vector;
import it.uniroma1.lcl.supWSD.data.Annotation;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.data.POSMap.TAG;
import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;
import it.uniroma1.lcl.supWSD.modules.extraction.features.SyntacticRelation;
import it.uniroma1.lcl.supWSD.modules.extraction.features.SyntacticRelation.DependecyType;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyNode;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author Simone Papandrea
 *
 */
public class SyntacticRelationsExtractor extends FeatureExtractor {

	public SyntacticRelationsExtractor() {
		super(0);
	}

	@Override
	public Collection<Feature> extract(Lexel lexel, Annotation annotation) {

		Collection<Feature> features = null;
		DependencyTree tree;
		DependencyNode node;
		int id;

		id = lexel.getTokenIndex() + 1;
		tree = annotation.getDepedencyTree(lexel);
		node = tree.getNode(id);

		if (node != null) {
			
			switch (node.getTag()) {

			case n:
				features = getNounFeature(node);
				break;

			case v:
				features = getVerbFeature(node);
				break;

			case a:
				features = getAdjFeature(node);
				break;

			default:
				break;
			}
		}

		return features;
	}

	private Vector<Feature> getNounFeature(DependencyNode node) {

		Vector<Feature> features;
		DependencyNode parent;
		TAG tag;

		features = new Vector<Feature>();
		parent = node.getParent();

		if (parent != null) {

			tag = parent.getTag();
			features.add(new SyntacticRelation(DependecyType.HEAD, parent.getLemma()));
			features.add(new SyntacticRelation(DependecyType.POS, tag.name()));
			features.add(new SyntacticRelation(DependecyType.POSITION, parent.compareTo(node) < 0 ? "left" : "right"));

			if (tag.equals(TAG.v))
				features.add(new SyntacticRelation(DependecyType.VOICE, node.getRelation().name()));
		}

		return features;
	}

	private Vector<Feature> getVerbFeature(DependencyNode node) {

		Vector<Feature> features;
		String lemma, tag;
		int diff;

		features = new Vector<Feature>();

		for (DependencyNode child : node) {

			diff = node.getIndex() - child.getIndex();
			lemma = child.getLemma();
			tag = child.getTag().name();

			if (diff == 1) {
				features.add(new SyntacticRelation(DependecyType.LEFT, lemma));
				features.add(new SyntacticRelation(DependecyType.LEFT_POS, tag));
			} else if (diff == -1) {
				features.add(new SyntacticRelation(DependecyType.RIGHT, lemma));
				features.add(new SyntacticRelation(DependecyType.RIGHT_POS, tag));
			}
		}

		features.add(new SyntacticRelation(DependecyType.VOICE, node.getRelation().name()));
		features.add(new SyntacticRelation(DependecyType.POS, node.getTag().name()));

		return features;
	}

	private Vector<Feature> getAdjFeature(DependencyNode node) {

		Vector<Feature> features;
		DependencyNode parent;

		features = new Vector<Feature>();
		parent = node.getParent();

		if (parent != null) {

			features.add(new SyntacticRelation(DependecyType.HEAD, parent.getLemma()));
			features.add(new SyntacticRelation(DependecyType.POS, parent.getTag().name()));
		}

		return features;
	}

	@Override
	public Class<? extends Feature> getFeatureClass() {

		return SyntacticRelation.class;
	}
}
