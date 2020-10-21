package com.yd.web.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelCollectionParams;
import org.jeecgframework.poi.excel.entity.params.ExcelImportEntity;
import org.jeecgframework.poi.util.PoiPublicUtil;
import org.jeecgframework.poi.util.PoiReflectorUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportBaseService {
    public ImportBaseService() {
    }

    public void addEntityToMap(String targetId, Field field, ExcelImportEntity excelEntity, Class<?> pojoClass, List<Method> getMethods, Map<String, ExcelImportEntity> temp) throws Exception {
        Excel excel = (Excel) field.getAnnotation(Excel.class);
        excelEntity = new ExcelImportEntity();
        excelEntity.setType(excel.type());
        excelEntity.setSaveUrl(excel.savePath());
        excelEntity.setSaveType(excel.imageType());
        excelEntity.setReplace(excel.replace());
        excelEntity.setDatabaseFormat(excel.databaseFormat());
        excelEntity.setSuffix(excel.suffix());
        excelEntity.setImportField(Boolean.valueOf(PoiPublicUtil.getValueByTargetId(excel.isImportField(), targetId, "false")));
        this.getExcelField(targetId, field, excelEntity, excel, pojoClass);
        if (getMethods != null) {
            List<Method> newMethods = new ArrayList();
            newMethods.addAll(getMethods);
            newMethods.add(excelEntity.getMethod());
            excelEntity.setMethods(newMethods);
        }

        temp.put(excelEntity.getName(), excelEntity);
    }

    public void getAllExcelField(String targetId, Field[] fields, Map<String, ExcelImportEntity> excelParams, List<ExcelCollectionParams> excelCollection, Class<?> pojoClass, List<Method> getMethods) throws Exception {
        ExcelImportEntity excelEntity = null;

        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            if (!PoiPublicUtil.isNotUserExcelUserThis((List) null, field, targetId)) {
                if (PoiPublicUtil.isCollection(field.getType())) {
                    ExcelCollection excel = (ExcelCollection) field.getAnnotation(ExcelCollection.class);
                    ExcelCollectionParams collection = new ExcelCollectionParams();
                    collection.setName(field.getName());
                    Map<String, ExcelImportEntity> temp = new HashMap();
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    Class<?> clz = (Class) pt.getActualTypeArguments()[0];
                    collection.setType(clz);
                    this.getExcelFieldList(StringUtils.isNotEmpty(excel.id()) ? excel.id() : targetId, PoiPublicUtil.getClassFields(clz), clz, temp, (List) null);
                    collection.setExcelParams(temp);
                    collection.setExcelName(PoiPublicUtil.getValueByTargetId(((ExcelCollection) field.getAnnotation(ExcelCollection.class)).name(), targetId, (String) null));
                    this.additionalCollectionName(collection);
                    excelCollection.add(collection);
                } else if (PoiPublicUtil.isJavaClass(field)) {
                    this.addEntityToMap(targetId, field, (ExcelImportEntity) excelEntity, pojoClass, getMethods, excelParams);
                } else {
                    List<Method> newMethods = new ArrayList();
                    if (getMethods != null) {
                        newMethods.addAll(getMethods);
                    }

                    newMethods.add(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
                    ExcelEntity excel = (ExcelEntity) field.getAnnotation(ExcelEntity.class);
                    this.getAllExcelField(StringUtils.isNotEmpty(excel.id()) ? excel.id() : targetId, PoiPublicUtil.getClassFields(field.getType()), excelParams, excelCollection, field.getType(), newMethods);
                }
            }
        }

    }

    private void additionalCollectionName(ExcelCollectionParams collection) {
        Set<String> keys = new HashSet();
        keys.addAll(collection.getExcelParams().keySet());
        Iterator var3 = keys.iterator();

        while (var3.hasNext()) {
            String key = (String) var3.next();
            collection.getExcelParams().put(collection.getExcelName() + "_" + key, collection.getExcelParams().get(key));
            collection.getExcelParams().remove(key);
        }

    }

    public void getExcelField(String targetId, Field field, ExcelImportEntity excelEntity, Excel excel, Class<?> pojoClass) throws Exception {
        excelEntity.setName(PoiPublicUtil.getValueByTargetId(excel.name(), targetId, (String) null));
        String fieldname = field.getName();
        excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getSetMethod(fieldname));
        if (StringUtils.isNotEmpty(excel.importFormat())) {
            excelEntity.setFormat(excel.importFormat());
        } else {
            excelEntity.setFormat(excel.format());
        }

    }

    public void getExcelFieldList(String targetId, Field[] fields, Class<?> pojoClass, Map<String, ExcelImportEntity> temp, List<Method> getMethods) throws Exception {
        ExcelImportEntity excelEntity = null;

        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            if (!PoiPublicUtil.isNotUserExcelUserThis((List) null, field, targetId)) {
                if (PoiPublicUtil.isJavaClass(field)) {
                    this.addEntityToMap(targetId, field, (ExcelImportEntity) excelEntity, pojoClass, getMethods, temp);
                } else {
                    List<Method> newMethods = new ArrayList();
                    if (getMethods != null) {
                        newMethods.addAll(getMethods);
                    }

                    ExcelEntity excel = (ExcelEntity) field.getAnnotation(ExcelEntity.class);
                    newMethods.add(PoiReflectorUtil.fromCache(pojoClass).getSetMethod(field.getName()));
                    this.getExcelFieldList(StringUtils.isNotEmpty(excel.id()) ? excel.id() : targetId, PoiPublicUtil.getClassFields(field.getType()), field.getType(), temp, newMethods);
                }
            }
        }

    }

    public Object getFieldBySomeMethod(List<Method> list, Object t) throws Exception {
        for (int i = 0; i < list.size() - 1; ++i) {
            Method m = (Method) list.get(i);
            t = m.invoke(t);
        }

        return t;
    }

    public void saveThisExcel(ImportParams params, Class<?> pojoClass, boolean isXSSFWorkbook, Workbook book) throws Exception {
        String path = PoiPublicUtil.getWebRootPath(this.getSaveExcelUrl(params, pojoClass));
        File savefile = new File(path);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyMMddHHmmss");
        FileOutputStream fos = new FileOutputStream(path + "/" + format.format(new Date()) + "_" + Math.round(Math.random() * 100000.0D) + (isXSSFWorkbook ? ".xlsx" : ".xls"));
        book.write(fos);
        IOUtils.closeQuietly(fos);
    }

    public String getSaveExcelUrl(ImportParams params, Class<?> pojoClass) throws Exception {
        String url = "";
        if (params.getSaveUrl().equals("upload/excelUpload")) {
            url = pojoClass.getName().split("\\.")[pojoClass.getName().split("\\.").length - 1];
            return params.getSaveUrl() + "/" + url;
        } else {
            return params.getSaveUrl();
        }
    }

    public void setFieldBySomeMethod(List<Method> setMethods, Object object, Object value) throws Exception {
        Object t = this.getFieldBySomeMethod(setMethods, object);
        ((Method) setMethods.get(setMethods.size() - 1)).invoke(t, value);
    }

    public void setValues(ExcelImportEntity entity, Object object, Object value) throws Exception {
        if (entity.getMethods() != null) {
            this.setFieldBySomeMethod(entity.getMethods(), object, value);
        } else {
            entity.getMethod().invoke(object, value);
        }

    }
}
