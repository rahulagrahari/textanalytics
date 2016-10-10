package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

import java.io.StringReader;
import java.util.List;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

/**
 * @author Simone Papandrea
 *
 */
class StanfordTokenizer extends Tokenizer {

	private static final String MODEL = "";
	private TokenizerFactory<CoreLabel> mTokenizerFactory;

	StanfordTokenizer(String modelFile) {

		super(modelFile);

	}

	@Override
	public String[] tokenize(String sentence) {

		String[] tokens;
		List<CoreLabel> labels;
		int length;

		labels = mTokenizerFactory.getTokenizer(new StringReader(sentence)).tokenize();
		length = labels.size();
		tokens = new String[length];

		for (int i = 0; i < length; i++)
			tokens[i] = labels.get(i).value();

		return tokens;
	}

	@Override
	public void load() throws Exception {

		mTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "normalizeParentheses=false,normalizeOtherBrackets=false,untokenizable=allKeep,escapeForwardSlashAsterisk=false,ptb3Escaping=false");
	}

	@Override
	public String getDefaultModel() {

		return MODEL;
	}

}
