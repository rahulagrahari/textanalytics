package it.si3p.supwsd.data;

import java.util.Objects;

/**
 * @author papandrea
 *
 */
public class Lexel implements Comparable<Lexel> {

	private final String mID;
	private String mName;
	private int mSentenceIndex = -1;
	private int mTokenIndex = -1;
	private int mOffset = -1;
	
	public Lexel(String id,String name){
		
		this.mID=id;
		setName(name);
	}
	
	public void setName(String name){
		
		this.mName=name;
	}
	
	public String getID(){
		
		return this.mID;
	}
	
	@Override
	public String toString(){
		
		return this.mName;
	}
	
	public int getSentenceIndex(){
		
		return this.mSentenceIndex;
	}
	
	public int getTokenIndex(){
		
		return this.mTokenIndex;
	}

	public int getOffset(){
		
		return this.mOffset;
	}
	
	public void setIndexes(int sentenceIndex,int tokenIndex){
		
		this.mSentenceIndex=sentenceIndex;
		this.mTokenIndex=tokenIndex;
	}
	
	public void setOffset(int offset){
		
		this.mOffset=offset;
	}
	
	@Override
	public boolean equals(Object o){
		
		if(o instanceof Lexel)			
			return this.getID().equals(((Lexel) o).getID());
		
		return false;
	}

	@Override
	public int hashCode(){		
		return Objects.hash(this.getID());
	}
	
	@Override
	public int compareTo(Lexel arg0) {
		return this.getID().compareTo(arg0.getID());
	}
}
