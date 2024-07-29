package com.meet.test;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("processtask")
public class ProcessTaskController {

    @GetMapping("/downloadExcelWithImage")
    public void downloadExcelWithImage(HttpServletResponse response) {
        try {
            // 设置响应头，指定文件名和内容类型
            response.setHeader("Content-Disposition", "attachment; filename=output.xlsx");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            // 创建工作簿、工作表和插入图片
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Images");

            // 调用下载方法，传递工作簿和工作表
            download("https://plmdemo.byd.com/iuap-apcom-file/rest/v1/file/chunk/65b9f1f011622a0a82754bc2/0/ocstream?downThumb=false&token=yonbip_esn_lightappb5a44ecbf59f8e4003b2d73bebfd8599&isWaterMark=false&fileName=png", workbook, sheet);

            // 写入 Excel 到响应输出流
            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace(); // 在实际应用中，你可能想要处理异常并返回适当的响应
        }
    }

    // 下载方法，接受图片 URL、工作簿和工作表
    public void download(String url, Workbook workbook, Sheet sheet) throws Exception {
        try {
            InputStream inputStream = new FileInputStream(url);
            byte[] imageBytes = IOUtils.toByteArray(inputStream);

            // 将图片插入到Excel中
            int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

            // 创建一个绘图对象
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();

            // 创建锚点
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0); // 图片在第1列
            anchor.setRow1(0); // 图片在第1行

            // 插入图片到锚点
            Picture picture = drawing.createPicture(anchor, pictureIdx);

            // 设置图片大小
            picture.resize(); // 默认大小

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}