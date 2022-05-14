package com.spring.example.config;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class ExchangeServiceConfig {

    ExchangeService service;
    @Autowired
    Environment environment;

    @Bean
    public ExchangeService configureExchangeService() throws URISyntaxException {
        service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        service.setUrl(new URI(environment.getProperty("exchange.server.url")));

        ExchangeCredentials credentials = new WebCredentials(environment.getProperty("exchange.mail.username"), environment.getProperty("exchange.mail.password"), environment.getProperty("exchange.server.domain"));
        service.setCredentials(credentials);

        return service;
    }
}
