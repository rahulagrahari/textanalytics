package it.uniroma1.lcl.supWSD.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.dependencyParser.dependencyTree.DependencyTree;

/**
 * @author Simone Papandrea
 *
 */
public class Annotation implements Iterable<Lexel> {

	public final static String ANNOTATION_TAG = "HEAD";
	private final String mText;
	private final List<Token[]> mTokens;
	private final SortedSet<Lexel> mLexels;
	private final List<DependencyTree> mDependencyTrees;
	private final List<String> mSentences;

	public Annotation(String sentence) {

		this.mText = sentence;
		this.mTokens = new ArrayList<Token[]>();
		this.mLexels = new TreeSet<Lexel>();
		this.mDependencyTrees = new ArrayList<DependencyTree>();
		this.mSentences = new ArrayList<String>();
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

	public List<Token[]> getTokenSentences() {

		return this.mTokens;
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
	
	public String getSentence(int index) {

		return this.mSentences.get(index);
	}
	
	public String getSentence(Lexel lexel) {

		return getSentence(lexel.getSentenceIndex());
	}
	
	public List<String> getSentences() {

		return this.mSentences;
	}
	
	public int getSentencesCount() {

		return Math.max(this.mTokens.size(),this.mSentences.size());
	}
	
	public SortedSet<Lexel> getLexels() {

		return this.mLexels;
	}

	public int getLexelsCount() {

		return this.getLexels().size();
	}
	

	public void annote(Vector<List<String>> words, String[][] POS, String[][] lemmas,
			DependencyTree[] dependecies) {

		int size;

		size = words.size();
		
		for (int i = 0; i < size; i++)			
			annote(words.get(i),POS!=null?POS[i]:null,lemmas!=null?lemmas[i]:null,dependecies!=null?dependecies[i]:null);		
	}

	public void annote(List<String> words, String[] POS, String[] lemmas,DependencyTree dependecyTree) {

		Token[] tokens;
		int length;

		length=words.size();
		tokens = new Token[length];
			
		for(int j=0;j<length;j++)				
			tokens[j]=new Token(words.get(j),POS!=null?POS[j]:null,lemmas!=null?lemmas[j]:null);
			
		this.mTokens.add(tokens);

		if(dependecyTree!=null)
			this.mDependencyTrees.add(dependecyTree);	
	}
	
	public void annote(String[] sentences) {
		
		for(String sentence:sentences)
			mSentences.add(sentence);
	}
		
	public boolean isAnnotated() {

		return !this.mTokens.isEmpty();
	}

	public void dispose() {

		this.mTokens.clear();
		this.mDependencyTrees.clear();
		this.mSentences.clear();
	}

	@Override
	public Iterator<Lexel> iterator() {

		return mLexels.iterator();
	}
}