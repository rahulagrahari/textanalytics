package it.si3p.supwsd.modules.preprocessing;

import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import it.si3p.supwsd.data.Annotation;

/**
 * @author papandrea
 *
 */
public abstract class StanfordPreprocessor implements Preprocessor {

	private StanfordCoreNLP mPipeline;
	final boolean mPOS;
	final boolean mLemma;
	final boolean mDepParse;
	private final Properties mProperties;

	
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

		this.mProperties = new Properties();
		this.mProperties.setProperty("annotators", annotators);
	}

	
	public void setPOSModel(String model){
		
		this.mProperties.setProperty("pos.model", model);
	}
	
	public void setDepparseSModel(String model){
		
		this.mProperties.setProperty("depparse.model", model);
	}

	@Override
	public void load() {

		this.mProperties.setProperty("tokenize.options", "normalizeParentheses=false,normalizeOtherBrackets=false,untokenizable=allKeep,escapeForwardSlashAsterisk=false,ptb3Escaping=false");
		this.mPipeline = new StanfordCoreNLP(this.mProperties);
	}

	@Override
	public void unload() {

	}

	@Override
	public void execute(Annotation annotation) {

		edu.stanford.nlp.pipeline.Annotation document;
		String text;
		//annotation.mText="Eight or ten years ago , a couple of French hoods stole a priceless Khmer head from the Musee Guimet , in Paris , and a week later crawled into the Salpetriere with unmistakable symptoms of leprosy .Hell &apos;s own amount of S0a2rHrOpFchaulmoogra_oilS0a2rHrOpF did nothing to alleviate their torment ; they expired amid indescribable fantods , imploring the Blessed One to forgive their desecration .Any reputable French interne can supply you with a dozen similar instances , and I &apos;ll presently recount a case out of my own personal experience , but , for the moment , let &apos;s resume our catalogue .";
		text=init(annotation);
		document = new edu.stanford.nlp.pipeline.Annotation(text);
		mPipeline.annotate(document);
		annote(annotation, document.get(SentencesAnnotation.class));
	}

	abstract void annote(Annotation annotation, List<CoreMap> sentences);

	abstract String init(Annotation annotation);
		
}