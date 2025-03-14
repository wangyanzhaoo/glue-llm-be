package com.rune.utils.minio;

import io.minio.MinioAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
public class MinioConfig {

    @Value(value = "${minio.endpoint}")
    private String endpoint;

    @Value(value = "${minio.accesskey}")
    private String accessKey;

    @Value(value = "${minio.secretkey}")
    private String secretKey;

    @Bean
    @Primary
    public MinioAsyncClient minioClient(){
         return MinioAsyncClient.builder()
                .endpoint(endpoint)
                 .credentials(accessKey, secretKey)
                 .build();
    }
}
