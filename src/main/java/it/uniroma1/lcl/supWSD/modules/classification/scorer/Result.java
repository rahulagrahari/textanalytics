package it.uniroma1.lcl.supWSD.modules.classification.scorer;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Simone Papandrea
 *
 */
public class Result implements Comparable<Result> {

	public static final String SENSE_UNK = "U";
	private final String mID;
	private final SortedSet<String> mSenses;
	private final SortedSet<Answer> mAnswers;
	private final static String SEPARATOR = "\t";

	
	public Result(String id, SortedSet<String> senses, String cls) {

		this(id, senses, new String[] { cls }, new double[] { 1 });

	}

	public Result(String id, SortedSet<String> senses,String[] classes, double[] probs) {

		this.mID = id;
		this.mSenses = senses;
		this.mAnswers = new TreeSet<Answer>();

		for (int i = 0; i < classes.length; i++)
			this.mAnswers.add(new Answer(classes[i], probs[i]));

	}

	public String getID() {

		return this.mID;
	}

	public boolean hasLegalAnswer(){
		
		return !this.mAnswers.first().getCls().equals(SENSE_UNK);
	}
	

	public SortedSet<Answer> getAnswers() {

		return this.mAnswers;
	}

	public int getCountAnswers() {

		return getAnswers().size();
	}

	public boolean isCorrect() {

		String cls= getFirstAnswerClass();
		
		return cls!=null && (mSenses==null ||mSenses.contains(cls));
	}
	
	public  Answer getFirstAnswer() {

		return this.mAnswers.first();
	}
	

	public String getFirstAnswerClass() {

		return  getFirstAnswer().getCls();
	}
	
	/*public double getValue() {

		double val = 0;
		int count;

		count = getCountAnswers();

		if (count > 0) {
			
			for (Answer answer : getAnswers())
				if (mSenses.contains(answer.getCls()))
					val++;

			val /= count;
		}

		return val;
	}*/

	
	@Override
	public int compareTo(Result arg0) {
		
		return this.getID().compareTo(arg0.getID());
	}

	@Override
	public boolean equals(Object arg0) {

		if (arg0 instanceof Result) {

			Result result = (Result) arg0;

			return this.getID().equals(result.getID());
		}

		return false;
	}

	@Override
	public int hashCode() {

		return this.getID().hashCode();
	}

	@Override
	public String toString() {

		return this.getID() + SEPARATOR + this.getFirstAnswerClass();//String.join(SEPARATOR, this.getAnswers());
	}
}
