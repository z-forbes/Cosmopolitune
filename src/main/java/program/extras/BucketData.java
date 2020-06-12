package program.extras;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BucketData {

    /** the region of the bucket **/
    private static final Regions CLIENT_REGION = Regions.US_EAST_2;
    /** the name of the bucket **/
    private static final String BUCKET_NAME = "elasticbeanstalk-us-east-2-128224929684";
    /** the path of the cache in the bucket **/
    private static final String KEY = "cache/cache.txt";

    /** the credentials required to access the bucket **/
    private static final String ACCESS_KEY = Confidential.BucketData_accessKey;
    private static final String SECRET_KEY = Confidential.BucketData_secretKey;

    /** the client used to communicate with the bucket **/
    private static AmazonS3 s3Client = initialiseApi();

    /**
     * initialises the api to save/upload from the bucket
     **/
    private static AmazonS3 initialiseApi() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(CLIENT_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
        return s3Client;
    }


    /** loads the cache from the bucket and returns it as a string **/
    public static String loadCache() {
        S3Object fullObject;
        try {
            fullObject = s3Client.getObject(new GetObjectRequest(BUCKET_NAME, KEY));
            String output = inputStreamToString(fullObject.getObjectContent());
            fullObject.close();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Was unable to load the cache from the s3 bucket.");
        }
    }

    /** converts an input stream object to a string and returns it **/
    private static String inputStreamToString(InputStream input) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }

    /** saves the cache back to the bucket once updated **/
    public static void saveCache(String newCache) {
        s3Client.putObject(BUCKET_NAME, KEY, newCache);
        System.out.println("Cache has been updated.");
    }

}
