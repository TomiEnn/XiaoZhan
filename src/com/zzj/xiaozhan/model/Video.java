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
	/**
	 * 视屏网络地址
	 */
	public String videoUrl;
	/**
	 * 解析后播放地址
	 */
	public String playVideoUrl;
	/**
	 * 文件大小
	 */
	private String size;
	
	public String getPlayVideoUrl() {
		return playVideoUrl;
	}
	public void setPlayVideoUrl(String playVideoUrl) {
		this.playVideoUrl = playVideoUrl;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
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
