package com.example.elasticsearch.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class Config {
    Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${elasticsearch.host}")
    public String host;
    @Value("${elasticsearch.port}")
    public int port;
    @Value("${elasticsearch.clusterName}")
    public String clusterName;

    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
    @Bean
    public Client client(){
        TransportClient client = null;
        try{
            logger.info("Connecting To elasticsearch at {}:{}, Cluster name {}", host, port, clusterName);
            Settings settings = Settings.builder()
                    .put("cluster.name", clusterName).build();

            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
