package com.zzu.ehome.bean;

public class RefreshEvent {
	private int refreshWhere;
	private int refreshId;

	public RefreshEvent(int refreshWhere) {
		this.refreshWhere = refreshWhere;
	}
	
	public RefreshEvent(int refreshWhere, int id) {
		this.refreshWhere = refreshWhere;
		this.refreshId = id;
	}
	
	public int getRefreshWhere() {
		return refreshWhere;
	}

	public void setRefreshWhere(int refreshWhere) {
		this.refreshWhere = refreshWhere;
	}
	
	public int getRefreshId() {
		return refreshId;
	}

	public void setRefreshId(int refreshId) {
		this.refreshId = refreshId;
	}
	
}
