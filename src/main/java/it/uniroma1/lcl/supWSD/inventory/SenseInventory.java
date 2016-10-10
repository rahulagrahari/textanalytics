package it.uniroma1.lcl.supWSD.inventory;

import it.uniroma1.lcl.supWSD.data.POSMap.TAG;

/**
 * @author Simone Papandrea
 *
 */
public interface SenseInventory {

	public  String getSense(String lemma,TAG pos);
	public void close();
}
