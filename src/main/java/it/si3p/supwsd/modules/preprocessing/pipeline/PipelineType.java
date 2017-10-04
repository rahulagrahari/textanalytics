package it.si3p.supwsd.modules.preprocessing.pipeline;

/**
 * @author papandrea
 *
 */
public enum PipelineType {

	UDPIPE("udpipe");

	private final String mValue;
	
	private PipelineType(String value){
		
		this.mValue=value;
	}
	
	public String getType()
	{
		return this.mValue;
	}
}

