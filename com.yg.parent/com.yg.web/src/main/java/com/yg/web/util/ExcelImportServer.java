package com.yg.web.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelCollectionParams;
import org.jeecgframework.poi.excel.entity.params.ExcelImportEntity;
import org.jeecgframework.poi.excel.entity.result.ExcelImportResult;
import org.jeecgframework.poi.excel.entity.result.ExcelVerifyHanlderResult;
import org.jeecgframework.poi.excel.imports.CellValueServer;
import org.jeecgframework.poi.exception.excel.ExcelImportException;
import org.jeecgframework.poi.exception.excel.enums.ExcelImportEnum;
import org.jeecgframework.poi.handler.inter.IExcelModel;
import org.jeecgframework.poi.util.PoiPublicUtil;
import org.jeecgframework.poi.util.PoiReflectorUtil;
import org.jeecgframework.poi.util.PoiValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.util.*;

public class ExcelImportServer extends ImportBaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(org.jeecgframework.poi.excel.imports.ExcelImportServer.class);
    private CellValueServer cellValueServer = new CellValueServer();
    private boolean verfiyFail = false;
    private CellStyle errorCellStyle;

    public ExcelImportServer() {
    }

    private void addListContinue(Object object, ExcelCollectionParams param, Row row, Map<Integer, String> titlemap, String targetId, Map<String, PictureData> pictures, ImportParams params) throws Exception {
        Collection collection = (Collection) PoiReflectorUtil.fromCache(object.getClass()).getValue(object, param.getName());
        Object entity = PoiPublicUtil.createObject(param.getType(), targetId);
        boolean isUsed = false;

        for (int i = row.getFirstCellNum(); i < param.getExcelParams().size(); ++i) {
            Cell cell = row.getCell(i);
            String titleString = (String) titlemap.get(i);
            if (param.getExcelParams().containsKey(titleString)) {
                if (((ExcelImportEntity) param.getExcelParams().get(titleString)).getType() == 2) {
                    String picId = row.getRowNum() + "_" + i;
                    this.saveImage(object, picId, param.getExcelParams(), titleString, pictures, params);
                } else {
                    this.saveFieldValue(params, entity, cell, param.getExcelParams(), titleString, row);
                }

                isUsed = true;
            }
        }

        if (isUsed) {
            collection.add(entity);
        }

    }

    private String getKeyValue(Cell cell) {
        Object obj = null;
        switch (cell.getCellType()) {
            case 0:
                obj = cell.getNumericCellValue();
                break;
            case 1:
                obj = cell.getStringCellValue();
                break;
            case 2:
                obj = cell.getCellFormula();
                break;
            case 3:
            default:
                cell.setCellType(1);
                obj = cell.getStringCellValue();
                break;
            case 4:
                obj = cell.getBooleanCellValue();
        }

        return obj == null ? null : obj.toString().trim();
    }

    private String getSaveUrl(ExcelImportEntity excelImportEntity, Object object) throws Exception {
        String url = "";
        if (excelImportEntity.getSaveUrl().equals("upload")) {
            if (excelImportEntity.getMethods() != null && excelImportEntity.getMethods().size() > 0) {
                object = this.getFieldBySomeMethod(excelImportEntity.getMethods(), object);
            }

            url = object.getClass().getName().split("\\.")[object.getClass().getName().split("\\.").length - 1];
            return excelImportEntity.getSaveUrl() + "/" + url;
        } else {
            return excelImportEntity.getSaveUrl();
        }
    }

    private <T> List<T> importExcel(Collection<T> result, Sheet sheet, Class<?> pojoClass, ImportParams params, Map<String, PictureData> pictures) throws Exception {
        List collection = new ArrayList();
        Map<String, ExcelImportEntity> excelParams = new HashMap();
        List<ExcelCollectionParams> excelCollection = new ArrayList();
        String targetId = null;
        if (!Map.class.equals(pojoClass)) {
            Field[] fileds = PoiPublicUtil.getClassFields(pojoClass);
            ExcelTarget etarget = (ExcelTarget) pojoClass.getAnnotation(ExcelTarget.class);
            if (etarget != null) {
                targetId = etarget.value();
            }

            this.getAllExcelField(targetId, fileds, excelParams, excelCollection, pojoClass, (List) null);
        }

        Iterator<Row> rows = sheet.rowIterator();

        for (int j = 0; j < params.getTitleRows(); ++j) {
            rows.next();
        }

        Map<Integer, String> titlemap = this.getTitleMap(rows, params, excelCollection);
        this.checkIsValidTemplate(titlemap, excelParams, params, excelCollection);
        Row row = null;
        Object object = null;

        while (rows.hasNext() && (row == null || sheet.getLastRowNum() - row.getRowNum() > params.getLastOfInvalidRow())) {
            row = (Row) rows.next();
            Iterator var23;
            ExcelCollectionParams param;
            if (params.getKeyIndex() != null && (row.getCell(params.getKeyIndex()) == null || StringUtils.isEmpty(this.getKeyValue(row.getCell(params.getKeyIndex())))) && object != null) {
                var23 = excelCollection.iterator();

                while (var23.hasNext()) {
                    param = (ExcelCollectionParams) var23.next();
                    this.addListContinue(object, param, row, titlemap, targetId, pictures, params);
                }
            } else {
                object = PoiPublicUtil.createObject(pojoClass, targetId);

                try {
                    int i = row.getFirstCellNum();

                    for (int le = titlemap.size(); i < le; ++i) {
                        Cell cell = row.getCell(i);
                        String titleString = (String) titlemap.get(i);
                        if (excelParams.containsKey(titleString) || Map.class.equals(pojoClass)) {
                            if (excelParams.get(titleString) != null && ((ExcelImportEntity) excelParams.get(titleString)).getType() == 2) {
                                String picId = row.getRowNum() + "_" + i;
                                this.saveImage(object, picId, excelParams, titleString, pictures, params);
                            } else {
                                this.saveFieldValue(params, object, cell, excelParams, titleString, row);
                            }
                        }
                    }

                    var23 = excelCollection.iterator();

                    while (var23.hasNext()) {
                        param = (ExcelCollectionParams) var23.next();
                        this.addListContinue(object, param, row, titlemap, targetId, pictures, params);
                    }

                    if (this.verifyingDataValidity(object, row, params, pojoClass)) {
                        collection.add(object);
                    }
                } catch (ExcelImportException var19) {
                    if (!var19.getType().equals(ExcelImportEnum.VERIFY_ERROR)) {
                        throw new ExcelImportException(var19.getType(), var19);
                    }
                }
            }
        }

        return collection;
    }

    private boolean verifyingDataValidity(Object object, Row row, ImportParams params, Class<?> pojoClass) {
        boolean isAdd = true;
        Cell cell = null;
        IExcelModel model;
        if (params.isNeedVerfiy()) {
            String errorMsg = PoiValidationUtil.validation(object);
            if (StringUtils.isNotEmpty(errorMsg)) {
                cell = row.createCell(row.getLastCellNum());
                cell.setCellValue(errorMsg);
                if (object instanceof IExcelModel) {
                    model = (IExcelModel) object;
                    model.setErrorMsg(errorMsg);
                } else {
                    isAdd = false;
                }

                this.verfiyFail = true;
            }
        }

        if (params.getVerifyHanlder() != null) {
            ExcelVerifyHanlderResult result = params.getVerifyHanlder().verifyHandler(object);
            if (!result.isSuccess()) {
                if (cell == null) {
                    cell = row.createCell(row.getLastCellNum());
                }

                cell.setCellValue((StringUtils.isNoneBlank(new CharSequence[]{cell.getStringCellValue()}) ? cell.getStringCellValue() + "," : "") + result.getMsg());
                if (object instanceof IExcelModel) {
                    model = (IExcelModel) object;
                    model.setErrorMsg((StringUtils.isNoneBlank(new CharSequence[]{model.getErrorMsg()}) ? model.getErrorMsg() + "," : "") + result.getMsg());
                } else {
                    isAdd = false;
                }

                this.verfiyFail = true;
            }
        }

        if (cell != null) {
            cell.setCellStyle(this.errorCellStyle);
        }

        return isAdd;
    }

    private Map<Integer, String> getTitleMap(Iterator<Row> rows, ImportParams params, List<ExcelCollectionParams> excelCollection) {
        Map<Integer, String> titlemap = new HashMap();
        String collectionName = null;
        ExcelCollectionParams collectionParams = null;
        Row row = null;

        label44:
        for (int j = 0; j < params.getHeadRows(); ++j) {
            row = (Row) rows.next();
            if (row != null) {
                Iterator cellTitle = row.cellIterator();

                while (true) {
                    String value;
                    int i;
                    do {
                        if (!cellTitle.hasNext()) {
                            continue label44;
                        }

                        Cell cell = (Cell) cellTitle.next();
                        value = this.getKeyValue(cell);
                        i = cell.getColumnIndex();
                    } while (!StringUtils.isNotEmpty(value));

                    if (titlemap.containsKey(i)) {
                        collectionName = (String) titlemap.get(i);
                        collectionParams = this.getCollectionParams(excelCollection, collectionName);
                        titlemap.put(i, collectionName + "_" + value);
                    } else if (StringUtils.isNotEmpty(collectionName) && collectionParams.getExcelParams().containsKey(collectionName + "_" + value)) {
                        titlemap.put(i, collectionName + "_" + value);
                    } else {
                        collectionName = null;
                        collectionParams = null;
                    }

                    if (StringUtils.isEmpty(collectionName)) {
                        titlemap.put(i, value);
                    }
                }
            }
        }

        return titlemap;
    }

    private ExcelCollectionParams getCollectionParams(List<ExcelCollectionParams> excelCollection, String collectionName) {
        Iterator var3 = excelCollection.iterator();

        ExcelCollectionParams excelCollectionParams;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            excelCollectionParams = (ExcelCollectionParams) var3.next();
        } while (!collectionName.equals(excelCollectionParams.getExcelName()));

        return excelCollectionParams;
    }

    public ExcelImportResult importExcelByIs(InputStream inputstream, Class<?> pojoClass, ImportParams params) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Excel import start ,class is {}", pojoClass);
        }

        List<T> result = new ArrayList();
        Workbook book = null;
        boolean isXSSFWorkbook = true;
        if (!((InputStream) inputstream).markSupported()) {
            inputstream = new PushbackInputStream((InputStream) inputstream, 8);
        }

        if (POIFSFileSystem.hasPOIFSHeader((InputStream) inputstream)) {
            book = new HSSFWorkbook((InputStream) inputstream);
            isXSSFWorkbook = false;
        } else if (POIXMLDocument.hasOOXMLHeader((InputStream) inputstream)) {
            book = new XSSFWorkbook(OPCPackage.open((InputStream) inputstream));
        }

        this.createErrorCellStyle((Workbook) book);

        for (int i = params.getStartSheetIndex(); i < params.getStartSheetIndex() + params.getSheetNum(); ++i) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" start to read excel by is ,startTime is {}", (new Date()).getTime());
            }

            Map pictures;
            if (isXSSFWorkbook) {
                pictures = PoiPublicUtil.getSheetPictrues07((XSSFSheet) ((Workbook) book).getSheetAt(i), (XSSFWorkbook) book);
            } else {
                pictures = PoiPublicUtil.getSheetPictrues03((HSSFSheet) ((Workbook) book).getSheetAt(i), (HSSFWorkbook) book);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" end to read excel by is ,endTime is {}", (new Date()).getTime());
            }

            result.addAll(this.importExcel(result, ((Workbook) book).getSheetAt(i), pojoClass, params, pictures));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" end to read excel list by pos ,endTime is {}", (new Date()).getTime());
            }
        }

        if (params.isNeedSave()) {
            this.saveThisExcel(params, pojoClass, isXSSFWorkbook, (Workbook) book);
        }

        return new ExcelImportResult(result, this.verfiyFail, (Workbook) book);
    }

    private void checkIsValidTemplate(Map<Integer, String> titlemap, Map<String, ExcelImportEntity> excelParams, ImportParams params, List<ExcelCollectionParams> excelCollection) {
        int i = 0;
        if (params.getImportFields() != null) {
            for (i = params.getImportFields().length; i < i; ++i) {
                if (!titlemap.containsValue(params.getImportFields()[i])) {
                    throw new ExcelImportException(ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE);
                }
            }
        } else {
            Collection<ExcelImportEntity> collection = excelParams.values();
            Iterator var12 = collection.iterator();

            while (var12.hasNext()) {
                ExcelImportEntity excelImportEntity = (ExcelImportEntity) var12.next();
                if (excelImportEntity.isImportField() && !titlemap.containsValue(excelImportEntity.getName())) {
                    LOGGER.error(excelImportEntity.getName() + "必须有,但是没找到");
                    throw new ExcelImportException(ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE);
                }
            }

            i = 0;

            for (int le = excelCollection.size(); i < le; ++i) {
                ExcelCollectionParams collectionparams = (ExcelCollectionParams) excelCollection.get(i);
                collection = collectionparams.getExcelParams().values();
                Iterator var9 = collection.iterator();

                while (var9.hasNext()) {
                    ExcelImportEntity excelImportEntity = (ExcelImportEntity) var9.next();
                    if (excelImportEntity.isImportField() && !titlemap.containsValue(collectionparams.getExcelName() + "_" + excelImportEntity.getName())) {
                        throw new ExcelImportException(ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE);
                    }
                }
            }
        }

    }

    private void saveFieldValue(ImportParams params, Object object, Cell cell, Map<String, ExcelImportEntity> excelParams, String titleString, Row row) throws Exception {
        Object value = this.cellValueServer.getValue(params.getDataHanlder(), object, cell, excelParams, titleString);
        if (object instanceof Map) {
            if (params.getDataHanlder() != null) {
                params.getDataHanlder().setMapValue((Map) object, titleString, value);
            } else {
                ((Map) object).put(titleString, value);
            }
        } else {
            this.setValues((ExcelImportEntity) excelParams.get(titleString), object, value);
        }

    }

    private void saveImage(Object object, String picId, Map<String, ExcelImportEntity> excelParams, String titleString, Map<String, PictureData> pictures, ImportParams params) throws Exception {
        if (pictures != null) {
            PictureData image = (PictureData) pictures.get(picId);
            if (image != null) {
                byte[] data = image.getData();
                String fileName = "pic" + Math.round(Math.random() * 1.0E11D);
                fileName = fileName + "." + PoiPublicUtil.getFileExtendName(data);
                if (((ExcelImportEntity) excelParams.get(titleString)).getSaveType() == 1) {
                    String path = this.getSaveUrl((ExcelImportEntity) excelParams.get(titleString), object);
                    File savefile = new File(path);
                    if (!savefile.exists()) {
                        savefile.mkdirs();
                    }

                    savefile = new File(path + "/" + fileName);
                    FileOutputStream fos = new FileOutputStream(savefile);

                    try {
                        fos.write(data);
                    } finally {
                        IOUtils.closeQuietly(fos);
                    }

                    this.setValues((ExcelImportEntity) excelParams.get(titleString), object, this.getSaveUrl((ExcelImportEntity) excelParams.get(titleString), object) + "/" + fileName);
                } else {
                    this.setValues((ExcelImportEntity) excelParams.get(titleString), object, data);
                }

            }
        }
    }

    private void createErrorCellStyle(Workbook workbook) {
        this.errorCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor((short) 10);
        this.errorCellStyle.setFont(font);
    }
}
