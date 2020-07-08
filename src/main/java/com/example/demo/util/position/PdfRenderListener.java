package com.example.demo.util.position;

import com.example.demo.entity.position.CharPosition;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 杨春生
 * @Date: 2019/11/29 10:19
 * @Description:
 */
public class PdfRenderListener implements RenderListener {

    private int pageNum;
    private float pageWidth;
    private float pageHeight;
    private StringBuilder contentBuilder = new StringBuilder();
    private List<CharPosition> charPositions = new ArrayList<>();


    public PdfRenderListener(int pageNum, float pageWidth, float pageHeight) {
        this.pageNum = pageNum;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
    }


    @Override
    public void beginTextBlock() {


    }


    @Override
    public void renderText(TextRenderInfo renderInfo) {
        List<TextRenderInfo> characterRenderInfos = renderInfo.getCharacterRenderInfos();
        for (TextRenderInfo textRenderInfo : characterRenderInfos) {
            String word = textRenderInfo.getText();
            if (word.length() > 1) {
                word = word.substring(word.length() - 1, word.length());
            }
            Rectangle2D.Float rectangle = textRenderInfo.getAscentLine().getBoundingRectange();
            double x = rectangle.getMinX();
            double y = rectangle.getMaxY();


            float xPercent = Math.round(x / pageWidth * 10000) / 10000f;
            float yPercent = Math.round((1 - y / pageHeight) * 10000) / 10000f;

            CharPosition charPosition = new CharPosition(pageNum, xPercent, yPercent);
            charPositions.add(charPosition);
            contentBuilder.append(word);
        }
    }


    @Override
    public void endTextBlock() {


    }


    @Override
    public void renderImage(ImageRenderInfo renderInfo) {


    }


    public String getContent() {
        return contentBuilder.toString();
    }


    public List<CharPosition> getcharPositions() {
        return charPositions;
    }
}



