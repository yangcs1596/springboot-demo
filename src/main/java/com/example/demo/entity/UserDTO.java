package com.example.demo.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * @Author: 杨春生
 * @Date: 2019/11/28 14:39
 * @Description:
 */
@Data
@XmlRootElement(name="xml")
@XmlType(propOrder={"name","age","sex","father","mother"})
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -1317712109244450480L;
    @NotNull(message = "用户姓名不能为空")
    private String name;
    private Integer age;
    private String sex;
    private String father;
    private String mother;


}
