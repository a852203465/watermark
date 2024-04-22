package cn.darkjrong.watermark;

import cn.darkjrong.watermark.enums.FileType;
import cn.darkjrong.watermark.exceptions.WatermarkException;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件类型工具类
 *
 * @author Rong.Jia
 * @date 2024/04/22
 */
@Slf4j
public class FileTypeUtils {

	/**
	 * 获取类型
	 *
	 * @param file 文件
	 * @return {@link String} mimeType
	 */
	private static String getMimeType(File file) {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("the target is a directory");
		}
		AutoDetectParser parser = new AutoDetectParser();
		parser.setParsers(MapUtil.newHashMap());
		Metadata metadata = new Metadata();
//		metadata.add(TikaCoreProperties.RESOURCE_NAME_KEY, file.getName());
		try (InputStream stream = new FileInputStream(file)) {
			parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
		}catch (Exception e){
			log.error(String.format("getMimeType(), Exception 【%s】", e.getMessage()), e);
			throw new WatermarkException(e.getMessage());
		}
		return metadata.get(HttpHeaders.CONTENT_TYPE);
	}

	/**
	 * 获取类型
	 *
	 * @param file 文件
	 * @return {@link String} mimeType
	 */
	private static String getMimeType(byte[] file) {
		AutoDetectParser parser = new AutoDetectParser();
		parser.setParsers(MapUtil.newHashMap());
		Metadata metadata = new Metadata();
//		metadata.add(TikaCoreProperties.RESOURCE_NAME_KEY, "1.xlsx");
		try (InputStream stream = new ByteArrayInputStream(file)) {
			parser.parse(stream, new DefaultHandler(), metadata, new ParseContext());
		}catch (Exception e){
			log.error(String.format("getMimeType(), Exception 【%s】", e.getMessage()), e);
			throw new WatermarkException(e.getMessage());
		}
		return metadata.get(HttpHeaders.CONTENT_TYPE);
	}

	/**
	 * 检查类型
	 *
	 * @param file     文件
	 * @param fileType 文件类型
	 * @return {@link Boolean}
	 */
	private static Boolean checkType(File file, FileType fileType){
		String type = getMimeType(file);
		Pattern p = Pattern.compile(fileType.getValue());
		Matcher m = p.matcher(type);
		return StrUtil.equals(fileType.getValue(), type) || m.matches();
	}

	/**
	 * 检查类型
	 *
	 * @param file     文件
	 * @param fileType 文件类型
	 * @return {@link Boolean}
	 */
	private static Boolean checkType(byte[] file, FileType fileType){
		String type = getMimeType(file);
		Pattern p = Pattern.compile(fileType.getValue());
		Matcher m = p.matcher(type);
		return StrUtil.equals(fileType.getValue(), type) || m.matches();
	}

	/**
	 * 是否是视频文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isVideo(File file){
		return checkType(file, FileType.VIDEO);
	}

	/**
	 * 是否是视频文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isVideo(byte[] file){
		return checkType(file, FileType.VIDEO);
	}

	/**
	 * 是否是图片
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isImage(File file){
		return checkType(file, FileType.IMAGE);
	}

	/**
	 * 是否是图片
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isImage(byte[] file){
		return checkType(file, FileType.IMAGE);
	}

	/**
	 * 是否是word(.doc)
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isDoc(File file) {
		return checkType(file, FileType.DOC);
	}

	/**
	 * 是否是word(.doc)
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isDoc(byte[] file) {
		return checkType(file, FileType.DOC);
	}

	/**
	 * 是否是powerpoint(.ppt)
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPpt(File file) {
		return checkType(file, FileType.PPT);
	}

	/**
	 * 是否是powerpoint(.ppt)
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPpt(byte[] file) {
		return checkType(file, FileType.PPT);
	}

	/**
	 * 是xls
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isXls(File file){
		return checkType(file, FileType.XLS);
	}

	/**
	 * 是xls
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isXls(byte[] file){
		return checkType(file, FileType.XLS);
	}

	/**
	 * 是word(.docx)
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isDocx(File file){
		return checkType(file, FileType.DOCX);
	}

	/**
	 * 是word(.docx)
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isDocx(byte[] file){
		return checkType(file, FileType.DOCX);
	}

	/**
	 * 是pptx
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPptx(File file){
		return checkType(file, FileType.PPTX);
	}

	/**
	 * 是pptx
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPptx(byte[] file){
		return checkType(file, FileType.PPTX);
	}

	/**
	 * 是xlsx
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isXlsx(File file){
		return checkType(file, FileType.XLSX);
	}

	/**
	 * 是xlsx
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isXlsx(byte[] file){
		return checkType(file, FileType.XLSX);
	}

	/**
	 * 是rar
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isRar(File file) {
		return checkType(file, FileType.RAR);
	}

	/**
	 * 是rar
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isRar(byte[] file) {
		return checkType(file, FileType.RAR);
	}

	/**
	 * 是Zip
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isZip(File file){
		return checkType(file, FileType.ZIP);
	}

	/**
	 * 是Zip
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isZip(byte[] file){
		return checkType(file, FileType.ZIP);
	}

	/**
	 * 是pdf
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPdf(File file) {
		return checkType(file, FileType.PDF);
	}

	/**
	 * 是pdf
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPdf(byte[] file) {
		return checkType(file, FileType.PDF);
	}

	/**
	 * 是纯文本
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPlain(File file){
		return checkType(file, FileType.PLAIN);
	}

	/**
	 * 是纯文本
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPlain(byte[] file){
		return checkType(file, FileType.PLAIN);
	}

	/**
	 * 是css
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isCss(File file){
		return checkType(file, FileType.CSS);
	}

	/**
	 * 是css
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isCss(byte[] file){
		return checkType(file, FileType.CSS);
	}

	/**
	 * 是rtf
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isRtf(File file) {
		return checkType(file, FileType.RTF);
	}

	/**
	 * 是rtf
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isRtf(byte[] file) {
		return checkType(file, FileType.RTF);
	}

	/**
	 * 是html
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isHtml(File file) {
		return checkType(file, FileType.HTML);
	}

	/**
	 * 是html
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isHtml(byte[] file) {
		return checkType(file, FileType.HTML);
	}

	/**
	 * 是java src
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isJavaSrc(File file){
		return checkType(file, FileType.JAVA);
	}

	/**
	 * 是java src
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isJavaSrc(byte[] file){
		return checkType(file, FileType.JAVA);
	}

	/**
	 * 是c src
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isCSrc(File file){
		return checkType(file, FileType.CSRC);
	}

	/**
	 * 是c src
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isCSrc(byte[] file){
		return checkType(file, FileType.CSRC);
	}

	/**
	 * 是c++ src
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isCPlusSrc(File file){
		return checkType(file, FileType.CPLUSSRC);
	}

	/**
	 * 是c++ src
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isCPlusSrc(byte[] file){
		return checkType(file, FileType.CPLUSSRC);
	}

	/**
	 * 是否是音频文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isAudio(File file) {
		return checkType(file, FileType.AUDIO);
	}

	/**
	 * 是否是音频文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isAudio(byte[] file) {
		return checkType(file, FileType.AUDIO);
	}

	/**
	 * 是否是Excel文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isExcel(File file){
		return isXlsx(file) || isXls(file);
	}

	/**
	 * 是否是Excel文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isExcel(byte[] file){
		return isXlsx(file) || isXls(file);
	}

	/**
	 * 是否是Word文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isWord(File file){
		return isDocx(file) || isDoc(file) || isRtf(file);
	}

	/**
	 * 是否是Word文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isWord(byte[] file){
		return isDocx(file) || isDoc(file) || isRtf(file);
	}

	/**
	 *  是否是PPT,PPTX文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPpts(File file){
		return isPpt(file) || isPptx(file);
	}

	/**
	 *  是否是PPT,PPTX文件
	 *
	 * @param file 文件
	 * @return {@link Boolean}
	 */
	public static Boolean isPpts(byte[] file){
		return isPpt(file) || isPptx(file);
	}

	/**
	 * 得到文件类型
	 *
	 * @param file 文件
	 * @return {@link String}
	 */
	public static String getFileType(File file) {
		return getMimeType(file);
	}

	/**
	 * 得到文件类型
	 *
	 * @param file 文件
	 * @return {@link String}
	 */
	public static String getFileType(byte[] file) {
		return getMimeType(file);
	}
































}
