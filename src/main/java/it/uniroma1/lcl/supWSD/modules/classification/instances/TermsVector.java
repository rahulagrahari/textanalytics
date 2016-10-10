package it.uniroma1.lcl.supWSD.modules.classification.instances;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Simone Papandrea
 *
 */
public class TermsVector implements Iterable<Term> {

	private Map<String, Term> mElements;

	public TermsVector() {

		mElements = new HashMap<String, Term>();
	}

	final int set(String value, int index) {

		Term term;

		term = mElements.get(value);

		if (term == null) {
			term = new Term(index);
			mElements.put(value, term);
		}

		term.occurence();

		return term.getIndex();
	}

	final void remove(String value) {

		this.mElements.remove(value);
	}

	public boolean isEmpty() {

		return this.mElements.size() == 0;
	}

	@Override
	public Iterator<Term> iterator() {

		return mElements.values().iterator();
	}

	public Set<String> getKeys() {

		return mElements.keySet();
	}

	public Term get(String value) {

		return this.mElements.get(value);
	}
}
