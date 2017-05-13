package net.lovexq.background.core.support.lianjia;

import com.alibaba.fastjson.JSON;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.background.core.support.activemq.MqProducer;
import net.lovexq.background.estate.dto.EstateItemDTO;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * 链家网爬虫多线程执行程序【适用于检查】
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

    /*
 * 获取区域链接列表
 */
    private static List<String> getRegionLinks(String html) throws Exception {
        List<String> regionLinks = new ArrayList();
        Document document = Jsoup.parse(html);
        if (LianJiaCrawler.INSTANCE.checkValidHtml(document)) {
            Elements linkElements = document.select("div[data-role='ershoufang'] > div > a");
            for (Element linkElement : linkElements) {
                regionLinks.add(linkElement.attr("href"));
            }
        }

        return regionLinks;
    }

    @Override
    public JsonResult call() throws Exception {
        JsonResult result = new JsonResult();
        long beginTime = System.currentTimeMillis();
        try {
            String assignRegion = lianJiaParam.getRegion();
            if (StringUtils.isNotBlank(assignRegion)) {
                String[] assignRegions = assignRegion.split(",");
                for (String region : assignRegions) {
                    fetchData(AppConstants.CHANNEL_ERSHOUFANG + region);
                }
            } else {
                String indexHtml = LianJiaCrawler.INSTANCE.doGet(lianJiaParam.getBaseUrl() + AppConstants.CHANNEL_ERSHOUFANG, lianJiaParam.getAppProperties().getLiaJiaCookie());
                List<String> regionLinkList = getRegionLinks(indexHtml);
                for (String regionLink : regionLinkList) {
                    fetchData(regionLink);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("检查更新执行完毕，累计耗时{}分", (endTime - beginTime) / 1000 / 60);
        return result;
    }

    /**
     * 按区域抓取数据
     *
     * @param regionLink
     * @throws Exception
     */
    private void fetchData(String regionLink) throws Exception {
        int sleepTime = 0;
        String regionHtml = LianJiaCrawler.INSTANCE.doGet(lianJiaParam.getBaseUrl() + regionLink, lianJiaParam.getAppProperties().getLiaJiaCookie());
        Document document = Jsoup.parse(regionHtml);
        if (LianJiaCrawler.INSTANCE.checkValidHtml(document)) {
            Element pageElement = document.select("div[comp-module='page']").first();
            // 获取当前页链接
            String curPageUrl;
            String pageUrl = pageElement.attr("page-url");
            Map pageData = JSON.parseObject(pageElement.attr("page-data"), Map.class);
            int curPage = MapUtils.getIntValue(pageData, "curPage");
            int totalPage = MapUtils.getIntValue(pageData, "totalPage") + 1;
            // 抓取每页数据
            while (curPage <= totalPage) {
                curPageUrl = pageUrl.replace("{page}", String.valueOf(curPage));
                String listHtml = LianJiaCrawler.INSTANCE.doGet(lianJiaParam.getBaseUrl() + curPageUrl, lianJiaParam.getAppProperties().getLiaJiaCookie());
                document = Jsoup.parse(listHtml);
                if (LianJiaCrawler.INSTANCE.checkValidHtml(document)) {
                    int count = 1;
                    Elements contentElements = document.select("ul[class='sellListContent'] > li");
                    Elements bigImgElements = document.select("div[class='bigImgList'] > div");

                    for (Element contentElement : contentElements) {
                        LOGGER.info("开始检查[{}]，第{}页，第{}条记录", regionLink, curPage, count);
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

                if (curPage % 5 == 0) {
                    sleepTime = 2000;
                    Thread.sleep(sleepTime);
                    LOGGER.info("休眠了{}毫秒", sleepTime);
                } else {
                    sleepTime = new Random().nextInt(200);
                    Thread.sleep(sleepTime);
                    LOGGER.info("休眠了{}毫秒", sleepTime);
                }
            }
        }
    }
}
