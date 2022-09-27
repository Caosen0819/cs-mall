package com.macro.mall.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @Author Caosen
 * @Date 2022/9/22 10:34
 * @Version 1.0
 */
@Configuration
public class EsUtilConfigClint2 {

    @Value("${elasticsearch.hosts}")
    private String hosts;

    private HttpHost[] getHttpHost(){
        if (hosts.length() > 0){
            System.out.println(hosts);

        }
        else {
            throw new RuntimeException("invalid");
        }
        String[] hosts_array = hosts.split(",");
        //用string类型创建host的集合

        HttpHost[] httpHosts = new HttpHost[hosts_array.length];

        int i = 0;
        for (String s : hosts_array) {
            //这里解析端口
            String[] hosts_array_in = s.split(":");
            //到这里就有了id和端口两个东西
            HttpHost http = new HttpHost(hosts_array_in[0], Integer.parseInt(hosts_array_in[1]), "http");
            httpHosts[i++] = http;

        }
        System.out.println("目前的配置加入了" + i + "个id及其端口");
        return httpHosts;
    }

    /**
     * 客户端
     * @return
     * @throws IOException
     */
    @Bean
    public ElasticsearchClient configClint() throws IOException {
        // Create the low-level client
        HttpHost[] httpHosts = getHttpHost();
        RestClient restClient = RestClient.builder(httpHosts).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // 客户端
        ElasticsearchClient client = new ElasticsearchClient(transport);

        return client;
    }
}
