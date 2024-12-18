package com.example.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.payment.entity.Notify;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface NotifyMapper extends BaseMapper<Notify> {

}
