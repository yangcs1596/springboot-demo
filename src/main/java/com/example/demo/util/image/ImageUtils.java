package com.example.demo.util.image;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Author: 杨春生
 * @Date: 2020/9/15 08:48
 * @Description:
 */
public class ImageUtils {
    public static void main(String[] args) throws Exception {
//        Thumbnails.of("C:\\Users\\Administrator\\Desktop\\a75adc9183a2431abdf8e5b048a3d2ad.jpg")
//                .imageType(BufferedImage.TYPE_INT_ARGB)
//                .size(100, 100)
//                .outputQuality(1)
//                .toFile("C:\\Users\\Administrator\\Desktop\\12345.jpg");
        // 解决底色会变黑的问题
        Thumbnails.Builder<File> builder = Thumbnails.of("C:\\Users\\Administrator\\Desktop\\a75adc9183a2431abdf8e5b048a3d2ad.jpg")
                .imageType(BufferedImage.TYPE_INT_ARGB)
                .forceSize(100, 100);
        builder.outputFormat("png").toFile("C:\\Users\\Administrator\\Desktop\\12345.jpg");
    }
}
