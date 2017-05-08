package net.lovexq.background.crawler.model;

import net.lovexq.seckill.common.model.BasicModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * 检查记录
 *
 * @author LuPindong
 * @time 2017-04-26 19:36
 */
@Entity
@Table(name = "crawler_record")
public class CrawlerRecordModel extends BasicModel implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String batch;
    private String historyCode;
    private String currentCode;
    private String state;
    private byte[] data;

    public CrawlerRecordModel() {
    }

    public CrawlerRecordModel(String batch, String currentCode, String state) {
        this.batch = batch;
        this.currentCode = currentCode;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getHistoryCode() {
        return historyCode;
    }

    public void setHistoryCode(String historyCode) {
        this.historyCode = historyCode;
    }

    public String getCurrentCode() {
        return currentCode;
    }

    public void setCurrentCode(String currentCode) {
        this.currentCode = currentCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CrawlerRecordModel)) return false;
        CrawlerRecordModel that = (CrawlerRecordModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CrawlerRecordModel{");
        sb.append("id=").append(id);
        sb.append(", batch='").append(batch).append('\'');
        sb.append('}');
        return sb.toString();
    }
}