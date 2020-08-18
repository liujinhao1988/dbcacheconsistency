package com.ljh.dbcacheconsistency.test;

import com.ljh.dbcacheconsistency.entity.PhoneInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class testp {
    @Test
    void  test(){
        PhoneInfo phoneInfo=new PhoneInfo();
        phoneInfo.setPhoneId(2);
        Integer phoneStock = phoneInfo.getPhoneStock();
        System.out.println(phoneStock);

    }
}
