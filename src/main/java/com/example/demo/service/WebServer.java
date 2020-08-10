package com.example.demo.service;

import com.example.demo.entity.UserDTO;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;

/**
 * @Author: 杨春生
 * @Date: 2019/11/28 14:41
 * @Description:
 */
@WebService(name = "WebServer", targetNamespace = "http://jaxweb.senssic.org/")
@HandlerChain(file = "handler-chain.xml")
// 使用handler过滤连
public interface WebServer {

    @WebMethod
    public int getAgeByName(@WebParam(name = "name") String arg0);

    @WebMethod
    public List<String> getNamesByAges(
            @WebParam(name = "age", targetNamespace = "") List<Integer> arg0);

    @WebMethod
    public List<UserDTO> getLUser();

    @WebMethod
    public List<UserDTO> getSUser();

    @WebMethod
    @WebResult(targetNamespace = "", header = true)//位于头信息
    @RequestWrapper(localName = "getBFile", targetNamespace = "http://jaxweb.senssic.org/", className = "org.senssic.jaxweb.GetBFile")
    @ResponseWrapper(localName = "getBFileResponse", targetNamespace = "http://jaxweb.senssic.org/", className = "org.senssic.jaxweb.GetBFileResponse")
    public byte[] getBFile(
            @WebParam(name = "byt", targetNamespace = "") byte arg0);

}
