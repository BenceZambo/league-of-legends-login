package webService;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AWSWebService {

    public static String bucketName;
    public static String basePath = System.getProperty("user.dir");
    public static String accessKey;
    public static String secretKey;
    public static String folder;




    public AWSWebService() {
    }

    public static AWSCredentials createAWSCredentials(){
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    public void WebService(File uploadFile, String keyName) throws IOException {
        AmazonS3 s3client = new AmazonS3Client(createAWSCredentials());
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            System.out.println(bucketName);
            System.out.println(keyName);
            System.out.println(uploadFile);
            s3client.putObject(new PutObjectRequest(
                    bucketName, keyName, uploadFile));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}