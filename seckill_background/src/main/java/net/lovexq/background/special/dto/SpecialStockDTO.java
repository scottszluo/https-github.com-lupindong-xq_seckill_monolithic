package net.lovexq.background.special.dto;

import net.lovexq.background.estate.model.EstateImageModel;
import net.lovexq.background.special.model.SpecialStockModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 特价库存DTO
 *
 * @author LuPindong
 * @time 2017-04-20 16:40
 */
public class SpecialStockDTO extends SpecialStockModel {

    private String detailHref;
    private String totalPriceOriginal;
    private String totalPriceCurrent;
    private String unitPriceStr;
    private String areaStr;
    private List<EstateImageModel> estateImageList = new ArrayList<>();

    public String getDetailHref() {
        return detailHref;
    }

    public void setDetailHref(String detailHref) {
        this.detailHref = detailHref;
    }

    public String getTotalPriceOriginal() {
        return totalPriceOriginal;
    }

    public void setTotalPriceOriginal(String totalPriceOriginal) {
        this.totalPriceOriginal = totalPriceOriginal;
    }

    public String getTotalPriceCurrent() {
        return totalPriceCurrent;
    }

    public void setTotalPriceCurrent(String totalPriceCurrent) {
        this.totalPriceCurrent = totalPriceCurrent;
    }

    public String getUnitPriceStr() {
        return unitPriceStr;
    }

    public void setUnitPriceStr(String unitPriceStr) {
        this.unitPriceStr = unitPriceStr;
    }

    public String getAreaStr() {
        return areaStr;
    }

    public void setAreaStr(String areaStr) {
        this.areaStr = areaStr;
    }

    public List<EstateImageModel> getEstateImageList() {
        return estateImageList;
    }

    public void setEstateImageList(List<EstateImageModel> estateImageList) {
        this.estateImageList = estateImageList;
    }

}