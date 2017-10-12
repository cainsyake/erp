package bobo.erp.model.market;

import bobo.erp.entity.market.MarketBid;
import bobo.erp.entity.market.MarketOrder;
import bobo.erp.entity.market.MarketSeries;
import bobo.erp.entity.market.data.AreaData;
import bobo.erp.entity.market.data.MarketData;
import bobo.erp.entity.market.data.ProductData;
import bobo.erp.entity.market.data.TimeData;
import bobo.erp.repository.market.MarketBidRepository;
import bobo.erp.repository.market.MarketOrderRepository;
import bobo.erp.repository.market.MarketSeriesRepository;
import bobo.erp.utils.ExcelImportUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 59814 on 2017/7/23.
 */
@Service
public class AddMarket {
    private Logger logger = LoggerFactory.getLogger(AddMarket.class);

    @Autowired
    private MarketSeriesRepository marketSeriesRepository;
    @Autowired
    private MarketOrderRepository marketOrderRepository;
    @Autowired
    private MarketBidRepository marketBidRepository;

    @Value("${filepath}")
    private String filepath;    //从配置文件中读取文件路径

    public String addMarket(String fileName, MultipartFile mfile, String uploader, String seriesName){
        File uploadDir = new  File(filepath);
        System.out.println("测试输出文件路径：" + filepath);
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File( filepath + "\\" + new Date().getTime() + ".xlsx");
        //初始化输入流
        InputStream is = null;
        try{
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是xls版本还是xlsx版本
            if(ExcelImportUtils.isExcel2007(fileName)){
                wb = new XSSFWorkbook(is);  //返回xlsx版本
            }else{
                wb = new HSSFWorkbook(is);  //返回xls版本
            }
            //根据excel里面的内容读取市场信息
            return readExcelValue(wb,tempFile, uploader, seriesName);    //正常的执行到这里就返回
        }catch(Exception e){
            e.printStackTrace();
        } finally{
            if(is !=null)
            {
                try{
                    is.close();
                }catch(IOException e){
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return "导入出错！请检查数据格式！";
    }

    @Transactional
    private String readExcelValue(Workbook wb, File tempFile, String uploader, String seriesName) {
        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet=wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows=sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if(totalRows>=2 && sheet.getRow(1) != null){
            totalCells=sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<MarketOrder> marketOrderList=new ArrayList<MarketOrder>();
        MarketOrder tempMarketOrder;

        int maxOrderTime = 1;
        int maxOrderProduct = 1;
        int maxOrderArea = 1;

        String br = "<br/>";

        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null){
                errorMsg += br+"第"+(r+1)+"行数据有问题，请仔细检查！";
                continue;
            }
            tempMarketOrder = new MarketOrder();
            Integer orderYear=0;          //年份
            Integer orderArea=0;          //区域
            Integer orderProduct=0;       //产品Id
            Integer orderQuantity=0;      //数量
            Integer orderTotalPrice=0;    //总价
            Integer orderDeliveryTime=0;  //交货期
            Integer orderAccountPeriod=0; //账期
            Integer orderQualificate=0;   //订单要求

            //循环Excel的列
            for(int c = 0; c <totalCells; c++){
                Cell cell = row.getCell(c);
                if (null != cell) {
                    switch (c) {
                        case 0:
                            try {
                                orderYear = Integer.valueOf((int) cell.getNumericCellValue());
                                if (orderYear > maxOrderTime){
                                    maxOrderTime = orderYear;
                                }
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (orderYear <= 0) {
                                rowMessage += "年份不能为0";
                            }
                            tempMarketOrder.setOrderYear(orderYear);
                            break;
                        case 1:
                            try {
                                orderArea = Integer.valueOf((int) cell.getNumericCellValue());
                                if (orderArea > maxOrderArea){
                                    maxOrderArea = orderArea;
                                }
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }

                            if (orderArea <= 0) {
                                rowMessage += "区域不能为0";
                            }
                            tempMarketOrder.setOrderArea(orderArea);
                            break;
                        case 2:
                            try {
                                orderProduct = Integer.valueOf((int) cell.getNumericCellValue());
                                if (orderProduct > maxOrderProduct){
                                    maxOrderProduct = orderProduct;
                                }
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }

                            if (orderProduct <= 0) {
                                rowMessage += "产品不能为0";
                            }
                            tempMarketOrder.setOrderProduct(orderProduct);
                            break;
                        case 3:
                            try {
                                orderQuantity = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (orderQuantity <= 0) {
                                rowMessage += "数量不能为0";
                            }
                            tempMarketOrder.setOrderQuantity(orderQuantity);
                            break;
                        case 4:
                            try {
                                orderTotalPrice = Integer.valueOf((int)cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (orderTotalPrice <= 0) {
                                rowMessage += "总价不能为0";
                            }
                            tempMarketOrder.setOrderTotalPrice(orderTotalPrice);
                            break;
                        case 5:
                            try {
                                orderDeliveryTime = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }

                            if (orderDeliveryTime <= 0) {
                                rowMessage += "交货期不能为0";
                            }
                            tempMarketOrder.setOrderDeliveryTime(orderDeliveryTime);
                            break;
                        case 6:
                            try {
                                orderAccountPeriod = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (orderAccountPeriod < 0) {
                                rowMessage += "账期不能小于0";
                            }
                            tempMarketOrder.setOrderAccountPeriod(orderAccountPeriod);
                            break;
                        case 7:
                            try {
                                orderQualificate = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (orderQualificate < 0) {
                                rowMessage += "资质认证不能小于0";
                            }
                            tempMarketOrder.setOrderQualificate(orderQualificate);
                            break;
                        default:
                            rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                            break;
                    }
                }
            }
            //拼接每行的错误提示
            if(!StringUtils.isEmpty(rowMessage)){
                errorMsg += br+"第"+(r+1)+"行，"+rowMessage;
            }else{
//                tempMarketOrder.setMarketSeriesId(seriesId);
                marketOrderList.add(tempMarketOrder);
            }
        }


        //获取第二个sheet 即为竞单部分
        Sheet sheet2=wb.getSheetAt(1);
        //得到Excel的行数
        int totalRows2=sheet2.getPhysicalNumberOfRows();
        //总列数
        int totalCells2 = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if(totalRows2>=2 && sheet2.getRow(1) != null){
            totalCells2=sheet2.getRow(1).getPhysicalNumberOfCells();
        }
        List<MarketBid> marketBidList=new ArrayList<MarketBid>();
        MarketBid tempMarketBid;

        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows2;r++){
            String rowMessage = "";
            Row row = sheet2.getRow(r);
            if (row == null){
                errorMsg += br+"竞单 第"+(r+1)+"行数据有问题，请仔细检查！";
                continue;
            }
            tempMarketBid = new MarketBid();
            Integer bidYear=0;        //年份
            Integer bidArea=0;        //区域
            Integer bidProduct=0;     //产品Id
            Integer bidQuantity=0;    //数量
            Integer bidQualificate=0; //竞单资质要求

            //循环Excel的列
            for(int c = 0; c <totalCells; c++){
                Cell cell = row.getCell(c);
                if (null != cell) {
                    switch (c) {
                        case 0:
                            try {
                                bidYear = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (bidYear <= 0) {
                                rowMessage += "年份不能为0";
                            }
                            tempMarketBid.setBidYear(bidYear);
                            break;
                        case 1:
                            try {
                                bidArea = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }

                            if (bidArea <= 0) {
                                rowMessage += "区域不能为0";
                            }
                            tempMarketBid.setBidArea(bidArea);
                            break;
                        case 2:
                            try {
                                bidProduct = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }

                            if (bidProduct <= 0) {
                                rowMessage += "产品不能为0";
                            }
                            tempMarketBid.setBidProduct(bidProduct);
                            break;
                        case 3:
                            try {
                                bidQuantity = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (bidQuantity <= 0) {
                                rowMessage += "数量不能为0";
                            }
                            tempMarketBid.setBidQuantity(bidQuantity);
                            break;
                        case 4:
                            try {
                                bidQualificate = Integer.valueOf((int) cell.getNumericCellValue());
                            } catch (Exception e) {
                                rowMessage += "数据录入错误";
                            }
                            if (bidQualificate < 0) {
                                rowMessage += "资质认证不能小于0";
                            }
                            tempMarketBid.setBidQualificate(bidQualificate);
                            break;
                        default:
                            rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                            break;
                    }
                }
            }
            //拼接每行的错误提示
            if(!StringUtils.isEmpty(rowMessage)){
                errorMsg += br+"第"+(r+1)+"行，"+rowMessage;
            }else{
                marketBidList.add(tempMarketBid);
            }
        }

        //删除上传的临时文件
        if(tempFile.exists()){
            tempFile.delete();
        }
        //全部验证通过才导入到数据库
        if(StringUtils.isEmpty(errorMsg)){
            //计算市场分析
            MarketData marketData = initData(maxOrderTime, maxOrderProduct, maxOrderArea);
            for (MarketOrder marketOrder : marketOrderList){
                dataAdd(marketData, marketOrder);
            }


            //先导入MarketSeries
            Date date = new Date();
            MarketSeries marketSeries = new MarketSeries();
            marketSeries.setMarketData(marketData);
            marketSeries.setMarketSeriesUploader(uploader);
            marketSeries.setMarketSeriesName(seriesName);
            marketSeries.setMarketSeriesAlterTime(date);
            marketSeries.setMarketSeriesUseCount(0);
            //保存MarketSeries并获取seriesId
            Integer seriesId =  marketSeriesRepository.save(marketSeries).getMarketSeriesId();
            for(MarketOrder marketOrder : marketOrderList){
                marketOrder.setMarketSeriesId(seriesId);
                marketOrderRepository.save(marketOrder);
            }
            for(MarketBid marketBid : marketBidList){
                marketBid.setMarketSeriesId(seriesId);
                marketBidRepository.save(marketBid);
            }
            errorMsg = "SeriesID:" + seriesId + "市场导入成功，本套市场共有"+marketOrderList.size()+"张订单和" + marketBidList.size()  + "张竞单。";
            logger.info("成功处理SeriesID:{} 规则的上传！ 操作者:{}", seriesId, uploader);
        }
        return errorMsg;
    }

    @Transactional
    public MarketData initData(int maxOrderTime, int maxOrderProduct, int maxOrderArea){
        MarketData marketData = new MarketData();
        List<TimeData> timeDataList = new ArrayList<TimeData>();
        for (int i = 1; i <= maxOrderTime; i++){
            TimeData timeData = new TimeData();
            timeData.setType(i);
            List<ProductData> productDataList = new ArrayList<ProductData>();
            for (int j = 1; j <= maxOrderProduct; j++){
                ProductData productData = new ProductData();
                productData.setType(j);
                List<AreaData> areaDataList = new ArrayList<AreaData>();
                for (int k = 1; k <= maxOrderArea; k++){
                    AreaData areaData = new AreaData();
                    areaData.setType(k);
                    areaData.setAveragePrice(0);
                    areaData.setRequirement(0);
                    areaData.setOrderQuantity(0);
                    areaDataList.add(areaData);
                }
                productData.setAreaDataList(areaDataList);
                productDataList.add(productData);
            }
            timeData.setProductDataList(productDataList);
            timeDataList.add(timeData);
        }
        marketData.setTimeDataList(timeDataList);
        return marketData;
    }

    @Transactional
    public MarketData dataAdd(MarketData marketData, MarketOrder marketOrder){
        List<TimeData> timeDataList = marketData.getTimeDataList();
        for (TimeData timeData : timeDataList){
            if (timeData.getType() == marketOrder.getOrderYear()){
                List<ProductData> productDataList = timeData.getProductDataList();
                for (ProductData productData : productDataList){
                    if (productData.getType() == marketOrder.getOrderProduct()){
                        List<AreaData> areaDataList = productData.getAreaDataList();
                        for (AreaData areaData : areaDataList){
                            if (areaData.getType() == marketOrder.getOrderArea()){
                                areaData.setAveragePrice(
                                        (areaData.getAveragePrice() * areaData.getRequirement() + marketOrder.getOrderTotalPrice()) /
                                                (areaData.getRequirement() + marketOrder.getOrderQuantity())
                                );
                                areaData.setRequirement(areaData.getRequirement() + marketOrder.getOrderQuantity());
                                areaData.setOrderQuantity(areaData.getOrderQuantity() + 1);
                            }
                        }
                    }
                }
            }
        }
        return marketData;
    }

}
