package com.example.demo.entity.position;

import java.util.List;

/**
 * @Author: 杨春生
 * @Date: 2019/11/29 10:18
 * @Description:
 */
public class PdfPageContentPositions {
    private String content;
    private List<float[]> positions;


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public List<float[]> getPositions() {
        return positions;
    }


    public void setPostions(List<float[]> positions) {
        this.positions = positions;
    }
}
