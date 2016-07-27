package com.zzu.ehome.bean;

public class Image {
	private String url;
    private int width;
    private int height;
    private int position;

    public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Image(String url, int width, int height, int position) {
        this.url = url;
        this.width = width;
        this.height = height;
       this.position=position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {

        return "image---->>url="+url+"width="+width+"height"+height;
    }
}
