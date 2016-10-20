package it.uniroma1.lcl.supWSD.modules.preprocessing;

import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import it.uniroma1.lcl.supWSD.data.Annotation;

/**
 * @author Simone Papandrea
 *
 */
abstract class StanfordPreprocessor implements Preprocessor {

	private StanfordCoreNLP mPipeline;
	private final String mAnnotators;
	final boolean mPOS;
	final boolean mLemma;
	final boolean mDepParse;

	StanfordPreprocessor(boolean split, boolean pos, boolean lemma, boolean depparse,boolean parse) {

		String annotators;

		mPOS = pos;
		mLemma = lemma;
		mDepParse = depparse;

		annotators = "tokenize";

		if (split)
			annotators += ",ssplit";

		if (pos)
			annotators += ",pos";

		if (lemma)
			annotators += ",lemma";

		if (depparse && parse)
			annotators += ",depparse";

		this.mAnnotators = annotators;
	}

	@Override
	public void load() {

		Properties props = new Properties();
		props.setProperty("annotators", mAnnotators);
		props.setProperty("tokenize.options", "normalizeParentheses=false,normalizeOtherBrackets=false,untokenizable=allKeep,escapeForwardSlashAsterisk=false,ptb3Escaping=false");
		
		this.mPipeline = new StanfordCoreNLP(props);
	}

	@Override
	public void unload() {

	}

	@Override
	public void execute(Annotation annotation) {

		edu.stanford.nlp.pipeline.Annotation document;
		String text;
		
		text=init(annotation);
		document = new edu.stanford.nlp.pipeline.Annotation(text);
		mPipeline.annotate(document);
		annote(annotation, document.get(SentencesAnnotation.class));
	}

	abstract void annote(Annotation annotation, List<CoreMap> sentences);

	abstract String init(Annotation annotation);
		
}