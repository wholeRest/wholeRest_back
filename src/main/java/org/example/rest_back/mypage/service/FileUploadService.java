package org.example.rest_back.mypage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

@Service
public class FileUploadService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public FileUploadService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        //파일 이름을 고유하게 하기 위해 현재 업로드 시간과 원래 파일 이름을 조합
        String filename = Instant.now().getEpochSecond() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, file.getInputStream(), metadata);

        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(bucketName, filename).toExternalForm();
    }

    public void deleteFile(String filename) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, filename));
    }
}
