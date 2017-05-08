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

    private List<EstateImageModel> estateImageList = new ArrayList<>();

    public List<EstateImageModel> getEstateImageList() {
        return estateImageList;
    }

    public void setEstateImageList(List<EstateImageModel> estateImageList) {
        this.estateImageList = estateImageList;
    }

}