package it.si3p.supwsd.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author papandrea
 *
 */
public class POSMap {
	
	private static final TAG DEF_POS=TAG.n;
	private final Map<String, String> mMap;
	public enum TAG{
		n,v,a,r
	}
	
	private static final String[] MAP = { 
			"n NN NNP NNPS NE NNS NN|NNS NN|SYM NN|VBG NP N",
			"v MD VB VBD VBD|VBN VBG VBG|NN VBN VBP VBP|TO VBZ VP VVD VVZ VVN VVB VVG VV V",
			"a A JJ JJR JJRJR JJS JJ|RB JJ|VBG",
			"r RB RBR RBS RB|RP RB|VBG WRB R IN IN|RP"};

	private static POSMap singleton = new POSMap();

	private POSMap() {

		this.mMap = new HashMap<String, String>();

		String token, values[];

		for (String pos : MAP) {

			values = pos.split("\\s");
			token = values[0];

			for (int i = 1; i < values.length; i++)
				this.mMap.put(values[i],token);
		}
	}

	public static POSMap getInstance() {
		return singleton;
	}

	public TAG getPOS(String POS) {

		POS=POS.toUpperCase();
		
		if(this.mMap.containsKey(POS))
			return TAG.valueOf(this.mMap.get(POS));
		else
			return DEF_POS;
	}
}