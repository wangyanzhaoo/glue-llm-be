package com.rune.datasource;

import com.rune.annotation.PrimaryEntity;
import com.rune.annotation.PrimaryRepositoryMarker;
import com.rune.annotation.SecondaryRepositoryMarker;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.rune",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = PrimaryRepositoryMarker.class
        ),
        excludeFilters = @ComponentScan.Filter(       // 明确排除其他数据源的 Repository
                type = FilterType.ANNOTATION,
                classes = SecondaryRepositoryMarker.class
        ),
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDataSourceConfig {

    private List<Class<?>> getPrimaryEntities() {
        List<Class<?>> entities = EntityScanner.scanEntitiesWithAnnotation("com.rune", PrimaryEntity.class);
        System.out.println("Primary entities: " + entities);
        return entities;
    }

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("primaryDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = builder
                .dataSource(dataSource)
                .persistenceUnit("primary")
                .build();
        List<Class<?>> primaryEntities = getPrimaryEntities();
        em.setPackagesToScan(primaryEntities.stream().map(Class::getPackageName).toArray(String[]::new));
        return em;
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
