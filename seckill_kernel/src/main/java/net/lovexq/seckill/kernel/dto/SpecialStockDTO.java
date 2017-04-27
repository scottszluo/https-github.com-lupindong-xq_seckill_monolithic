package net.lovexq.seckill.kernel.dto;

import net.lovexq.seckill.kernel.model.EstateImageModel;
import net.lovexq.seckill.kernel.model.SpecialStockModel;

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