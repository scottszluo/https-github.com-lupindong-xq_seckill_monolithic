package net.lovexq.seckill.kernel.model;

import net.lovexq.seckill.common.model.BasicModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 检查记录
 *
 * @author LuPindong
 * @time 2017-04-26 19:36
 */
@Entity
@Table(name = "check_record")
public class CheckRecordModel extends BasicModel {
    @Id
    @GeneratedValue
    private Long id;
    private String batch;
    private String historyCode;
    private String currentCode;
    private Integer status; // -1初始状态,0删除->下架,1更新-放盘,2插入-放盘
    private byte[] data;

    public CheckRecordModel() {
    }

    public CheckRecordModel(String batch, String currentCode, Integer status) {
        this.batch = batch;
        this.currentCode = currentCode;
        this.status = status;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(o instanceof CheckRecordModel)) return false;
        CheckRecordModel that = (CheckRecordModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckRecordModel{");
        sb.append("id=").append(id);
        sb.append(", batch='").append(batch).append('\'');
        sb.append('}');
        return sb.toString();
    }
}