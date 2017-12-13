package com.mine.date.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mine.date.model.DateState;

/**
 * 缓存
 * @author zoom
 *
 */
public class CacheService {
	private static CacheService cache;
	private CacheService(){
		
	}
	public static CacheService getInstance(){
		if(cache == null){
			cache = new CacheService(); 
		}
		return cache;
	}
	public void deleteAllCache(){
		deleteDateStateCache();
	}
	public void deleteDateStateCache(){
		queryDateState.clear();
		queryDateStates.clear();
	}
	private Map<String, DateState> queryDateState = new ConcurrentHashMap<>();
	private Map<String, List<DateState>> queryDateStates = new ConcurrentHashMap<>();
	public Map<String, DateState> getQueryDateState() {
		return queryDateState;
	}
	public void setQueryDateState(Map<String, DateState> queryDateState) {
		this.queryDateState = queryDateState;
	}
	public Map<String, List<DateState>> getQueryDateStates() {
		return queryDateStates;
	}
	public void setQueryDateStates(Map<String, List<DateState>> queryDateStates) {
		this.queryDateStates = queryDateStates;
	}
	
	
}
