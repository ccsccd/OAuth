package org.redrock.demo.Controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/web")
public class MainController {
    //重定向到vice的授权接口
    @GetMapping("/leadToAuthorize")
    public void leadToAuthorize(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("http://localhost:8080/oAuth/authorize?" +
                "redirectUri="+ "http://localhost:8080/web/index");
    }
    //两次访问vice，先用code换token，再用token拉取用户信息
    @GetMapping("/index")
    public void index(String code, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String accessToken = restTemplate.getForObject("http://localhost:8080/oAuth/getTokenByCode?" +
                "code="+code, String.class);
        String username = restTemplate.getForObject("http://localhost:8080/oAuth/getUserInfoByToken?" +
                "token="+accessToken, String.class);
        req.getSession().setAttribute("username",username);
        resp.sendRedirect("/web/getUserInfo");
    }
    //展示用户信息
    @GetMapping("/getUserInfo")
    public String getUserInfo(HttpServletRequest req) {
        Object username = req.getSession().getAttribute("username");
        return "欢迎用户"+username.toString();
    }

}
