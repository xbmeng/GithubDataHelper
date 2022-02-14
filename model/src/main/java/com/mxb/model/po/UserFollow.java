package com.mxb.model.po;

import com.mxb.model.po.base.Bean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "github_follow")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserFollow extends Bean {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "following")
    private String following;

}
