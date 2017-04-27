package net.lovexq.seckill.core.support.lianjia;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.core.support.activemq.MqProducer;
import net.lovexq.seckill.kernel.dto.EstateItemDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Queue;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * 链家网爬虫多线程执行程序【适用于检查更新】
 *
 * @author LuPindong
 * @time 2017-04-22 02:02
 */
public class LianJiaCheckCallable implements Callable<JsonResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LianJiaCheckCallable.class);

    private LianJiaParam lianJiaParam;

    private MqProducer mqProducer;

    private Queue queue;

    public LianJiaCheckCallable(LianJiaParam lianJiaParam, MqProducer mqProducer, Queue queue) {
        this.lianJiaParam = lianJiaParam;
        this.mqProducer = mqProducer;
        this.queue = queue;
    }

    @Override
    public JsonResult call() throws Exception {
        JsonResult result = new JsonResult();
        long beginTime = System.currentTimeMillis();
        try {
            Integer curPage = lianJiaParam.getCurPage();
            Integer totalPage = lianJiaParam.getTotalPage();
            int sleepTime = 0;

            String regionHtml = LianJiaCrawler.INSTANCE.doGet(lianJiaParam.getBaseUrl() + AppConstants.CHANNEL_ERSHOUFANG, lianJiaParam.getAppProperties().getLiaJiaCookie());
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
                        Elements bigImgElements = document.select("div[class='bigImgList'] > div");

                        for (Element contentElement : contentElements) {
                            LOGGER.info("开始检查更新：第{}页，第{}条记录", curPage, count);
                            // 解析列表数据
                            EstateItemDTO dto = LianJiaCrawler.INSTANCE.parseListData(contentElement);
                            // 解析默认图片
                            dto = LianJiaCrawler.INSTANCE.parseCoverImgData(dto, bigImgElements);

                            dto.setBatch(lianJiaParam.getBatch());
                            // 转为二进制数据
                            byte[] dataArray = ProtoStuffUtil.serialize(dto);
                            // 发送消息
                            mqProducer.sendQueueMessage(dataArray, queue);
                            count++;
                        }
                    }
                    curPage++;

                    sleepTime = new Random().nextInt(1000);
                    Thread.sleep(sleepTime);
                    LOGGER.info("休眠了{}毫秒", sleepTime);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("执行完毕，累计耗时{}分", (endTime - beginTime) / 1000 / 60);
        return result;
    }
}
