package com.mine.date.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mine.date.model.DateState;

/**
 * 排序
 * @author zoom
 *
 */
public class SortUtils {
	private static Logger log = LoggerFactory.getLogger(SortUtils.class);
	public static void sortDateState(List<DateState> dates){
		Collections.sort(dates, new Comparator<DateState>(){

			@Override
			public int compare(DateState o1, DateState o2) {
				return o1.compareTo(o2);
			}
			
		});
	}
}
