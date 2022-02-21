package cn.darkjrong.watermark;

import cn.darkjrong.watermark.domain.WatermarkParam;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import org.junit.Test;

import java.awt.*;
import java.io.File;

public class WatermarkProcessorTest {

    @Test
    public void image() throws WatermarkException {

        File file = new File("F:\\test\\1.jpg");
        File imageFile = new File("F:\\1 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(30F)
//                .imageFile(imageFile)
//                .xMove(100)
//                .yMove(100)
                .alpha(1F)
                .bespread(Boolean.TRUE)
                .color(Color.red)
                .build();

        WatermarkUtils.addWatermark(param, new File("F:\\test\\tesa1.jpeg"));

    }

    @Test
    public void pdf() throws Exception {

        File file = new File("F:\\test\\技术面试常见智力题.pdf");
        File imageFile = new File("F:\\3 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
//                .degree(40.0F)
                .fontSize(30)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.6F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.red).build();

        WatermarkUtils.addWatermark(param, new File("F:\\test\\1.pdf"));


    }

    @Test
    public void excel() throws WatermarkException {

        File file = new File("F:\\test\\demo.xlsx");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(40.0F)
                .fontSize(100)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.4F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.red).build();


        WatermarkUtils.addWatermark(param, new File("F:\\\\test\\\\dem1o.xlsx"));


    }

    @Test
    public void word() throws WatermarkException {

        File file = new File("F:\\doc.doc");
        File imageFile = new File("F:\\4 - 副本.jpeg");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(20.0F)
                .fontSize(50)
//                .imageFile(imageFile)
//                .xMove(200)
                .alpha(0.5F)
//                .yMove(200)
                .bespread(Boolean.FALSE)
                .color(Color.red).build();

        WatermarkUtils.addWatermark(param);


    }

    @Test
    public void ppt() throws WatermarkException {

        File file = new File("F:\\测试\\demo1.pptx");

        WatermarkParam param = WatermarkParam.builder()
                .file(file)
                .text("小i机器人")
                .degree(40.0F)
                .fontSize(100)
//                .imageFile(imageFile)
//                .xMove(30)
                .alpha(0.1F)
//                .yMove(30)
                .bespread(Boolean.TRUE)
                .color(Color.red).build();


        WatermarkUtils.addWatermark(param);


    }







}
