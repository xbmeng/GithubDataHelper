package com.mxb.githubdata.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mxb.common.base.ApiResult;
import com.mxb.common.util.DateUtil;
import com.mxb.common.util.GithubAPIRequestUtil;
import com.mxb.githubdata.domain.entity.GithubUserBO;
import com.mxb.githubdata.domain.entity.UserFollowBO;
import com.mxb.githubdata.repository.ContributorRepo;
import com.mxb.githubdata.repository.GithubUserRepo;
import com.mxb.githubdata.repository.UserFollowRepo;
import com.mxb.model.po.GithubUser;
import com.mxb.model.po.RepoContributor;
import com.mxb.model.po.UserFollow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class GithubUserService {
    public static final String BASEURL = "http://api.github.com/";

    //public static final String ACCESS_TOKEN = "06c78999cb0b07e20afcbc6cd1f1d5fcdb4e05";

    @Resource
    GithubUserRepo githubUserRepo;

    @Resource
    UserFollowRepo userFollowRepo;

    @Resource
    ContributorRepo contributorRepo;

    private static final int MAX_COT = 5;//每个人可以延伸出50条边

    public boolean isAccess(JSONObject jsonObject) {
        if (jsonObject.containsKey("message") && jsonObject.getString("message").contains("access blocked"))
            return false;
        if (jsonObject.containsKey("message") && jsonObject.getString("message").contains("Not Found"))
            return false;
        return true;
    }

//    private String getDataFromGithubAPI(String url) {
//        String result = "";
//        MyOkHttpRetryInterceptor myOkHttpRetryInterceptor =
//                new MyOkHttpRetryInterceptor.Builder()
//                        .executionCount(3)
//                        .retryInterval(2000)
//                        .build();
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
//                .addInterceptor(myOkHttpRetryInterceptor)
//                .readTimeout(10, TimeUnit.SECONDS).build();
//        Request request = new Request.Builder()
//                .url(url)
//                //.header("Accept","application/vnd.github.v3+json")
//                .addHeader("Accept", "application/vnd.github.v3+json")
//                .addHeader("Authorization", " token " + ACCESS_TOKEN + "0f")
//                .build();
//        Call call = okHttpClient.newCall(request);
//
//        Response response = null;
//        try {
//            response = call.execute();
//            result = (response.body().string());
//        } catch (Exception e) {
//            System.out.println("--------捕获异常-------");
//            e.printStackTrace();
//            result = getDataFromGithubAPI(url);
//        }finally {
//            response.body().close();
//        }
//        System.out.println("请求成功: " + url);
//
//        return result;
//    }

    public ApiResult getUserInfo(String userName) {
        String url = BASEURL + "users/" + userName;
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
        Object resJson = JSON.parse(result);
        if (resJson instanceof JSONObject)
        {
            JSONObject temp = (JSONObject) resJson;
            while (temp.getString("message") != null && temp.getString("message").contains("API rate limit"))
            {
                System.out.println("rate limit");
                System.out.println(result);
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
                resJson = JSON.parse(result);
                if(resJson instanceof JSONObject)
                    temp = (JSONObject) resJson;
                else
                    break;
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!isAccess(jsonObject))
            return ApiResult.fail("github未找到目标");

        GithubUserBO githubUserBO = new GithubUserBO();
        githubUserBO.setId(jsonObject.getString("id"));
        githubUserBO.setUserName(jsonObject.getString("login"));
        githubUserBO.setJoiningTime(jsonObject.getDate("created_at"));
        githubUserBO.setFollowersNumber(jsonObject.getInteger("followers"));
        githubUserBO.setFollowingNumber(jsonObject.getInteger("following"));
        githubUserBO.setRepoNumber(jsonObject.getInteger("public_repos"));

        String reposUrl = BASEURL + "users/" + userName + "/repos";
        JSONObject userRepo = getUserRepo(reposUrl);
        githubUserBO.setStars(userRepo.getString("stars"));
        githubUserBO.setReposInfo(userRepo.getString("repo_infos"));
        githubUserBO.setLangusge(userRepo.getString("language"));

        JSONArray events = getUserEvents(BASEURL + "users/" + userName + "/events");
        githubUserBO.setReceivedEvents(events.toJSONString());

        boolean ifSuccess = githubUserRepo.creatOrUpdate(githubUserBO);
        if (ifSuccess)
            return ApiResult.success();
        else
            return ApiResult.fail("数据库插入失败");
    }

    private JSONObject getUserRepo(String url) {
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
        Object resJson = JSON.parse(result);
        if (resJson instanceof JSONObject)
        {
            JSONObject temp = (JSONObject) resJson;
            while (temp.getString("message") != null && temp.getString("message").contains("API rate limit"))
            {
                System.out.println("rate limit");
                System.out.println(result);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
                resJson = JSON.parse(result);
                if(resJson instanceof JSONObject)
                    temp = (JSONObject) resJson;
                else
                    break;
            }
        }
        JSONArray jsonArray = JSON.parseArray(result);
        JSONObject ret = new JSONObject();
        if (jsonArray.size() == 0) {
            System.out.println("此用户没有仓库");
            return ret;
        }

        JSONArray stars = new JSONArray();
        JSONArray repoInfos = new JSONArray();
        Map<String, Long> languageMap = new LinkedHashMap<String, Long>();
        Iterator<Object> it = jsonArray.iterator();
        while (it.hasNext()) {
            JSONObject arrayObj = (JSONObject) it.next();

            String id = arrayObj.getString("id");
            String name = arrayObj.getString("name");

            JSONObject star = new JSONObject();//stargazers_count
            JSONObject repoInfo = new JSONObject();
            star.put("repo_id", id);
            star.put("repo_name", name);
            star.put("stars", arrayObj.getInteger("stargazers_count"));

            repoInfo.put("repo_id", id);
            repoInfo.put("repo_name", name);
            repoInfo.put("size", arrayObj.getInteger("size"));

            String languageUrl = arrayObj.getString("languages_url");
            JSONObject language = getUserLanguage(languageUrl);
//            System.out.println(language);
            language.entrySet().forEach(item -> {
//                        System.out.println(item);
                        if (!languageMap.containsKey(item.getKey())) {
                            languageMap.put(item.getKey(), Long.valueOf(item.getValue().toString()));
                        } else {
                            long lineNum = languageMap.get(item.getKey());
                            languageMap.put(item.getKey(), Long.valueOf(item.getValue().toString()) + lineNum);
                        }
                    }
            );
            stars.add(star);
            repoInfos.add(repoInfo);
        }
        ret.put("stars", stars);
        ret.put("repo_infos", repoInfos);
        ret.put("language", JSON.toJSONString(languageMap));
//        System.out.println(ret.getString("language"));
        return ret;
    }

    private JSONArray getUserEvents(String url) {
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
        Object resJson = JSON.parse(result);
        if (resJson instanceof JSONObject)
        {
            JSONObject temp = (JSONObject) resJson;
            while (temp.getString("message") != null && temp.getString("message").contains("API rate limit"))
            {
                System.out.println("rate limit");
                System.out.println(result);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
                resJson = JSON.parse(result);
                if(resJson instanceof JSONObject)
                    temp = (JSONObject) resJson;
                else
                    break;
            }
        }
        JSONArray jsonArray = JSON.parseArray(result);
        JSONArray ret = new JSONArray();
        if (jsonArray.size() == 0) {
            System.out.println("此用户最近没有事件");
            return ret;
        }
        Iterator<Object> it = jsonArray.iterator();
        //三件最近时间
        int cot = 3;
        while (it.hasNext() && cot > 0) {
            JSONObject arrayObj = (JSONObject) it.next();
            JSONObject item = new JSONObject();
            item.put("type", arrayObj.getString("type"));
            item.put("date", arrayObj.getDate("created_at"));
            item.put("repo", arrayObj.getJSONObject("repo").getString("name"));
            ret.add(item);
            cot--;
        }
        return ret;
    }

    private JSONObject getUserLanguage(String url) {
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
        Object resJson = JSON.parse(result);
        if (resJson instanceof JSONObject)
        {
            JSONObject temp = (JSONObject) resJson;
            while (temp.getString("message") != null && temp.getString("message").contains("API rate limit"))
            {
                System.out.println("rate limit");
                System.out.println(result);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
                resJson = JSON.parse(result);
                if(resJson instanceof JSONObject)
                    temp = (JSONObject) resJson;
                else
                    break;
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!isAccess(jsonObject))
            return new JSONObject();

        return jsonObject;
    }

    //TODO:修改github_follow表主码
    public ApiResult getUserFollow(String userName) {
        GithubUser g = new GithubUser();
        g.setUserName(userName);
        GithubUser temp = githubUserRepo.queryByKeys(g);
        if (temp == null) {
            getUserInfo(userName);
            temp = githubUserRepo.queryByKeys(g);
        }
        String userId = temp.getId();

        //获得正在following的信息
        JSONArray following = getFollowing(userName);
        Iterator followingIt = following.iterator();

        int cot = MAX_COT;
        while (followingIt.hasNext() && cot > 0) {
            JSONObject arrayObj = (JSONObject) followingIt.next();
            UserFollowBO userFollow = new UserFollowBO();
            userFollow.setUserId(userId);
            userFollow.setFollowing(arrayObj.getString("id"));
            if (githubUserRepo.queryById(userFollow.getFollowing()) == null)
                getUserInfo(arrayObj.getString("login"));

            if (userFollowRepo.queryByKeys(userFollow) == null)
                userFollowRepo.create(userFollow);

            cot--;
        }

        //获得follower的信息
        JSONArray followers = getFollower(userName);
        Iterator followerIt = followers.iterator();
        cot = MAX_COT;
        while (followerIt.hasNext() && cot > 0) {
            JSONObject arrayObj = (JSONObject) followerIt.next();
            UserFollowBO userFollowBO = new UserFollowBO();
            userFollowBO.setUserId(arrayObj.getString("id"));
            if (githubUserRepo.queryById(userFollowBO.getUserId()) == null)
                getUserInfo(arrayObj.getString("login"));
            userFollowBO.setFollowing(userId);

            if (userFollowRepo.queryByKeys(userFollowBO) == null)
                userFollowRepo.create(userFollowBO);

            cot--;
        }

        return ApiResult.success();
    }

    private JSONArray getFollowing(String userName) {
        String url = BASEURL + "users/" + userName + "/following";
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
        Object resJson = JSON.parse(result);
        if (resJson instanceof JSONObject)
        {
            JSONObject temp = (JSONObject) resJson;
            while (temp.getString("message") != null && temp.getString("message").contains("API rate limit"))
            {
                System.out.println("rate limit");
                System.out.println(result);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
                resJson = JSON.parse(result);
                if(resJson instanceof JSONObject)
                    temp = (JSONObject) resJson;
                else
                    break;
            }
        }
        JSONArray jsonArray = JSON.parseArray(result);

        if (jsonArray.size() == 0) {
            System.out.println(userName + "没有follow任何用户");
            return new JSONArray();
        }
        return jsonArray;
    }

    private JSONArray getFollower(String userName) {
        String url = BASEURL + "users/" + userName + "/followers";
        String result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
        Object resJson = JSON.parse(result);
        if (resJson instanceof JSONObject)
        {
            JSONObject temp = (JSONObject) resJson;
            while (temp.getString("message") != null && temp.getString("message").contains("API rate limit"))
            {
                System.out.println("rate limit");
                System.out.println(result);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = GithubAPIRequestUtil.getDataFromGithubAPI(url);
                resJson = JSON.parse(result);
                if(resJson instanceof JSONObject)
                    temp = (JSONObject) resJson;
                else
                    break;
            }
        }
        JSONArray jsonArray = JSON.parseArray(result);

        if (jsonArray.size() == 0) {
            System.out.println(userName + "没有follow任何用户");
            return new JSONArray();
        }
        return jsonArray;
    }

    public ApiResult getAllUsersFollow() {
        List<RepoContributor> repoContributorList = contributorRepo.queryAll();
        for (RepoContributor r : repoContributorList)
        {
            List<UserFollow> followList = userFollowRepo.queryByUserId(r.getId());
            if(followList.size() >= MAX_COT)
                continue;
            getUserFollow(r.getName());
        }

        return ApiResult.success();
    }

    public JSONObject getUserBasicInfo(String userName)
    {
        GithubUser temp = new GithubUser();
        temp.setUserName(userName);
        GithubUser githubUser = githubUserRepo.queryByKeys(temp);
        JSONObject jsonObject = new JSONObject();
        if(githubUser == null)
            return jsonObject;

        jsonObject.put("Repos", githubUser.getRepoNumber());
        jsonObject.put("following_number", githubUser.getFollowingNumber());
        jsonObject.put("follower_number", githubUser.getFollowersNumber());
        jsonObject.put("joining_time", DateUtil.DateToDay(githubUser.getJoiningTime()));

        return jsonObject;
    }

    public JSONObject getUserLangInfo(String userName)
    {
        GithubUser temp = new GithubUser();
        temp.setUserName(userName);
        GithubUser githubUser = githubUserRepo.queryByKeys(temp);
        if(githubUser == null)
            return new JSONObject();

        JSONObject langObj = (JSONObject)JSON.parse(githubUser.getLangusge());
        System.out.println(langObj);

        return langObj;
    }

    public JSONArray getUserRecentEvents(String userName)
    {
        GithubUser temp = new GithubUser();
        temp.setUserName(userName);
        GithubUser githubUser = githubUserRepo.queryByKeys(temp);
        if(githubUser == null)
            return new JSONArray();

        JSONArray events = (JSONArray) JSON.parse(githubUser.getReceivedEvents());

        return events;
    }

    public JSONArray getReposSize(String userName)
    {
        GithubUser temp = new GithubUser();
        temp.setUserName(userName);
        GithubUser githubUser = githubUserRepo.queryByKeys(temp);
        if(githubUser == null)
            return new JSONArray();

        JSONArray reposSize = (JSONArray) JSON.parse(githubUser.getReposInfo());

        return reposSize;
    }

    public JSONArray getUserRepoStars(String userName)
    {
        GithubUser temp = new GithubUser();
        temp.setUserName(userName);
        GithubUser githubUser = githubUserRepo.queryByKeys(temp);
        if(githubUser == null)
            return new JSONArray();

        JSONArray repoStars = (JSONArray) JSON.parse(githubUser.getStars());

        return repoStars;
    }

    public JSONArray getUserFollowInfo(String userName)
    {
        List<String> userFollows = userFollowRepo.queryByName(userName);
        JSONArray userFollowArray = new JSONArray();

        if(userFollows == null)
            return userFollowArray;

        System.out.println(userFollows);
        for(String item : userFollows)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("source", userName);
            jsonObject.put("des", item);
            userFollowArray.add(jsonObject);
        }

        return userFollowArray;
    }

}
