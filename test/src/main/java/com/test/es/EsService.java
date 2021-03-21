package com.test.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

@Service
public class EsService {
	final String severName = "127.0.0.1";
	final int port = 9200;
	final String domain = "http";
	
	RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost(severName, port, domain))); 
	
	//인덱스 id로 조회(1개)
	
	public Map<String, Object> listOne(String id) throws IOException{
		GetSourceRequest GetSourceRequest = new GetSourceRequest("test", id); //index,id 값 입력
		GetSourceResponse response =
			    client.getSource(GetSourceRequest, RequestOptions.DEFAULT);
		Map<String, Object> source = response.getSource();
		return source;
	}
	
	//인덱스 데이터 전체조회
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
	
	//field : value 검색 가능
	public Map<String, Object> listAll2(String name) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		SearchRequest searchRequest = new SearchRequest("test"); 
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.termQuery("name", name));
		sourceBuilder.from(0); 
		sourceBuilder.size(10);
		searchRequest.source(sourceBuilder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		searchResponse.getHits().getHits();
		for(SearchHit s:searchResponse.getHits().getHits())
		  {
			list.add(s.getSourceAsMap());
		  }
		map.put("test", list);
		System.out.println(">>>>>>"+list);
		return map;
	}
	
	//다중검색
	public Map<String, Object> listAll3(String name1,String name2) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		
		MultiSearchRequest requests = new MultiSearchRequest();
		
		SearchRequest searchRequest = new SearchRequest("test"); 
		SearchRequest searchRequest2 = new SearchRequest("test");
		
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.termQuery("name", name1));
		searchRequest.source(sourceBuilder);
		
		SearchSourceBuilder sourceBuilder2 = new SearchSourceBuilder();
		sourceBuilder2.query(QueryBuilders.termQuery("name", name2));
		searchRequest2.source(sourceBuilder2);
		
		requests.add(searchRequest);
		requests.add(searchRequest2);
		
		MultiSearchResponse searchResponse = client.msearch(requests, RequestOptions.DEFAULT);
		for(Item i : searchResponse.getResponses())
		  {
			for(SearchHit s:i.getResponse().getHits().getHits()) {
				Map<String, Object>
				  sourceMap = s.getSourceAsMap();
				  list.add(sourceMap);
				}
		  }
		map.put("test", list);
		return map;
	
	}

}
