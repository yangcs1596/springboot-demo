//package com.example.demo.util.officetool;
//
//import com.itextpdf.text.Image;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfSignatureAppearance;
//import com.itextpdf.text.pdf.PdfStamper;
//import com.itextpdf.text.pdf.security.*;
//
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.security.KeyStore;
//import java.security.PrivateKey;
//import java.security.cert.Certificate;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * @Author: 杨春生
// * @Date: 2020/1/13 10:43
// * @Description:
// */
//public class SignPositionPdf {
//    public static  ByteArrayOutputStream sign(InputStream pdfInputStream, //
//                                              InputStream keyStoreStream, //
//                                              Image signImage, //
//                                              List<OrderQuickDocumentPositionDTO> orderQuickDocumentPositionDTOList,
//                                              String reason, //
//                                              String location) throws Exception{
//        // 证书密码
//        String keyStorePassword = "password";
//        // 获取证书相关信息
//        // 读取pdf内容
//        PdfReader pdfReader = new PdfReader(pdfInputStream);
//        // 签章位置对象
//        List<RectangleCentre> rectangleCentreList = new LinkedList<>();
//        // 读取keystore
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        char[] password = keyStorePassword.toCharArray();
//        keyStore.load(keyStoreStream, password);
//        //
//        String alias = keyStore.aliases().nextElement();
//        // 获得私钥
//        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);
//        // 获得证书链
//        Certificate[] chain = keyStore.getCertificateChain(alias);
//
//        for (OrderQuickDocumentPositionDTO orderQuickDocumentPositionDTO : orderQuickDocumentPositionDTOList){
//            RectangleCentre rectangleCentreBase = new RectangleCentre();
//            rectangleCentreBase.setPage(orderQuickDocumentPositionDTO.getPageOrder());
//            // 类型 0 pdf右下角签名, 1 pdf关键字定位签名
//            rectangleCentreBase.setType(1);
//            rectangleCentreBase.setCentreX(orderQuickDocumentPositionDTO.getSignX().floatValue());
//            rectangleCentreBase.setCentreY();
//
//            rectangleCentreList.add(rectangleCentreBase);
//        }
//
//        if (rectangleCentreList.isEmpty()) {
//            return null;
//        }
//
//        ByteArrayOutputStream result = null;
//
//        RectangleCentre rectangleCentre;
//        for (int i = 0; i < rectangleCentreList.size(); i++) {
//            rectangleCentre = rectangleCentreList.get(i);
//            if (i > 0) {
//                // 多次签名，得重新读取
//                pdfReader = new PdfReader(result.toByteArray());
//            }
//            // 创建临时字节流 重复使用
//            result = new ByteArrayOutputStream();
//            // 创建签章工具
//            PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, result, '\0', null, true);
//            // 获取数字签章属性对象，设定数字签章的属性
//            PdfSignatureAppearance appearance = pdfStamper.getSignatureAppearance();
//            // 设置签章原因
//            appearance.setReason(reason);
//            // 设置签章地点
//            appearance.setLocation(location);
//            // 设置签章图片
//            appearance.setSignatureGraphic(signImage);
//            // 设置签章级别
//            appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
//            // 设置签章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
//            appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
//            // 设置签章位置 图章左下角x，原点为pdf页面左下角，图章左下角y，图章右上角x，图章右上角y
//            appearance.setVisibleSignature( rectangleCentre.getPosition(signImage), rectangleCentre.getPage(), null);
//
//            // 签章算法 可以自己实现
//            // 摘要算法
//            ExternalDigest digest = new BouncyCastleDigest();
//            // 签章算法
//            ExternalSignature signature = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA1, null);
//            // 进行盖章操作 CMS高级电子签名(CAdES)的长效签名规范
//            MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
//        }
//        // 写入输出流中
//        return result;
//    }
//}
