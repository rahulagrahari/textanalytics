package it.si3p.supwsd.modules.preprocessing.units.lemmatizer;

import it.si3p.supwsd.modules.preprocessing.units.Unit;

/**
 * @author papandrea
 *
 */
class SimpleLemmatizer extends Unit implements Lemmatizer {

	private final static String MODEL  = "/";
	
	SimpleLemmatizer(String model) {

		super(model);

	}

	public String[] lemmatize(String[] words,String[] POS)  {

		String[] lemmas,vals;
		String item,model,lemma;
		int length, index;

		length = words.length;
		lemmas = new String[length];
		model=this.getModel();
		
		for (int i = 0; i < length; i++) {

			item = words[i];
			index = item.indexOf(model);

			if (index > 0) {

				vals=item.split(model);
				words[i]=vals[0];
				lemma=vals[1];			
			}
			else
				lemma="";
			
			lemmas[i]=lemma;
		}

		return lemmas;
	}


	
	@Override
	public void load() {
	
	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}
}
