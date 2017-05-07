package net.lovexq.seckill.kernel.model;

import net.lovexq.seckill.common.model.BasicModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * SysUser - 系统配置
 *
 * @author LuPindong
 * @date 2015年9月23日 下午9:54:35
 */
@Entity
@Table(name = "sys_config")
public class SysConfigModel extends BasicModel implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11, updatable = false)
    private Integer id;

    /**
     * 关键词
     */
    @Column(name = "config_key", length = 50, nullable = false, unique = true)
    private String configKey;

    /**
     * 内容
     */
    @Column(name = "config_value", length = 200)
    private String configValue;

    public SysConfigModel() {
        super();
    }

    public SysConfigModel(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SysConfigModel)) return false;
        SysConfigModel that = (SysConfigModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(configKey, that.configKey) &&
                Objects.equals(configValue, that.configValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configKey, configValue);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SysConfigModel{");
        sb.append("id=").append(id);
        sb.append(", configKey='").append(configKey).append('\'');
        sb.append(", configValue='").append(configValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}