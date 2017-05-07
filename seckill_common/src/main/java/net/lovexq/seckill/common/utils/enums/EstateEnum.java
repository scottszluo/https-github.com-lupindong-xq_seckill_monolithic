package net.lovexq.seckill.common.utils.enums;

/**
 * Created by LuPindong on 2017-5-6.
 */
public enum EstateEnum {

    FOR_SALE("在售"), DEAL("成交"), INVALID("下架"), SPECIAL("特价");

    String value;

    EstateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
