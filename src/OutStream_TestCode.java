import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;

import twitter4j.JSONObject;


public class OutStream_TestCode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		System.out.println("Hello World!");
		
		String k = "This is a new test, trying to check if this thing will really work";
		
		try{
			
			FileOutputStream fos=new FileOutputStream("temp.gz");
		    Writer writer = new OutputStreamWriter(new GZIPOutputStream(fos), "UTF-8");
		    
		    JSONObject j_obj = new JSONObject();
		    j_obj.put("test", k);
			writer.write(j_obj.toString());
			writer.flush();
			writer.close();
		    System.out.println("Done!");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
