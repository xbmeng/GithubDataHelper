package com.mxb.model.po;

import com.mxb.model.po.base.Bean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "github_user")
@EqualsAndHashCode(callSuper = true)
@Data
public class GithubUser extends Bean {

    @Column(name = "username")
    String userName;

    @Column(name = "joining_time")
    Date joiningTime;

    @Column(name = "language")
    String langusge;

    @Column(name = "stars")
    String stars;

    @Column(name = "repo_number")
    Integer repoNumber;

    @Column(name = "followering_number")
    Integer followingNumber;

    @Column(name = "followers_number")
    Integer followersNumber;

    @Column(name = "repos_info")
    String reposInfo;

//    @Column(name = "recent_followers")
//    String recentFollwers;

//    @Column(name = "recent_follwing")
//    String recentFollwing;

    @Column(name = "received_events")
    String receivedEvents;

}
