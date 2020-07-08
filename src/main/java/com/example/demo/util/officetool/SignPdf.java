package com.example.demo.util.officetool;

import com.google.common.collect.Maps;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfSignatureAppearance.RenderingMode;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.security.*;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class SignPdf {
    /**
     * pdf右下角x坐标
     */
    public static final Integer PDF_RIGHT_DOWN_POINT_X = 595;

    /**
     * @param password     秘钥密码
     * @param keyStorePath 秘钥文件路径
     * @param signPdfSrc   签名的PDF文件
     * @param signImage    签名图片文件
     * @param x            x坐标
     * @param y            y坐标
     * @return
     */
    public static byte[] sign(String password, String keyStorePath, String signPdfSrc, String signImage,
                              float x, float y) {
        File signPdfSrcFile = new File(signPdfSrc);
        PdfReader reader = null;
        ByteArrayOutputStream signPDFData = null;
        PdfStamper stp = null;
        FileInputStream fos = null;
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            KeyStore ks = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
            fos = new FileInputStream(keyStorePath);
            // 私钥密码 为Pkcs生成证书是的私钥密码 123456
            ks.load(fos, password.toCharArray());
            String alias = (String) ks.aliases().nextElement();
            PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);
            reader = new PdfReader(signPdfSrc);
            signPDFData = new ByteArrayOutputStream();
            // 临时pdf文件
            File temp = new File(signPdfSrcFile.getParent(), System.currentTimeMillis() + ".pdf");
            stp = PdfStamper.createSignature(reader, signPDFData, '\0', temp, true);
            stp.setFullCompression();
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setReason("数字签名，不可改变");
            // 使用png格式透明图片
            Image image = Image.getInstance(signImage);
            sap.setImageScale(0);
            sap.setSignatureGraphic(image);
            sap.setRenderingMode(RenderingMode.GRAPHIC);
            // 是对应x轴和y轴坐标
            sap.setVisibleSignature(new Rectangle(x, y, x + 185, y + 68), 1,
                    UUID.randomUUID().toString().replaceAll("-", ""));
            stp.getWriter().setCompressionLevel(5);
            ExternalDigest digest = new BouncyCastleDigest();
            ExternalSignature signature = new PrivateKeySignature(key, DigestAlgorithms.SHA512, provider.getName());
            MakeSignature.signDetached(sap, digest, signature, chain, null, null, null, 0, CryptoStandard.CADES);
            stp.close();
            reader.close();
            return signPDFData.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (signPDFData != null) {
                try {
                    signPDFData.close();
                } catch (IOException e) {
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        byte[] fileData = sign("123456", "E:\\home\\keystore.p12", //
//                "E:\\home\\1111.pdf",//
//                "E:\\home\\7.jpg", 50, 290);
//        FileOutputStream f = new FileOutputStream(new File("E:\\home\\1111_signed_dest.pdf"));
//        f.write(fileData);
//        f.close();
//    }

    public static void main(String[] args) throws GeneralSecurityException, IOException, DocumentException {

        InputStream in = new FileInputStream("D:\\测试文件夹\\三类技能证书提交要求.pdf");

        InputStream keyStoreStream = new FileInputStream("E:\\\\home\\\\keystore.p12");
        //InputStream keyStoreStreamNew = new FileInputStream("E:\\home\\keystore.p12");
        String reason = "电子签名";
        String location = "厦门";
        String chapterPath = "D:\\测试文件夹\\picture0.jpg";
        String chapterContent = "SIGN";

        // 图片签章
        Image signImage = Image.getInstance(chapterPath);
        // 文本签章
        Image signImageText = Image.getInstance(getImage(chapterContent).toByteArray());

        // 首次签订
        ByteArrayOutputStream byteOut = sign(in, keyStoreStream, signImage, "当事人签署：", reason, location);
        in.close();
        if (byteOut == null) {
            return;
        }

        try (OutputStream out = new FileOutputStream("D:\\\\测试文件夹\\\\三类技能证书提交要求3333.pdf")) {
            out.write(byteOut.toByteArray());
        }

    }


    /**
     * @param pdfInputStream 需要签章的pdf 输入流
     * @param keyStoreStream 证书输入流
     * @param signImage      签章图片（可以是文字生成的图片）
     * @param signKeyWord    签章关键字
     * @param reason         签订原因
     * @param location       签订地址
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static ByteArrayOutputStream sign(InputStream pdfInputStream, ////需要签章的pdf文件路径
                                             InputStream keyStoreStream, //  签完章的pdf文件路径
                                             Image signImage, // p12 路径
                                             String signKeyWord, //
                                             String reason, // 签名的原因，显示在pdf签名属性中，随便填
                                             String location) // 签名的地点，显示在pdf签名属性中，随便填
            throws GeneralSecurityException, IOException, DocumentException {
        // 证书密码
        String keyStorePassword = "password";
        // 读取pdf内容
        PdfReader pdfReader = new PdfReader(pdfInputStream);
        int pageNum = pdfReader.getNumberOfPages();
        // 签章位置对象
        List<RectangleCentre> rectangleCentreList = new LinkedList<>();
        //用于存放已经存在签名的pdf页码
        Map<Integer, Integer> hasSignPageMap = Maps.newHashMap();
        // 下标从1开始
        for (int page = 1; page <= pageNum; page++) {
            RectangleCentre rectangleCentreBase = new RectangleCentre();
            rectangleCentreBase.setPage(page);
            // 类型 0 pdf右下角签名, 1 pdf关键字定位签名
            rectangleCentreBase.setType(0);
            rectangleCentreBase.setCentreX(PDF_RIGHT_DOWN_POINT_X);
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
            pdfReaderContentParser.processContent(page, new RenderListener() {

                StringBuilder sb = new StringBuilder("");
                int maxLength = signKeyWord.length();

                @Override
                public void renderText(TextRenderInfo textRenderInfo) {
                    // 只适用 单字块文档 以及 关键字整个为一个块的情况
                    // 设置 关键字文本为单独的块，不然会错位
                    boolean isKeywordChunk = textRenderInfo.getText().length() == maxLength;
                    if (isKeywordChunk) {
                        // 文档按照 块 读取
                        sb.delete(0, sb.length());
                        sb.append(textRenderInfo.getText());
                    } else {
                        // 有些文档 单字一个块的情况
                        // 拼接字符串
                        sb.append(textRenderInfo.getText());
                        // 去除首部字符串，使长度等于关键字长度
                        if (sb.length() > maxLength) {
                            sb.delete(0, sb.length() - maxLength);
                        }
                    }
                    // 判断是否匹配上
                    if (signKeyWord.equals(sb.toString())) {
                        RectangleCentre rectangleCentre = rectangleCentreBase.copy();

                        // 计算中心点坐标

                        com.itextpdf.awt.geom.Rectangle2D.Float baseFloat = textRenderInfo.getBaseline()
                                .getBoundingRectange();
                        com.itextpdf.awt.geom.Rectangle2D.Float ascentFloat = textRenderInfo.getAscentLine()
                                .getBoundingRectange();

                        float centreX;
                        float centreY;
                        if (isKeywordChunk) {
                            centreX = baseFloat.x + ascentFloat.width / 2;
                            centreY = baseFloat.y + ((ascentFloat.y - baseFloat.y) / 2);
                        } else {
                            centreX = baseFloat.x + ascentFloat.width - (maxLength * ascentFloat.width / 2);
                            centreY = baseFloat.y + ((ascentFloat.y - baseFloat.y) / 2);
                        }

                        rectangleCentre.setCentreX(centreX + 55);
                        rectangleCentre.setCentreY(centreY - 15);
                        // 类型 0 pdf右下角签名, 1 pdf关键字定位签名
                        rectangleCentre.setType(1);
                        hasSignPageMap.put(rectangleCentre.getPage(), rectangleCentre.getPage());
                        rectangleCentreList.add(rectangleCentre);
                        // 匹配完后 清除
                        sb.delete(0, sb.length());
                    }
                }


                @Override
                public void renderImage(ImageRenderInfo arg0) {
                    // nothing
                }

                @Override
                public void endTextBlock() {
                    // nothing
                }

                @Override
                public void beginTextBlock() {
                    // nothing
                }
            });
            //如果当前页没有签名,在页面右下角签名
            if (!hasSignPageMap.containsKey(page)) {
                hasSignPageMap.put(page, page);
                rectangleCentreList.add(rectangleCentreBase);
            }
        }
        if (rectangleCentreList.isEmpty()) {
            return null;
        }

        // 获取证书相关信息
        // 读取keystore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        char[] password = keyStorePassword.toCharArray();
        keyStore.load(keyStoreStream, password);
        //
        String alias = keyStore.aliases().nextElement();
        // 获得私钥
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);
        // 获得证书链
        Certificate[] chain = keyStore.getCertificateChain(alias);

        ByteArrayOutputStream result = null;

        RectangleCentre rectangleCentre;
        for (int i = 0; i < rectangleCentreList.size(); i++) {
            rectangleCentre = rectangleCentreList.get(i);
            if (i > 0) {
                // 多次签名，得重新读取
                pdfReader = new PdfReader(result.toByteArray());
            }
            // 创建临时字节流 重复使用
            result = new ByteArrayOutputStream();
            // 创建签章工具
            PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, result, '\0', null, true);
            // 获取数字签章属性对象，设定数字签章的属性
            PdfSignatureAppearance appearance = pdfStamper.getSignatureAppearance();
            // 设置签章原因
            appearance.setReason(reason);
            // 设置签章地点
            appearance.setLocation(location);
            // 设置签章图片
            appearance.setSignatureGraphic(signImage);
            // 设置签章级别
            appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
            // 设置签章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
            appearance.setRenderingMode(RenderingMode.GRAPHIC);
            // 设置签章位置 图章左下角x，原点为pdf页面左下角，图章左下角y，图章右上角x，图章右上角y
            appearance.setVisibleSignature(((Integer) 1).equals(rectangleCentre.getType()) ? rectangleCentre.getRectangle(signImage) : rectangleCentre.getDefault(signImage), rectangleCentre.getPage(), null);

            // 签章算法 可以自己实现
            // 摘要算法
            ExternalDigest digest = new BouncyCastleDigest();
            // 签章算法
            ExternalSignature signature = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA1, null);
            // 进行盖章操作 CMS高级电子签名(CAdES)的长效签名规范
            MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, CryptoStandard.CMS);
        }
        // 写入输出流中
        return result;
    }


    public static ByteArrayOutputStream getImage(String content) throws IOException {
        /*
         * Because font metrics is based on a graphics context, we need to create a
         * small, temporary image so we can ascertain the width and height of the final
         * image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("宋体", Font.PLAIN, 50);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(content);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.RED);
        g2d.drawString(content, 0, fm.getAscent());
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos;
    }

}
