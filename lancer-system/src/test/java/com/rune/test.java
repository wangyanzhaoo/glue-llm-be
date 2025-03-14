package com.rune;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;


@SpringBootTest
public class test {

    @Resource
    StringEncryptor encryptor;

    @Test
    void contextLoads() {
        jacketEncrypt();
    }

    @Test
    void jacketEncrypt() {
        //加密
        String password = encryptor.encrypt("Mysql123.");
        System.out.println("mysql-password-密文: " + password);
        String redisPassword = encryptor.encrypt("Redis123.");
        System.out.println("redis-密文: " + redisPassword);
        String minioPassword = encryptor.encrypt("Minio123.");
        System.out.println("minio-密文: " + minioPassword);
    }
}
