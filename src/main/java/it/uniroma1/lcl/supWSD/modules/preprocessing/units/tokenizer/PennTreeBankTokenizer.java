package it.uniroma1.lcl.supWSD.modules.preprocessing.units.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import it.uniroma1.lcl.supWSD.modules.preprocessing.units.Unit;

/**
 * @author Simone Papandrea
 *
 */
class PennTreeBankTokenizer extends Unit implements Tokenizer {

	private static final String[] PENN_TREE_BANK_ANNOTATIONS = new String[] { "\\[(/?\\w+)\\]= |$1| ", "^\"=`` ",
			"([ \\(\\[\\{<])\"=$1 `` ", "\\.\\.\\.= ... ", "([,;:@#$%&])= $1 ",
			"([^\\.])(\\.)([\\]\\)\\}>\"\\']*)\\s*$=$1 $2$3 ", "([?!])= $1 ", "([\\]\\[\\(\\)\\{\\}<>])= $1 ",
			"\\(=-LRB-", "\\)=-RRB-", "\\[=-LSB-", "\\]=-RSB-", "\\{=-LCB-", "\\}=-RCB-", "--= -- ", "$= ", "^= ",
			"\"= '' ", "([^'])' =$1 ' ", "''= '' ", "``= `` ", "'([sSmMdD]) = '$1 ", "'ll = 'll ", "'re = 're ",
			"'ve = 've ", "n't = n't ", "'LL = 'LL ", "'RE = 'RE ", "'VE = 'VE ", "N'T = N'T ",
			" ([Cc])annot = $1an not ", " ([Dd])'ye = $1' ye ", " ([Gg])imme = $1im me ", " ([Gg])onna = $1on na ",
			" ([Gg])otta = $1ot ta ", " ([Ll])emme = $1em me ", " ([Mm])ore'n = $1ore 'n ", " '([Tt])is = '$1 is ",
			" '([Tt])was = '$1 was ", " ([Ww])anna = $1an na ",
			" '((?!['tTnNsSmMdD] |\\s|[2-9]0s |em |till |cause |ll |LL |ve |VE |re |RE )[^\\s]+) = ` $1 ", "  *= ",
			"^ *=" };
	private Pattern mSegmenter;
	private List<PennTreebankAnnotation> mPennTreebankAnnotations;

	PennTreeBankTokenizer() {

		super(null);

		mPennTreebankAnnotations = new ArrayList<PennTreebankAnnotation>();
		mSegmenter = Pattern.compile("\\s+");
	}

	public String[] tokenize(String sentence) {

		for (PennTreebankAnnotation pb : mPennTreebankAnnotations)
			sentence = pb.match(sentence);
		
		return 	mSegmenter.split(sentence);
	}

	@Override
	public void load() throws Exception {

		for (String annotation : PENN_TREE_BANK_ANNOTATIONS)
			mPennTreebankAnnotations.add(new PennTreebankAnnotation(annotation));
	}

	@Override
	public String getDefaultModel() {

		return null;
	}

	private class PennTreebankAnnotation {

		private final Pattern mPattern;
		private final String mReplacement;

		PennTreebankAnnotation(String annotation) {

			String[] values = annotation.split("=");

			mPattern = Pattern.compile(values[0]);
			mReplacement = values.length == 2 ? values[1] : "";

		}

		String match(String input) {

			return mPattern.matcher(input).replaceAll(mReplacement);
		}
	}
}
