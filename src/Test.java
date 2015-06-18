import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String k = "#S hit hello world";
	//	k = k.toLowerCase();
	//	k= "閒的简体是闲还是间啊？";
		
		String langdetect_profiles = "/Users/boY/Desktop/Code Me/Java/Libraries/langdetect-09-13-2011/profiles/";
		Test t = new Test();
		try{
		t.init(langdetect_profiles);
		String test = t.detect(k);
		System.out.println(test);
		
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_h");
		String formattedDate = sdf.format(d);
		System.out.println(formattedDate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(String profileDirectory) throws LangDetectException {
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
