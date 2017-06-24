package it.si3p.supwsd.modules.writer;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;
import it.si3p.supwsd.modules.classification.instances.AmbiguityTest;
import it.si3p.supwsd.modules.classification.scorer.Result;

/**
 * @author papandrea
 *
 */
public abstract class Writer {

	private final static String SCORES_DIR = "scores";
	private static String DIRECTORY = ".";
	protected static final String ENCODING = "ISO8859-1";
	
	public static void setDirectory(String dir) {

		DIRECTORY = dir;
		new File(dir + File.separator + SCORES_DIR).mkdirs();
	}
	
	public abstract void write(Map<AmbiguityTest, TreeSet<Result>> map)throws IOException;
	
	protected static final String getDir(){
		
		return DIRECTORY+File.separator+SCORES_DIR;
	}
	
	public static void clear() {

		File dir = new File(getDir());

		for (File file : dir.listFiles())
			file.delete();
	}
}
