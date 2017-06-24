package it.si3p.supwsd.mns;

import java.io.IOException;
import it.si3p.supwsd.modules.parser.ParserType;

/**
 * @author papandrea
 *
 */
public class MNSFactory {

	private static MNSFactory instance;

	private MNSFactory() {

	}

	public static MNSFactory getInstance() {

		if (instance == null)
			instance = new MNSFactory();

		return instance;
	}

	public MNS getMNS(ParserType parserType, String mnsFile) throws IOException {

		MNS mns = null;

		switch (parserType) {

		case SENSEVAL:
		case PLAIN:
			mns = new SensEvalMNS(mnsFile);
			break;

		default:

			if (mnsFile != null)
				mns = new MNS(mnsFile);
			break;
		}

		return mns;
	}
}
