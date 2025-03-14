package com.rune.datasource;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

public class MultiTransactionStatus implements TransactionStatus {

    private final List<TransactionStatus> statuses = new ArrayList<>();
    private final List<PlatformTransactionManager> transactionManagers;
    private final TransactionDefinition definition;


    public MultiTransactionStatus(List<PlatformTransactionManager> transactionManagers, TransactionDefinition definition) {
        this.transactionManagers = transactionManagers;
        this.definition = definition;

        TransactionSynchronizationManager.initSynchronization();

        for (PlatformTransactionManager transactionManager : transactionManagers) {
            statuses.add(transactionManager.getTransaction(definition));
        }
    }

    public void commit() {
        for (int i = 0; i < statuses.size(); i++) {
            transactionManagers.get(i).commit(statuses.get(i));
        }
    }

    public void rollback() {
        for (int i = statuses.size() - 1; i >= 0; i--) { // 逆序回滚
            transactionManagers.get(i).rollback(statuses.get(i));
        }
    }

    @Override
    public boolean isNewTransaction() {
        return statuses.stream().allMatch(TransactionStatus::isNewTransaction);
    }

    @Override
    public boolean hasSavepoint() {
        return statuses.stream().anyMatch(TransactionStatus::hasSavepoint);
    }

    @Override
    public void setRollbackOnly() {
        statuses.forEach(TransactionStatus::setRollbackOnly);
    }

    @Override
    public boolean isRollbackOnly() {
        return statuses.stream().anyMatch(TransactionStatus::isRollbackOnly);
    }

    @Override
    public void flush() {
        statuses.forEach(TransactionStatus::flush);
    }

    @Override
    public boolean isCompleted() {
        return statuses.stream().allMatch(TransactionStatus::isCompleted);
    }

    @Override
    public Object createSavepoint() throws TransactionException {
        return null;
    }

    @Override
    public void rollbackToSavepoint(Object savepoint) throws TransactionException {

    }

    @Override
    public void releaseSavepoint(Object savepoint) throws TransactionException {

    }
}

