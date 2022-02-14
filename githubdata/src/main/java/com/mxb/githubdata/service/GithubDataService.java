package com.mxb.githubdata.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mxb.common.base.ApiResult;
import com.mxb.common.util.GithubAPIRequestUtil;
import com.mxb.githubdata.domain.entity.GithubRepoInfoBO;
import com.mxb.githubdata.domain.entity.RepoCommentBO;
import com.mxb.githubdata.domain.entity.RepoContributorBO;
import com.mxb.githubdata.repository.CommentRepo;
import com.mxb.githubdata.repository.ContributorRepo;
import com.mxb.githubdata.repository.GithubRepoInfoRepo;
import com.mxb.model.po.GithubRepoInfo;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GithubDataService {

    public static final String BASEURL = "http://api.github.com/";

    public static final String ACCESS_TOKEN = "27761323d68e6685b899c1f2388851403d24f5ae";

    @Resource
    private GithubRepoInfoRepo githubRepoInfoRepo;

    @Resource
    private ContributorRepo contributorRepo;

    @Resource
    private CommentRepo commentRepo;

    //TODO:重构
    @Transactional
    public ApiResult getGithubRepoData(String userName, String repoName) {
        String url = BASEURL + "repos/" + userName + "/" + repoName;
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
//        String result = "";
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().
//                connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS).build();
//        Request request = new Request.Builder()
//                .url(url)
//                //.header("Accept","application/vnd.github.v3+json")
//                .addHeader("Accept", "application/vnd.github.v3+json")
//                .addHeader("Authorization", "token " + ACCESS_TOKEN)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            result = (response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println(result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject.containsKey("message") && jsonObject.getString("message").contains("Not Found"))
            return ApiResult.fail("github未找到目标repository");

        GithubRepoInfoBO githubRepoInfo = new GithubRepoInfoBO();
        githubRepoInfo.setId(jsonObject.getString("id"));
        githubRepoInfo.setName(jsonObject.getString("name"));
        githubRepoInfo.setUsername(userName);
        githubRepoInfo.setDescription(jsonObject.getString("description"));
        githubRepoInfo.setLanguage(jsonObject.getString("language"));
        githubRepoInfo.setSize(jsonObject.getInteger("size"));
        githubRepoInfo.setLastPush(jsonObject.getDate("pushed_at"));
        githubRepoInfo.setLicense(jsonObject.getJSONObject("license").getString("key"));
        githubRepoInfo.setRepoUpdateTime(jsonObject.getDate("updated_at"));
        githubRepoInfo.setRepoCreatedTime(jsonObject.getDate("created_at"));

        boolean ifSuccess = githubRepoInfoRepo.creatOrUpdate(githubRepoInfo);
        if (ifSuccess)
            return ApiResult.success();
        else
            return null;
    }

    @Transactional
    public ApiResult getGithubRepoContributors(String userName, String repoName) {
        String url = BASEURL + "repos/" + userName + "/" + repoName + "/contributors";
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);

        GithubRepoInfo g = new GithubRepoInfo();
        g.setName(repoName);
        g.setUsername(userName);
        GithubRepoInfo githubRepoInfo = githubRepoInfoRepo.queryByKeys(g);
        if (githubRepoInfo == null)
        {
            getGithubRepoData(userName, repoName);
            githubRepoInfo = githubRepoInfoRepo.queryByKeys(g);
        }

        String repoId = githubRepoInfo.getId();
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().
//                connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS).build();
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Accept", "application/vnd.github.v3+json")
//                .addHeader("Authorization", "token " + ACCESS_TOKEN)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        try {
//            Response response = call.execute();
//            result = (response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println(result);
        JSONArray jsonArray = JSON.parseArray(result);
//        List<RepoContributor> list = new ArrayList<>();
        Iterator<Object> it = jsonArray.iterator();
        while (it.hasNext()) {
            JSONObject arrayObj = (JSONObject) it.next();
            RepoContributorBO repoContributor = new RepoContributorBO();
            repoContributor.setRepoId(repoId);
            repoContributor.setId(arrayObj.getString("id"));
            repoContributor.setName(arrayObj.getString("login"));
            repoContributor.setContributions(arrayObj.getInteger("contributions"));

            contributorRepo.create(repoContributor);
        }

        return ApiResult.success();
    }

    public ApiResult getGithubRepoComments(String userName,String repoName)
    {
        getGithubRepoContributors(userName,repoName);
        String url = BASEURL + "repos/" + userName + "/" + repoName + "/comments";
        String result = "";

        GithubRepoInfo g = new GithubRepoInfo();
        g.setName(repoName);
        g.setUsername(userName);
        GithubRepoInfo githubRepoInfo = githubRepoInfoRepo.queryByKeys(g);
//        if (githubRepoInfo == null)
//        {
//            getGithubRepoData(userName, repoName);
//            githubRepoInfo = githubRepoInfoRepo.queryByKeys(g);
//        }

        String repoId = githubRepoInfo.getId();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("Authorization", "token " + ACCESS_TOKEN)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            result = (response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        JSONArray jsonArray = JSON.parseArray(result);
        List<CommentRepo> list = new ArrayList<>();
        Iterator<Object> it = jsonArray.iterator();
        while (it.hasNext()) {
            JSONObject arrayObj = (JSONObject) it.next();
            RepoCommentBO repoCommentBO = new RepoCommentBO();
            repoCommentBO.setId(arrayObj.getString("id"));
            repoCommentBO.setUserId(arrayObj.getJSONObject("user").getString("id"));
            repoCommentBO.setRepoId(repoId);
            repoCommentBO.setCommitId(arrayObj.getString("commit_id"));
            repoCommentBO.setBody(arrayObj.getString("body"));
            repoCommentBO.setCommentCreatedTime(arrayObj.getDate("created_at"));
            repoCommentBO.setCommentUpdateTime(arrayObj.getDate("updated_at"));
            repoCommentBO.setLine(arrayObj.getInteger("line"));
            repoCommentBO.setPath(arrayObj.getString("path"));

            commentRepo.create(repoCommentBO);
        }
        return ApiResult.success();
    }

//    public ApiResult getGithubRepoRelationship(String userName,String repoName)
//    {
//
//    }
}
