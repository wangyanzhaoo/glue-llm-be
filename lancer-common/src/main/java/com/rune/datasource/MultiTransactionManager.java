package com.rune.datasource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

public class MultiTransactionManager implements PlatformTransactionManager {

    private final List<PlatformTransactionManager> transactionManagers;

    public MultiTransactionManager(PlatformTransactionManager... transactionManagers) {
        this.transactionManagers = List.of(transactionManagers);
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        return new MultiTransactionStatus(transactionManagers, definition);
    }

    @Override
    public void commit(TransactionStatus status) {
        if (status instanceof MultiTransactionStatus multiStatus) {
            multiStatus.commit();
        }
    }

    @Override
    public void rollback(TransactionStatus status) {
        if (status instanceof MultiTransactionStatus multiStatus) {
            multiStatus.rollback();
        }
    }
}

