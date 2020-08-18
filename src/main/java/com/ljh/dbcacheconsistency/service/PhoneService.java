package com.ljh.dbcacheconsistency.service;

import com.ljh.dbcacheconsistency.entity.PhoneInfo;

public interface PhoneService {
    void buyByPhoneId(Integer phoneId);

    PhoneInfo findSpecsByPhoneId(Integer phoneId);
}
