package it.uniroma1.lcl.supWSD.modules.preprocessing.units;

import java.util.concurrent.Callable;

/**
 * @author Simone Papandrea
 *
 */
public abstract class Unit implements Callable<Void> {

	private final String mModel;
	
	public Unit(String modelPath){
		
		if(modelPath==null || modelPath.isEmpty())
			modelPath=getDefaultModel();
		
		this.mModel=modelPath;
	}
	
	public abstract void load() throws Exception;
	
	public void unload(){};
	
	
	protected abstract String getDefaultModel();
	
	protected String getModel(){
		
		return mModel;
	}


	public final Void call() throws Exception {
		
		load();
		
		return null;

	}
}
