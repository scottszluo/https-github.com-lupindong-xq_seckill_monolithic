package net.lovexq.seckill.kernel.dto;

import net.lovexq.seckill.kernel.model.EstateImage;
import net.lovexq.seckill.kernel.model.EstateItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 房源条目DTO
 *
 * @author LuPindong
 * @time 2017-04-20 16:40
 */
public class EstateItemDto extends EstateItem {

    private String coverUrl;

    private List<EstateImage> estateImageList = new ArrayList<>();

    private String detailHref;

    public EstateItemDto() {
        super();
    }

    public EstateItemDto(Long id) {
        super(id);
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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
