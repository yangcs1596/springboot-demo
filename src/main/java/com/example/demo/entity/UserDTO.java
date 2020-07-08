package com.example.demo.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @Author: 杨春生
 * @Date: 2019/11/28 14:39
 * @Description:
 */
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -1317712109244450480L;
    private String name;
    private Integer age;
    private String sex;
    private String father;
    private String mother;


}
