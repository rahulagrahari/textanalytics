package it.uniroma1.lcl.supWSD.modules.classification.scorer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTest;

/**
 * @author Simone Papandrea
 *
 */
public class Score {

	private int mAttempted = 0;
	private int mCorrected = 0;
	private final int mTotal;
	private final Map<AmbiguityTest, TreeSet<Result>> mResults;
	private final Map<String, AmbiguityTest> mIDS;

	public Score(int total) {

		mTotal = total;
		mResults = new HashMap<AmbiguityTest, TreeSet<Result>>();
		mIDS = new HashMap<String, AmbiguityTest>();
	}

	public void add(AmbiguityTest ambiguity, Collection<Result> tests) {

		TreeSet<Result> results;

		if (mResults.containsKey(ambiguity))
			results = mResults.get(ambiguity);
		else {
			results = new TreeSet<Result>();
			mResults.put(ambiguity, results);
		}

		for (Result result : tests) {

			if (check(result)) {
				results.add(result);
				mIDS.put(result.getID(), ambiguity);
			}
		}
	}

	private boolean check(Result result) {

		boolean status = true;
		TreeSet<Result> results;
		Result first;
		Answer answer;
		String id;

		id = result.getID();

		if (mIDS.containsKey(id)) {

			results = mResults.get(mIDS.get(id));
			answer=result.getFirstAnswer();
			first = results.floor(result);	
			status =answer.getProb()!=1 && (first.getFirstAnswer().getProb()==1 || answer.compareTo(first.getFirstAnswer()) < 0);

			if (status)
				results.remove(first);
		}

		return status;
	}

	public Map<AmbiguityTest, TreeSet<Result>> getResults() {

		return mResults;
	}

	public void evaluate() {

		for (Entry<AmbiguityTest, TreeSet<Result>> e : mResults.entrySet()) {

			for (Result result : e.getValue()) {
	
				if (result.hasLegalAnswer()) {

					if (result.isCorrect())
						this.mCorrected++;

					this.mAttempted++;
				}
			}
		}
	}

	public double precision() {

		double precision = 0;
		int attempted;

		attempted = this.getAttempted();

		if (attempted > 0)
			precision = ((double) this.getCorrected()) / attempted;

		return precision;
	}

	public double recall() {

		double recall = 0;
		int total;

		total = this.getTotal();

		if (total > 0)
			recall = ((double) this.getCorrected()) / total;

		return recall;

	}

	public double f() {

		double f = 0;
		double precision, recall, d;

		precision = this.precision();
		recall = this.recall();
		d = precision + recall;

		if (d > 0)
			f = 2 * ((precision * recall) / d);

		return f;
	}

	public int getAttempted() {

		return this.mAttempted;
	}

	public int getCorrected() {

		return this.mCorrected;
	}

	public float getAttemptedPerc() {

		float p = 0;
		int total;

		total = this.getTotal();

		if (total > 0)
			p = (float) this.getAttempted() / this.getTotal();

		return p;
	}

	public int getTotal() {

		return this.mTotal;
	}

}
