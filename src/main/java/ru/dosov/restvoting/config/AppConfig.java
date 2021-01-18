package ru.dosov.restvoting.config;

import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.dosov.restvoting.util.DateTimeFormatter;

import java.sql.SQLException;
import java.time.LocalTime;

@Configuration
//https://docs.spring.io/spring-boot/docs/2.4.1/reference/html/appendix-configuration-metadata.html#configuration-metadata-annotation-processor-setup
@EnableConfigurationProperties
@ConfigurationProperties("appattributes")
public class AppConfig {

    private String baseurl;

    public final static LocalTime DEAD_LINE = LocalTime.parse("11:00:00");

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    @Configuration
    static class FormatterConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatter(new DateTimeFormatter());
        }
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}
