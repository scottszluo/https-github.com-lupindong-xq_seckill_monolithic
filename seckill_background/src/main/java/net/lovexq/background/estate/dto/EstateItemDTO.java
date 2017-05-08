package net.lovexq.background.estate.dto;

import net.lovexq.background.estate.model.EstateImageModel;
import net.lovexq.background.estate.model.EstateItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 房源条目DTO
 *
 * @author LuPindong
 * @time 2017-04-20 16:40
 */
public class EstateItemDTO extends EstateItemModel {

    private List<EstateImageModel> estateImageList = new ArrayList<>();

    private String batch;

    private String detailHref;

    private Long bodyLength;

    private Boolean isNew = false;

    private String CrawlerState;

    public EstateItemDTO() {
        super();
    }

    public EstateItemDTO(Long id) {
        super(id);
    }

    public List<EstateImageModel> getEstateImageList() {
        return estateImageList;
    }

    public void setEstateImageList(List<EstateImageModel> estateImageList) {
        this.estateImageList = estateImageList;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getDetailHref() {
        return detailHref;
    }

    public void setDetailHref(String detailHref) {
        this.detailHref = detailHref;
    }

    public Long getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(Long bodyLength) {
        this.bodyLength = bodyLength;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public String getCrawlerState() {
        return CrawlerState;
    }

    public void setCrawlerState(String crawlerState) {
        CrawlerState = crawlerState;
    }
}
