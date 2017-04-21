package net.lovexq.seckill.kernel.service.impl;

import com.alibaba.fastjson.JSON;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.core.config.AppProperties;
import net.lovexq.seckill.core.support.LianJiaCrawler;
import net.lovexq.seckill.core.support.activemq.MqProducer;
import net.lovexq.seckill.kernel.dto.EstateItemDto;
import net.lovexq.seckill.kernel.model.EstateImage;
import net.lovexq.seckill.kernel.model.EstateItem;
import net.lovexq.seckill.kernel.repository.EstateImageRepository;
import net.lovexq.seckill.kernel.repository.EstateItemRepository;
import net.lovexq.seckill.kernel.service.EstateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 房产业务层实现类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
@Service
public class EstateServiceImpl implements EstateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstateServiceImpl.class);

    @Autowired
    private EstateItemRepository estateItemRepository;

    @Autowired
    private EstateImageRepository estateImageRepository;

    @Autowired
    private MqProducer mqProducer;

    @Autowired
    private AppProperties appProperties;

    @Override
    public void invokeCrawler(String baseUrl, String region, Integer curPage, Integer totalPage) throws Exception {
        try {
            String regionHtml = LianJiaCrawler.INSTANCE.doGet(baseUrl + AppConstants.CHANNEL_ERSHOUFANG + region, appProperties.getLiaJiaCookie());
            Document document = Jsoup.parse(regionHtml);
            if (LianJiaCrawler.INSTANCE.checkValidHtml(document)) {
                Element pageElement = document.select("div[comp-module='page']").first();
                // 获取当前页链接
                String pageUrl = pageElement.attr("page-url");

                // 抓取每页数据
                while (curPage <= totalPage) {
                    String curPageUrl = pageUrl.replace("{page}", String.valueOf(curPage));
                    String listHtml = LianJiaCrawler.INSTANCE.doGet(baseUrl + curPageUrl, appProperties.getLiaJiaCookie());
                    document = Jsoup.parse(listHtml);
                    if (LianJiaCrawler.INSTANCE.checkValidHtml(document)) {
                        int count = 1;
                        Elements contentElements = document.select("ul[class='sellListContent'] > li");

                        for (Element contentElement : contentElements) {
                            LOGGER.info("开始处理第{}页，第{}条记录", curPage, count);
                            // 解析列表数据
                            EstateItemDto dto = LianJiaCrawler.INSTANCE.parseListData(contentElement);
                            // 解析详情数据
                            dto = LianJiaCrawler.INSTANCE.parseDetailData(dto, appProperties.getLiaJiaCookie());
                            // 转换为json格式
                            String jsonText = JSON.toJSONString(dto);
                            // 发送消息
                            mqProducer.sendQueueMessage(jsonText);
                            count++;

                            Thread.sleep(new Random().nextInt(20000));
                        }
                    }
                    curPage++;
                    if (curPage % 3 == 0) {
                        Thread.sleep(100000);
                    } else {
                        Thread.sleep(50000);
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void saveCrawlerData(String msgText) throws Exception {
        try {
            EstateItemDto dto = JSON.parseObject(msgText, EstateItemDto.class);
            // 先查看数据库是否已存在该记录
            EstateItem entity = estateItemRepository.findByHouseId(dto.getHouseId());
            if (entity != null) {
                BeanUtils.copyProperties(dto, entity, "id");
                estateItemRepository.save(entity);
            } else {
                entity = new EstateItem();
                BeanUtils.copyProperties(dto, entity);
                estateItemRepository.save(entity);

                List<EstateImage> imageList = dto.getEstateImageList();
                if (!CollectionUtils.isEmpty(imageList)) {
                    for (EstateImage image : imageList) {
                        if (StringUtils.isNotBlank(image.getPicture_id())) {
                            estateImageRepository.save(image);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
