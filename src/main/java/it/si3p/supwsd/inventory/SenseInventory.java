package it.si3p.supwsd.inventory;

import it.si3p.supwsd.data.POSMap.TAG;

/**
 * @author papandrea
 *
 */
public interface SenseInventory {

	public  String getSense(String lemma,TAG pos);
	public void close();
}
