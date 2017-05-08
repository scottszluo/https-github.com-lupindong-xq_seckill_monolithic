package net.lovexq.seckill.common.model;

import net.lovexq.seckill.common.LocalDateTimeType;
import net.lovexq.seckill.common.utils.TimeUtil;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@TypeDefs(
        {
                @TypeDef(
                        name = "localDateTimeType",
                        defaultForType = LocalDateTimeType.class,
                        typeClass = LocalDateTimeType.class
                )
        }
)
@MappedSuperclass
public class BasicModel {

    /**
     * 创建时间
     */
    @Type(type = "localDateTimeType")
    @Column(name = "create_time", nullable = false, insertable = true, updatable = false)
    protected LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @Type(type = "localDateTimeType")
    @Column(name = "update_time", nullable = false, insertable = true, updatable = true)
    protected LocalDateTime updateTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @PrePersist
    public void prePersist() {
        setCreateTime(TimeUtil.nowDateTime());
        setUpdateTime(TimeUtil.nowDateTime());
    }

    @PreUpdate
    public void preUpdate() {
        setUpdateTime(TimeUtil.nowDateTime());
    }
}