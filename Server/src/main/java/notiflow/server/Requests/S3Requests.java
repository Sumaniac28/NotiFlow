package notiflow.server.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class S3Requests {

    @JsonProperty("bucketID")
    private String bucketID;

    @JsonProperty("s3ObjectKeys")
    private List<String> s3ObjectKeys;


    public String getBucketID() {
        return bucketID;
    }

    public void setBucketID(String BucketID) {
        this.bucketID = BucketID;
    }

    public List<String> getS3ObjectKeys() {
        return s3ObjectKeys;
    }

    public void setS3ObjectKeys(List<String> s3ObjectKeys) {
        this.s3ObjectKeys = s3ObjectKeys;
    }

    @Override
    public String toString() {
        return "S3Requests{" +
                "BucketID='" + bucketID + '\'' +
                ", s3ObjectKeys=" + s3ObjectKeys +
                '}';
    }
}
