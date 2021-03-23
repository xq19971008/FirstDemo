package com.qiangxie.community.mapper;

import com.qiangxie.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author shkstart Email:shkstart@126.com
 * @Description 操作数据库的工具类
 * @date 上午9:10:02
 */
@Mapper
public interface UserMapper {
    //如果是类，则不用写@Param
    @Insert("insert into user (account_id,name,token,gmt_create,gmt_modified) values (#{accountId},#{name},#{token},#{gmtGreate},#{gmtModified})")
    void insert(User user);


    //如果不是类，则需要加注解
    @Select("select * from user where token = #{token}")
    User finbByToken(@Param("token") String token);
}
