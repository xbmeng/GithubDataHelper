package com.mxb.githubdata.repository;

import com.mxb.githubdata.domain.entity.RepoContributorBO;
import com.mxb.githubdata.repository.mapper.ContributorMapper;
import com.mxb.model.po.RepoContributor;
import com.mxb.model.repo.BaseRepo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ContributorRepo extends BaseRepo<RepoContributor, RepoContributorBO> {

    @Resource
    ContributorMapper contributorMapper;

    public ContributorRepo() {
        super(RepoContributor.class, RepoContributorBO.class);
    }

    public boolean create(RepoContributorBO repoContributorBO)
    {
        if(queryById(repoContributorBO.getId()) != null)
            return true;
        return contributorMapper.insertSelective(repoContributorBO) > 0 ? true : false;
    }

    public RepoContributor queryById(String id)
    {
        return contributorMapper.selectByPrimaryKey(id);
    }

    public List<RepoContributor> queryAll()
    {
        return contributorMapper.selectAll();
    }
}
