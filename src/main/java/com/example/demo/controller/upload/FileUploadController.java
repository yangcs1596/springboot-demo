package com.example.demo.controller.upload;

import com.example.demo.entity.UserDTO;
import com.example.demo.util.converter.PDFToImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: 杨春生
 * @Date: 2019/12/5 14:30
 * @Description:
 */
@Slf4j
@Api(value = "上传接口",tags={"文件操作接口"})
@Controller
@RequestMapping(value = {"/test"})
public class FileUploadController {
    /**
     * 功能描述: 上传。返回转换后多张图片流
     * ERROR : 不要返回base64的编码，图片若太大，则返回的长度会太长
     *
     * @param:
     * @return:
     * @auther: 杨春生
     * @date: 2019/12/5 14:36
     */
    @ApiOperation(value="上传待签字文档", notes="问题注意点", response = UserDTO.class)
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object unsigned(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        if (null == file) {
            return "文件为空";
        }
        // 10M
        long maxFileLen = 10485760;
        if (file.getSize() > maxFileLen) {
            return "大小超过限制";
        }
        String fileName = file.getOriginalFilename();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        log.info("fileName:" + fileName);
        log.info("fileExt:" + fileExt);
        String filePath = "D:\\测试文件夹\\上传测试\\" + fileName;
        File destFile = new File(filePath);
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
        } catch (IOException e) {
            log.error("待签名文件上传报错", e);
            return "系统错误";
        }
        String pdfPath = filePath;
        //判断是否为word的文件
        if (!fileExt.contains("pdf")) {
            //转换word文件为pdf
            pdfPath = PDFToImage.word2pdf(filePath);
            //转换成功后，是否将word的原文件删除，否
        }
        //将pdf转成图片
        Map<String, Object> imgPaths = PDFToImage.pdf2png(pdfPath);

        ArrayList<String> picture = new ArrayList<>();
        imgPaths.forEach((k, v) -> {
            byte[] bt = new byte[0];
            try {
                bt = Files.readAllBytes(Paths.get((String) v));
            } catch (IOException e) {
                log.info("读取图片异常");
            }
            String base64str = Base64.encodeBase64String(bt);
            //import org.apache.commons.codec.binary
            picture.add("data:image/jpg;base64," + base64str);
        });
        return picture;
    }
}
