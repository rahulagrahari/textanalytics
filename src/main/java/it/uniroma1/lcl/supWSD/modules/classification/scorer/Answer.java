package it.uniroma1.lcl.supWSD.modules.classification.scorer;

/**
 * @author Simone Papandrea
 *
 */
public class Answer implements CharSequence,Comparable<Answer>{
	
	private static final String SEPARATOR="|";
	private final String mClass;
	private final double mProb;
	private final String mContent;
	
	public Answer(String cls,double prob){
		
		this.mClass=cls;
		this.mProb=prob;
		this.mContent=cls+SEPARATOR+prob;
	}
	
	public String getCls(){
		
		return this.mClass;
	}
	
	public double getProb(){
		
		return this.mProb;
	}
	
	@Override
	public boolean equals(Object arg0){
		
		if(arg0 instanceof Answer){
			
			Answer answer=(Answer) arg0;
			
			return this.getClass().equals(answer.getClass());
		}
		
		return false;
	}
	
	@Override
	public int compareTo(Answer arg0) {
		
		return Double.compare(arg0.getProb(),this.getProb());
	}

	@Override
	public String toString(){
		
		return this.mContent;
	}

	@Override
	public char charAt(int index) {

		return mContent.charAt(index);
	}

	@Override
	public int length() {
		
		return mContent.length();
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		
		return mContent.subSequence(start, end);
	}
		
		
}
