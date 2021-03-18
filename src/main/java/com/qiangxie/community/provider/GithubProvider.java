package com.qiangxie.community.provider;

import com.alibaba.fastjson.JSON;
import com.qiangxie.community.dto.AccessTokenDTO;
import com.qiangxie.community.dto.GithubUser;
import jdk.nashorn.internal.scripts.JS;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author shkstart Email:shkstart@126.com
 * @Description 操作数据库的工具类
 * @date 上午9:10:02
 */

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();


        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //access_token=bd1310b044b5013b8ef07dabb26635c775eb0b82&scope=user&token_type=bearer
            //access_token是在不断变化的
            //此string返回的是这个值，但是要截取其中的access_token
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            System.out.println(string);
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            //返回是一个JSON的格式
            String string = response.body().string();
            //自动的将string变成对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
