package com.autoparts.serivce;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final AmazonS3 s3;

    @SneakyThrows
    public void upload(String imagePath,
                       String fileName,
                       ObjectMetadata metadata,
                       InputStream content){
        try (content){
            s3.putObject(imagePath,fileName,content,metadata);
        } catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    @SneakyThrows
    public byte[] get(String imagePath, String key){
        try {
            S3Object object = s3.getObject(imagePath, key);

            return IOUtils.toByteArray(object.getObjectContent());
        }catch (AmazonServiceException | IOException e){
            throw new IllegalStateException("Failed to download file to s3", e);
        }
    }

}
