package com.mxb.githubdata.repository.mapper;

import com.mxb.model.po.GithubUser;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface GithubUserMapper extends BaseMapper<GithubUser> {
}
