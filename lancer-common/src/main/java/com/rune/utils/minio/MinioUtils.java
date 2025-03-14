package com.rune.utils.minio;

import com.rune.exception.BadRequestException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yyk-sun
 * @date 2024/03/22 16:43
 * @description TODO
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtils {

    private final MyMinioClient myMinioClient;

    /**
     * 替换乱码文件名称
     *
     * @param url         完整路径
     * @param newFileName 替换的名字
     * @param bucketName  桶名称
     * @return
     */
    public static String replaceFileName(String url, String newFileName, String bucketName) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String needReplace = "/" + bucketName + "/";
            String oldFileName = path.replace(needReplace, "");
            String newPath = path.replace(oldFileName, newFileName);
            URI newUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), newPath, uri.getQuery(), uri.getFragment());
            return newUri.toString();
        } catch (Exception e) {
            throw new BadRequestException("文件上传失败");
        }
    }

    /**
     * 获取路径下文件名
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称 类似 like 'xxx%'
     * @param prefix     文件路径前缀
     * @return
     */
    public List<String> listObjects(String bucketName, String objectName, String prefix) {
        ArrayList<String> strings = new ArrayList<>();
        try {
            ListObjectsArgs.Builder builder = ListObjectsArgs.builder();
            builder.bucket(bucketName);
            if (StringUtils.isNotBlank(prefix)) {
                builder.prefix(prefix);
                builder.keyMarker(prefix + objectName);
            } else {
                builder.keyMarker(objectName);
            }
            ListObjectsArgs objectsArgs = builder.build();
            Iterable<Result<Item>> results = myMinioClient.listObjects(objectsArgs);
            for (Result<Item> result : results) {
                strings.add(result.get().objectName());
            }
        } catch (Exception e) {
            log.error("获取minio文件列表失败：{}", e.getMessage());
            throw new BadRequestException("获取文件列表失败");
        }
        return strings;
    }

    /**
     * 获取预签名上传/下载URL
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称
     * @return
     */
    public String getObjectUrl(String bucketName, String objectName, String method) {
        String url;
        try {
            url = myMinioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method((Objects.equals(method, "GET")) ? Method.GET : Method.PUT)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(3000, TimeUnit.SECONDS)
                            .build());
        } catch (Exception e) {
            throw new BadRequestException("文件上传/下载失败");
        }
        return replaceFileName(url, objectName, bucketName);
    }

    /**
     * 初始化分片
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称
     * @param totalPart  分片数量
     * @return
     */
    public Map<String, Object> initMultiPartUpload(String bucketName, String objectName, int totalPart) {
        Map<String, Object> result = new HashMap<>();
        String uploadId = myMinioClient.initMultiPartUpload(bucketName, null, objectName, null, null);
        result.put("uploadId", uploadId);
        List<String> partList = new ArrayList<>();
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        for (int i = 1; i <= totalPart; i++) {
            reqParams.put("partNumber", String.valueOf(i));
            String uploadUrl;
            try {
                uploadUrl = myMinioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(bucketName)
                                .object(objectName)
                                .expiry(1, TimeUnit.DAYS)
                                .extraQueryParams(reqParams)
                                .build());
            } catch (Exception e) {
                throw new BadRequestException("文件上传失败");
            }
            partList.add(uploadUrl);
        }
        result.put("uploadUrls", partList);
        result.put("fileFullName", objectName);
        return result;
    }

    /**
     * 文件合并
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称
     * @param uploadId   分片Id
     * @param totalPart  分片数量
     */
    public void mergeMultipartUpload(String bucketName, String objectName, String uploadId, Integer totalPart) {
        try {
            Part[] parts = new Part[totalPart];
            ListPartsResponse partResult = myMinioClient.listMultipart(bucketName, null, objectName, totalPart, 0, uploadId, null, null).join();
            int partNumber = 1;
            for (Part part : partResult.result().partList()) {
                parts[partNumber - 1] = new Part(partNumber, part.etag());
                partNumber++;
            }
            myMinioClient.mergeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
        } catch (Exception e) {
            throw new BadRequestException("文件上传失败");
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称
     * @throws Exception
     */
    public void removeObject(String bucketName, String objectName) {
        try {
            myMinioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param bucketName       桶名称
     * @param sourceBucketName 源桶名称
     * @param objectName       文件名称
     */
    public void copyObject(String bucketName, String sourceBucketName, String objectName) {
        try {
            myMinioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .source(
                                    CopySource.builder()
                                            .bucket(sourceBucketName)
                                            .object(objectName)
                                            .build())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
