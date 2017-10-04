
package it.si3p.supwsd.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import it.si3p.supwsd.inventory.SenseInventoryType;
import it.si3p.supwsd.modules.classification.classifiers.ClassifierType;
import it.si3p.supwsd.modules.extraction.extractors.FeatureExtractor;
import it.si3p.supwsd.modules.parser.ParserType;
import it.si3p.supwsd.modules.preprocessing.pipeline.PipelineType;
import it.si3p.supwsd.modules.preprocessing.units.dependencyParser.DependencyParserType;
import it.si3p.supwsd.modules.preprocessing.units.lemmatizer.LemmatizerType;
import it.si3p.supwsd.modules.preprocessing.units.splitter.SplitterType;
import it.si3p.supwsd.modules.preprocessing.units.tagger.TaggerType;
import it.si3p.supwsd.modules.preprocessing.units.tokenizer.TokenizerType;
import it.si3p.supwsd.modules.writer.WriterType;

/**
 * @author papandrea
 *
 */
public class Config {

	private ParserType mParserType;
	private ClassifierType mClassifierType;
	private SplitterType mSplitterType;
	private TokenizerType mTokenizerType;
	private TaggerType mTaggerType;
	private LemmatizerType mLemmatizerType;
	private DependencyParserType mDParserType;
	private PipelineType mPipelineType;
	private String mPipelineModel;
	private String mSplitterModel, mTokenizerModel, mTaggerModel, mLemmatizerModel, mDParserModel;
	private WriterType mWriterType;
	private SenseInventoryType mSenseInventory;
	private String mWorkingDir;
	private String mMNS;
	private String mDict;
	private final List<FeatureExtractor> mFeatureExtractors;

	private Config() {

		this.mFeatureExtractors = new ArrayList<FeatureExtractor>();

	}

	public static Config load(String file) throws IOException, ParserConfigurationException, SAXException {

		Config config = new Config();
		SAXParserFactory SAXfactory;
		SAXParser SAXParser;
		InputStream inputStream;
		ConfigHandler handler;

		inputStream = new FileInputStream(file);
		SAXfactory = SAXParserFactory.newInstance();
		SAXfactory.setValidating(true);
		SAXfactory.setNamespaceAware(true);
		SAXParser = SAXfactory.newSAXParser();
		SAXParser.setProperty(ConfigHandler.JAXP_SCHEMA_LANGUAGE,ConfigHandler. W3C_XML_SCHEMA);
		handler = new ConfigHandler(config);
		SAXParser.parse(inputStream, handler);

		return config;
	}

	public ParserType getParserType() {

		return this.mParserType;
	}

	public WriterType getWriterType() {

		return this.mWriterType;
	}

	public ClassifierType getClassifierType() {

		return this.mClassifierType;
	}

	public FeatureExtractor[] getFeatureExtractors() {

		return mFeatureExtractors.toArray(new FeatureExtractor[mFeatureExtractors.size()]);
	}

	public String getPipelineModel() {

		return this.mPipelineModel;
	}
	
	public PipelineType getPipelineType() {

		return this.mPipelineType;
	}
	
	public SplitterType getSplitterType() {

		return this.mSplitterType;
	}

	public TokenizerType getTokenizerType() {

		return this.mTokenizerType;
	}

	public TaggerType getTaggerType() {

		return this.mTaggerType;
	}

	public LemmatizerType getLemmatizerType() {

		return this.mLemmatizerType;
	}

	public DependencyParserType getDParserType() {

		return this.mDParserType;
	}

	public String getSplitterModel() {

		return this.mSplitterModel;
	}

	public String getTokenizerModel() {

		return this.mTokenizerModel;
	}

	public String getTaggerModel() {

		return this.mTaggerModel;
	}

	public String getLemmatizerModel() {

		return this.mLemmatizerModel;
	}

	public String getDParserModel() {

		return this.mDParserModel;
	}

	public SenseInventoryType getSenseInventory() {

		return this.mSenseInventory;
	}

	public String getWorkingDir() {

		return this.mWorkingDir;
	}

	public String getMNS() {

		return this.mMNS;
	}

	public String getDict() {

		return this.mDict;
	}

	void setWriterType(WriterType writerType) {

		this.mWriterType = writerType;
	}

	void setParserType(ParserType parserType) {

		this.mParserType = parserType;
	}

	void setClassifierType(ClassifierType classifierType) {

		this.mClassifierType = classifierType;
	}

	void addFeatureExtractor(FeatureExtractor featureExtractor) {

		this.mFeatureExtractors.add(featureExtractor);
	}

	void setPipeline(PipelineType type, String model) {

		this.mPipelineType = type;
		this.mPipelineModel = model;
	}
	
	void setSplitter(SplitterType type, String model) {

		this.mSplitterType = type;
		this.mSplitterModel = model;
	}

	void setTokenizer(TokenizerType type, String model) {

		this.mTokenizerType = type;
		this.mTokenizerModel = model;
	}

	void setTagger(TaggerType type, String model) {

		this.mTaggerType = type;
		this.mTaggerModel = model;
	}

	void setLemmatizer(LemmatizerType type, String model) {

		this.mLemmatizerType = type;
		this.mLemmatizerModel = model;
	}

	void setDParser(DependencyParserType type, String model) {

		this.mDParserType = type;
		this.mDParserModel = model;
	}

	void setSenseInventory(SenseInventoryType senseInventoryType, String dict) {

		this.mSenseInventory = senseInventoryType;
		this.mDict = dict;
	}

	void setWorkingDir(String workingDir) {

		this.mWorkingDir = workingDir;
	}

	void setMNS(String mns) {

		this.mMNS = mns;
	}
}
