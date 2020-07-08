package com.example.demo.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * @Author: 杨春生
 * @Date: 2019/11/20 17:57
 * @Description:
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileEntity", propOrder = {
        "id",
        "test"
})
public class FileEntity implements Serializable {
    private static final long serialVersionUID = 983354952117407868L;
    @XmlElement(nillable = true)
    private String absolute_path;
    @XmlElement(nillable = true)
    private String prn;
    @XmlElement(nillable = true)
    private String appConfig;
    private String id;
    private String test;
}
