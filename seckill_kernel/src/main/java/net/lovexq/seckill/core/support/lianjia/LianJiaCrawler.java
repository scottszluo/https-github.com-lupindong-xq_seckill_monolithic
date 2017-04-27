package net.lovexq.seckill.core.support.lianjia;

import com.alibaba.fastjson.JSON;
import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.kernel.dto.EstateItemDTO;
import net.lovexq.seckill.kernel.model.EstateImageModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 链家网爬虫
 *
 * @author LuPindong
 * @time 2017-04-19 23:02
 */
public enum LianJiaCrawler {

    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(LianJiaCrawler.class);

    private static Header[] agentHeaderArr;

    static {

        Header header00 = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        Header header01 = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6");
        Header header02 = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.12 Safari/535.11");
        Header header03 = new BasicHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)");
        Header header04 = new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:34.0) Gecko/20100101 Firefox/34.0");
        Header header05 = new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/44.0.2403.89 Chrome/44.0.2403.89 Safari/537.36");
        Header header06 = new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        Header header07 = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        Header header08 = new BasicHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0");
        Header header09 = new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
        Header header10 = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1");
        Header header11 = new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
        Header header12 = new BasicHeader("User-Agent", "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11");
        Header header13 = new BasicHeader("User-Agent", "Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11");

        agentHeaderArr = new Header[]{header00, header01, header02, header03, header04, header05, header06, header07, header08, header09, header10, header11, header12, header13};
    }

    /**
     * 执行Get请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url, String cookieValue) {
        // url必须以"/"结尾
        if (!url.endsWith("/")) url += "/";

        Header headerAccept = new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        Header headerEncoding = new BasicHeader("Accept-Encoding", "gzip, deflate, sdch");
        Header headerLanguage = new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
        Header headerCache = new BasicHeader("Cache-Control", "max-age=0");
        Header headerConnection = new BasicHeader("Connection", "keep-alive");
        Header headerUpgrade = new BasicHeader("Upgrade-Insecure-Requests", "1");
        Header headerCookie = new BasicHeader("Cookie", cookieValue);

        Header headerAgent = agentHeaderArr[new Random().nextInt(14)];
        Header[] headerArray = new Header[]{headerAccept, headerEncoding, headerLanguage, headerCache, headerConnection, headerUpgrade, headerCookie, headerAgent};
        return doGet(url, headerArray);
    }

    /**
     * 执行Get请求
     *
     * @param url
     * @return
     */
    public static String doGet(String url, Header[] headerArray) {
        String responseBody = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeaders(headerArray);
            LOGGER.info("开始执行GET请求：{}", httpGet);
            responseBody = httpClient.execute(httpGet, responseHandler);
        } catch (ClientProtocolException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return responseBody;
    }

    /**
     * 检查是否合法的Document
     */
    public static boolean checkValidHtml(Document document) throws Exception {
        String text = document.select("head > title").text();
        if (text.startsWith("验证异常流量")) {
            throw new Exception("验证异常流量，请稍后重试！");
        }
        return true;
    }

    /**
     * 解析列表数据
     *
     * @param contentElement
     */
    public static EstateItemDTO parseListData(Element contentElement) throws Exception {
        EstateItemDTO estateItem = new EstateItemDTO(IdWorker.INSTANCE.nextId());
        try {
            estateItem.setSaleStatus("放盘");

            Element titleElement = contentElement.select("div[class='title'] > a").first();
            estateItem.setTitle(titleElement.text());// 标题
            estateItem.setDetailHref(titleElement.attr("href")); // 详情链接

            String positionInfo = contentElement.select("div[class='positionInfo']").first().text();
            Pattern compilePosition = Pattern.compile("\\d+[年]");
            Matcher matcherPosition = compilePosition.matcher(positionInfo);
            if (matcherPosition.find()) {
                estateItem.setBuildingAge(matcherPosition.group().replaceAll("\\D", ""));// 建筑年代
            }

            String followInfo = contentElement.select("div[class='followInfo']").first().text();
            Pattern compileFocus = Pattern.compile("\\d+[人]");
            Matcher matcherFocus = compileFocus.matcher(followInfo);
            if (matcherFocus.find()) {
                estateItem.setFocusNum(Integer.valueOf(matcherFocus.group().replaceAll("\\D", "")));// 关注人数
            }

            Pattern compileWatch = Pattern.compile("[共]\\d+[次]");
            Matcher matcherWatch = compileWatch.matcher(followInfo);
            if (matcherWatch.find()) {
                estateItem.setWatchNum(Integer.valueOf(matcherWatch.group().replaceAll("\\D", "")));// 看房人数
            }

            String dataHId = contentElement.select("div[class='priceInfo'] > div[class='unitPrice']").first().attr("data-hid");
            estateItem.setHouseId(dataHId); // 编号

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return estateItem;
    }

    /**
     * 解析详情数据
     *
     * @param estateItem
     * @param cookieValue
     */
    public static EstateItemDTO parseDetailData(EstateItemDTO estateItem, String cookieValue) throws Exception {
        String detailHtml = doGet(estateItem.getDetailHref(), cookieValue);
        Document document = Jsoup.parse(detailHtml);
        if (checkValidHtml(document)) {
            // 获取页面数据
            Elements baseElements = document.select("div[class='base'] li");
            for (Element element : baseElements) {
                String text = element.text();
                if (text.contains("房屋户型")) {
                    estateItem.setModel(text.substring(4));
                } else if (text.contains("所在楼层")) {
                    estateItem.setFloor(text.substring(4));
                } else if (text.contains("房屋朝向")) {
                    estateItem.setDirection(text.substring(4));
                } else if (text.contains("装修情况")) {
                    estateItem.setDecoration(text.substring(4));
                }
            }

            Elements areaElements = document.select("div[class='areaName'] > span[class='info'] > a");
            estateItem.setRegionAName(areaElements.get(0).text());
            estateItem.setRegionBName(areaElements.get(1).text());
            // 获取js脚本中的数据
            Elements scriptElements = document.getElementsByTag("script");
            for (Element element : scriptElements) {
                String elementData = element.data().trim();
                if (elementData.startsWith("require(['ershoufang/sellDetail/detailV3']")) {
                    estateItem.setTotalPrice(new BigDecimal(getSubValue(elementData, "totalPrice")));
                    estateItem.setUnitPrice(new BigDecimal(getSubValue(elementData, "price")));
                    estateItem.setArea(new BigDecimal(getSubValue(elementData, "area")));
                    //estateItem.setHouseId(getSubValue(elementData, "houseId"));
                    estateItem.setResBlockId(getSubValue(elementData, "resblockId"));
                    estateItem.setResBlockName(getSubValue(elementData, "resblockName"));
                    estateItem.setCityId(getSubValue(elementData, "cityId"));
                    // 处理经纬度
                    String resblockPosition = getSubValue(elementData, "resblockPosition");
                    String[] positionArray = resblockPosition.split(",");
                    estateItem.setLongitude(new BigDecimal(positionArray[0]));
                    estateItem.setLatitude(new BigDecimal(positionArray[1]));
                    // 处理图片
                    String imageJsonStr = getSubValue(elementData, "images", "}]");
                    if (StringUtils.isNotBlank(imageJsonStr)) {
                        estateItem.setEstateImageList(JSON.parseArray(imageJsonStr + "}]", EstateImageModel.class));
                    }
                }
            }
        }
        return estateItem;
    }

    /**
     * 获取指定key的值
     *
     * @param text
     * @param beginKey
     * @return
     */
    private static String getSubValue(String text, String beginKey) {
        return getSubValue(text, beginKey, "',");
    }

    /**
     * 获取指定key的值
     *
     * @param text
     * @param beginKey
     * @param endKey
     * @return
     */
    private static String getSubValue(String text, String beginKey, String endKey) {
        String subText = StringUtils.substringBetween(text, beginKey + ":", endKey);
        if (StringUtils.isNotBlank(subText)) {
            return subText.trim().replace("'", "");
        }
        return "";
    }

    /**
     * 解析默认图片
     *
     * @param estateItem
     * @param bigImgElements
     * @return
     */
    public EstateItemDTO parseCoverImgData(EstateItemDTO estateItem, Elements bigImgElements) {
        Element imgElement = bigImgElements.select("div[class='item'][data-houseid='" + estateItem.getHouseId() + "'] > a > img").first();
        if (imgElement != null) {
            String original = imgElement.attr("data-original");
            estateItem.setCoverUrl(original);
        }
        return estateItem;
    }

}