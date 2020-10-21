package com.yd.web.util;

import org.apache.poi.util.IOUtils;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.result.ExcelImportResult;
import org.jeecgframework.poi.excel.imports.sax.SaxReadExcel;
import org.jeecgframework.poi.excel.imports.sax.parse.ISaxRowRead;
import org.jeecgframework.poi.exception.excel.ExcelImportException;
import org.jeecgframework.poi.handler.inter.IExcelReadRowHanlder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ExcelImportUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(org.jeecgframework.poi.excel.ExcelImportUtil.class);

    private ExcelImportUtil() {
    }

    public static <T> List<T> importExcel(File file, Class<?> pojoClass, ImportParams params) {
        FileInputStream in = null;

        List var4;
        try {
            in = new FileInputStream(file);
            var4 = (new ExcelImportServer()).importExcelByIs(in, pojoClass, params).getList();
        } catch (ExcelImportException var9) {
            throw new ExcelImportException(var9.getType(), var9);
        } catch (Exception var10) {
            LOGGER.error(var10.getMessage(), var10);
            throw new ExcelImportException(var10.getMessage(), var10);
        } finally {
            IOUtils.closeQuietly(in);
        }

        return var4;
    }

    public static <T> List<T> importExcel(InputStream inputstream, Class<?> pojoClass, ImportParams params) throws Exception {
        return (new ExcelImportServer()).importExcelByIs(inputstream, pojoClass, params).getList();
    }

    public static <T> ExcelImportResult<T> importExcelVerify(InputStream inputstream, Class<?> pojoClass, ImportParams params) throws Exception {
        return (new ExcelImportServer()).importExcelByIs(inputstream, pojoClass, params);
    }

    public static <T> ExcelImportResult<T> importExcelVerify(File file, Class<?> pojoClass, ImportParams params) {
        FileInputStream in = null;

        ExcelImportResult var4;
        try {
            in = new FileInputStream(file);
            var4 = (new ExcelImportServer()).importExcelByIs(in, pojoClass, params);
        } catch (ExcelImportException var9) {
            throw new ExcelImportException(var9.getType(), var9);
        } catch (Exception var10) {
            LOGGER.error(var10.getMessage(), var10);
            throw new ExcelImportException(var10.getMessage(), var10);
        } finally {
            IOUtils.closeQuietly(in);
        }

        return var4;
    }

    public static <T> List<T> importExcelBySax(InputStream inputstream, Class<?> pojoClass, ImportParams params) {
        return (new SaxReadExcel()).readExcel(inputstream, pojoClass, params, (ISaxRowRead)null, (IExcelReadRowHanlder)null);
    }

    public static void importExcelBySax(InputStream inputstream, Class<?> pojoClass, ImportParams params, IExcelReadRowHanlder hanlder) {
        (new SaxReadExcel()).readExcel(inputstream, pojoClass, params, (ISaxRowRead)null, hanlder);
    }

    public static <T> List<T> importExcelBySax(InputStream inputstream, ISaxRowRead rowRead) {
        return (new SaxReadExcel()).readExcel(inputstream, (Class)null, (ImportParams)null, rowRead, (IExcelReadRowHanlder)null);
    }
}

