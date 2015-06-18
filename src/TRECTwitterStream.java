import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.language.LanguageIdentifier;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

import twitter4j.FilterQuery;
import twitter4j.RawStreamListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TRECTwitterStream {

	
	
	
//	private static String Index_Directory = "/Users/boY/Desktop/Code Me/Java/TrecTwitter/TweetsIndex";
	private static String Index_Directory = "/webdex/expir/Trec_Twitter/Test_Bed/Twitter_Stream_Index";
	private static Path path = FileSystems.getDefault().getPath(Index_Directory);
	
	private static String langdetect_profiles = "/webdex/expir/Trec_Twitter/Test_Bed/profiles";
//	private static String langdetect_profiles = "/Users/boY/Desktop/Code Me/Java/Libraries/langdetect-09-13-2011/profiles/";
	private static FSDirectory indexDir=null;
	private static IndexWriterConfig config = null;
	private static IndexWriter indexWriter = null;
	private ArrayList<String> swear_words = new ArrayList<String>();
	int swear_words_sz =0;
	TRECTwitterStream(){
		try{
			BufferedReader br = new BufferedReader(new FileReader("/webdex/expir/Trec_Twitter/Test_Bed/swears.txt"));
		//	BufferedReader br = new BufferedReader(new FileReader("swears.txt"));
			String line = "";
			while((line=br.readLine())!=null){
				swear_words.add(line);
			}
			swear_words_sz = swear_words.size();	
		}catch(Exception e){
			
		}
	}

	

    @SuppressWarnings("unused")
    public static void main(String args[]) throws IOException {

    	TRECTwitterStream tts = new TRECTwitterStream();
    	try{
    	tts.init(langdetect_profiles);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
              .setOAuthConsumerKey("**********************")
              .setOAuthConsumerSecret("******")
              .setOAuthAccessToken("**************")
              .setOAuthAccessTokenSecret("******************");
    	
	RawStreamListener listener = new RawStreamListener() {

	    int cnt = 0;

	    public void onException(Exception ex) {
		ex.printStackTrace();
	    }

	    @Override
	    public void onMessage(String msg) {
		try {
		    if (msg.startsWith("{\"created_at")) {
			Status status = TwitterObjectFactory.createStatus(msg);
			if (status.getLang().equals("en")) {
			    System.out.println(status.getUser().getName() + "\t" + status.getText().replaceAll("\n", " ").trim());
			 //   System.out.println("Message:"+status);
			//    System.out.println("Msg:"+msg);
			    System.out.println("Tweet id:"+status.getId());
			    System.out.println("Tweet:"+status.getText());
			    System.out.println("=================================");
			    TRECTwitterStream t = new TRECTwitterStream();
			    boolean flag = t.check_Validity(status.getText());
			    String lang_detect = "en";
			    try{
			    lang_detect = t.detect(status.getText());
			    
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			  //  if(){
			    // This is the part where I shall attempt to write a file. 
			    if(flag==true && lang_detect.equals("en")){
			   
			 /*   String out_time = LocalDateTime.now ( ).toString ().replace ( "T", " " );
			    int ind1 = out_time.indexOf(" ");
				int ind2 = out_time.indexOf(":");
				String day = out_time.substring(0,ind1);
			    String f_name = day+"_"+out_time.substring(ind1+1,ind2)+".gz";
			   */
			    	Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_h");
					String formattedDate = sdf.format(d);
					String f_name = formattedDate+".gz";
			    try{
			    FileOutputStream fos=new FileOutputStream(new File("/webdex/expir/Trec_Twitter/Test_Bed/Twitter_Stream_gz_files/"+f_name),true);
			 //   FileOutputStream fos=new FileOutputStream(new File(f_name),true);
			   // FileOutputStream fos=new FileOutputStream(new File(f_name),true); 
			    indexDir = FSDirectory.open(path);
				config = new IndexWriterConfig(new StandardAnalyzer());
				indexWriter = new IndexWriter(indexDir, config);
			    
			    Document doc = new Document();
				doc.add(new StringField("doc_id",String.valueOf(status.getId()),Field.Store.YES));
				doc.add(new VecTextField("text",status.getText(),Field.Store.YES));
				indexWriter.addDocument(doc);
				indexWriter.close();
			    Writer writer = new OutputStreamWriter(new GZIPOutputStream(fos), "UTF-8"); 		
			    writer.write(msg);
			    writer.write("\n");
				writer.flush();
				writer.close();
				fos.flush();
				fos.close();
			    }catch(Exception e){
			    	System.out.println("<--Beging---->");
			    	System.out.println("Tweets ID:"+status.getId());
			    	System.out.println("Tweets:"+status.getText());
			    	System.out.println("Was called for null");
			    	e.printStackTrace();
			    	System.out.println("<--Ending---->");
			    }
			    
			}
		    
		    }
		    } else {
			// System.out.println(msg);
		    }
		} catch (TwitterException e) {
		    e.printStackTrace();
		}
	    }

	};

//	FilterQuery fq = new FilterQuery();        

  //  String keywords[] = {"star"};

  //  fq.track(keywords); 
	
	TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	twitterStream.addListener(listener);
	twitterStream.sample();
//	twitterStream.filter(fq);
	
    }
    
    boolean check_Validity(String text){
		boolean flag = false;
		
		text = text.toLowerCase();
		for(int i=0;i<swear_words_sz;i++){
			if(text.contains(swear_words.get(i))){
				return flag;
			}
		}
		
		
		flag=true;
		return flag;
	}
    
    public static void init(String profileDirectory) throws LangDetectException {
		DetectorFactory.loadProfile(profileDirectory);
	}

	public String detect(String text) throws LangDetectException {
		Detector detector;
		detector = DetectorFactory.create();
		detector.append(text);
		ArrayList<Language> langlist = detector.getProbabilities();
		
		return detector.detect();
		
	}
    
    
    
    
    
    
}
