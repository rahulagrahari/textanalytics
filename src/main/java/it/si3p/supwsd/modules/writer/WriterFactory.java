package it.si3p.supwsd.modules.writer;

/**
 * @author papandrea
 *
 */
public class WriterFactory {

	private static WriterFactory instance;

	private WriterFactory() {

	}

	public static WriterFactory getInstance() {

		if (instance == null)
			instance = new WriterFactory();

		return instance;
	}

	public Writer getWriter(WriterType WriterType){

		Writer writer = null;

		switch (WriterType) {

		case PLAIN:
			writer = new PlainWriter();
			break;

		case SINGLE:
			writer = new SingleWriter();
			break;
			
		default:
			writer = new AllWriter();
			break;
		}

		return writer;
	}
}
