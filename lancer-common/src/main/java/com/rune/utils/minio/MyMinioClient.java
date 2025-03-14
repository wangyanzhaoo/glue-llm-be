package com.rune.utils.minio;

import com.google.common.collect.Multimap;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.messages.Part;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class MyMinioClient extends MinioAsyncClient {

    public MyMinioClient(MinioAsyncClient client) {
        super(client);
    }

    /**
     * 初始化分片
     * @param bucket 桶
     * @param region /
     * @param object 文件
     * @param headers /
     * @param extraQueryParams /
     * @return
     */
    public  String initMultiPartUpload(String bucket, String region, String object, Multimap<String, String> headers, Multimap<String, String> extraQueryParams){
        try {
            return this.createMultipartUploadAsync(bucket, region, object, headers, extraQueryParams).join().result().uploadId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 合并上传后分片数据
     * @param bucketName 桶名
     * @param region
     * @param objectName 文件名
     * @param uploadId 分片id
     * @param parts /
     * @param extraHeaders /
     * @param extraQueryParams /
     */
    public void mergeMultipartUpload(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams){
        try {
            this.completeMultipartUpload(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询分片上传情况
     * @param bucketName 桶名
     * @param region /
     * @param objectName 文件名称
     * @param maxParts 分片数
     * @param partNumberMarker /
     * @param uploadId 分片id
     * @param extraHeaders /
     * @param extraQueryParams /
     * @return
     */
    public CompletableFuture<ListPartsResponse> listMultipart(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams){
        try {
            return this.listPartsAsync(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
