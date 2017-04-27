package net.lovexq.seckill.kernel.dto;

import net.lovexq.seckill.kernel.model.EstateImageModel;
import net.lovexq.seckill.kernel.model.EstateItemModel;

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
}
