package net.lovexq.seckill.kernel.model;

import net.lovexq.seckill.common.model.BasicModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 特价库存
 *
 * @author LuPindong
 * @time 2017-04-19 23:35
 */

@Entity
@Table(name = "special_stock")
public class SpecialStock extends BasicModel {
    @Id
    private Long id;              //主键
    private String title;         //标题x
    private String houseId;       //编号x
    private BigDecimal totalPrice;//总价x
    private BigDecimal unitPrice; //单价x
    private String model;         //房屋户型x
    private String direction;     //房屋朝向x
    private BigDecimal area;      //建筑面积x
    private String floor;         //所在楼层x
    private String decoration;    //装修情况x
    private String buildingAge;   //建筑年代x
    private String saleStatus;     //销售状态：放盘，成交，下架
    private String coverUrl;       //默认图片

    private Integer number;         //库存数量
    private LocalDateTime startTime;//开始时间
    private LocalDateTime endTime;  //结束时间

    public SpecialStock() {
    }

    public SpecialStock(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal price) {
        this.unitPrice = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDecoration() {
        return decoration;
    }

    public void setDecoration(String decoration) {
        this.decoration = decoration;
    }

    public String getBuildingAge() {
        return buildingAge;
    }

    public void setBuildingAge(String buildingAge) {
        this.buildingAge = buildingAge;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialStock)) return false;
        SpecialStock that = (SpecialStock) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpecialStock{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", houseId='").append(houseId).append('\'');
        sb.append(", number=").append(number);
        sb.append('}');
        return sb.toString();
    }
}