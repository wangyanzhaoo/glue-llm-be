package com.rune.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionManagerConfig {

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("primaryTransactionManager") PlatformTransactionManager txManager1,
            @Qualifier("secondaryTransactionManager") PlatformTransactionManager txManager2) {
        return new MultiTransactionManager(txManager1, txManager2);
    }
}
