package com.test.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.test.es.EsService;

@RestController
public class TestController {
	
	@Autowired
	private EsService ESS;

	@GetMapping("/")
	public String test() {
		
		return "hi";
	}
	
	@GetMapping("/get1")
	public Map<String, Object> get1() throws IOException{
	    Map<String, Object> map = new HashMap<String, Object>();
	    
	    RestHighLevelClient client = new RestHighLevelClient(
	            RestClient.builder(
	                    new HttpHost("localhost", 9200, "http")));
	    
	    GetRequest request = new GetRequest("test", "1");
	    try {
	        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
	        System.out.println(">>>>>>>>>>성공");
	        map.put("test", getResponse);
	    } catch (ElasticsearchException e) {
	        if (e.status() == RestStatus.NOT_FOUND) {
	            System.out.println(">>>>>>>>>실패");
	        }
	    }
	    
		return map;
	}
	
	//return map source 긁어오기
	@GetMapping("/get2/{id}")
	public  Map<String, Object> get2(@PathVariable("id") String id) throws IOException{
		int port = 9200;
		RestHighLevelClient client = new RestHighLevelClient(
	            RestClient.builder(
	                    new HttpHost("localhost", port, "http"))); //해당 severname,port 여기로?
		GetSourceRequest GetSourceRequest = new GetSourceRequest("test", id); //index,id 값 입력
		GetSourceResponse response =
			    client.getSource(GetSourceRequest, RequestOptions.DEFAULT);
		Map<String, Object> source = response.getSource();
		return source;
	}
	
	
	//전체 소스 리스트만 긁어오기
	@GetMapping("getAll")
	public Map<String, Object> getAll() throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		RestHighLevelClient client = new RestHighLevelClient(
	            RestClient.builder(
	                    new HttpHost("localhost", 9200, "http")));
		List<Object> list = new ArrayList<Object>();
		SearchResponse response = client.search(new SearchRequest("test"), RequestOptions.DEFAULT);
		for (SearchHit hit : response.getHits()) {
		    list.add(hit.getSourceAsMap());
		}
		map.put("test", list);
		return map; 
	}
	
	
	//비즈니스로직 분리해서 로컬 테스트
	
	//인덱스 id로 조회
	@GetMapping("serviceTest/{id}")
	public Map<String, Object> serviceTest(@PathVariable("id") String id) throws IOException{
		Map<String, Object> list = new HashMap<String, Object>();
		ESS.listOne(id);
		list.put("list", ESS.listOne(id));
		return list;
	}
	
	//해당 인덱스 데이터 전체조회
	@GetMapping("serviceTest2")
	public Map<String, Object> serviceTest2() throws IOException{
	return ESS.listAll();
	}
	
	//field : value 검색 기능
	@GetMapping("serviceTest3/{name}")
	public Map<String, Object> serviceTest3(@PathVariable("name") String name) throws IOException{
		return ESS.listAll2(name);
	}
	
	
	//field : value로 다중 검색 기능
	@GetMapping("serviceTest4")
	public Map<String, Object> serviceTest4(String name1,String name2) throws IOException{
		name1 ="김건희555555";
		name2 ="김건희4";
		return ESS.listAll3(name1,name2);
	}
	
	
}
	
