package pis.socket.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pis.socket.common.MariaDB;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(value = "pis.socket.svc", annotationClass = MariaDB.class, sqlSessionFactoryRef = "mariaSqlSessionFactory")
public class DataSourceConfig {

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("parkingpass2020!");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPoolSize("1");
        encryptor.setConfig(config);
        return encryptor;
    }

    @Bean(name = "mariaDataSource")
    @ConfigurationProperties("spring.datasource.maria")
    public DataSource mariaDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "mariaTransactionManager")
    public PlatformTransactionManager mariaTransactionManager(@Qualifier("mariaDataSource") DataSource mariaDataSource) {
        return new DataSourceTransactionManager(mariaDataSource);
    }

    @Bean(name = "mariaSqlSessionFactory")
    public SqlSessionFactory mariaSqlSessionFactory(@Qualifier("mariaDataSource") DataSource mariaDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(mariaDataSource);
        sessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:**/mapper/maria/*.xml"));
        return sessionFactoryBean.getObject();
    }


}
