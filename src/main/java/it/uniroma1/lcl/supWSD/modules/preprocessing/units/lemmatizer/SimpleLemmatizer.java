package it.uniroma1.lcl.supWSD.modules.preprocessing.units.lemmatizer;


/**
 * @author Simone Papandrea
 *
 */
class SimpleLemmatizer extends Lemmatizer {

	private final static String MODEL  = "/";
	
	SimpleLemmatizer(String model) {

		super(model);

	}

	@Override
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
	public void load() throws Exception {
	
	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}
}
