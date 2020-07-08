package com.example.demo.filter.handler;


import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: 杨春生
 * @Date: 2019/11/28 14:46
 * @Description:
 */
public class LicenseHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            Boolean out = (Boolean) context
                    .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if (!out) {
                SOAPMessage message = context.getMessage();
                SOAPEnvelope enve = message.getSOAPPart().getEnvelope();
                SOAPHeader header = enve.getHeader();
                SOAPBody body = enve.getBody();
                Node bn = body.getChildNodes().item(0);
                String partname = bn.getLocalName();
                if ("list".equals(partname) || "addUser".equals(partname)) {
                    if (header == null) {
                        // 添加一个错误信息
                        System.out.println("ttt");
                        SOAPFault fault = body.addFault();
                        fault.setFaultString("头部信息不能为空!");
                        throw new SOAPFaultException(fault);
                    }
                    @SuppressWarnings("unchecked")
                    Iterator<SOAPHeaderElement> iterator = header
                            .extractAllHeaderElements();
                    if (!iterator.hasNext()) {
                        // 添加一个错误信息
                        System.out.println("ttt");
                        SOAPFault fault = body.addFault();
                        fault.setFaultString("头部信息不正确!");
                        throw new SOAPFaultException(fault);
                    }
                    while (iterator.hasNext()) {
                        SOAPHeaderElement ele = iterator.next();
                        System.out.println(ele.getTextContent());
                        // 获取头部信息
                    }
                }
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {

    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
