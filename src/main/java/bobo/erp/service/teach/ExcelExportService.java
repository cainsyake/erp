package bobo.erp.service.teach;

import bobo.erp.entity.state.marketing.AdvertisingState;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


/**
 * Created by bobo on 2017/11/18.
 */
@Service
public class ExcelExportService {

    public Integer exportAdReport(List<AdvertisingState> advertisingStateList, List<String> productNameList, List<String> areaNameList, String title){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("AD");

        //通用单元格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); //居中
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框

        int productQuantity = productNameList.size();
        int areaQuantity = areaNameList.size();
        int n = 0;
        for (AdvertisingState advertisingState : advertisingStateList){

            CellRangeAddress cra = new CellRangeAddress((3+productQuantity) * n + 2, (3+productQuantity) * n + 2, 2, 2 + areaQuantity);
            sheet.addMergedRegion(cra);
            XSSFRow titleRow = sheet.createRow((3+productQuantity) * n + 2);
            XSSFCell titleCell = titleRow.createCell(2);
            titleCell.setCellValue((n + 1) + "广告投放情况");

            XSSFCellStyle titleStyle = workbook.createCellStyle();  //每组首行样式
            titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            titleCell.setCellStyle(titleStyle);

            XSSFRow secondRow = sheet.createRow((3+productQuantity) * n + 3);
            XSSFCell productCell = secondRow.createCell(2);
            productCell.setCellValue("产品");
            productCell.setCellStyle(cellStyle);
            for (int i = 0; i < areaQuantity; i++){
                XSSFCell areaCell = secondRow.createCell(3 + i);
                areaCell.setCellValue(areaNameList.get(i));
                areaCell.setCellStyle(cellStyle);
            }

            for (int i = 0; i < productQuantity; i++){
                XSSFRow adRow = sheet.createRow((3+productQuantity) * n + 4 + i);
                XSSFCell productNameCell = adRow.createCell(2);
                productNameCell.setCellValue(productNameList.get(i));
                productNameCell.setCellStyle(cellStyle);

                for (int j = 0; j < areaQuantity; j++){
                    XSSFCell adCell = adRow.createCell(3 + j);
                    //先根据i(产品标号) 计算出起始k 再根据j(区域标号)对k进行叠加运算
                    Integer adValue = 0;
                    switch (i){
                        case 0:
                            switch (j){
                                case 0:
                                    adValue = advertisingState.getAd1();
                                    break;
                                case 1:
                                    adValue = advertisingState.getAd2();
                                    break;
                                case 2:
                                    adValue = advertisingState.getAd3();
                                    break;
                                case 3:
                                    adValue = advertisingState.getAd4();
                                    break;
                                case 4:
                                    adValue = advertisingState.getAd5();
                                    break;
                            }
                            break;
                        case 1:
                            switch (j){
                                case 0:
                                    adValue = advertisingState.getAd6();
                                    break;
                                case 1:
                                    adValue = advertisingState.getAd7();
                                    break;
                                case 2:
                                    adValue = advertisingState.getAd8();
                                    break;
                                case 3:
                                    adValue = advertisingState.getAd9();
                                    break;
                                case 4:
                                    adValue = advertisingState.getAd10();
                                    break;
                            }
                            break;
                        case 2:
                            switch (j){
                                case 0:
                                    adValue = advertisingState.getAd11();
                                    break;
                                case 1:
                                    adValue = advertisingState.getAd12();
                                    break;
                                case 2:
                                    adValue = advertisingState.getAd13();
                                    break;
                                case 3:
                                    adValue = advertisingState.getAd14();
                                    break;
                                case 4:
                                    adValue = advertisingState.getAd15();
                                    break;
                            }
                            break;
                        case 3:
                            switch (j){
                                case 0:
                                    adValue = advertisingState.getAd16();
                                    break;
                                case 1:
                                    adValue = advertisingState.getAd17();
                                    break;
                                case 2:
                                    adValue = advertisingState.getAd18();
                                    break;
                                case 3:
                                    adValue = advertisingState.getAd19();
                                    break;
                                case 4:
                                    adValue = advertisingState.getAd20();
                                    break;
                            }
                            break;
                        case 4:
                            switch (j){
                                case 0:
                                    adValue = advertisingState.getAd21();
                                    break;
                                case 1:
                                    adValue = advertisingState.getAd22();
                                    break;
                                case 2:
                                    adValue = advertisingState.getAd23();
                                    break;
                                case 3:
                                    adValue = advertisingState.getAd24();
                                    break;
                                case 4:
                                    adValue = advertisingState.getAd25();
                                    break;
                            }
                            break;
                    }
                    if (adValue == null){
                        adValue = 0;
                    }
                    adCell.setCellValue(adValue); //放置广告值
                    adCell.setCellStyle(cellStyle);
                }
            }

            n++;

        }

        try {

            File reportDir = new File("report/ad");
            if(!reportDir.exists()){
                reportDir.mkdirs();
            }

            String fileName = "report/ad/" + title + ".xlsx";   //直接在web中建立dir
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }
}
