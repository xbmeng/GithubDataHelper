package com.mxb.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface CopyUtil {
    /**
     * 复制单个元素
     */
    static <T> T copyBean(Object o, Class<T> clazz) {
        if (o == null)
            return null;
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(o, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("copyProperties exception", e);
        }
    }

    /**
     * 仅复制List中的第一个元素
     */
    static <T> T copyFirstBean(List<?> list, Class<T> clazz) {
        if (CollectionUtils.isEmpty(list))
            return null;
        return copyBean(list.get(0), clazz);
    }

    /**
     * 对list中的元素逐个复制
     */
    static <T> List<T> copyListBean(List<?> list, Class<T> clazz) {
        if (CollectionUtils.isEmpty(list))
            return Collections.emptyList();
        return list.stream().map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
