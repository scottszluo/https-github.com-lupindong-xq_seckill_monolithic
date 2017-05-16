package net.lovexq.background.special.model;

import net.lovexq.seckill.common.model.BasicModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * 特价秒杀
 *
 * @author LuPindong
 * @time 2017-04-19 23:35
 */

@Entity
@Table(name = "special_order")
public class SpecialOrderModel extends BasicModel implements Serializable {
    @Id
    private Long id;              //主键

    private Long stockId;       //编号x
    private String account;       //用户账号
    private String state;       //状态


    public SpecialOrderModel() {
    }

    public SpecialOrderModel(Long id) {
        this.id = id;
    }

    public SpecialOrderModel(Long id, Long stockId, String account) {
        this.id = id;
        this.stockId = stockId;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialOrderModel)) return false;
        SpecialOrderModel that = (SpecialOrderModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpecialOrderModel{");
        sb.append("id=").append(id);
        sb.append(", stockId='").append(stockId).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append('}');
        return sb.toString();
    }
}