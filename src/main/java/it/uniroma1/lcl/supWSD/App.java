package it.uniroma1.lcl.supWSD;


/**
 * @author Simone Papandrea
 *
 */
public class App {

	public static void main(String[] args) {

		final String usage = "Usage: train|test config.xml corpus.xml keys.key";
		String command, corpus, conf, keys;

		try {

			if (checkArgs(args)) {
				
				command = args[0];
				conf = args[1];
				corpus = args[2];
				keys = args.length == 4 ? args[3] : null;

				if (command.equals("train"))
					SupWSD.train(conf, corpus, keys);
				else
					SupWSD.test(conf, corpus, keys);
				
			} else
				throw new IllegalArgumentException(usage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static boolean checkArgs(String[] args) {

		boolean valid=false;
		int length;
		
		if(args!=null){
		
			length = args.length;
			valid= (length == 4 && args[0].equals("train")) || ((length == 3 || length == 4) && args[0].equals("test"));
	
		}
		
		return valid;
	}
}