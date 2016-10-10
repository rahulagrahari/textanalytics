package it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree;

import java.util.HashMap;
import java.util.Map;

import it.uniroma1.lcl.supWSD.data.POSMap;

/**
 * @author Simone Papandrea
 *
 */
public class DependencyTree {

	private final Map<Integer, DependencyNode> mTree;
	

	public DependencyTree() {

		this.mTree = new HashMap<Integer, DependencyNode>();

	}

	public void add(int govIndex,String govLemma,String govTag, int depIndex,String depLemma,String depTag, DependencyRelation dependencyRelation) {

		DependencyNode gov,dep;
		
		gov=mTree.get(govIndex);
		
		if (gov==null){
			
			gov=new DependencyNode(govIndex,govLemma,POSMap.getInstance().getPOS(govTag));
			mTree.put(govIndex, gov);
		}
		
		dep=mTree.get(depIndex);
		
		if (dep==null){
			
			dep=new DependencyNode(depIndex,depLemma,POSMap.getInstance().getPOS(depTag));
			mTree.put(depIndex, dep);
		}
		
		gov.addChild(dependencyRelation, dep);
	}

	public DependencyNode getNode(int index){
		
		return this.mTree.get(index);
	}
}
