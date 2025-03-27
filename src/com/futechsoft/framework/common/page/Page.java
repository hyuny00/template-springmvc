package com.futechsoft.framework.common.page;

import java.util.List;

public class Page<T> {

	private Pageable pageable;

	private List<T> list;

	public Page(Pageable pageable, List<T> list, long count) {
		this.pageable = pageable;
		this.list = list;

		pageable.setTotalCount(count);

	}

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
