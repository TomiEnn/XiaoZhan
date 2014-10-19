package com.zzj.xiaozhan.model;

import java.util.List;

/**
 * 卡片布局中的内容
 */
public class Card {

	/**
	 * 图片的地址
	 */
	private String imageUrl;
	/**
	 * 发布人的名字
	 */
	private String name;
	/**
	 * 发布人的头像
	 */
	private String userImageUrl;
	/**
	 * 文章的标题
	 */
	private String title;
	/**
	 * 文章的内容
	 */
	private String content;
	/**
	 * 跳转链接地址
	 */
	private String webUrl;
	/**
	 * 额外的内容
	 */
	private String more;
	
	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
