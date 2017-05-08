package net.lovexq.seckill.core.support.lianjia;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.core.support.activemq.MqProducer;
import net.lovexq.seckill.kernel.dto.EstateItemDTO;
import net.lovexq.seckill.kernel.model.EstateImageModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Queue;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 链家网爬虫多线程执行程序【适用于新增操作】
 *
 * @author LuPindong
 * @time 2017-04-22 02:02
 */
public class LianJiaAddCallable implements Callable<JsonResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LianJiaAddCallable.class);

    private LianJiaParam lianJiaParam;

    private MqProducer mqProducer;

    private Queue queue;

    public LianJiaAddCallable(LianJiaParam lianJiaParam, MqProducer mqProducer, Queue queue) {
        this.lianJiaParam = lianJiaParam;
        this.mqProducer = mqProducer;
        this.queue = queue;
    }

    @Override
    public JsonResult call() throws Exception {
        JsonResult result = new JsonResult();
        long beginTime = System.currentTimeMillis();
        try {
            Long bodyLength = 0L;
            Integer curPage = lianJiaParam.getCurPage();
            if (curPage == null) {
                curPage = 0;
            } else {
                curPage = curPage - 1;
            }
            List<EstateItemDTO> estateItemList = lianJiaParam.getEstateItemList();
            for (; curPage < estateItemList.size(); curPage++) {
                EstateItemDTO estateItem = estateItemList.get(curPage);
                LOGGER.info("开始新增[{}]，第{}条记录", estateItem.getBatch() + "：" + estateItem.getHouseCode(), curPage + 1);
                // 解析详情数据
                estateItem = LianJiaCrawler.INSTANCE.parseDetailData(estateItem, lianJiaParam.getAppProperties().getLiaJiaCookie());
                List<EstateImageModel> imageList = estateItem.getEstateImageList();

                if ((CollectionUtils.isNotEmpty(imageList) && "1".equals(estateItem.getCrawlerState())) || !"1".equals(estateItem.getCrawlerState())) {
                    // 转为二进制数据
                    byte[] dataArray = ProtoStuffUtil.serialize(estateItem);
                    // 发送消息
                    mqProducer.sendQueueMessage(dataArray, queue);
                }

                bodyLength = estateItem.getBodyLength();
                if ((bodyLength % 10000000) > 8000000) {
                    Thread.sleep(2000);
                    LOGGER.info("休眠了{}秒", 2000 / 1000);
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("新增执行完毕，累计耗时{}分", (endTime - beginTime) / 1000 / 60);
        return result;
    }
}
