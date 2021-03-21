package com.test.crawling;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class StockVO {
	private String ranking;
	private String name;
	private String searchRate;
	private String presentPrice;
	private String fullTime;
	private String fluctuationRate;
	private String price;
	private String up;
	private String down;
	private String per;
	private String roe;
}

