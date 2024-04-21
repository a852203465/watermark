# Java 实现 doc, docx, xlsx, xls, ppt, pptx, image, pdf 增加水印

## 1 自定义格式文档增加水印
### 1.1 自定义类继承 AbstractWatermarkProcessor 类
```java
public class WpsWatermarkProcessor extends AbstractWatermarkProcessor {

    @Override
    public void process(WatermarkParam watermarkParam) throws WatermarkException {
        super.process(watermarkParam);
    }

    @Override
    public Boolean supportType(File file) {
        return super.supportType(file);
    }
}
```
### 1.2 将自定义类添加到处理集合中
```java
    WatermarkUtils.addProcessor(new WpsWatermarkProcessor());
```

## 2 使用方式
    静态调用WatermarkUtils.addWatermark()即可;
```java
        File file = new File("F:\\图片_3 - 副本.jpg");
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

        WatermarkUtils.addWatermark(param);
```

## 3. 版本记录
### 3.1 v1.0
以文件模式实现 doc, docx, xlsx, xls, ppt, pptx, image, pdf 增加水印


























