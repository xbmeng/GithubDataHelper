package com.mxb.githubdata.repository;

import com.mxb.githubdata.domain.entity.GithubUserBO;
import com.mxb.githubdata.repository.mapper.GithubUserMapper;
import com.mxb.model.po.GithubUser;
import com.mxb.model.repo.BaseRepo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class GithubUserRepo extends BaseRepo<GithubUser, GithubUserBO> {

    public GithubUserRepo() {
        super(GithubUser.class, GithubUserBO.class);
    }

    @Resource
    GithubUserMapper githubUserMapper;

    public GithubUser queryById(String id) {
        return githubUserMapper.selectByPrimaryKey(id);
    }

    public GithubUser queryByKeys(GithubUser githubUser) {
//        int
        return githubUserMapper.selectOne(githubUser);
    }

    public boolean creatOrUpdate(GithubUserBO githubUserBO) {
        if (queryById(githubUserBO.getId()) == null)
            return create(githubUserBO);
        return update(githubUserBO);
    }

    public boolean create(GithubUserBO githubUserBO) {
        return githubUserMapper.insertSelective(githubUserBO) > 0 ? true : false;
    }

    public boolean update(GithubUserBO githubUserBO) {
        return githubUserMapper.updateByPrimaryKeySelective(githubUserBO) > 0 ? true : false;
    }

}
