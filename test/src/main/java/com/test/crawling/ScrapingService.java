package com.test.crawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ScrapingService {
	private static String KOREA_COVID_DATAS_URL = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13";
	
	public List<StockVO> stockList() throws Exception {
		List<StockVO> stockList = new ArrayList<>();
		
		String URL = "https://finance.naver.com/sise/lastsearch2.nhn";
		Document doc = Jsoup.connect(URL).get();
		
		Elements contents = doc.select("table[class=type_5] tbody");
		
		for(Element content : contents) {	
			Elements tdContents = content.select("td");
			
			StockVO stockVO = StockVO.builder()
					.ranking(tdContents.get(1).text())
					.name(tdContents.get(2).text())
					.searchRate(tdContents.get(2).text())
					.presentPrice(tdContents.get(3).text())
					.fullTime(tdContents.get(4).text())
					.fluctuationRate(tdContents.get(5).text())
					.price(tdContents.get(6).text())
					.up(tdContents.get(7).text())
					.down(tdContents.get(8).text())
					.per(tdContents.get(9).text())
					.roe(tdContents.get(10).text())
					.build();
			
			stockList.add(stockVO);
		}
		return stockList;
	}
	
	public List<KoreaStats> getKoreaCovidDatas() throws IOException {
		List<KoreaStats> koreaStatsList = new ArrayList<>();
        Document doc = Jsoup.connect(KOREA_COVID_DATAS_URL).get();
        Elements contents = doc.select("table tbody tr");
        for(Element content : contents){
            Elements tdContents = content.select("td");

            KoreaStats koreaStats = KoreaStats.builder()
                    .country(content.select("th").text())
                    .diffFromPrevDay((tdContents.get(0).text()))
                    .total((tdContents.get(1).text()))
                    .death((tdContents.get(2).text()))
                    .incidence((tdContents.get(3).text()))
                    .inspection((tdContents.get(4).text()))
                    .build();

            koreaStatsList.add(koreaStats);
        }
		return koreaStatsList;
  }

}
