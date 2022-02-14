package com.mxb.githubdata.repository;

import com.mxb.githubdata.domain.entity.UserFollowBO;
import com.mxb.githubdata.repository.mapper.UserFollowMapper;
import com.mxb.model.po.UserFollow;
import com.mxb.model.repo.BaseRepo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserFollowRepo extends BaseRepo<UserFollow, UserFollowBO> {
    public UserFollowRepo() {
        super(UserFollow.class, UserFollowBO.class);
    }

    @Resource
    UserFollowMapper userFollowMapper;

    public List<UserFollow> queryByUserId(String id)
    {
        return userFollowMapper.selectByUserId(id);
    }

    public boolean create(UserFollowBO userFollowBO)
    {
        return userFollowMapper.insertSelective(userFollowBO) > 0 ? true : false;
    }

    public UserFollow queryByKeys(UserFollowBO userFollow)
    {
        return userFollowMapper.selectOne((UserFollow)userFollow);
    }

    public List<String> queryByName(String userName)
    {
        return userFollowMapper.selectByUserName(userName);
    }

}
