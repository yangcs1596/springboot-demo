package com.example.demo.util;

import cn.hutool.core.lang.UUID;
import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * word,pdf转化为图片工具类
 *
 * @Author: 杨春生
 * @Date: 2019/11/21 09:01
 * @Description:
 */
@Slf4j
public class OfficeUtil {
//    @Autowired
//    private DocumentConverter converter;
//
//    public void WordToImage(File inputFile, File outputFile) throws OfficeException {
//        converter.convert(inputFile).to(outputFile).execute();
//    }

    private static OpenOfficeConnection startOpenOffice() {
        //OpenOffice的安装目录，linux环境下需要手动启动openoffice服务
        String OpenOffice_HOME = "D:\\OpenOffice\\OpenOffice4\\";
        // 启动OpenOffice的服务
        String command = OpenOffice_HOME + "program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("启动openoffice服务异常！");
        }
        //创建连接
        OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
        return connection;
    }

    /**
     * 功能描述:  doc转为pdf
     */
    private static void doc2Pdf(String docPath, String pdfPath) {
        File inputFile = new File(docPath);
        File outputFile = new File(pdfPath);
        OpenOfficeConnection connection = startOpenOffice();
        try {
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            //converter.convert(inputFile, outputFile);
            DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
            DocumentFormat txt = formatReg.getFormatByFileExtension("odt");
            DocumentFormat pdf = formatReg.getFormatByFileExtension("pdf");
            converter.convert(inputFile, txt, outputFile, pdf);
        } catch (ConnectException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 把ppt word excel等文件生成图片文件
     *
     * @param docPath    文件路径
     * @param imgDirPath 图片保存文件夹
     * @param fileName   文件名称点的前部分
     */
    public static void doc2Imags(String docPath, String imgDirPath, String fileName) {
        String pdfPath = String.format("%s%s.pdf", FilenameUtils.getFullPath(docPath), FilenameUtils.getBaseName(docPath));
        try {
            doc2Pdf(docPath, pdfPath);
            pdf2Imgs(pdfPath, imgDirPath, fileName);
            File pdf = new File(pdfPath);
            /*if(pdf.isFile()){
                pdf.delete();
            }*/
            System.out.println(pdfPath);
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将pdf转换成图片
     *
     * @param pdfPath
     * @param imgDirPath
     * @return 返回转换后图片的名字
     * @throws Exception
     */
    private static List<String> pdf2Imgs(String pdfPath, String imgDirPath, String fileName) throws Exception {
        Document document = new Document();
        document.setFile(pdfPath);
        float scale = 1f;//放大倍数
        float rotation = 0f;//旋转角度
        List<String> imgNames = new ArrayList<String>();
        int pageNum = document.getNumberOfPages();
        File imgDir = new File(imgDirPath);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        for (int i = 0; i < pageNum; i++) {
            BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
                    Page.BOUNDARY_CROPBOX, rotation, scale);
            RenderedImage rendImage = image;
            try {
                String filePath = imgDirPath + File.separator + fileName + i + ".png";
                File file = new File(filePath);
                ImageIO.write(rendImage, "png", file);
                imgNames.add(FilenameUtils.getName(filePath));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            image.flush();
        }
        document.dispose();
        return imgNames;
    }

    private static String getRelationName(Class dto) {
        if (dto == null) {
            return null;
        }
        UnderlinedNameConversion underlinedNameConversion = new UnderlinedNameConversion();
        String tableName = underlinedNameConversion.getTableName(dto);
        String indexName = "tb_" + tableName.replace("_d_t_o", StringUtils.EMPTY);
        return indexName;
    }

    /**
     * 编码格式错误的问题
     * @param path
     * @param content
     * @param encoding
     * @throws IOException
     */
    public static void write(String path, String content, String encoding)
            throws IOException {
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), encoding));
        writer.write(content);
        writer.close();
    }
    /**
     * 按格式读取文件内容
     * @param path
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String read(String path, String encoding) throws IOException {
        String content = "";
        File file = new File(path);
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), encoding));
        while ((line = reader.readLine()) != null) {
            content += line + "\n";
        }
        reader.close();
        return content;
    }

    public static void main(String[] args) throws Exception{
//        String p = new File("/home/file_server/").getAbsolutePath() + File.separator;
        System.out.println(Timestamp.valueOf(LocalDateTime.now()));
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
//        OfficeUtil.doc2Pdf("C:\\\\Users\\\\Administrator\\\\Desktop\\\\二维码\\\\测试文件夹\\\\123.docx","C:\\\\Users\\\\Administrator\\\\Desktop\\\\二维码\\\\测试文件夹\\\\OfficeUtil.pdf");
        OfficeUtil.pdf2Imgs("C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\OfficeUtil.pdf","C:\\Users\\Administrator\\Desktop\\二维码\\测试文件夹\\","OfficeUtil");
//        //解决流程
//        String str = "加急123||and456or|789";
//        System.out.println(str.substring(0, 2));
//        UserDTO userDTO = new UserDTO();
//        userDTO.setAge(4455);
//        System.out.println(userDTO);
//        File inputFile = new File("D:\\测试文件夹\\三类技能证书提交要求.docx");
//        File outputFile = new File("D:\\测试文件夹\\三类技能证书提交要求444.html");
//        OpenOfficeConnection connection =  new SocketOpenOfficeConnection("192.168.88.65", 8100);
//        try {
//            connection.connect();
//            //远程连接 用StreamOpenOfficeDocumentConverter
//            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
//            //DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
//            DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
//            DocumentFormat txt = formatReg.getFormatByFileExtension("odt");
//            DocumentFormat pdf = formatReg.getFormatByFileExtension("html");
//            converter.convert(inputFile, txt, outputFile, pdf);
//            //write("D:\\测试文件夹\\三类技能证书提交要求.html", read("D:\\测试文件夹\\三类技能证书提交要求.html", "GBK"), "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
    }

    /* ******* 递归树形数据 *************
    public static void main(String[] args) {
//        String docPath = "D:\\测试文件夹\\officeUtil\\三类技能证书提交要求.docx";
//        String pdfPath = "D:\\测试文件夹\\officeUtil\\";
//        doc2Imags(docPath, pdfPath, "picture");
//        getRelationName(UserDTO.class);
//        System.out.println(Pinyin4jUtil.getFirstSpellPinYin("令狐冲", false));
//        System.out.println(StringUtils.isBlank(null));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("ID", 1);
        m1.put("CODE", 001);
        m1.put("NAME", "一级1");
        m1.put("PARENTID", 0);
        m1.put("LEVEL", 1);
        data.add(m1);

        Map<String, Object> m3 = new HashMap<String, Object>();
        m3.put("ID", 3);
        m3.put("CODE", 00101);
        m3.put("NAME", "一级1的二级1");
        m3.put("PARENTID", 1);
        m3.put("LEVEL", 2);
        data.add(m3);

        Map<String, Object> m4 = new HashMap<String, Object>();
        m4.put("ID", 4);
        m4.put("CODE", 00102);
        m4.put("NAME", "一级1的二级2");
        m4.put("PARENTID", 1);
        m4.put("LEVEL", 2);
        data.add(m4);

        Map<String, Object> m5 = new HashMap<String, Object>();
        m5.put("ID", 5);
        m5.put("CODE", 0010101);
        m5.put("NAME", "一级1的二级1的三级1");
        m5.put("PARENTID", 3);
        m5.put("LEVEL", 3);
        data.add(m5);

        Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("ID", 2);
        m2.put("CODE", 002);
        m2.put("NAME", "一级2");
        m2.put("PARENTID", 0);
        m2.put("LEVEL", 1);
        data.add(m2);

        Map<String, Object> m6 = new HashMap<String, Object>();
        m6.put("ID", 6);
        m6.put("CODE", 00201);
        m6.put("NAME", "一级2的二级1");
        m6.put("PARENTID", 2);
        m6.put("LEVEL", 2);
        data.add(m6);

        Map<String, Object> m7 = new HashMap<String, Object>();
        m7.put("ID", 7);
        m7.put("CODE", 00202);
        m7.put("NAME", "一级2的二级2");
        m7.put("PARENTID", 2);
        m7.put("LEVEL", 2);
        data.add(m7);
        Tree tree = genTree(data, null);
        System.out.println(JSONObject.toJSON(tree));
    }*/
    private static Tree genTree(List<Map<String, Object>> data, Tree tree) {
        if (tree != null) {
            int id = tree.getId();
            List<Tree> children = tree.getChildren();
            for (Map<String, Object> map : data) {
                Integer parentid = Integer.valueOf(map.get("PARENTID").toString());
                if (parentid == id) {
                    Tree t = new Tree();
                    t.setId(Integer.valueOf(map.get("ID").toString()));
                    t.setCode(map.get("CODE").toString());
                    t.setName(map.get("NAME").toString());
                    t.setParentId(Integer.valueOf(map.get("PARENTID").toString()));
                    t.setLevel(Integer.valueOf(map.get("LEVEL").toString()));
                    t.setChildren(new ArrayList<Tree>());
                    children.add(t);
                    genTree(data, t);
                }
            }
            tree.setChildren(children);
        } else {
            tree = new Tree();
            tree.setId(0);
            tree.setCode("Root");
            tree.setName("顶级");
            tree.setParentId(-1);
            tree.setLevel(0);
            tree.setChildren(new ArrayList<Tree>());
            genTree(data, tree);
        }
        return tree;
    }

}

class Tree {
    private int id;
    private int parentId;
    private String code;
    private String name;
    private int level;
    private List<Tree> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "{id:" + id + ",code:" + code + ",name:" + name + ",parentid:" + parentId + ",level:" + level + ",children:[" + printChildren() + "]}";
    }

    private String printChildren() {
        String childrenStr = "";
        for (Tree t : children) {
            childrenStr += t.toString();
        }
        return childrenStr;
    }
}
