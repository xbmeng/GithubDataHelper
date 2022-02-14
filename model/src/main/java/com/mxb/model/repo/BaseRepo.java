package com.mxb.model.repo;

import com.mxb.common.util.CopyUtil;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseRepo<PO,BO> {

    private final Class<PO> poClass;

    private final Class<BO> boClass;

    public BaseRepo(Class<PO> poClass, Class<BO> boClass) {
        this.poClass = poClass;
        this.boClass = boClass;
    }

    protected BO toBO(PO po) {
        return CopyUtil.copyBean(po, boClass);
    }

    protected PO toPO(BO bo) {
        return CopyUtil.copyBean(bo, poClass);
    }

    protected List<BO> toBOs(List<PO> pos) {
        if (CollectionUtils.isEmpty(pos))
            return Collections.emptyList();
        return pos.stream().map(this::toBO)
                .collect(Collectors.toList());
    }
}
