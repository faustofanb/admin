package io.github.faustofanb.admin.module.batch.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import jakarta.servlet.http.HttpServletResponse;

public class ExcelUtils {

    public static <T> void write(HttpServletResponse response, String filename, String sheetName, Class<T> head,
            List<T> data) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), head)
                .sheet(sheetName)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);
    }
}
