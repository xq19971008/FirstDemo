package com.qiangxie.community.controller;

import com.qiangxie.community.dto.AccessTokenDTO;
import com.qiangxie.community.dto.GithubUser;
import com.qiangxie.community.mapper.UserMapper;
import com.qiangxie.community.model.User;
import com.qiangxie.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.UUID;

/**
 * @author shkstart Email:shkstart@126.com
 * @Description 操作数据库的工具类
 * @date 上午9:10:02
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        //code为第一步响应所接收到的代码
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);

//        System.out.println(githubUser);
        if (githubUser != null) {
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtGreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtGreate());
            userMapper.insert(user);

            response.addCookie(new Cookie("token", token));

//            登陆成功，写cookie和session
            request.getSession().setAttribute("githubUser", githubUser);
            //成功则跳转会主页面
            return "redirect:/";

        } else {
            //登陆失败，重新登陆
            //失败也跳转回页面
            return "redirect:/";
        }
    }


}
