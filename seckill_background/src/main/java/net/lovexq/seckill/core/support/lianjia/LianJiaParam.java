package net.lovexq.seckill.core.support.lianjia;

import net.lovexq.seckill.core.properties.AppProperties;
import net.lovexq.seckill.kernel.dto.EstateItemDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 链家网参数
 *
 * @author LuPindong
 * @time 2017-04-22 02:29
 */
public class LianJiaParam {

    private List<EstateItemDTO> estateItemList = new ArrayList<>();
    private AppProperties appProperties;
    private String batch;
    private String baseUrl;
    private String region;
    private Integer curPage;
    private Integer totalPage;

    public LianJiaParam(List<EstateItemDTO> estateItemList, AppProperties appProperties, String batch, Integer curPage) {
        this.estateItemList = estateItemList;
        this.appProperties = appProperties;
        this.batch = batch;
        this.curPage = curPage;
    }

    public LianJiaParam(AppProperties appProperties, String batch, String baseUrl, String region) {
        this.appProperties = appProperties;
        this.batch = batch;
        this.baseUrl = baseUrl;
        this.region = region;
    }

    public LianJiaParam(AppProperties appProperties, String baseUrl, String region, Integer curPage, Integer totalPage) {
        this.appProperties = appProperties;
        this.baseUrl = baseUrl;
        this.region = region;
        this.curPage = curPage;
        this.totalPage = totalPage;
    }


    public List<EstateItemDTO> getEstateItemList() {
        return estateItemList;
    }

    public void setEstateItemList(List<EstateItemDTO> estateItemList) {
        this.estateItemList = estateItemList;
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
