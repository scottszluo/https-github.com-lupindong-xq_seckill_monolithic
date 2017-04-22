package net.lovexq.seckill.core.support.lianjia;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.kernel.dto.EstateItemDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * 链家网多线程执行
 *
 * @author LuPindong
 * @time 2017-04-22 02:02
 */
public class LianJiaCallable implements Callable<JsonResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LianJiaCallable.class);

    private LianJiaParam lianJiaParam;

    public LianJiaCallable(LianJiaParam lianJiaParam) {
        this.lianJiaParam = lianJiaParam;
    }

    @Override
    public JsonResult call() throws Exception {
        JsonResult result = new JsonResult();
        try {
            Integer curPage = lianJiaParam.getCurPage();
            Integer totalPage = lianJiaParam.getTotalPage();

            String regionHtml = LianJiaCrawler.INSTANCE.doGet(lianJiaParam.getBaseUrl() + AppConstants.CHANNEL_ERSHOUFANG + lianJiaParam.getRegion(), lianJiaParam.getAppProperties().getLiaJiaCookie());
            Document document = Jsoup.parse(regionHtml);
            if (LianJiaCrawler.INSTANCE.checkValidHtml(document)) {
                Element pageElement = document.select("div[comp-module='page']").first();
                // 获取当前页链接
                String pageUrl = pageElement.attr("page-url");

                // 抓取每页数据
                while (curPage <= totalPage) {
                    String curPageUrl = pageUrl.replace("{page}", String.valueOf(curPage));
                    String listHtml = LianJiaCrawler.INSTANCE.doGet(lianJiaParam.getBaseUrl() + curPageUrl, lianJiaParam.getAppProperties().getLiaJiaCookie());
                    document = Jsoup.parse(listHtml);
                    if (LianJiaCrawler.INSTANCE.checkValidHtml(document)) {
                        int count = 1;
                        Elements contentElements = document.select("ul[class='sellListContent'] > li");

                        for (Element contentElement : contentElements) {
                            LOGGER.info("开始处理{}：第{}页，第{}条记录", lianJiaParam.getRegion(), curPage, count);
                            // 解析列表数据
                            EstateItemDto dto = LianJiaCrawler.INSTANCE.parseListData(contentElement);
                            // 解析详情数据
                            dto = LianJiaCrawler.INSTANCE.parseDetailData(dto, lianJiaParam.getAppProperties().getLiaJiaCookie());
                            // 转为二进制数据
                            byte[] dataArray =  ProtoStuffUtil.serialize(dto);
                            // 发送消息
                            lianJiaParam.getMqProducer().sendQueueMessage(dataArray);
                            count++;

                            Thread.sleep(new Random().nextInt(10000));
                        }
                    }
                    curPage++;
                    if (curPage % 3 == 0) {
                        Thread.sleep(180000);
                    } else {
                        Thread.sleep(90000);
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        }

        return result;
    }
}
