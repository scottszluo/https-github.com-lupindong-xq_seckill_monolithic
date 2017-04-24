package net.lovexq.seckill.kernel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * 房产图片
 *
 * @author LuPindong
 * @time 2017-04-20 16:25
 */
@Entity
public class EstateImage {
    @Id
    private String picture_id;
    @Column(name = "house_code")
    private String houseCode;
    private String picture_url;
    private String picture_source_url;
    @Column(name = "picture_type")
    private int pictureType;
    private String appid;
    private String app_pkid;
    private String upload_user_id;
    private String se_status;
    private String audit_status;
    private String bit_status;
    private String ctime;
    private String mtime;
    private String url;
    private String uri;
    private String type;

    public String getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(String picture_id) {
        this.picture_id = picture_id;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getPicture_source_url() {
        return picture_source_url;
    }

    public void setPicture_source_url(String picture_source_url) {
        this.picture_source_url = picture_source_url;
    }

    public int getPictureType() {
        return pictureType;
    }

    public void setPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getApp_pkid() {
        return app_pkid;
    }

    public void setApp_pkid(String app_pkid) {
        this.app_pkid = app_pkid;
    }

    public String getUpload_user_id() {
        return upload_user_id;
    }

    public void setUpload_user_id(String upload_user_id) {
        this.upload_user_id = upload_user_id;
    }

    public String getSe_status() {
        return se_status;
    }

    public void setSe_status(String se_status) {
        this.se_status = se_status;
    }

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public String getBit_status() {
        return bit_status;
    }

    public void setBit_status(String bit_status) {
        this.bit_status = bit_status;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstateImage)) return false;
        EstateImage that = (EstateImage) o;
        return Objects.equals(picture_id, that.picture_id) &&
                Objects.equals(houseCode, that.houseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(picture_id, houseCode);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EstateImage{");
        sb.append("picture_id='").append(picture_id).append('\'');
        sb.append(", house_code='").append(houseCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}