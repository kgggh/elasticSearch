package com.test.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

@Service
public class EsService {
	final String severName = "localhost";
	final int port = 9200;
	final String domain = "http";
	
	RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost(severName, port, domain))); 
	
	public Map<String, Object> listOne(String id) throws IOException{
		GetSourceRequest GetSourceRequest = new GetSourceRequest("test", id); //index,id 값 입력
		GetSourceResponse response =
			    client.getSource(GetSourceRequest, RequestOptions.DEFAULT);
		Map<String, Object> source = response.getSource();
		return source;
	}
	
	public Map<String, Object> listAll() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		SearchResponse response = client.search(new SearchRequest("test"), RequestOptions.DEFAULT);
		for (SearchHit hit : response.getHits()) {
		    list.add(hit.getSourceAsMap());
		}
		map.put("test", list);
		return map;
	}

}
