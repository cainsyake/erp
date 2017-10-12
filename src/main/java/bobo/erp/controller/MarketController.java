package bobo.erp.controller;

import bobo.erp.entity.market.MarketBid;
import bobo.erp.entity.market.MarketOrder;
import bobo.erp.entity.market.MarketSeries;
import bobo.erp.model.market.AddMarket;
import bobo.erp.model.market.UseMarket;
import bobo.erp.repository.market.MarketSeriesRepository;
import bobo.erp.utils.ExcelImportUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by 59814 on 2017/7/23.
 */
@Controller
public class MarketController {
    private Logger logger = LoggerFactory.getLogger(MarketController.class);

    @Autowired
    private AddMarket addMarket;

    @Autowired
    private UseMarket useMarket;

    @Autowired
    private MarketSeriesRepository marketSeriesRepository;

    @PostMapping(value = "addMarket")
    @ResponseBody
    public String  addMarket(@RequestParam(value = "filename") MultipartFile file,
                          @RequestParam(value = "marketSeriesUploader") String uploader,
                          @RequestParam(value = "marketSeriesName") String seriesName) throws IOException{
        logger.info("收到添加市场请求，seriesName:{}", seriesName);
        if(file == null){
            return "市场文件是空的";
        }
        String fileName=file.getOriginalFilename(); //获取文件名
        if(!ExcelImportUtils.validateExcel(fileName)){  //验证文件名是否合格
            return "上传的文件必须是excel2007及更新版本的xlsx文件！";
        }
        long size=file.getSize();   //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        if(StringUtils.isEmpty(fileName) || size==0){
            return "市场文件是空的";
        }
        String message = addMarket.addMarket(fileName, file, uploader, seriesName);
        return message;
    }

    @GetMapping(value = "findOrderBySeriesId/{seriesId}")
    @ResponseBody
    public List<MarketOrder> findOrderBySeriesId(@PathVariable("seriesId") Integer seriesId){
        return useMarket.findOrderBySeriesId(seriesId);
    }

    @GetMapping(value = "findBidBySeriesId/{seriesId}")
    @ResponseBody
    public List<MarketBid> findBidBySeriesId(@PathVariable("seriesId") Integer seriesId){
        return useMarket.findBidBySeriesId(seriesId);
    }

    @GetMapping(value = "findMarketSeriesAll")
    @ResponseBody
    public List<MarketSeries> findMarketSeriesAll(){
        return useMarket.findMarketSeriesAll();
    }

    @PostMapping(value = "getMarketSeries/{seriesId}")
    @ResponseBody
    public MarketSeries getMarketSeries(@PathVariable("seriesId") Integer seriesId){
        return marketSeriesRepository.findOne(seriesId);
    }

}
