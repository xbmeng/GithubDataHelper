package com.mxb.githubdata.userinterface.controller;

import com.alibaba.fastjson.JSONArray;
import com.mxb.githubdata.config.SwaggerConfig;
import com.mxb.githubdata.service.GithubUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "获取github用户数据")
@RestController
@CrossOrigin
@RequestMapping(value = SwaggerConfig.API_V1 + "/getgithubuser")
public class GithubUserController {
    @Resource
    private GithubUserService githubUserService;

//    @ApiOperation(value = "调用github api获取用户信息")
//    @PostMapping(value = "/getuserinfo", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResult getRepoInfo(String userName) {
//        return githubUserService.getUserInfo(userName);
//    }
//
//    @ApiOperation(value = "调用github api获取用户follow信息")
//    @PostMapping(value = "/getuserfollow", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResult getUserFollow(String userName) {
//        return githubUserService.getUserFollow(userName);
//    }
//
//    @ApiOperation(value = "调用github api获取全部用户follow信息")
//    @PostMapping(value = "/getallusersfollow", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResult getUserFollow() {
//        return githubUserService.getAllUsersFollow();
//    }
//
//    @ApiOperation(value = "数据库获取用户基本信息")
//    @PostMapping(value = "/getuserbasicinfo", produces = MediaType.APPLICATION_JSON_VALUE)
//    public JSONObject getUserBasicInfo(String userName) { return githubUserService.getUserBasicInfo(userName); }

//    @ApiOperation(value = "数据库获取用户仓库语言")
//    @PostMapping(value = "/getuserlanginfo", produces = MediaType.APPLICATION_JSON_VALUE)
//    public JSONObject getUserLangInfo(String userName) { return githubUserService.getUserLangInfo(userName); }

//    @ApiOperation(value = "数据库获取用户最近事件")
//    @PostMapping(value = "/getuserrecentevent", produces = MediaType.APPLICATION_JSON_VALUE)
//    public JSONArray getUserEvents(String userName) { return githubUserService.getUserRecentEvents(userName); }

//    @ApiOperation(value = "数据库获取用户仓库大小")
//    @PostMapping(value = "/getuserrepossize", produces = MediaType.APPLICATION_JSON_VALUE)
//    public JSONArray getReposSize(String userName) { return githubUserService.getReposSize(userName); }

//    @ApiOperation(value = "数据库获取用户star情况")
//    @PostMapping(value = "/getuserrepostars", produces = MediaType.APPLICATION_JSON_VALUE)
//    public JSONArray getUserRepoStars(String userName) { return githubUserService.getUserRepoStars(userName); }

    @ApiOperation(value = "数据库获取用户follow信息")
    @PostMapping(value = "/getuserfollowinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONArray getUserFollowInfo(String userName) { return githubUserService.getUserFollowInfo(userName); }
}
