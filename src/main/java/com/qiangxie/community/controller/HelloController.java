package com.qiangxie.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author shkstart Email:shkstart@126.com
 * @Description 操作数据库的工具类
 * @date 上午9:10:02
 */
@Controller
public class HelloController {
    @GetMapping("/hello")
    //model用来存放
    public String Hello(@RequestParam(name="name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }
}
