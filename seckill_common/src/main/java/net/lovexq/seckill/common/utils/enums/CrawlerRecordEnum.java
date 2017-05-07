package net.lovexq.seckill.common.utils.enums;

/**
 * Created by LuPindong on 2017-5-6.
 */
public enum CrawlerRecordEnum {

    DEFAULT("默认"), DELETE("删除"), UPDATE("更新"), CREATE("新增");

    String value;

    CrawlerRecordEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
