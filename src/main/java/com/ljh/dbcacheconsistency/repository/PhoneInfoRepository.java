package com.ljh.dbcacheconsistency.repository;

import com.ljh.dbcacheconsistency.entity.PhoneInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface PhoneInfoRepository extends JpaRepository<PhoneInfo,Integer> {
    PhoneInfo findByPhoneId(Integer phoneId);



}