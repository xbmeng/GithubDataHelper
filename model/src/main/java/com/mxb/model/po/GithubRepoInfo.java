package com.mxb.model.po;


import com.mxb.model.po.base.Bean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "github_repo_info")
@EqualsAndHashCode(callSuper = true)
@Data
public class GithubRepoInfo extends Bean {

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "description")
    private String description;

    @Column(name = "repo_created")
    private Date repoCreatedTime;

    @Column(name = "repo_updated")
    private Date repoUpdateTime;

    @Column(name = "repo_push")
    private Date lastPush;

    @Column(name = "language")
    private String language;

    @Column(name = "size")
    private Integer size;

    @Column(name = "license")
    private String license;

}
