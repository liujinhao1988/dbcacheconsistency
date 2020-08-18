package com.ljh.dbcacheconsistency.controller;


import com.ljh.dbcacheconsistency.entity.PhoneInfo;
import com.ljh.dbcacheconsistency.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    @Autowired
    PhoneService phoneService;

    @GetMapping("/buyByPhoneId/{phoneId}")
    public void buyByPhoneId(@PathVariable("phoneId") Integer phoneId){
        phoneService.buyByPhoneId(phoneId);

    }


    @GetMapping("/findSpecsByPhoneId/{phoneId}")
    public PhoneInfo findSpecsByPhoneId(@PathVariable("phoneId") Integer phoneId){
        PhoneInfo specsByPhoneId = phoneService.findSpecsByPhoneId(phoneId);
        return specsByPhoneId;
    }
}
