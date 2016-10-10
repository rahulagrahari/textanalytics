package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tagger;

import java.util.List;

/**
 * @author Simone Papandrea
 *
 */
class SimpleTagger extends Tagger {

	private final static String MODEL = "[ \t\n\r\f]+";
	
	SimpleTagger(String model) {

		super(model);
	}


	@Override
	public String[] tag(List<String> words) {

		String[] tags,vals;
		String item, model,tag;
		int length, index;

		length = words.size();
		tags = new String[length];
		model=this.getModel();
		
		for (int i = 0; i < length; i++) {

			item = words.get(i);
			index = item.indexOf(model);

			if (index > 0) {

				vals=item.split(model);
				words.set(i,vals[0]);
				tag=vals[1];
			}
			else
				tag="";
			
			tags[i]=tag;
		}

		return tags;
	}

	@Override
	public void load() throws Exception {

	
	}

	@Override
	protected String getDefaultModel() {

		return MODEL;
	}

}
