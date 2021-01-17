package ru.dosov.restvoting.config;

import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.time.LocalTime;

@Configuration

//https://docs.spring.io/spring-boot/docs/2.4.1/reference/html/appendix-configuration-metadata.html#configuration-metadata-annotation-processor-setup
@EnableConfigurationProperties
@ConfigurationProperties("appattributes")
public class AppConfig {

    private String baseurl;

    public static LocalTime deadLine;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public static LocalTime getDeadLine() {
        return deadLine;
    }

    public static void setDeadLine(LocalTime deadLine) {
        AppConfig.deadLine = deadLine;
    }
}
