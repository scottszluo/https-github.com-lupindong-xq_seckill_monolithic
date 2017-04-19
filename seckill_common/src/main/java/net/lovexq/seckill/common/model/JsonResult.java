package net.lovexq.seckill.common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * JsonResult - JSON传输数据模型
 *
 * @author LuPindong
 * @date 2016年3月22日 上午11:00:12
 */
public class JsonResult implements Serializable {

    private static final long serialVersionUID = -8445014037130200665L;

    private Integer status = 200;// 状态码

    private String message = "操作成功！";// 消息

    private Object data;// 数据

    private Long timestamp = System.currentTimeMillis();

    public JsonResult() {
        super();
    }

    public JsonResult(Object data) {
        super();
        this.data = data;
    }

    public JsonResult(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public JsonResult(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonResult)) return false;
        JsonResult that = (JsonResult) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(message, that.message) &&
                Objects.equals(data, that.data) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, data, timestamp);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JsonResult{");
        sb.append("status=").append(status);
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append(", timestamp=").append(timestamp);
        sb.append('}');
        return sb.toString();
    }
}
