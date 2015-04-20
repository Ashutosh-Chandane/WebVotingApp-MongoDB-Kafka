package votingsystem;

import com.mongodb.MongoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.Arrays;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.data.authentication.UserCredentials;

/**
 * Created by ASHU on 02-03-2015.
 */

@ComponentScan(basePackages = {"votingsystem"})
@EnableAutoConfiguration
@EnableScheduling
@EnableMongoRepositories
public class ApplicationConf extends AbstractMongoConfiguration {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationConf.class, args);
    }

    @Override
    protected String getDatabaseName() {
        return  "cmpe273";
    }

    @Override
    public MongoClient mongo() throws Exception {
        MongoCredential credential = MongoCredential.createCredential("ashu", "cmpe273", "ashu".toCharArray());
        MongoClient mclient =  new MongoClient(new ServerAddress("localhost",27017), Arrays.asList(credential));
        return mclient;
    }

    @Override
    protected String getMappingBasePackage() {
        return "votingsystem.domain";
    }

    @Bean
    MongoMappingContext springDataMongoMappingContext() {
        return new MongoMappingContext();
    }
}
