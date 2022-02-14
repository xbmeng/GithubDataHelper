package com.mxb.githubdata.repository.mapper;

import com.mxb.model.po.GithubRepoInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

@Component
@Mapper
public interface GithubRepoInfoMapper extends BaseMapper<GithubRepoInfo> {
}
