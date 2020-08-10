package com.example.demo.message;

import com.example.demo.entity.UserDTO;

/**
 * @Author: 杨春生
 * @Date: 2020/7/22 10:13
 * @Description:
 */
public interface iRabbitMq {
    void sendHello(UserDTO userDTO);
}
