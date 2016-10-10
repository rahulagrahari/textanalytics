package it.uniroma1.lcl.supWSD.mns;

import java.io.IOException;
import it.uniroma1.lcl.supWSD.modules.parser.ParserType;
import net.didion.jwnl.JWNLException;

/**
 * @author Simone Papandrea
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

	public MNS getMNS(ParserType parserType, String mnsFile) throws IOException, JWNLException {

		MNS mns = null;

		switch (parserType) {

		case SENSEVAL:

			mns = new SensEvalMNS(mnsFile);
			break;

		case PLAIN:

			break;

		default:

			if (mnsFile != null)
				mns = new MNS(mnsFile);
			break;
		}

		return mns;
	}
}
