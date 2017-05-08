package net.lovexq.seckill.kernel.model;

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
@Table(name = "special_killed")
public class SpecialKilledModel extends BasicModel implements Serializable {
    @Id
    private Long id;              //主键

    private String houseCode;       //编号x
    private String account;       //用户账号
    private String state;       //状态


    public SpecialKilledModel() {
    }

    public SpecialKilledModel(Long id) {
        this.id = id;
    }

    public SpecialKilledModel(Long id, String houseCode, String account) {
        this.id = id;
        this.houseCode = houseCode;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
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
        if (!(o instanceof SpecialKilledModel)) return false;
        SpecialKilledModel that = (SpecialKilledModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpecialKilledModel{");
        sb.append("id=").append(id);
        sb.append(", houseCode='").append(houseCode).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append('}');
        return sb.toString();
    }
}