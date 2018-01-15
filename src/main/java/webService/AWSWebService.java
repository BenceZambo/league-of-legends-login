package webService;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AWSWebService {

    public static String bucketName;
    public static String basePath = System.getProperty("user.dir");
    public static String accessKey;
    public static String secretKey;
    public static String folder;
    public static String region;

    public AWSWebService() {
    }

    public static AWSCredentials createAWSCredentials(){
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    public void WebService(File uploadFile, String keyName) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        System.out.println(keyName);
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
        s3client.putObject(
                bucketName,
                keyName,
                uploadFile
        );
    }
}