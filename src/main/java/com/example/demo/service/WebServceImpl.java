package com.example.demo.service;

import com.example.demo.entity.UserDTO;
import com.example.demo.service.imp.WebServer;

import javax.jws.WebService;
import java.util.List;

/**
 * @Author: 杨春生
 * @Date: 2019/11/28 14:43
 * @Description:
 */

@WebService(endpointInterface = "org.senssic.jaxweb.impl.WebServer", portName = "senssicServer")
public class WebServceImpl implements WebServer {

    @Override
    public int getAgeByName(String arg0) {
        return 0;
    }

    @Override
    public List<String> getNamesByAges(List<Integer> arg0) {
        return null;
    }

    @Override
    public List<UserDTO> getLUser() {
        return null;
    }

    @Override
    public List<UserDTO> getSUser() {
        return null;
    }

    @Override
    public byte[] getBFile(byte arg0) {
        return null;
    }

}
