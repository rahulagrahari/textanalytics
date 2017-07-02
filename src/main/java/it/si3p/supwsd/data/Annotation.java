package it.si3p.supwsd.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author papandrea
 *
 */
public class Annotation implements Iterable<Lexel> {

	public final static String ANNOTATION_TAG = "S0a2rHrOpF";
	private final Integer mID;
	private final String mText;
	private final List<Token[]> mTokens;
	private final SortedSet<Lexel> mLexels;
	private final List<DependencyTree> mDependencyTrees;

	public Annotation(int id,String sentence) {

		this.mID=id;
		this.mText = sentence;
		this.mTokens = new ArrayList<Token[]>();
		this.mLexels = new TreeSet<Lexel>();
		this.mDependencyTrees = new ArrayList<DependencyTree>();
	}

	public void addLexel(Lexel lexel) {

		this.mLexels.add(lexel);
	}

	public void addLexels(Collection<Lexel> lexels) {

		this.mLexels.addAll(lexels);
	}

	public String getText() {

		return this.mText;
	}

	public Token getToken(Lexel lexel) {
		
		return this.getTokens(lexel)[lexel.getTokenIndex()];
	}
	
	public Token[] getTokens(int index) {

		return this.mTokens.get(index);
	}

	public Token[] getTokens(Lexel lexel) {

		return getTokens(lexel.getSentenceIndex());
	}


	public DependencyTree getDepedencyTree(int index) {

		return this.mDependencyTrees.get(index);
	}
	
	public DependencyTree getDepedencyTree(Lexel lexel) {

		return getDepedencyTree(lexel.getSentenceIndex());
	}
	
	public List<DependencyTree> getDepedencyTree() {

		return this.mDependencyTrees;
	}
	
	public List<Token[]> getSentences() {

		return mTokens;
	}
		
	public SortedSet<Lexel> getLexels() {

		return this.mLexels;
	}

	public int getLexelsCount() {

		return this.getLexels().size();
	}
	

	public void annote( String[][] words, String[][] POS, String[][] lemmas,
			DependencyTree[] dependecies) {

		int size;

		size = words.length;
		
		for (int i = 0; i < size; i++)			
			annote(words[i],POS!=null?POS[i]:null,lemmas!=null?lemmas[i]:null,dependecies!=null?dependecies[i]:null);		
	}

	public void annote(String[] words, String[] POS, String[] lemmas,DependencyTree dependecyTree) {

		Token[] tokens;
		int length;

		length=words.length;
		tokens = new Token[length];
			
		for(int j=0;j<length;j++)				
			tokens[j]=new Token(words[j],POS!=null?POS[j]:null,lemmas!=null?lemmas[j]:null);
			
		this.mTokens.add(tokens);

		if(dependecyTree!=null)
			this.mDependencyTrees.add(dependecyTree);	
	}
	
		
	public boolean isAnnotated() {

		return !this.mTokens.isEmpty();
	}

	public void dispose() {

		this.mTokens.clear();
		this.mDependencyTrees.clear();
	}

	@Override
	public Iterator<Lexel> iterator() {

		return mLexels.iterator();
	}

	public Integer getID() {
	
		return mID;
	}
	
	@Override
	public String toString(){
		
		return this.mText;
	}
	
}