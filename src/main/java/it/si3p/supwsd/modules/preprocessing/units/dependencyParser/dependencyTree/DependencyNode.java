package it.si3p.supwsd.modules.preprocessing.units.dependencyParser.dependencyTree;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import it.si3p.supwsd.data.POSMap.TAG;

/**
 * @author papandrea
 *
 */
public class DependencyNode implements Comparable<DependencyNode>,Iterable<DependencyNode>{

	private DependencyNode mParent = null;
	private DependencyRelation mRelation =DependencyRelation.ACTIVE;
	private final Set<DependencyNode> mChildren;
	private final String mLemma;
	private final int mIndex;
	private final TAG mTag;

	public DependencyNode(int index,String lemma,TAG tag) {

		this.mIndex = index;
		this.mLemma=lemma;
		this.mTag = tag;
		this.mChildren = new TreeSet<DependencyNode>();
	}


	void addChild(DependencyRelation dependencyRelation, DependencyNode dependencyNode) {

		dependencyNode.mParent=this;
		dependencyNode.mRelation= dependencyRelation;
		this.mChildren.add(dependencyNode);
	}

	public int getIndex() {

		return this.mIndex;
	}

	public String getLemma() {

		return mLemma;
	}

	public TAG getTag() {

		return this.mTag;
	}
	
	public DependencyNode getParent() {

		return this.mParent;
	}

	public DependencyRelation getRelation() {

		return this.mRelation;
	}


	@Override()
	public String toString() {

		return getLemma();
	}

	@Override()
	public int hashCode() {

		return Integer.hashCode(this.getIndex());
	}

	@Override
	public boolean equals(Object object) {

		if (object != null) {

			if (object instanceof DependencyNode) {

				DependencyNode dependencyNode = (DependencyNode) object;

				return this.getIndex() == dependencyNode.getIndex();
			}
		}

		return false;
	}

	@Override
	public int compareTo(DependencyNode arg0) {

		return Integer.compare(this.getIndex(),arg0.getIndex());
	}


	@Override
	public Iterator<DependencyNode> iterator() {
	
		return this.mChildren.iterator();
	}
}
