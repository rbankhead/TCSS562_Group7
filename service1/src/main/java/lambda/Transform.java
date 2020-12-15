package lambda;
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import saaf.Inspector;
import saaf.Response;
import java.util.HashMap;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;


public class Transform implements RequestHandler<Request, HashMap<String, Object>> {


    public HashMap<String, Object> handleRequest(Request request, Context context) {
        

        Inspector inspector = new Inspector();
        //inspector.inspectAll();
        //BufferedReader br;
        final String lineSep="\n";
        final String bucketName = "java.service1.tcss562f20.rab";
        final String fileName = "output.csv";
        String line;
        StringWriter sw = new StringWriter();
        AmazonS3 mys3Client = AmazonS3ClientBuilder.standard().build();
        S3Object s3Object = mys3Client.getObject(new GetObjectRequest(bucketName,"input.csv"));
	InputStream objectData = s3Object.getObjectContent();
	
        try {
        	Scanner s = new Scanner(objectData);
        	line = s.nextLine();
        	sw.append(line+","+"Gross Margin"+","+"Order processing time"+lineSep);
        	
        	while (s.hasNext()) {
        	    line = s.nextLine();
		    String[] cols = line.split(",");
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		    Date firstDate = sdf.parse(cols[7]);
		    Date secondDate = sdf.parse(cols[5]);

		    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		    
		    float grossMargin = (Float.parseFloat(cols[13]))/(Float.parseFloat(cols[11]));
		    
		    String addedColumn = String.valueOf(grossMargin);
		    sw.append(line+","+addedColumn);
		    
		    addedColumn = String.valueOf(diff);
		    sw.append(","+addedColumn+lineSep);
		    }
        }
	
        catch(Exception e){
            sw.append(e.toString());
        }
        
        
        

        
        byte[] bytes = sw.toString().getBytes(StandardCharsets.UTF_8);
        InputStream is = new ByteArrayInputStream(bytes);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(bytes.length);
        meta.setContentType("test/plain");
        
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        s3Client.putObject(bucketName, fileName, is, meta);
        
        
        //****************END FUNCTION IMPLEMENTATION***************************
        
        //Collect final information such as total runtime and cpu deltas.
        return inspector.finish();
    }
}
