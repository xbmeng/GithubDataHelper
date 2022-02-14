package com.mxb.githubdata.userinterface.controller;

import com.mxb.common.base.ApiResult;
import com.mxb.githubdata.config.SwaggerConfig;
import com.mxb.githubdata.service.GithubDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "获取github仓库数据")
@RestController
@RequestMapping(value = SwaggerConfig.API_V1 + "/getgithubdata")
public class GithubDataController {

    @Resource
    private GithubDataService githubDataService;

    @ApiOperation(value = "调用github api获取仓库信息")
    @PostMapping(value = "/getrepoinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getRepoInfo(String userName,String repoName)
    {
        return githubDataService.getGithubRepoData(userName,repoName);
    }

    @ApiOperation(value = "调用github api获取仓库贡献者信息")
    @PostMapping(value = "/getrepocontributorsinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getRepoContriInfo(String userName,String repoName)
    {
        return githubDataService.getGithubRepoContributors(userName,repoName);
    }

    @ApiOperation(value = "调用github api获取仓库评论信息")
    @PostMapping(value = "/getrepocomments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult getRepoComments(String userName,String repoName)
    {
        return githubDataService.getGithubRepoComments(userName,repoName);
    }

//    @ApiOperation(value = "获取仓库贡献者关系图")
//    @PostMapping(value = "/getreporelationship", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResult getRepoRelationship(String userName,String repoName)
//    {
//        return githubDataService.getGithubRepoRelationship(userName,repoName);
//    }
}
