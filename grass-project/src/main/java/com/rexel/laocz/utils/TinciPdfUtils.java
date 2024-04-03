package com.rexel.laocz.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rexel.oss.utils.AttachmentHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PDF生成
 *
 * @author Chunhui.Qu
 */
@Slf4j
@Component
public class TinciPdfUtils {
    @Autowired
    private AttachmentHelper attachmentHelper;

    /**
     * 转换为PDF文件
     *
     * @param contentMap contentMap
     * @return url地址
     */
    public String convertToPdf(LinkedHashMap<String, String> contentMap, String fileName) {
        // 创建一个新的 PDF 文档
        Document document = new Document(PageSize.A4);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.setMargins(5, 5, 5, 5);
            document.open();

            int columns = 5;;
            PdfPTable table = new PdfPTable(columns);
            // 设置表格的宽度比例
            table.setWidthPercentage(100);

            // 添加图片到表格
            int i = 0;
            int cellCount = 0;
            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                String titleStr = entry.getKey();
                String imageUrl = entry.getValue();

                // 根据地址获取图片
                Image image = getImageFromUrl(imageUrl);
                if (image == null) {
                    continue;
                }

                // 设置图片大小
                image.scaleToFit(80, 80);
                image.setAlignment(Element.ALIGN_CENTER);

                // 创建标题文本
                Paragraph title = new Paragraph(titleStr);
                // 设置标题文本居中对齐
                title.setAlignment(Element.ALIGN_CENTER);

                // 将图片添加到表格单元格
                PdfPCell cell = new PdfPCell();
                cell.setPaddingLeft(5);
                cell.setPaddingRight(5);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                // 将标题文本和图片添加到单元格
                cell.addElement(title);
                cell.addElement(image);

                table.addCell(cell);

                // 每行结束后换行
                if ((i + 1) % columns == 0) {
                    table.completeRow();
                    cellCount = 0;
                } else {
                    cellCount += 1;
                }
                i ++;
            }
            if (cellCount > 0) {
                table.completeRow();
            }

            // 将表格添加到文档
            document.add(table);
            writer.close();

            // 生成PDF文件流
            byte[] content = outputStream.toByteArray();
            MultipartFile file = new MockMultipartFile(
                    fileName, fileName + ".pdf", "application/pdf", content);

            // 上传至文件服务器
            return attachmentHelper.upload(file, "pdf", "laocz");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return null;
    }

    /**
     * 将URL转换为图片
     *
     * @param imageUrl 图片地址
     * @return Image
     * @throws Exception e
     */
    private Image getImageFromUrl(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();

        // 读取字节数据并创建Image对象
        byte[] imageData = new byte[connection.getContentLength()];
        int bytesRead;
        int offset = 0;
        while (offset < imageData.length
                && (bytesRead = inputStream.read(imageData, offset, imageData.length - offset)) >= 0) {
            offset += bytesRead;
        }

        return Image.getInstance(imageData);
    }
}
