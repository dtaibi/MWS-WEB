package eu.dandelion;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpException;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.NIOFSDirectory;



public class DataIndex {

//	HashMap<String, Long> hmap = new HashMap<String,Long>();
//	HashSet<String> hset;
	// String localDir="C:\\Users\\Mohsin\\workspace\\indexData\\";
	String localDir="/Users/davide/mws-web-indexData/";
	
	ArrayList<DandelionDataObject> dandelionObjList;
	Path pathToIndex;
	NIOFSDirectory dir;
	StandardAnalyzer analyzer; 
	IndexWriterConfig config; 
    IndexWriter writer; 
    IndexReader reader;
    CharArraySet stopSet;    
    String[] stopWords =
    	    {
    	    	 "0", "1", "2", "3", "4", "5", "6", "7", "8",
    	    	 "9", "000", "$", "�",
    	    	"a", "about", "above", "above", "across", "after",
    	    	 "afterwards", "again", "against", "all", "almost",
    	    	 "alone", "along", "already", "also","although",
    	    	 "always","am","among", "amongst", "amoungst",
    	    	 "amount",  "an", "and", "another", "any",
    	    	 "anyhow","anyone","anything","anyway",
    	    	 "anywhere", "are", "around", "as",
    	    	 "at", "back","be","became", "because",
    	    	 "become","becomes", "becoming", "been",
    	    	 "before", "beforehand", "behind", "being",
    	    	 "below", "beside", "besides", "between", "beyond",
    	    	 "bill", "both", "bottom","but", "by", "call", "can",
    	    	 "cannot", "cant", "co", "con", "could", "couldnt", "cry",
    	    	 "de", "describe", "detail", "do", "done", "down", "due",
    	    	 "during", "each", "eg", "eight", "either", "eleven","else",
    	    	 "elsewhere", "empty", "enough", "etc", "even", "ever", "every",
    	    	 "everyone", "everything", "everywhere", "except", "few", "fifteen",
    	    	 "fify", "fill", "find", "fire", "first", "five", "for", "former",
    	    	 "formerly", "forty", "found", "four", "from", "front", "full", "further",
    	    	 "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her",
    	    	 "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him",
    	    	 "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc",
    	    	 "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last",
    	    	 "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me",
    	    	 "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly",
    	    	 "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", 
    	    	 "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not",
    	    	 "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only",
    	    	 "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out",
    	    	 "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same",
    	    	 "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should",
    	    	 "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone",
    	    	 "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", 
    	    	 "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", 
    	    	 "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these",
    	    	 "they", "thickv", "thin", "third", "this", "those", "though", "three", "through",
    	    	 "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards",
    	    	 "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via",
    	    	 "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where",
    	    	 "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which",
    	    	 "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within",
    	    	 "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"    		
    	    	 };

    
	
    
	public DataIndex() throws IOException
	{
		pathToIndex= Paths.get(makeFolder());
		dir = new NIOFSDirectory(pathToIndex);
		analyzer = new StandardAnalyzer();
		config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir,config);
		writer.commit();
		reader = DirectoryReader.open(dir);
		updateStopList();
		dandelionObjList = null;
		
		
	}
	
	public DataIndex(String path) throws IOException
	{
		pathToIndex= Paths.get(path);
		dir = new NIOFSDirectory(pathToIndex);
		analyzer = new StandardAnalyzer();
		config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir,config);
		writer.commit();
		reader = DirectoryReader.open(dir);
		updateStopList();
		dandelionObjList = null;
	}

//	public HashMap<String, Long> getTermFrequiences() throws IOException
//	{
//		
//		indexobj.openDirForReading();
//		ArrayList<String> tempList= new ArrayList<String>(hset);
//		
//		for (int i=0; i<tempList.size(); i++)	
//		{
//		//	System.out.println(indexobj.stemContent(tempList.get(i)));
//			hmap.put(tempList.get(i),indexobj.termFreq(indexobj.stemContent(tempList.get(i))));
//		}
//	    return  hmap;
//	}
	
	public void cacheContent(String content, String type) throws IOException
	{
	//	indexobj.openDirForWriting();
		dandelionObjList = null;
		
		try {
			dandelionObjList=EntityExtractionService.postTextType(content, type,0.0);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		String listString = "";
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher;
		
			for (String s : getLabelList(dandelionObjList))
			{
				matcher = pattern.matcher(s);
				if(matcher.find())
				{	
					String words []= s.split(" ");
					for(int i=0; i<words.length; i++)
					{
//						hset.add(words[i]);
					    listString += words[i] + "\t";
					}
				}
				else{
//				hset.add(s);
			    listString += s + "\t";
				}
			}
				System.out.println(listString);
			addDoc(listString);
		//	indexobj.endWriting();
	}
	
	private ArrayList<String> getLabelList(ArrayList<DandelionDataObject> obj)
	{
		ArrayList<String> labels = new ArrayList<String>();
		for(int i=0; i<obj.size(); i++)
		{
			labels.add(obj.get(i).getUri()); // .getLabel());
		}
		return labels;
	}
	   public  String stemContent(String input) throws IOException {
	    	 
	    	
	        TokenStream tokenStream = analyzer.tokenStream("title",new StringReader(input));
	        tokenStream  = new StopFilter(tokenStream, stopSet);
	      //  System.out.println(analyzer.getStopwordSet());
	        
	        tokenStream = new PorterStemFilter(tokenStream);
	 
	        StringBuilder sb = new StringBuilder();
	   //     OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
	        CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);
	        tokenStream.reset();
	        try{
	            while (tokenStream.incrementToken()) {
	                if (sb.length() > 0) {
	                    sb.append(" ");
	                }
	                sb.append(charTermAttr.toString());
	            }
	   tokenStream.close();
	        }
	        catch (IOException e){
	            System.out.println(e.getMessage());
	        }
	        
	        return sb.toString();
	}
//	    private String filterStopWords(String content)
//	    {
//	    	content = content.replaceAll("\\d+(?:[.,]\\d+)*\\s*", "");
//	    	for(int i=0; i<stopWords.length; i++)
//	    		{System.out.println(content);
//	    		content = content.replaceAll(stopWords[i], "");}
//	    	return content;
//	    }
	    
	    private void updateStopList()
	    {
	    	stopSet = CharArraySet.copy(analyzer.getStopwordSet());
			for(int i=0; i<stopWords.length; i++)
				stopSet.add(stopWords[i]);
	    }
	
	public void openDirForReading(String path) throws IOException
	{
		pathToIndex= Paths.get(path);
		dir = new NIOFSDirectory(pathToIndex);
		reader = DirectoryReader.open(dir);
	}
	
	public void openDirForReading() throws IOException
	{
		pathToIndex= Paths.get(pathToIndex.toString());
		dir = new NIOFSDirectory(pathToIndex);
		reader = DirectoryReader.open(dir);
	}
	
	public void EndCachingSession() throws IOException
	{
	//	dir.close();
		writer.close();
	}
	
	public void addDoc(String content) throws IOException {
	//	content =stemContent(content);
		FieldType fieldType = new FieldType();
	    fieldType.setStoreTermVectors(true);
	    fieldType.setStoreTermVectorPositions(true);
	    fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
	    fieldType.setStored(true);
	    Document doc = new Document();
	    doc.add(new Field("content", content, fieldType));
	    writer.addDocument(doc);
	}
	
//	public ArrayList <Pair<Integer, Integer>> termFreqInAllDoc(String term) throws IOException
//	{
//		ArrayList <Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
//		PostingsEnum pe = MultiFields.getTermDocsEnum(reader, "content", new BytesRef(term));
//	    while(pe.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {	  //DocsEnum.NO_MORE_DOCS
//	    	list.add(new Pair<Integer, Integer>(pe.docID(),pe.freq()));
//	     //     System.out.println("Term Frequency of the word '"+term+"' in doc "+pe.docID()+" is "+ de.freq());
//	    }
//	    
//	    return list;
//	}
	
	public HashMap<String, Long> getAllTermsAndTermFrequiences() throws IOException
	{
		openDirForReading();
		HashMap<String,Long> hmap= new HashMap<String,Long>(); 
		Fields fields = MultiFields.getFields(reader);
        for (String field : fields) {
            Terms terms = fields.terms(field);
            TermsEnum termsEnum = terms.iterator();
            while (termsEnum.next() != null) {
              //   System.out.println(termsEnum.term().utf8ToString());
                 hmap.put(termsEnum.term().utf8ToString(),termFreq(termsEnum.term().utf8ToString()));
            }
        }
		
	    return hmap;
	}
	
	public int documentFreq(String term) throws IOException
	{
		return reader.docFreq(new Term("content",term));
	}
	
	public long termFreq(String term) throws IOException
	{
	//	System.out.println(reader.totalTermFreq(new Term("content",term))+": "+reader.getSumTotalTermFreq(term));
		return reader.totalTermFreq(new Term("content",term));
	}
	
	public float idf(String term) throws IOException
	{
		DefaultSimilarity simi = new DefaultSimilarity();
		float idf = simi.idf(reader.docFreq(new Term("content",term)), reader.maxDoc());
//System.out.println(reader.maxDoc());
		return idf;
	}
	
	public void clearCache()
	{
		
		File file = new File(pathToIndex.toString());
        File[] files = file.listFiles(); 
        for (File f:files) 
        {
        	if (f.isFile() && f.exists()) 
            { f.delete();}
        }
        file.delete();
		
	}
	
	private String makeFolder() 
	{	
		File dir = new File(localDir+System.currentTimeMillis());
		dir.setWritable(true);
		dir.setExecutable(true);
		dir.mkdir();
//		System.out.println(dir.toString());
		return dir.toString();
	}
		
	
	
	public String getCachePath()
	{
		return pathToIndex.toString();
	}
	
}
