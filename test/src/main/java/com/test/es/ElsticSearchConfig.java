package com.test.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

@Component
public class ElsticSearchConfig {
	final String severName = "localhost";
	final int port = 9200;
	final String domain = "http";
	
	@SuppressWarnings("unused")
	public void connect() {
	RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost(severName, port, domain))); 
	}
}
