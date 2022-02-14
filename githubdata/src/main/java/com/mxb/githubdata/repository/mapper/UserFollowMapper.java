package com.mxb.githubdata.repository.mapper;

import com.mxb.model.po.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    @Select("select * from github_follow where user_id = #{userId}")
    public List<UserFollow> selectByUserId(@Param("userId") String id);

    @Select("select c.username from (select user_id,username,following from github_follow a join github_user b on a.user_id = b.id) temp join github_user c on temp.following = c.id where temp.username = #{userName}")
    public List<String> selectByUserName(@Param("userName") String name);
}
