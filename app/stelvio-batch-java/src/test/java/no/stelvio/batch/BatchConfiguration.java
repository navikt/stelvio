package no.stelvio.batch;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import no.stelvio.batch.controller.support.DefaultBatchControllerService;
import no.stelvio.batch.domain.BatchDO;
import no.stelvio.batch.domain.BatchHistDO;
import no.stelvio.batch.repository.support.HibernateBatchHistRepository;
import no.stelvio.batch.repository.support.HibernateBatchRepository;
import no.stelvio.batch.support.ControllerServiceHistorySupport;
import no.stelvio.batch.support.ControllerServiceHistorySupportTest;

@Configuration

public class BatchConfiguration {

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(testBatchDataSource());
        //sessionFactory.setPackagesToScan();
        sessionFactory.setAnnotatedClasses(BatchDO.class, BatchHistDO.class);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
                setProperty("hibernate.hbm2ddl.auto", "update");
                setProperty("hibernate.current_session_context_class", "thread");
                setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.internal.NoCachingRegionFactory");
                setProperty("hibernate.transaction.factory_class", "org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory");
                setProperty("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform");
            }
        };
    }

    @Bean
    public DataSource testBatchDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:mydb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public DummyBatchImplementation dummyBatch() {
        return new DummyBatchImplementation();
    }

    @Bean
    DummyBatchWithInputParameters dummyBatchWithInputParameters() {
        DummyBatchWithInputParameters dummy = new DummyBatchWithInputParameters();
        dummy.setTimeToRun(1);
        dummy.setExitCode(8);
        return dummy;
    }

    @Bean
    HibernateTemplate hibernateTemplate() {
        HibernateTemplate template = new HibernateTemplate();
        template.setSessionFactory((SessionFactory) sessionFactory());
        return template;
    }

    @Bean
    @Scope("prototype")
    HibernateBatchHistRepository batchHistRepository() {
        HibernateBatchHistRepository repo = new HibernateBatchHistRepository();
        repo.setHibernateTemplate(hibernateTemplate());
        return repo;
    }

    @Bean
    @Scope("prototype")
    HibernateBatchRepository batchRepository() {
        HibernateBatchRepository repo = new HibernateBatchRepository();
        repo.setHibernateTemplate(hibernateTemplate());
        return repo;
    }

    @Bean
    public ControllerServiceHistorySupportTest controllerServiceHistorySupportTest() {
        ControllerServiceHistorySupportTest supportTest = new ControllerServiceHistorySupportTest();
        supportTest.setHistRepository(batchHistRepository());
        supportTest.setBatchControllerService(defaultBatchControllerService());
        supportTest.setControllerServiceHistorySupport(controllerServiceHistorySupport());
        supportTest.setReader(batchParameterReader());
        supportTest.setBatchRepo(batchRepository());
        return supportTest;
    }

    @Bean
    public DefaultBatchControllerService defaultBatchControllerService() {
        DefaultBatchControllerService service = new DefaultBatchControllerService();
        service.setControllerServiceHistorySupport(controllerServiceHistorySupport());
        service.setBatchNameMap(batchNameMapper());
        return service;
    }

    @Bean
    public Map<String, Class> batchNameMapper() {
        HashMap<String, Class> mapper = new HashMap<>();
        mapper.put("btc.testbatch.dummyBatch", DummyBatchImplementation.class);
        mapper.put("btc.testbatch.dummyBatchWithInputParameters", DummyBatchWithInputParameters.class);
        return mapper;
    }

    @Bean
    public ControllerServiceHistorySupport controllerServiceHistorySupport() {
        ControllerServiceHistorySupport historySupport = new ControllerServiceHistorySupport();
        historySupport.setRepository(batchHistRepository());
        historySupport.setReader(batchParameterReader());
        return historySupport;
    }

    @Bean
    public StelvioBatchParameterReader batchParameterReader() {
        StelvioBatchParameterReader reader = new StelvioBatchParameterReader();
        reader.setBatchRepository(batchRepository());
        return reader;
    }
}


