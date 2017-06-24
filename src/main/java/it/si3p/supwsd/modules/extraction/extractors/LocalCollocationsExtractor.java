package it.si3p.supwsd.modules.extraction.extractors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.data.Lexel;
import it.si3p.supwsd.data.Token;
import it.si3p.supwsd.modules.extraction.features.Feature;
import it.si3p.supwsd.modules.extraction.features.LocalCollocation;

/**
 * @author papandrea
 *
 */
public class LocalCollocationsExtractor extends FeatureExtractor {

	private static final String DEFAULT = "^";
	private final String[] mLocalCollocations;
	private static final String[] DEFAULT_COLLOCATIONS = new String[] { "-2,-2", "-1,-1", "1,1", "2,2", "-2,-1", "-1,1",
			"1,2", "-3,-1", "-2,1", "-1,2", "1,3" };

	public LocalCollocationsExtractor(int cutoff,String collocationsFile) throws IOException{

		super(cutoff);
		
		if(collocationsFile==null)
			mLocalCollocations=DEFAULT_COLLOCATIONS;
		else
			mLocalCollocations=readCollocations(collocationsFile);
		
	}

	public LocalCollocationsExtractor(int cutoff,String[] collocations) {

		super(cutoff);
		
		this.mLocalCollocations = collocations;
	}


	@Override
	public Collection<Feature> extract(Lexel lexel, Annotation annotation) {

		Vector<Feature> features;
		Token[] tokens;
		String value, token, indexes[];
		int i, j, start, end;
		int id, size;

		tokens = annotation.getTokens(lexel);
		id = lexel.getTokenIndex();
		features = new Vector<Feature>();
		size = tokens.length;

		for (String collocation : mLocalCollocations) {

			indexes = collocation.split(",");
			i = Integer.valueOf(indexes[0]);
			j = Integer.valueOf(indexes[1]);

			if (i <= j) {

				value = "";
				start = id + i;
				end = id + j;

				for (int k = start; k <= end; k++) {
					
					if (k != id) {

						if (k > -1 && k < size)
							token = tokens[k].getWord().toLowerCase();
						else
							token = DEFAULT;

						value += " " + token;
					}
				}
				features.add(new LocalCollocation(i, j, value.trim()));

			} else
				throw new IllegalArgumentException("Invalid Local Collocations");
		}

		return features;
	}


	@Override
	public Class<? extends Feature> getFeatureClass() {
		
		return LocalCollocation.class;
	}
	
	
	private static String[] readCollocations(String collocationsFile) throws IOException {

		List<String> collocations;
		String line;
		
		collocations = new ArrayList<String>();

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(collocationsFile)))){

			while ((line = reader.readLine()) != null)
				collocations.add(line);
		}

		return collocations.toArray(new String[collocations.size()]);
	}

}
