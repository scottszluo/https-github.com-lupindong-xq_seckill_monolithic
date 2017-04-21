package net.lovexq.seckill.kernel.service;

/**
 * 房产业务层抽象类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
public interface EstateService {

    void invokeCrawler(String baseUrl, String region, Integer curPage, Integer totalPage) throws Exception;

    void saveCrawlerData(String msgText) throws Exception;
}
