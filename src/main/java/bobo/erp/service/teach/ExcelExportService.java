package bobo.erp.service.teach;

import bobo.erp.entity.state.marketing.AdvertisingState;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


/**
 * Created by bobo on 2017/11/18.
 */
@Service
public class ExcelExportService {
    @Value("${reportpath}")
    private String reportpath;    //从配置文件中读取文件路径

    public Integer exportAdReport(List<AdvertisingState> advertisingStateList, List<String> productNameList, List<String> areaNameList, String title){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("AD");

        //给单子名称一个长度
//        sheet.setDefaultColumnWidth((short)15);
        // 生成一个样式
//        HSSFCellStyle style = wb.createCellStyle();

        int productQuantity = productNameList.size();
        int areaQuantity = areaNameList.size();
        int n = 0;
        for (AdvertisingState advertisingState : advertisingStateList){

            CellRangeAddress cra=new CellRangeAddress((3+productQuantity) * n + 2, (3+productQuantity) * n + 2, 2, 2 + areaQuantity);
            sheet.addMergedRegion(cra);
            XSSFRow titleRow = sheet.createRow((3+productQuantity) * n + 2);
            XSSFCell titleCell = titleRow.createCell(2);
            titleCell.setCellValue((n + 1) + "广告投放情况");

            XSSFRow secondRow = sheet.createRow((3+productQuantity) * n + 3);
            XSSFCell productCell = secondRow.createCell(2);
            productCell.setCellValue("产品");
            for (int i = 0; i < areaQuantity; i++){
                XSSFCell areaCell = secondRow.createCell(3 + i);
                areaCell.setCellValue(areaNameList.get(i));
            }

            for (int i = 0; i < productQuantity; i++){
                XSSFRow adRow = sheet.createRow((3+productQuantity) * n + 4 + i);
                XSSFCell productNameCell = adRow.createCell(2);
                productNameCell.setCellValue(productNameList.get(i));
                for (int j = 0; j < areaQuantity; j++){
                    XSSFCell adCell = adRow.createCell(3 + j);
                    adCell.setCellValue(i + j); //放置广告值 测试用
                }
            }

            n++;

        }

        try {
//            String fileName = "D:/maet-erp/reportoutput/" + title + ".xlsx";  //可以使用的fileName
            /*  弃用
            File dir = new  File(reportpath);
            if (!dir.exists()){
                dir.mkdirs();   //如果该目录不存在则新建
            }
            String fileName = reportpath + "/" + title + ".xlsx";
            */

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
