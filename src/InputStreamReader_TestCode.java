import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.zip.GZIPInputStream;


public class InputStreamReader_TestCode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Hello World!");
		
		try{
			GZIPInputStream gz = new GZIPInputStream(new FileInputStream("2015-06-09_13.gz"));
			BufferedReader br = new BufferedReader(new InputStreamReader(gz));
			String line = "";
			int ctr=0;
			while((line=br.readLine())!=null){
				System.out.println(line);
				ctr++;
			}
			System.out.println("Done!");
			System.out.println("Ctr:"+ctr);
	//		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
			// "created_at":"Tue Jun 09 15:41:08 +0000 2015"
		//	Date t_d = sdf.parse(w[1]);
			
	//		String out_time = LocalDateTime.now ( ).toString ().replace ( "T", " " );
	//		System.out.println("Time:"+out_time);
			
	//		int ind1 = out_time.indexOf(" ");
	//		int ind2 = out_time.indexOf(":");
			
	//		System.out.println("Hour:"+out_time.substring(ind1+1,ind2));
	//		String day = out_time.substring(0,ind1);
	//		System.out.println("Day:"+day+"_"+out_time.substring(ind1+1,ind2));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
