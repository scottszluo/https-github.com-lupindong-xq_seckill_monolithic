package net.lovexq.seckill.kernel.model;

import net.lovexq.seckill.common.model.BasicModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 房源图片
 *
 * @author LuPindong
 * @time 2017-04-20 16:25
 */
@Entity
@Table(name = "estate_image")
public class EstateImage extends BasicModel {
    @Id
    private Long pictureId;
    private String houseCode;
    private Integer pictureType;
    private String pictureSourceUrl;
    private String url;

    public EstateImage() {
    }

    public EstateImage(String houseCode) {
        this.houseCode = houseCode;
    }

    public EstateImage(Long pictureId, String houseCode) {
        this.pictureId = pictureId;
        this.houseCode = houseCode;
    }

    public EstateImage(String houseCode, Integer pictureType) {
        this.houseCode = houseCode;
        this.pictureType = pictureType;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public Integer getPictureType() {
        return pictureType;
    }

    public void setPictureType(Integer pictureType) {
        this.pictureType = pictureType;
    }

    public String getPictureSourceUrl() {
        return pictureSourceUrl;
    }

    public void setPictureSourceUrl(String pictureSourceUrl) {
        this.pictureSourceUrl = pictureSourceUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstateImage)) return false;
        EstateImage that = (EstateImage) o;
        return Objects.equals(pictureId, that.pictureId) &&
                Objects.equals(houseCode, that.houseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pictureId, houseCode);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EstateImage{");
        sb.append("pictureId='").append(pictureId).append('\'');
        sb.append(", houseCode='").append(houseCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}