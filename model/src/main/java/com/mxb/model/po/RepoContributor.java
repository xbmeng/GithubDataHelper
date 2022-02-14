package com.mxb.model.po;

import com.mxb.model.po.base.Bean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "github_repo_contributor")
@EqualsAndHashCode(callSuper = true)
@Data
public class RepoContributor extends Bean {

    @Column(name = "name")
    private String name;

    @Column(name = "repo_id")
    private String repoId;

    @Column(name = "contributions")
    private Integer contributions;
}
