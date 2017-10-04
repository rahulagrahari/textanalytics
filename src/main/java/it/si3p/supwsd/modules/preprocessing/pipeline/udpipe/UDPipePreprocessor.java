package it.si3p.supwsd.modules.preprocessing.pipeline.udpipe;

import java.io.File;
import org.apache.commons.lang3.SystemUtils;
import cz.cuni.mff.ufal.udpipe.Model;
import cz.cuni.mff.ufal.udpipe.Pipeline;
import it.si3p.supwsd.data.Annotation;
import it.si3p.supwsd.modules.preprocessing.Preprocessor;

/**
 * @author papandrea
 *
 */
public abstract class UDPipePreprocessor implements Preprocessor {

	private final String mModelPath;
	private Model mModel;

	public UDPipePreprocessor(String model) {

		this.mModelPath = model;
	}

	@Override
	public void load() throws Exception {

		System.load(new File("./lib/libudpipe_java."+(SystemUtils.IS_OS_WINDOWS?"dll":"so")).getAbsolutePath());
		
		mModel = Model.load(mModelPath);

		if(mModel==null)
			throw new IllegalArgumentException("Cannot load model from file '" + mModelPath + "'");
	}

	@Override
	public void unload() {
		
	}

	@Override
	public void execute(Annotation annotation) {

		String processed;
		String text;
		Pipeline pipeline;

		text = init(annotation);
		pipeline = new Pipeline(mModel, "tokenize", Pipeline.getDEFAULT(), Pipeline.getDEFAULT(), "conllu");
		processed = pipeline.process(text);
		pipeline.delete();

		annote(annotation, processed);
	}

	abstract void annote(Annotation annotation, String processed);

	abstract String init(Annotation annotation);

}