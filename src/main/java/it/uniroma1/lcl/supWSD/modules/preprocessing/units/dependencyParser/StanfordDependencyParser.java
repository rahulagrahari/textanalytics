package it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.TypedDependency;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;
import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyRelation;

/**
 * @author Simone Papandrea
 *
 */
class StanfordDependencyParser extends Unit implements DependencyParser {

	private edu.stanford.nlp.parser.nndep.DependencyParser mParser;

	StanfordDependencyParser(String configFile) {

		super(configFile);
	}

	@Override
	public String getDefaultModel() {

		return edu.stanford.nlp.parser.nndep.DependencyParser.DEFAULT_MODEL;
	}

	@Override
	public DependencyTree parse(String[] lemmas, String[] POS) {

		DependencyTree dtree;
		List<TaggedWord> tokens;
		Collection<TypedDependency> dependencies;
		IndexedWord gov, dep;

		tokens = new ArrayList<TaggedWord>();

		for (int i = 0; i < lemmas.length; i++)
			tokens.add(new TaggedWord(lemmas[i], POS[i]));

		dependencies = mParser.predict(tokens).typedDependencies();
		dtree = new DependencyTree();

		for (TypedDependency dependency : dependencies) {

			gov = dependency.gov();

			if (gov.tag() != null) {
				dep = dependency.dep();
				dtree.add(gov.index(), gov.value(), gov.tag(), dep.index(), dep.value(), dep.tag(),
						dependency.reln().getShortName().equals("nsubjpass") ? DependencyRelation.PASSIVE : DependencyRelation.ACTIVE);
			}
		}

		return dtree;
	}

	@Override
	public void load() throws Exception {

		mParser = edu.stanford.nlp.parser.nndep.DependencyParser.loadFromModelFile(this.getModel());
	}

	@Override
	public void unload() {

	}
}
