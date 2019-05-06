package org.redrock.demo.Service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.redrock.demo.Entity.UserEntity;
import org.redrock.demo.Mapping.UserMapping;
import org.redrock.demo.Service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MainServiceImpl implements MainService {
    private String secret = "this is vice controller's secret";

    @Autowired
    private UserMapping userMapping;
    @Override
    public String checkUser(String username,String password) {
        String res = "用户名或密码错误";
        UserEntity user = userMapping.checkUser(username, password);
            if (user != null) {
                res = "1";
            }
        return res;
    }

    @Override
    public void insertCode(String username,String code) {
        userMapping.insertCode(username,code);
    }

    @Override
    public String getUser(String code) {
        return userMapping.getUser(code);
    }

    @Override
    public UserEntity getInfo(String username) {
        return userMapping.getInfo(username);
    }

    @Override
    public String createToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");

            Date nowDate = new Date();
            Date expireDate = getAfterDate(nowDate,0,0,0,0,2,0);

            String token = JWT.create()
                    .withHeader(map)

                    .withIssuer("vice")
                    .withSubject("vice controller's token")

                    .withClaim("username",username)
                    .withClaim("auth","nothing")

                    .withAudience("main")
                    .withIssuedAt(nowDate)
                    .withExpiresAt(expireDate)

                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public String verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("vice")
                    .build();

            DecodedJWT jwt = verifier.verify(token);

            String subject = jwt.getSubject();
            System.out.println(subject);

            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get("username");
            System.out.println(claim.asString());
            Claim claim1 = claims.get("auth");
            System.out.println(claim1.asString());

            List<String> audience = jwt.getAudience();
            System.out.println(audience.get(0));

            return claim.asString();
        } catch (JWTVerificationException exception){
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if(date == null){
            date = new Date();
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        if(year != 0){
            cal.add(Calendar.YEAR, year);
        }
        if(month != 0){
            cal.add(Calendar.MONTH, month);
        }
        if(day != 0){
            cal.add(Calendar.DATE, day);
        }
        if(hour != 0){
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if(minute != 0){
            cal.add(Calendar.MINUTE, minute);
        }
        if(second != 0){
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }
}
