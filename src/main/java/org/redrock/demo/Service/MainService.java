package org.redrock.demo.Service;

import org.redrock.demo.Entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface MainService {
    String checkUser(String username,String password);
    void insertCode(String username,String code);
    String getUser(String code);
    UserEntity getInfo(String username);
    String createToken(String username);
    String verifyToken(String token);
    Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second);
}
