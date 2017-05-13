package net.lovexq.background.core.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LuPindong
 * @time 2017-05-13 07:37
 */
public class PageX<T> extends PageImpl<T> implements Serializable {

    // 反序列化调用无参构造器时使用
    public PageX() {
        super(new ArrayList<T>());
    }

    public PageX(List<T> content) {
        super(content);
    }

    public PageX(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }


}