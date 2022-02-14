package com.mxb.githubdata.repository;

import com.mxb.githubdata.domain.entity.GithubRepoInfoBO;
import com.mxb.githubdata.repository.mapper.GithubRepoInfoMapper;
import com.mxb.model.po.GithubRepoInfo;
import com.mxb.model.repo.BaseRepo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class GithubRepoInfoRepo extends BaseRepo<GithubRepoInfo, GithubRepoInfoBO> {

    @Resource
    GithubRepoInfoMapper githubRepoInfoMapper;

    public GithubRepoInfoRepo() {
        super(GithubRepoInfo.class, GithubRepoInfoBO.class);
    }

    public boolean creatOrUpdate(GithubRepoInfoBO githubRepoInfoBO)
    {
        if(queryById(githubRepoInfoBO.getId()) == null)
            return creat(githubRepoInfoBO);
        else
            return update(githubRepoInfoBO);
    }

    public GithubRepoInfo queryById(String id){return githubRepoInfoMapper.selectByPrimaryKey(id);}

    public boolean creat(GithubRepoInfoBO githubRepoInfoBO)
    {
        return githubRepoInfoMapper.insertSelective(githubRepoInfoBO) > 0 ? true: false;
    }

    public boolean update(GithubRepoInfoBO githubRepoInfoBO)
    {
        return githubRepoInfoMapper.updateByPrimaryKeySelective(githubRepoInfoBO) > 0 ? true : false;
    }

    public GithubRepoInfo queryByKeys(GithubRepoInfo githubRepoInfo)
    {
        return githubRepoInfoMapper.selectOne(githubRepoInfo);
    }

}
