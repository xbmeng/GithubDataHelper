package com.mxb.model.po;

import com.mxb.model.po.base.Bean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "github_repo_comment")
@EqualsAndHashCode(callSuper = true)
@Data
public class RepoComment extends Bean {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "repo_id")
    private String repoId;

    @Column(name = "commit_id")
    private String commitId;

    @Column(name = "line")
    private Integer line;

    @Column(name = "comment_created")
    private Date commentCreatedTime;

    @Column(name = "comment_updated")
    private Date commentUpdateTime;

    @Column(name = "path")
    private String path;

    @Column(name = "body")
    private String body;

}
