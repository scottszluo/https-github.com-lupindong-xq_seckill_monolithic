package net.lovexq.seckill.kernel.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 房产条目
 *
 * @author LuPindong
 * @time 2017-04-19 23:35
 */
@Entity
public class EstateItem {
    @Id
    private Long id;              //主键
    private String title;         //标题x
    private String houseId;       //编号x
    /**
     * 基本信息
     */
    private BigDecimal totalPrice;//总价x
    private BigDecimal unitPrice; //单价x
    private String model;         //房屋户型x
    private String direction;     //房屋朝向x
    private BigDecimal area;      //建筑面积x
    private String floor;         //所在楼层x
    private String decoration;    //装修情况x
    private String buildingAge;   //建筑年代x
    /**
     * 小区信息
     */
    private String resBlockId;    //小区IDx
    private String resBlockName;  //小区名称x
    private BigDecimal latitude;  //纬度x
    private BigDecimal longitude; //经度x
    private String cityId;        //城市IDx
    private String cityAbbr;      //城市缩写x
    private String cityName;      //城市名称x
    private String regionAName;      //大区域名称
    private String regionBName;      //小区域名称

    /**
     * 其他信息
     */
    private Integer focusNum;      //关注人数x
    private Integer watchNum;      //看房人数x

    public EstateItem() {
    }

    public EstateItem(Long id) {
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

    public String getResBlockId() {
        return resBlockId;
    }

    public void setResBlockId(String resBlockId) {
        this.resBlockId = resBlockId;
    }

    public String getResBlockName() {
        return resBlockName;
    }

    public void setResBlockName(String resBlockName) {
        this.resBlockName = resBlockName;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityAbbr() {
        return cityAbbr;
    }

    public void setCityAbbr(String cityAbbr) {
        this.cityAbbr = cityAbbr;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionAName() {
        return regionAName;
    }

    public void setRegionAName(String regionAName) {
        this.regionAName = regionAName;
    }

    public String getRegionBName() {
        return regionBName;
    }

    public void setRegionBName(String regionBName) {
        this.regionBName = regionBName;
    }

    public Integer getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(Integer focusNum) {
        this.focusNum = focusNum;
    }

    public Integer getWatchNum() {
        return watchNum;
    }

    public void setWatchNum(Integer watchNum) {
        this.watchNum = watchNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstateItem)) return false;
        EstateItem that = (EstateItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EstateItem{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append('}');
        return sb.toString();
    }
}