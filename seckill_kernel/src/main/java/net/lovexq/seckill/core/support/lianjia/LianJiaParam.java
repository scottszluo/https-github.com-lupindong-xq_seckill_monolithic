package net.lovexq.seckill.core.support.lianjia;

import net.lovexq.seckill.core.config.AppProperties;
import net.lovexq.seckill.core.support.activemq.MqProducer;

/**
 * 链家网参数
 *
 * @author LuPindong
 * @time 2017-04-22 02:29
 */
public class LianJiaParam {

    private MqProducer mqProducer;
    private AppProperties appProperties;
    private String baseUrl;
    private String region;
    private Integer curPage;
    private Integer totalPage;

    public LianJiaParam(MqProducer mqProducer, AppProperties appProperties, String baseUrl, String region, Integer curPage, Integer totalPage) {
        this.mqProducer = mqProducer;
        this.appProperties = appProperties;
        this.baseUrl = baseUrl;
        this.region = region;
        this.curPage = curPage;
        this.totalPage = totalPage;
    }

    public MqProducer getMqProducer() {
        return mqProducer;
    }

    public void setMqProducer(MqProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
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
