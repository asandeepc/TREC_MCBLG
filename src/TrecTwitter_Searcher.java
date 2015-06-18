import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.FSDirectory;


public class TrecTwitter_Searcher {

	
//	private static String Index_Directory = "/Users/boY/Desktop/Code Me/Java/TrecTwitter/TweetsIndex";
	private static String Index_Directory = "/webdex/expir/Trec_Twitter/Test_Bed/Twitter_Stream_Index";
	
	private static Path path = FileSystems.getDefault().getPath(Index_Directory);
	private static IndexReader reader = null;
    private static IndexSearcher searcher = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrecTwitter_Searcher s = new TrecTwitter_Searcher();
		String text = args[0];
		
		s.test_search(text);
	}
	
	TrecTwitter_Searcher(){
		try{
			reader = DirectoryReader.open(FSDirectory.open(path));
			searcher = new IndexSearcher(reader);
			float mu = 3000f;
			searcher.setSimilarity(new LMDirichletSimilarity(mu));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	void test_search(String query){
		try{
			Analyzer analyzer = new StandardAnalyzer();
			Query q = new QueryParser("text",analyzer).parse(query);
			TopDocs results = searcher.search(q, 10);
			ScoreDoc[] hits = results.scoreDocs;
	/*	TopScoreDocCollector collector = TopScoreDocCollector.create(10);
		StandardAnalyzer analyzer = new StandardAnalyzer();
        
		
		Query q = new QueryParser("text",analyzer).parse(query);
	    
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		*/
	    int hits_length = hits.length;
		for(int i=0;i<hits_length;i++){
			int docId = hits[i].doc;
		//	System.out.println(docId);
			Document d = searcher.doc(docId);
	    	String pathName = d.get("doc_id");
	    	String text = d.get("text");
			System.out.println("Doc id:"+pathName);
		//	System.out.println("Score:"+hits[i].score);
			System.out.println("Text:"+text);
			System.out.println("Doc:"+hits[i].doc);
	    	System.out.println("------");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	

}
