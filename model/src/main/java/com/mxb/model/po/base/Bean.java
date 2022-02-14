package com.mxb.model.po.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 基础bean
 */
@Data
public class Bean {

    @Id
    @Column(name = "id")
    private String id;

    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;
}
