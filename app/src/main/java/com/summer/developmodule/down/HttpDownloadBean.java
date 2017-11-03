package com.summer.developmodule.down;

/**
 * Created by yujunlong on 2017/2/24.
 */

public class HttpDownloadBean {
    private String url = null;//下载url
    private String storagepath = null;//存储位置
    private String filepath = null;//文件路径
    private long current_length = 0L;//当前进度
    private long total_length = 0L;//总共进度

    public long getTotal_length() {
        return total_length;
    }

    public void setTotal_length(long total_length) {
        this.total_length = total_length;
    }

    public long getCurrent_length() {
        return current_length;
    }

    public void setCurrent_length(long current_length) {
        this.current_length = current_length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStoragepath() {
        return storagepath;
    }

    public void setStoragepath(String storagepath) {
        this.storagepath = storagepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String path) {
        this.filepath = path;
    }
}
