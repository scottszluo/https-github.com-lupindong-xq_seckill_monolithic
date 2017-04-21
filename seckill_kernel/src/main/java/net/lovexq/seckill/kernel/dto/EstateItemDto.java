package net.lovexq.seckill.kernel.dto;

import net.lovexq.seckill.kernel.model.EstateImage;
import net.lovexq.seckill.kernel.model.EstateItem;

import java.util.List;

/**
 * 房产条目DTO
 *
 * @author LuPindong
 * @time 2017-04-20 16:40
 */
public class EstateItemDto extends EstateItem {

    private List<EstateImage> estateImageList;

    private String detailHref;

    public EstateItemDto() {
        super();
    }

    public EstateItemDto(Long id) {
        super(id);
    }

    public List<EstateImage> getEstateImageList() {
        return estateImageList;
    }

    public void setEstateImageList(List<EstateImage> estateImageList) {
        this.estateImageList = estateImageList;
    }

    public String getDetailHref() {
        return detailHref;
    }

    public void setDetailHref(String detailHref) {
        this.detailHref = detailHref;
    }
}
