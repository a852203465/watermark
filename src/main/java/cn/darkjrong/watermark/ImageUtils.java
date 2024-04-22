package cn.darkjrong.watermark;

import cn.darkjrong.watermark.domain.FontAttribute;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

/**
 * 图片操作工具类
 *
 * @author Rong.Jia
 * @date 2021/08/29
 */
@Slf4j
public class ImageUtils {

    private static final String IMAGE_FORMAT = "png";

    /**
     * 根据指定的文本创建图片
     *
     * @param text     文本
     * @param color    文字颜色
     * @param fontSize 字体大小
     * @param alpha    透明度
     * @param degree   旋转角度
     * @return {@link File}
     * @throws WatermarkException 水印异常
     */
    public static File createImage(String text, Color color, Integer fontSize, Float degree, Float alpha) throws WatermarkException {
        byte[] imageByte = createImageByte(text, color, fontSize, degree, alpha);
        try {
            return FileUtil.writeBytes(imageByte, createFile());
        } catch (Exception e) {
            log.error(String.format("************,createImage(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }

    /**
     * 根据指定的文本创建图片
     *
     * @param text     文本
     * @param color    文字颜色
     * @param fontSize 字体大小
     * @param alpha    透明度
     * @param degree   旋转角度
     * @return {@link byte[]} 图像字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] createImageByte(String text, Color color, Integer fontSize, Float degree, Float alpha) throws WatermarkException {

        Font font = new Font("宋体", Font.PLAIN, fontSize);
        FontAttribute fontAttribute = getWidthAndHeight(text, font);

        int width = fontAttribute.getWidth();
        int height = fontAttribute.getHeight();

        // 创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        //创建图片画布
        Graphics2D g = image.createGraphics();

        // 增加下面代码使得背景透明
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();

        g = image.createGraphics();
        g.setColor(new Color(242, 242, 242));

        // 设置透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // 设置画笔字体
        g.setFont(font);
        g.translate(10, 10);
        g.setColor(color);

        // 画出一行字符串
        g.drawString(text, 0, font.getSize());
        g.dispose();

        try {
            Image rotate = ImgUtil.rotate(image, Convert.toInt(degree));
            return ImgUtil.toBytes(rotate, ImgUtil.IMAGE_TYPE_PNG);
        } catch (Exception e) {
            log.error(String.format("************,createImageByte(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }

    /**
     * 创建图像
     *
     * @param imageFile 图像文件
     * @param degree    旋转角度
     * @param alpha     透明度
     * @return {@link File}
     * @throws WatermarkException 水印异常
     */
    public static File createImage(File imageFile, Float degree, Float alpha) throws WatermarkException {
        try {
            byte[] bytes = createImage(FileUtil.readBytes(imageFile), degree, alpha);
            return FileUtil.writeBytes(bytes, imageFile);
        } catch (Exception e) {
            log.error(String.format("************,createImage(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }

    /**
     * 创建图像
     *
     * @param imageFile 图像文件
     * @param degree    旋转角度
     * @param alpha     透明度
     * @return {@link byte[]} 图像字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] createImage(byte[] imageFile, Float degree, Float alpha) throws WatermarkException {
        ByteArrayInputStream alphaInput = null;
        ByteArrayInputStream rotateInput = null;
        try {
            byte[] rotate = rotate(imageFile, degree);
            byte[] alphaBytes = transferAlpha(rotateInput = new ByteArrayInputStream(rotate), 1);
            return changeAlpha(alphaInput = new ByteArrayInputStream(alphaBytes), Convert.toInt(alpha));
        } catch (Exception e) {
            log.error(String.format("************,createImage(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(rotateInput);
            IoUtil.close(alphaInput);
        }
    }

    /**
     * 获取宽度和高度
     * 获取文本宽度和高度
     *
     * @param text 文本
     * @param font 字体
     * @return {@link FontAttribute} 文本属性
     */
    private static FontAttribute getWidthAndHeight(String text, Font font) {

        Rectangle2D r = font.getStringBounds(text, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));

        int unitHeight = (int) Math.floor(r.getHeight());

        // 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 20;

        // 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        int height = unitHeight + 20;

        return FontAttribute.builder().width(width).height(height).build();
    }

    /**
     * 创建文件
     *
     * @return {@link File}
     */
    private static File createFile() {
        String property = System.getProperty("java.io.tmpdir");
        return new File(property + "/kbs-watermark-" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + ".png");
    }

    /**
     * 调节图片透明度（支持透明背景）
     *
     * @param inputStream 源路径
     * @param targetFile  生成路径
     * @param alpha       透明度   （0全透明---10不透明）
     * @return {@link File}
     * @throws WatermarkException 水印异常
     */
    public static File changeAlpha(InputStream inputStream, File targetFile, int alpha) throws WatermarkException {
        try {
            byte[] bytes = changeAlpha(inputStream, alpha);
            FileUtil.writeBytes(bytes, targetFile);
            return targetFile;
        } catch (IORuntimeException e) {
            log.error(String.format("************,changeAlpha(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }

    /**
     * 调节图片透明度（支持透明背景）
     *
     * @param inputStream 源路径
     * @param alpha       透明度   （0全透明---10不透明）
     * @return {@link byte[]} 图像字节数组
     * @throws WatermarkException 水印异常
     */
    public static byte[] changeAlpha(InputStream inputStream, int alpha) throws WatermarkException {

        //检查透明度是否越界
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 10) {
            alpha = 10;
        }

        ByteArrayOutputStream out = null;
        try {
            BufferedImage image = ImageIO.read(inputStream);
            int weight = image.getWidth();
            int height = image.getHeight();

            BufferedImage output = new BufferedImage(weight, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = output.createGraphics();
            output = g2.getDeviceConfiguration().createCompatibleImage(weight, height, Transparency.TRANSLUCENT);
            g2.dispose();
            g2 = output.createGraphics();

            //调制透明度
            for (int j1 = output.getMinY(); j1 < output.getHeight(); j1++) {
                for (int j2 = output.getMinX(); j2 < output.getWidth(); j2++) {
                    int rgb = output.getRGB(j2, j1);
                    rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
                    output.setRGB(j2, j1, rgb);
                }
            }
            g2.setComposite(AlphaComposite.SrcIn);
            g2.drawImage(image, 0, 0, weight, height, null);
            g2.dispose();
            out = new ByteArrayOutputStream();
            ImageIO.write(output, IMAGE_FORMAT, out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error(String.format("************,changeAlpha(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }

    /**
     * 图片旋转
     *
     * @param srcFile 原始图
     * @param angel   旋转角度
     * @return 结果图字节数据组
     * @throws WatermarkException 水印异常
     */
    public static byte[] rotate(File srcFile, Float angel) throws WatermarkException {
        try {
            return rotate(FileUtil.readBytes(srcFile), angel);
        } catch (IORuntimeException e) {
            log.error(String.format("************,rotate(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }
    }

    /**
     * 图片旋转
     *
     * @param srcFile 原始图
     * @param angel   旋转角度
     * @return 结果图字节数据组
     * @throws WatermarkException 水印异常
     */
    public static byte[] rotate(byte[] srcFile, Float angel) throws WatermarkException {
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        try {
            out = new ByteArrayOutputStream(16 * 1024);
            in = new ByteArrayInputStream(srcFile);
            Image src = ImageIO.read(in);
            int srcWidth = src.getWidth(null);
            int srcHeight = src.getHeight(null);
            Rectangle rectDes = calcRotatedSize(new Rectangle(new Dimension(srcWidth, srcHeight)), angel);

            BufferedImage res = new BufferedImage(rectDes.width, rectDes.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rectDes.width - srcWidth) / 2, (rectDes.height - srcHeight) / 2);
            g2.rotate(Math.toRadians(angel), srcWidth / 2.0, srcHeight / 2.0);
            g2.drawImage(src, null, null);
            g2.dispose();
            ImageIO.write(res, IMAGE_FORMAT, out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error(String.format("************,rotate(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        } finally {
            IoUtil.close(out);
            IoUtil.close(in);
        }
    }

    /**
     * 计算旋转后的图片
     *
     * @param src   被旋转的图片
     * @param angel 旋转角度
     * @return 旋转后的图片
     */
    public static Rectangle calcRotatedSize(Rectangle src, Float angel) {

        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angelAlpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angelDaltaWidth = Math.atan((double) src.height / src.width);
        double angelDaltaHeight = Math.atan((double) src.width / src.height);
        int lenDaltaWidth = (int) (len * Math.cos(Math.PI - angelAlpha - angelDaltaWidth));
        int lenDaltaHeight = (int) (len * Math.cos(Math.PI - angelAlpha - angelDaltaHeight));
        int desWidth = src.width + lenDaltaWidth * 2;
        int desHeight = src.height + lenDaltaHeight * 2;
        return new java.awt.Rectangle(new Dimension(desWidth, desHeight));
    }

    /**
     * 对图片中的 黑色或白色进行透明化处理
     *
     * @param input 原始图
     * @param color   颜色
     * @return 结果图字节数据组
     * @throws WatermarkException 水印异常
     */
    public static byte[] transferAlpha(InputStream input, int color) throws WatermarkException {

        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream(16 * 1024);
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(input));
            BufferedImage bufferedImage = new BufferedImage(
                    imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if (checkColor(rgb, 16, color)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }

            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            g2D.dispose();
            ImageIO.write(bufferedImage, IMAGE_FORMAT, out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error(String.format("************,transferAlpha(),Exception【%s】", e.getMessage()), e);
            throw new WatermarkException(e.getMessage());
        }finally {
            IoUtil.close(out);
        }
    }

    /**
     * 检查颜色是否为 白色 或者 黑色阈值范围
     *
     * @param rgb   颜色值
     * @param color 0:白色 1:黑色
     * @return 检查结果
     */
    private static boolean checkColor(int rgb, int offset, int color) {
        int R = (rgb & 0xff0000) >> 16;
        int G = (rgb & 0xff00) >> 8;
        int B = (rgb & 0xff);

        if (color == 0) {
            return ((255 - R) <= offset) && ((255 - G) <= offset) && ((255 - B) <= offset);
        } else {
            return ((R <= offset) && (G <= offset) && (B <= offset));
        }
    }


}
