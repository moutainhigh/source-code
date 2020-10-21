package com.yg.web.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yg.core.QiniuHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QrCodeUtil {

	private static final String IMAGE_SUFFIX_PNG = "png";

	private static final String IMAGE_SUFFIX_JPG = "jpg";

	/**
	 * 生成指定大小的二维码返回输入流
	 * @param content	二维码内容
	 * @param width		宽度
	 * @param height	告诉
	 * @param response	输出流
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void makeQrCode(String content, int width, int height, HttpServletResponse response) throws WriterException, IOException{
		Map<EncodeHintType, Object> hints = new HashMap<>(6);
		// 设置编码
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		//容错率
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		//二维码边框宽度，这里文档说设置0-4，但是设置后没有效果，不知原因
		hints.put(EncodeHintType.MARGIN, 0);

		// 生成矩阵
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		
		MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_SUFFIX_PNG, response.getOutputStream());
	}

	/**
	 *生成指定大小的二维码，上传到图片服务器
	 * @param content	二维码内容
	 * @param width		二维码宽度
	 * @param height	二维码高度
	 * @return 图片服务器地址
	 * @throws WriterException
	 * @throws IOException
	 */
	public static String makeQrCode(String content, int width, int height) throws WriterException, IOException{
		Map<EncodeHintType, Object> hints = new HashMap<>(6);
		// 设置编码
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		//容错率
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		//二维码边框宽度，这里文档说设置0-4，但是设置后没有效果，不知原因，
		hints.put(EncodeHintType.MARGIN, 0);

		// 生成矩阵
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

		// 定义输出流,输出图片到流输出流中
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_SUFFIX_PNG, outputStream);

		// 将图片输入流上传到图片服务器
		return QiniuHelper.upload(outputStream.toByteArray());
	}

	public static void main(String[] args) {
		System.out.println("123");
		try {
			String imageUrl = makeQrCode("123", 100, 100);
			System.out.println(imageUrl);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
