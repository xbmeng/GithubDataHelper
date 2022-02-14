package com.mxb.githubdata.repository;

import com.mxb.githubdata.domain.entity.RepoCommentBO;
import com.mxb.githubdata.domain.entity.RepoContributorBO;
import com.mxb.githubdata.repository.mapper.CommentMapper;
import com.mxb.model.po.RepoComment;
import com.mxb.model.po.RepoContributor;
import com.mxb.model.repo.BaseRepo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class CommentRepo extends BaseRepo<RepoComment, RepoCommentBO> {
    public CommentRepo() {
        super(RepoComment.class, RepoCommentBO.class);
    }

    @Resource
    CommentMapper commentMapper;

    public boolean create(RepoCommentBO repoCommentBO)
    {
        if(queryById(repoCommentBO.getId()) != null)
            return true;
        return commentMapper.insertSelective(repoCommentBO) > 0 ? true : false;
    }

    public RepoComment queryById(String id)
    {
        return commentMapper.selectByPrimaryKey(id);
    }
}
