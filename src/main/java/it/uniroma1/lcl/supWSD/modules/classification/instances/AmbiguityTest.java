package it.uniroma1.lcl.supWSD.modules.classification.instances;

import it.uniroma1.lcl.supWSD.modules.extraction.features.Feature;

/**
 * @author Simone Papandrea
 *
 */
public class AmbiguityTest extends Ambiguity{

	private String[] mClasses;

	public AmbiguityTest(String lexel,String[] senses) {
		
		super(lexel);

		this.mClasses=senses;
	}

	public String[] getClasses() {

		return this.mClasses;
	}
	
	@Override
	protected int addFeature(Feature feature) {

		int index;
		String key,def;
		
		key=feature.getKey();
		index=this.getIndex(key,feature.getName());

		if(index==-1){
		
			def=feature.getDefaultValue();
			
			if(def!=null)				
				index=this.getIndex(key,def);
		}
		
		return index;
	}

	private int getIndex(String key,String val) {

		int index = -1;
		TermsVector termsVector;
		Term term;

		termsVector = this.mStatistic.get(key);

		if (termsVector != null) {

			term = termsVector.get(val);

			if (term != null)
				index = term.getIndex();
		}

		return index;
	}
	
	public int setStatistic(String key, String value, int index) {

		TermsVector termsVector;

		termsVector = this.mStatistic.get(key);

		if (termsVector == null) {
			
			termsVector = new TermsVector();
			this.mStatistic.put(key, termsVector);
		}

		return termsVector.set(value, index);
	}
	
}