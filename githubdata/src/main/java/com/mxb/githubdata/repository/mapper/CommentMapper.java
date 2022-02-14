package com.mxb.githubdata.repository.mapper;

import com.mxb.model.po.RepoComment;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface CommentMapper extends BaseMapper<RepoComment> {
}
