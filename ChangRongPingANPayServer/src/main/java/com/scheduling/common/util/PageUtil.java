package com.scheduling.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;



public class PageUtil {
	public PageUtil() {}
    
	/**
	 * 获得页面中的页号信息，放到page对象中
	 * @param <T>
	 * @param request
	 * @return
	 */
	public static <T> Page<T> getPageObj(HttpServletRequest request) {
    	int pageNo = 1;
    	int pageSize = 15;
		String pageIndexName = ServletRequestUtils.getStringParameter(request, "pageNum", "1");
		if (pageIndexName != null && StringUtils.isNotBlank(pageIndexName) && (pageIndexName.indexOf("null")== -1)) {
			pageNo = Integer.parseInt(pageIndexName);
		}
		Page<T> page = new Page<T>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setOrder(ServletRequestUtils.getStringParameter(request, "orderDirection", "asc"));
		page.setOrderBy(ServletRequestUtils.getStringParameter(request, "orderField", ""));
		return page;
    }
}

/**
 * Copyright (c) 2010,天安怡和 All rights reserved.
 */