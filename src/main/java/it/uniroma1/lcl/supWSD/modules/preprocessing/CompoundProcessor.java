package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.data.POSMap;
import it.uniroma1.lcl.supWSD.modules.classification.Serializer;


class CompoundProcessor  {
	
	private static final String FORMAT="s%012d.t%012d";
	private static final int mNgram=5;
	private static final POSMap mPOSMap=POSMap.getInstance();
	private final int mID;
	
	CompoundProcessor(int id){
		
		this.mID=id;
	}
	
	protected List<Lexel> getCompounds(String[][] words,String[][] lemmas, String[][] POS) {

		Compound compound;
		List<Lexel> lexels;
		String lemma,pos,word,l[],p[],w[];
		int length, min, max,id = 0,j;
		List<String>lc,pc,wc;
		Lexel lexel;
		
		lexels= new ArrayList<Lexel>();
		length = lemmas.length;
		
		for (int i = 0; i < lemmas.length; i++) {

			l = lemmas[i];
			p = POS[i];
			w=words[i];
			lc=new ArrayList<String>();
			pc=new ArrayList<String>();
			wc=new ArrayList<String>();
			length = l.length;
			j=0;
			
			while (j < length) {

				min = Math.min(mNgram, length - j);
				max=j+min;
				compound=getCompound(Arrays.copyOfRange(w,j,max),Arrays.copyOfRange(l,j,max),Arrays.copyOfRange(p,j,max));
						
				if(compound!=null){
					
					lemma=compound.mLemma;
					pos=compound.mPOS;
					word=compound.mWord;
					j+=compound.mCount;
				}
				else{
					
					lemma=l[j];
					pos=p[j];
					word=w[j];
					j++;
				}
				
				lexel=new Lexel(String.format(FORMAT,mID, ++id), lemma);
				lexel.set(i, lc.size());
				lexels.add(lexel);
				lc.add(lemma);
				pc.add(pos);
				wc.add(word);
			}
			
			lemmas[i]=lc.toArray(new String[lc.size()]);
			POS[i]=pc.toArray(new String[pc.size()]);
			words[i]=wc.toArray(new String[wc.size()]);
		}
		
		return lexels;
	}

	private Compound getCompound(String[] words,String[]lemmas,String[]POS){
		
		Compound compound=null;
		int length;
		String lemma,word;
		TreeSet<POSCompound>tags;
		
		lemma=lemmas[0];
		word=words[0];
		length=lemmas.length;
		tags=new TreeSet<POSCompound>();
		tags.add(new POSCompound(mPOSMap.getPOS(POS[0]),POS[0],0));
		
		for(int i=1;i<length;i++){
			
			lemma+="_"+lemmas[i];
			word+="_"+words[i];
			tags.add(new POSCompound(mPOSMap.getPOS(POS[i]),POS[i],i));
			
			for(POSCompound tag:tags)			
				if(Serializer.modelExists(lemma + "." + tag.mTag)){
					compound=new Compound(word,lemma,tag.mPOS,i+1);
					break;
				}
					
		}
				
		return compound;
	}
	
	private static class Compound{
		
		private final String mWord;
		private final String mLemma;
		private final String mPOS;
		private final int mCount;
		
		Compound(String word,String lemma,String pos, int count){
			
			this.mWord=word;
			this.mLemma=lemma;
			this.mPOS=pos;
			this.mCount=count;
		}		
	}
	
private static class POSCompound implements Comparable<POSCompound>{
		
		private final POSMap.TAG mTag;
		private final String mPOS;
		private final int mIndex;
		
		POSCompound(POSMap.TAG tag,String pos, int index){
			
			this.mTag=tag;
			this.mPOS=pos;
			this.mIndex=index;
		}

		@Override
		public int compareTo(POSCompound arg0) {
			
			int diff;
			
			diff=mTag.compareTo(arg0.mTag);
			
			if(diff==0)
				diff=Integer.compare(mIndex, arg0.mIndex);
			return diff;
		}		
	}
}