package com.d205.sdutyplus.global.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String KEY;
    private static final String ALGORITHM = "PBEWithMD5AndDES";

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(KEY);
        config.setPoolSize(1);//인스턴스 수 = 프로젝트 1개만 할 거임
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations("1000");//반복할 해싱 횟수
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");//salt 생성 클래스 지정
        config.setStringOutputType("base64");//인코딩

        encryptor.setConfig(config);

        return encryptor;
    }
}