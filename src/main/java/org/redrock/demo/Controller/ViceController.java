package org.redrock.demo.Controller;

import org.redrock.demo.Service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/oAuth")
public class ViceController {
    @Autowired
    private MainService mainService;
    //重定向到本服的授权登录页面
    @GetMapping("/authorize")
    public void authorize(String redirectUri, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("http://localhost:8080/login.html?redirectUri="+redirectUri);
    }
    //本服的授权并登录，生成code并将其存入数据库，携带code重定向回main
    @PostMapping("/login")
    public String login(String username, String password, String redirectUri, HttpServletResponse resp) throws IOException {
        String res = mainService.checkUser(username,password);
        if ("1".equals(res)){
            Random random = new Random();
            String code = String.valueOf(random.nextInt(10)*1000+random.nextInt(10)*100+random.nextInt(10)*10+random.nextInt(10));
            mainService.insertCode(username,code);
            resp.sendRedirect(redirectUri+"?code="+code);
            return null;
        }else {
            return res;
        }
    }
    //接收code，返回token
    @GetMapping("/getTokenByCode")
    public String getTokenByCode(String code) {
        String username=mainService.getUser(code);
        if(username!=null){
            String token = mainService.createToken(username);
            return token;
        }
        return "the code is wrong";
    }
    //接收token，返回用户信息（暂时只有用户名）
    @GetMapping("/getUserInfoByToken")
    public String getUserInfoByToken(String token){
        String username=mainService.verifyToken(token);
        if (username!=null){
            return mainService.getInfo(username).getUsername();
        }
        return "the token is wrong";
    }

}
