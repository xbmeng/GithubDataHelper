package com.mxb.githubdata.repository.mapper;

import com.mxb.model.po.RepoContributor;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface ContributorMapper extends BaseMapper<RepoContributor> {
}
