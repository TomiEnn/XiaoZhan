package com.zzj.xiaozhan.model;

/**
 * 视屏类
 * 
 * @author Tomi_Enn
 * 
 */
public class Video {
	
	
	/**
	 * 视屏序号
	 */
	public String number;
	/**
	 * 视屏名字
	 */
	public String title;
	/**
	 * 发布时间
	 */
	public String time;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
