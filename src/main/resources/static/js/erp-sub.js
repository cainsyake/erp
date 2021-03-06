window.onload=subOnLoad;    //页面载入时执行此函数

/**
 * 全局变量 rule (因为rule几乎不会变动,为了节省请求资源,页面载入时请求一次即可)
 */
var rule = new Object();
initRule($("#nowUserName").val());  //获取rule
var runningState = new Object();
var orderMeetingState = 0;
var bidMeetingState = 0;


function subOnLoad() {
    var nowUserName = $("#nowUserName").val();  //从页面读取用户名
    //TODO 可以改成只发一次请求，获取打包的runningState对象和meetingStateList对象

    $.ajax({
        type:"POST",
        url:"/runningStatePacking",
        cache:false,
        data: {username: nowUserName},
        dataType:"json",
        success:function (data) {   //请求成功后服务器返回runningState Packing
            runningState = data.runningState;
            orderMeetingState = data.meetingStateList[0];
            bidMeetingState = data.meetingStateList[1];

            /**
             * 调用页面初始化函数
             * 将pageController与infoController合并
             */
            // btnController(runningState);    //页面按钮控制函数
            // pageController(runningState); //页面表格控制函数
            // infoController(runningState); //页面内容控制函数

            /**
             * 测试对象树遍历及对象复制
             */
            // var testObj = deepCopy(rule);
            // console.log('输出处理前对象');
            // console.log(testObj);
            // console.log('开始遍历对象');
            // eachProps(testObj);
            // console.log('结束遍历对象,输出处理后对象');
            // console.log(testObj);
            // console.log('检查rule对象');
            // console.log(rule);
            // var ajaxData = {rule: testObj, username: 'cloneTest'};
            // $.ajax({
            //     url: '/addRule',
            //     type: 'POST',
            //     contentType: "application/json",
            //     data: JSON.stringify(ajaxData),
            //     dataType:'json',
            //     success:function (rs) {
            //         console.log('Clone Success');
            //     },
            //     error:function (rs) {
            //         console.log(rs);
            //     }
            // });
            // console.log('ALL END');
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function initRule(username) {
    $.ajax({
        type:"POST",
        url:"/getTeachClassRule/" + username,
        cache:false,
        async: false,   //同步Ajax请求
        dataType:"json",
        success:function (ruleReturn) {   //请求成功后服务器返回rule对象
           rule = ruleReturn;
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function deepCopy(obj) {
    var str, newobj = obj.constructor === Array ? [] : {};
    if(typeof obj !== 'object'){
        return;
    } else if(window.JSON){
        str = JSON.stringify(obj), //系列化对象
            newobj = JSON.parse(str); //还原
    } else {
        for(var i in obj){
            newobj[i] = typeof obj[i] === 'object' ?
                cloneObj(obj[i]) : obj[i];
        }
    }
    return newobj;
}

function eachProps(obj) {

    for (var prop in obj) {
        if (typeof (obj[prop]) == 'object') {
            eachProps(obj[prop]);
        } else if (typeof (obj[prop]) == 'array') {
            obj[prop].forEach(function (subObj, index, array) {
                eachProps(subObj);
            });
        } else if (prop === 'id') {
            // console.log('捕捉到id:' + obj[prop]);
            delete obj[prop];
            // console.log(obj);
            // console.log('-----');

        }
    }

}

function getRunningState(username) {
    $.ajax({
        type:"POST",
        url:"/getSubRunningState/" + username,
        cache:false,
        async: false,
        dataType:"json",
        success:function (runningState) {   //请求成功后服务器返回runningState对象
            return runningState;
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function getMeetingState(username) {
    $.ajax({
        type:"POST",
        url:"/getMeetingState",
        data: {username: username},
        cache:false,
        async: false,
        dataType:"json",
        success:function (stateList) {   //请求成功后服务器返回list
            return stateList;   //list[0]为选单会状态,list[0]为竞单会状态
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function getLineInfo(type) {
    var lines = rule.ruleLineList;
    for (var i = 0; i < lines.length; i++){
        if (lines[i].type == type){
            return lines[i];
        }
    }
    return null;    //异常情况
}

function getFactoryInfo(type) {
    var factories = rule.ruleFactoryList;
    for (var i = 0; i < factories.length; i++){
        if (factories[i].type == type){
            return factories[i];
        }
    }
    return null;    //异常情况
}

function getQualificationInfo(type) {
    var qualifications = rule.ruleQualificationList;
    for (var i = 0; i < qualifications.length; i++){
        if (qualifications[i].type == type){
            return qualifications[i];
        }
    }
    return null;    //异常情况
}

function getAreaInfo(type) {
    var areas = rule.ruleAreaList;
    for (var i = 0; i < areas.length; i++){
        if (areas[i].type == type){
            return areas[i];
        }
    }
    return null;    //异常情况
}

function getMaterialInfo(type) {
    var materials = rule.ruleMaterialList;
    for (var i = 0; i < materials.length; i++){
        if (materials[i].type == type){
            return materials[i];
        }
    }
    return null;    //异常情况
}

function getProductInfo(type) {
    var products = rule.ruleProductList;
    for (var i = 0; i < products.length; i++){
        if (products[i].type == type){
            return products[i];
        }
    }
    return null;    //异常情况
}

function getProductBomInfo(type) {
    var productBoms = rule.ruleProductBomList;
    for (var i = 0; i < productBoms.length; i++){
        if (productBoms[i].type == type){
            return productBoms[i];
        }
    }
    return null;    //异常情况
}

/**
 * Calculate materi quantity in producing.
 * 1, get all products produce quantity list(produceList).
 * 2, generate two list(materialList and halfProductList).
 * 3, foreach produceList, get product type and use getProductBom(type).
 * 4, add the quantity from Bom to the two list.
 * 5, make ajax request.
 */


function infoController(runningState) {
    document.getElementById("valueCash").innerHTML = runningState.financeState.cashAmount;   //输出现金金额
    document.getElementById("valueRunningTime").innerHTML = "第 " + runningState.baseState.timeYear + " 年 第 " + runningState.baseState.timeQuarter + " 季";

    var receivableStateList = runningState.financeState.receivableStateList;
    var receivableTotal = 0;
    $.each(receivableStateList,function(n,receivableState){
        if(receivableState.accountPeriod == 1){
            receivableTotal += receivableState.amounts;
        }
    });
    document.getElementById("valueReceivableTotal").innerHTML = receivableTotal;    //输出到期应收款

    var debtList = runningState.financeState.debtStateList;
    var debtTotal = 0;
    for(var i = 0; i<eval(debtList).length; i++ ){
        debtTotal += debtList[i].amounts;
    }
    var financialStatementList = runningState.financeState.financialStatementList;
    // var financialStatementListNum = eval(financialStatementList).length;
    var debtLimit = 0;
    var tempYear = 1;
    if(runningState.baseState.timeYear == 1){
        debtLimit = rule.ruleParam.paramLoanRatio * rule.ruleParam.paramInitialCash - debtTotal;
    }else {
        $.each(financialStatementList,function(n,financialStatement) {
            tempYear = financialStatement.year;
            if(tempYear == runningState.baseState.timeYear - 1){
                debtLimit = rule.ruleParam.paramLoanRatio * financialStatement.ownersEquity - debtTotal;
                if(debtLimit < 0){
                    debtLimit = 0;
                }
            }
        });
    }
    document.getElementById("valueDebtLimit").innerHTML = debtLimit;    //输出贷款限额 至长贷框
    document.getElementById("valueDebtLimit2").innerHTML = debtLimit;   //输出贷款限额 至短贷框

    var purchaseStateList = runningState.stockState.purchaseStateList;
    var purchaseTotalAmounts = 0;
    $.each(purchaseStateList,function(n,purchaseState) {
        tempType = purchaseState.type;
        tempQuantity = purchaseState.quantity;
        tempDeliveryTime = purchaseState.deliveryTime;
        if(tempDeliveryTime == 1){
            for(var j = 1; j < 6; j++){
                if(tempType == j){
                    eval("var price = rule.ruleMaterial.material" + j + "Price");
                    purchaseTotalAmounts += tempQuantity * price;
                }
            }
        }
    });
    document.getElementById("valueUpdatePurchaseAmounts").innerHTML = purchaseTotalAmounts;   //输出原料采购总价


    for(var j = 1; j < rule.ruleParam.paramLongTermLoanTimeLimit+1; j++){
        var tempAmounts = 0;
        for(var i = 0; i<eval(debtList).length; i++ ){
            if(debtList[i].debtType == 2  && debtList[i].repaymentPeriod == j){
                tempAmounts += debtList[i].amounts;
            }
        }
        eval("document.getElementById('valueLongDebt" + j + "').innerHTML = tempAmounts");    //输出长贷信息
    }

    for(var j = 1; j < 5; j++){
        var tempAmounts = 0;
        for(var i = 0; i<eval(debtList).length; i++ ){
            if(debtList[i].debtType == 1  && debtList[i].repaymentPeriod == j){
                tempAmounts = debtList[i].amounts;
                if(tempAmounts == null){
                    tempAmounts = 0;
                }
            }
        }
        eval("document.getElementById('valueShortDebt" + j + "').innerHTML = tempAmounts");    //输出短贷信息
    }

    var receivableList = runningState.financeState.receivableStateList;
    for(var j = 1; j < 5; j++){
        var tempAmounts = 0;
        for(var i = 0; i<eval(receivableList).length; i++ ){
            if(receivableList[i].accountPeriod == j){
                tempAmounts += receivableList[i].amounts;
            }
        }
        eval("document.getElementById('valueReceivable" + j + "').innerHTML = tempAmounts");    //输出应收款信息
    }

    var materialList = runningState.stockState.materialStateList;
    var ruleMaterial = rule.ruleMaterial;
    var materialNum = 0;
    for(var i = 1; i < 6; i++){
        if(eval("ruleMaterial.material" + i + "Name !== null")){
            materialNum++;
        }
    }
    for(var j = 1; j < materialNum+1; j++){
        var tempQuantity = 0;
        for(var i = 0; i < eval(materialList).length; i++){
            if(materialList[i].type == j){
                tempQuantity = materialList[i].quantity;
            }
        }
        eval("document.getElementById('valueMaterialStock" + j + "').innerHTML = tempQuantity");    //输出原料库存信息
    }

    var productList = runningState.stockState.productStateList;
    var ruleProduct = rule.ruleProduct;
    var productNum = 0;
    for(var i = 1; i < 6; i++){
        if(eval("ruleProduct.product" + i + "Name !== null")){
            productNum++;
        }
    }
    for(var j = 1; j < productNum+1; j++){
        var tempQuantity = 0;
        for(var i = 0; i < eval(productList).length; i++){
            if(productList[i].type == j){
                tempQuantity = productList[i].quantity;
            }
        }
        eval("document.getElementById('valueProductStock" + j + "').innerHTML = tempQuantity");    //输出产品库存信息
    }

    var purchaseList = runningState.stockState.purchaseStateList;
    var maxMaterialTime = 0;
    for(var i = 1; i < materialNum+1; i++){
        if(eval("ruleMaterial.material" + i + "Time > maxMaterialTime")){
            maxMaterialTime = eval("ruleMaterial.material" + i + "Time");
        }
    }
    for(var j = 1; j < materialNum+1; j++){
        for(var k = 1; k < maxMaterialTime+1; k++){
            var tempQuantity = 0;
            for(var i = 0; i < eval(purchaseList).length; i++){
                if(purchaseList[i].type == j && purchaseList[i].deliveryTime == k){
                    tempQuantity = purchaseList[i].quantity;
                }
            }
            eval("document.getElementById('valueMaterialStock" + j + "T" + k + "').innerHTML = tempQuantity");    //输出原料订单信息
        }
    }

    var factoryStateList = runningState.factoryStateList;
    var txt1 = "";  //在建
    var txt2 = "";  //转产
    var txt3 = "";  //继续转产
    var txt4 = "";  //出售
    var txt5 = "";  //开始生产
    var txt8 = "";  //出售厂房列表
    var txt9 = "";  //厂房退租
    var txt10 = ""; //租转买
    $.each(factoryStateList,function(n, factoryState) {
        var lineStateList = factoryState.lineStateList;
        var content = factoryState.content;
        var factoryRentPrice = 0;
        var factoryBuyPrice = 0;
        for(var i = 1; i < 6; i++){
            if(factoryState.type == i){
                factoryRentPrice = eval("rule.ruleFactory.factory" + i + "RentPrice");
                factoryBuyPrice = eval("rule.ruleFactory.factory" + i + "BuyPrice");
            }
        }
        if(content != 0){
            $.each(lineStateList,function(n, lineState){
                var lineName = "";
                var productName = "";
                var unitInvest = 0;
                var ruleLineNum = 0;
                var ruleProductNum = 0;
                var changTime = 0;
                var changInvest = 0;
                var scrapValue = 0;
                var procCost = 0;
                for(var i = 1; i < 6; i++){
                    if(eval("rule.ruleLine.line" + i + "Name") != ""){
                        ruleLineNum++;
                    }
                    if(eval("rule.ruleProduct.product" + i + "Name") != ""){
                        ruleProductNum++;
                    }
                    if(lineState.type == i){
                        lineName = eval("rule.ruleLine.line" + i + "Name");
                        unitInvest = eval("rule.ruleLine.line" + i + "UnitInvest");
                        changTime =  eval("rule.ruleLine.line" + i + "ChangeTime");
                        changInvest =  eval("rule.ruleLine.line" + i + "ChangeInvest");
                        scrapValue = eval("rule.ruleLine.line" + i + "ScrapValue");
                    }
                    if(lineState.productType == i){
                        productName = eval("rule.ruleProduct.product" + i + "Name");
                        procCost = eval("rule.ruleProduct.product" + i + "ProcCost");
                    }
                }
                if(lineState.owningState < 0 && lineState.produceState != null){

                    txt1 +="<tr>" +
                        "<td style='text-align: center'><input type='checkbox' name='buildLineBox' value='" + lineState.id + "'></td>" +
                        "<td style='text-align: center'>" + lineState.id + "</td>" +
                        "<td style='text-align: center'>" + factoryState.id + "</td>" +
                        "<td style='text-align: center'>" + lineName + "</td>" +
                        "<td style='text-align: center'>" + productName + "</td>" +
                        "<td style='text-align: center'>" + (-lineState.owningState) + "</td>" +
                        "<td style='text-align: center' id='valueUnitInvest" + lineState.id + "'>" + unitInvest + "</td>" +
                        "</tr>";
                }
                if(lineState.owningState > 0 && lineState.produceState == 0){
                    txt2 += "<tr>" +
                        "<td style='text-align: center'><input type='checkbox' name='changeLineBox' value='" + lineState.id + "'></td>" +
                        "<td style='text-align: center'>" + lineState.id + "</td>" +
                        "<td style='text-align: center'>" + factoryState.id + "</td>" +
                        "<td style='text-align: center'>" + lineName + "</td>" +
                        "<td style='text-align: center'>" + productName + "</td>" +
                        "<td style='text-align: center'>" + changTime + "</td>" +
                        "<td style='text-align: center' id='valueChangeInvest" + lineState.id + "'>" + changInvest + "</td>" +
                        "</tr>";

                    txt4 += "<tr>" +
                        "<td style='text-align: center'><input type='checkbox' name='saleLineBox' value='" + lineState.id + "'></td>" +
                        "<td style='text-align: center'>" + lineState.id + "</td>" +
                        "<td style='text-align: center'>" + factoryState.id + "</td>" +
                        "<td style='text-align: center'>" + lineName + "</td>" +
                        "<td style='text-align: center'>" + productName + "</td>" +
                        "<td style='text-align: center'>" + lineState.owningState + "</td>" +
                        "<td style='text-align: center' id='valueLineValue" + lineState.id + "'>" + lineState.value + "</td>" +
                        "<td style='text-align: center'id='valueLineScrapValue" + lineState.id + "'>" + scrapValue + "</td>" +
                        "</tr>";
                    var productDevStateList = runningState.devState.productDevStateList;
                    var devFinishCheck = 0;
                    $.each(productDevStateList,function(n, productDevState){
                        if(lineState.productType == productDevState.type){
                            devFinishCheck = productDevState.state;
                        }
                    });
                    if(devFinishCheck == 1){
                        txt5 += "<tr>" +
                            "<td style='text-align: center'><input type='checkbox' name='beginProductionBox' value='" + lineState.id + "'></td>" +
                            "<td style='text-align: center'>" + lineState.id + "</td>" +
                            "<td style='text-align: center'>" + factoryState.id + "</td>" +
                            "<td style='text-align: center'>" + lineName + "</td>" +
                            "<td style='text-align: center'>" + productName + "</td>" +
                            "<td style='display: none' id='valueProcCost" + lineState.id + "'>" + procCost + "</td>" +
                            "</tr>";
                    }
                }
                if((lineState.owningState > 0) &&  (lineState.produceState > (-changTime)) && (lineState.produceState < 0)){
                    txt3 +="<tr>" +
                        "<td style='text-align: center'><input type='checkbox' name='continueChangeBox' value='" + lineState.id + "'></td>" +
                        "<td style='text-align: center'>" + lineState.id + "</td>" +
                        "<td style='text-align: center'>" + factoryState.id + "</td>" +
                        "<td style='text-align: center'>" + lineName + "</td>" +
                        "<td style='text-align: center'>" + productName + "</td>" +
                        "<td style='text-align: center'>" + (-lineState.produceState) + "</td>" +
                        "<td style='text-align: center' id='valueContinueChangeInvest" + lineState.id + "'>" + changInvest + "</td>" +
                        "</tr>";
                }
            });
        }

        var factoryTimeQuarter = factoryState.finanPaymentQuarter;
        var factoryTimeYear = factoryState.finalPaymentYear;
        if(factoryState.owningState > 0){
            txt8 += "<tr>" +
                "<td style='text-align: center'><input type='checkbox' name='saleFactoryBox' value='" + factoryState.id + "'></td>" +
                "<td style='text-align: center'>" + factoryState.id + "</td>" +
                "<td style='text-align: center'>已购买</td>" +
                "<td style='text-align: center' id='valueFactoryContent" + factoryState.id + "'>" + factoryState.content + "</td>" +
                "<td style='text-align: center'>第" + factoryTimeYear + " 年第 " + factoryTimeQuarter + " 季" + "</td>" +
                "<td style='display: none' id='valueFactoryRentPrice" + factoryState.id + "'>" + factoryRentPrice + "</td>" +
                "</tr>";
        }
        if(factoryState.owningState == 0 && factoryState.content == 0){
            txt9 += "<tr>" +
                "<td style='text-align: center'><input type='checkbox' name='exitRentBox' value='" + factoryState.id + "'></td>" +
                "<td style='text-align: center'>" + factoryState.id + "</td>" +
                "<td style='text-align: center'>租赁中</td>" +
                "<td style='text-align: center'>" + factoryState.content + "</td>" +
                "<td style='text-align: center'>第" + factoryTimeYear + " 年第 " + factoryTimeQuarter + " 季" + "</td>" +
                "</tr>";
        }
        if(factoryState.owningState == 0 && (runningState.baseState.timeQuarter == factoryTimeQuarter)){
            txt10 += "<tr>" +
                "<td style='text-align: center'><input type='checkbox' name='rentToBuyBox' value='" + factoryState.id + "'></td>" +
                "<td style='text-align: center'>" + factoryState.id + "</td>" +
                "<td style='text-align: center'>租赁中</td>" +
                "<td style='text-align: center'>" + factoryState.content + "</td>" +
                "<td style='text-align: center'>第" + factoryTimeYear + " 年第 " + factoryTimeQuarter + " 季" + "</td>" +
                "<td style='display: none' id='valueFactoryRentPrice" + factoryState.id + "'>" + factoryBuyPrice + "</td>" +
                "</tr>";
        }
    });
    document.getElementById("tbodyBuildLine").innerHTML = txt1;   //输出可续建的生产线信息
    document.getElementById("tbodyChangeLine").innerHTML = txt2;   //输出可转产的生产线信息
    document.getElementById("tbodyContinueChange").innerHTML = txt3;   //输出可继续转产的生产线信息
    document.getElementById("tbodySaleLine").innerHTML = txt4;   //输出可出售的生产线信息
    document.getElementById("tbodyBeginProduction").innerHTML = txt5;   //输出可开始生产的生产线信息
    document.getElementById("tbodyFactoryTreatment1").innerHTML = txt8;   //输出可开始出售的厂房信息
    document.getElementById("tbodyFactoryTreatment2").innerHTML = txt9;   //输出可退租的厂房信息
    document.getElementById("tbodyFactoryTreatment3").innerHTML = txt10;   //输出可租转买的厂房信息

    var txt6 = "";
    var orderStateList = runningState.marketingState.orderStateList;
    $.each(orderStateList,function(n, orderState){
        if(orderState.year == runningState.baseState.timeYear){
            if(orderState.execution == 0){
                for(var i = 1; i < productNum + 1; i++){
                    if(orderState.typeId == i){
                        var productName = eval("rule.ruleProduct.product" + i + "Name");
                    }
                }
                txt6 += "<tr>" +
                    "<td style='text-align: center'><input type='checkbox' name='deliveryBox' value='" + orderState.id + "'></td>" +
                    "<td style='text-align: center'>" + orderState.orderId + "</td>" +
                    "<td style='text-align: center'>" + orderState.year + "</td>" +
                    "<td style='text-align: center'>" + orderState.area + "</td>" +
                    "<td style='text-align: center'>" + productName + "</td>" +
                    "<td style='text-align: center'>" + orderState.quantity + "</td>" +
                    "<td style='text-align: center'>" + orderState.totalPrice + "</td>" +
                    "<td style='text-align: center'>" + orderState.deliveryTime + "</td>" +
                    "<td style='text-align: center'>" + orderState.accountPeriod + "</td>" +
                    "<td style='text-align: center'>" + orderState.qualificate + "</td>" +
                    "</tr>";
            }
        }
    });
    document.getElementById("tbodyDelivery").innerHTML = txt6;   //输出可交单的订单信息

    var productDevList = runningState.devState.productDevStateList;
    var txt7 = "";
    var txt30 = "";
    for(var i = 1; i < productNum + 1; i++){
        var check = 0;
        $.each(productDevList,function(n, productDev) {
            var productName = "";
            var unitInvest = 0;
            var devTime = 0;
            for(var j = 1; j < productNum + 1; j++){
                if(productDev.type == j){
                    productName = eval("rule.ruleProduct.product" + j + "Name");
                    unitInvest = eval("rule.ruleProduct.product" + j + "DevInvest");
                    devTime = eval("rule.ruleProduct.product" + j + "DevTime");
                }
            }
            if(productDev.type == i && productDev.state != 1){
                check = 1;
                txt7 += "<tr>" +
                    "<td style='text-align: center'><input type='checkbox' name='productDevBox' value='" + productDev.type + "'></td>" +
                    "<td style='text-align: center'>" + productName + "</td>" +
                    "<td style='text-align: center'>还需投资 " + (1- productDev.state) + " 次</td>" +
                    "<td style='text-align: center' id='valueProductDev" + productDev.type + "'>" + unitInvest + "</td>" +
                    "</tr>";
                txt30 += "<tr>" +
                    "<td style='text-align: center'>" + productName + "</td>" +
                    "<td style='text-align: center'>还需投资 " + (1- productDev.state) + " 次</td>" +
                    "</tr>";
            }
            if(productDev.type == i && productDev.state == 1){
                check = 1;
                txt30 += "<tr>" +
                    "<td style='text-align: center'>" + productName + "</td>" +
                    "<td style='text-align: center'>研发完成</td>" +
                    "</tr>";
            }

        });
        if(check == 0 ){
            var productName = eval("rule.ruleProduct.product" + i + "Name");
            var unitInvest = eval("rule.ruleProduct.product" + i + "DevInvest");
            var devTime = eval("rule.ruleProduct.product" + i + "DevTime");
            txt7 += "<tr>" +
                "<td style='text-align: center'><input type='checkbox' name='productDevBox' value='" + i + "'></td>" +
                "<td style='text-align: center'>" + productName + "</td>" +
                "<td style='text-align: center'>还需投资 " + devTime + " 次</td>" +
                "<td style='text-align: center' id='valueProductDev" + i + "'>" + unitInvest + "</td>" +
                "</tr>";
        }
    }
    document.getElementById("tbodyProductDev").innerHTML = txt7;   //输出可研发的产品信息
    document.getElementById("tbodyProdectDevInfo").innerHTML = txt30;

    var marketDevList = runningState.devState.marketDevStateList;
    var txt8 = "";
    var txt31 = "";
    var marketNum = 0;
    for(var i = 1; i < 6; i++){
        if(eval("rule.ruleMarket.market" + i + "Name") != ""){
            marketNum++;
        }
    }
    for(var i = 1; i < marketNum + 1; i++){
        var check = 0;
        $.each(marketDevList,function(n, marketDev) {
            var marketName = "";
            var unitInvest = 0;
            var devTime = 0;
            for(var j = 1; j < marketNum + 1; j++){
                if(marketDev.type == j){
                    marketName = eval("rule.ruleMarket.market" + j + "Name");
                    unitInvest = eval("rule.ruleMarket.market" + j + "UnitInvest");
                    devTime = eval("rule.ruleMarket.market" + j + "DevTime");
                }
            }
            if(marketDev.type == i && marketDev.state != 1){
                check = 1;
                txt8 += "<tr>" +
                    "<td style='text-align: center'><input type='checkbox' name='marketDevBox' value='" + marketDev.type + "'></td>" +
                    "<td style='text-align: center'>" + marketName + "</td>" +
                    "<td style='text-align: center'>还需投资 " + (1- marketDev.state) + " 次</td>" +
                    "<td style='text-align: center' id='valueMarketDev" + marketDev.type + "'>" + unitInvest + "</td>" +
                    "</tr>";
                txt31 += "<tr>" +
                    "<td style='text-align: center'>" + marketName + "</td>" +
                    "<td style='text-align: center'>还需投资 " + (1- marketDev.state) + " 次</td>" +
                    "</tr>";
            }
            if(marketDev.type == i && marketDev.state == 1){
                check = 1;
                txt31 += "<tr>" +
                    "<td style='text-align: center'>" + marketName + "</td>" +
                    "<td style='text-align: center'>开拓完成</td>" +
                    "</tr>";
            }
        });
        if(check == 0 ){
            var marketName = eval("rule.ruleMarket.market" + i + "Name");
            var unitInvest = eval("rule.ruleMarket.market" + i + "UnitInvest");
            var devTime = eval("rule.ruleMarket.market" + i + "DevTime");
            txt8 += "<tr>" +
                "<td style='text-align: center'><input type='checkbox' name='marketDevBox' value='" + i + "'></td>" +
                "<td style='text-align: center'>" + marketName + "</td>" +
                "<td style='text-align: center'>还需投资 " + devTime + " 次</td>" +
                "<td style='text-align: center' id='valueMarketDev" + i + "'>" + unitInvest + "</td>" +
                "</tr>";
        }
    }
    document.getElementById("tbodyMarketDev").innerHTML = txt8;   //输出可研发的市场信息
    document.getElementById("tbodyMarketDevInfo").innerHTML = txt31;

    var qualificationDevList = runningState.devState.qualificationDevStateList;
    var txt9 = "";
    var txt32 = "";
    var qualificationNum = 0;
    for(var i = 1; i < 3; i++){
        var isoName = eval("rule.ruleIso.iso" + i + "Name");
        if(typeof(isoName)!="undefined" && isoName != ""){
            qualificationNum++;
        }
    }
    for(var i = 1; i < qualificationNum + 1; i++){
        var check = 0;
        $.each(qualificationDevList,function(n, qualificationDev) {
            var qualificationName = "";
            var unitInvest = 0;
            var devTime = 0;
            for(var j = 1; j < qualificationNum + 1; j++){
                if(qualificationDev.type == j){
                    qualificationName = eval("rule.ruleIso.iso" + j + "Name");
                    unitInvest = eval("rule.ruleIso.iso" + j + "UnitInvest");
                    devTime = eval("rule.ruleIso.iso" + j + "DevTime");
                }
            }
            if(qualificationDev.type == i && qualificationDev.state != 1){
                check = 1;
                txt9 += "<tr>" +
                    "<td style='text-align: center'><input type='checkbox' name='qualificationDevBox' value='" + qualificationDev.type + "'></td>" +
                    "<td style='text-align: center'>" + qualificationName + "</td>" +
                    "<td style='text-align: center'>还需投资 " + (1- qualificationDev.state) + " 次</td>" +
                    "<td style='text-align: center' id='valueMarketDev" + qualificationDev.type + "'>" + unitInvest + "</td>" +
                    "</tr>";
                txt32 += "<tr>" +
                    "<td style='text-align: center'>" + qualificationName + "</td>" +
                    "<td style='text-align: center'>还需投资 " + (1- qualificationDev.state) + " 次</td>" +
                    "</tr>";
            }
            if(qualificationDev.type == i && qualificationDev.state == 1){
                check = 1;
                txt32 += "<tr>" +
                    "<td style='text-align: center'>" + qualificationName + "</td>" +
                    "<td style='text-align: center'>认证完成</td>" +
                    "</tr>";
            }
        });
        if(check == 0 ){
            var qualificationName = eval("rule.ruleIso.iso" + i + "Name");
            var unitInvest = eval("rule.ruleIso.iso" + i + "UnitInvest");
            var devTime = eval("rule.ruleIso.iso" + i + "DevTime");
            txt9 += "<tr>" +
                "<td style='text-align: center'><input type='checkbox' name='qualificationDevBox' value='" + i + "'></td>" +
                "<td style='text-align: center'>" + qualificationName + "</td>" +
                "<td style='text-align: center'>还需投资 " + devTime + " 次</td>" +
                "<td style='text-align: center' id='valueQualificationDev" + i + "'>" + unitInvest + "</td>" +
                "</tr>";
        }
    }
    document.getElementById("tbodyQualificationDev").innerHTML = txt9;   //输出可研发的资质认证信息
    document.getElementById("tbodyQualificationDevInfo").innerHTML = txt32;



}

/**
 * 页面控制器
 * @param rule
 * @param runningState
 * 1.   变量命名规则：(驼峰规则)
 * 1.1  仅渲染表格(如input): frame[区块名] (如 广告投放表格为: frameAdvertising)
 * 1.2  仅注入内容(如现金数额): dataContent[内容名] (如 现金数额: dataContenCash)
 * 1.3  同时渲染表格和注入内容(如库存情况): block[区块名] (如 原料库存表: blockMaterialStock)
 * 1.4  *建议固定id的DOM不要在JS中渲染
 *
 * 2.   每个渲染块前必须有注释说明该块用途(如 //渲染厂房设备信息)
 * 3.   建议增加渲染异常检测
 * 4.   我很后悔开始没用MVVM框架(如Vue.js)来写前端部分
 * 5.   第一层循环用 i, 第二层用 j, 第三层用 k
 */
function pageController(runningState) {

    //广告投放表格渲染
    var frameAdvertising = '';
    frameAdvertising += "<table class='table table-bordered'>" +
        "<thead>" +
        "<tr>" +
        "<th></th>";
    // var ruleMarket = rule.ruleMarket;
    var areaQuantity = rule.areaQuantity;
    for (var i = 0; i < areaQuantity; i++){
        frameAdvertising += "<th style='text-align: center'>" + getAreaInfo(i + 1).name + "</th>";
    }
    frameAdvertising += "</tr>" +
        "</thead>" +
        "<tbody>";
    var productQuantity = rule.productQuantity;
    for (var i = 0; i < productQuantity; i++){
        frameAdvertising += "<tr>" +
            "<th style='text-align: center'>" + getProductInfo(i + 1).name + "</th>";
        for(var j = 1; j < areaQuantity + 1; j++){
            var strId = 'adValueProduct' + ( i + 1) + 'Area' + j;
            frameAdvertising += "<td><input type='number' min='0' style='width:80px' class='form-control' id='" + strId + "'/></td>";
        }
        frameAdvertising += "</tr>";
    }

    frameAdvertising += "</tbody>" +
        "</table>";
    $('#advertisingForm').html(frameAdvertising);

    txt2 = "<table class='table table-bordered'>" +
        "<thead>" +
        "<tr>" +
        "<th>长贷</th>";
    var ruleParam = rule.ruleParam;
    for(var i = 1; i < ruleParam.paramLongTermLoanTimeLimit+1; i++){
        txt2 += "<th>" + i + "</th>";
    }
    txt2 += "</tr>" +
        "</thead>" +
        "<tbody>" +
        "<tr>" +
        "<th>金额</th>";
    for(var i = 1; i < ruleParam.paramLongTermLoanTimeLimit+1; i++){
        txt2 += "<td id='valueLongDebt" + i + "'></td>";
    }
    txt2 += "</tr>" +
        "</tbody>" +
        "</table>";
    document.getElementById("divLongDebtInfo").innerHTML = txt2;

    txt3 = "<table class='table table-bordered'>" +
        "<thead>" +
        "<tr>" +
        "<th>原料</th>";
    var ruleMaterial = rule.ruleMaterial;
    var materialNum = 0;
    for(var i = 1; i < 6; i++){
        if(eval("ruleMaterial.material" + i + "Name !== null")){
            txt3 += "<th>" + eval("ruleMaterial.material" + i + "Name") + "</th>";
            materialNum++;
        }
    }
    txt3 += "</tr>" +
        "</thead>" +
        "<tbody>" +
        "<tr>" +
        "<th>数量</th>";
    for(var i = 1; i < materialNum+1; i++){
        txt3 += "<td id='valueMaterialStock" + i + "'></td>";
    }
    txt3 += "</tr>" +
        "</tbody>" +
        "</table>";
    document.getElementById("divMaterialStock").innerHTML = txt3;

    txt4 = "<table class='table table-bordered'>" +
        "<thead>" +
        "<tr>" +
        "<th>产品</th>";
    var productNum = 0;
    for(var i = 1; i < 6; i++){
        if(eval("ruleProduct.product" + i + "Name !== null")){
            txt4 += "<th>" + eval("ruleProduct.product" + i + "Name") + "</th>";
            productNum++;
        }
    }
    txt4 += "</tr>" +
        "</thead>" +
        "<tbody>" +
        "<tr>" +
        "<th>数量</th>";
    for(var i = 1; i < productNum+1; i++){
        txt4 += "<td id='valueProductStock" + i + "'></td>";
    }
    txt4 += "</tr>" +
        "</tbody>" +
        "</table>";
    document.getElementById("divProductStock").innerHTML = txt4;

    var maxMaterialTime = 0;
    for(var i = 1; i < 6; i++){
        if(eval("ruleMaterial.material" + i + "Time > maxMaterialTime")){
            maxMaterialTime = eval("ruleMaterial.material" + i + "Time");
        }
    }
    var txt5 = "<table class='table table-bordered'>" +
        "<thead>" +
        "<tr>" +
        "<th>采购单</th>";
//        var ruleMaterial = rule.ruleMaterial;
//        var materialNum = 0;
    for(var i = 1; i < materialNum + 1; i++){
        txt5 += "<th>" + eval("ruleMaterial.material" + i + "Name") + "</th>";
    }
    txt5 += "</tr>" +
        "</thead>" +
        "<tbody>";
    for(var i = 1; i < maxMaterialTime + 1; i++){
        txt5 += "<tr>" +
            "<th>到货期" + i + "</th>";
        for(var j = 1; j < materialNum + 1; j++){
            txt5 += "<td id='valueMaterialStock" + j + "T" + i + "'></td>";
        }
        txt5 += "</tr>";
    }
    txt5 += "</tbody>" +
        "</table>";
    document.getElementById("divMaterialPurchase").innerHTML = txt5;

    txt6 = "<select name='longDebtTime'>";
    for(var i = 1; i < ruleParam.paramLongTermLoanTimeLimit+1; i++){
        txt6 += "<option value=" + i + ">还款期 " + i + " 年</option>";
    }
    txt6 += "</select>" +
        "<br><br>" +
        "<input type='number' min='0' placeholder='贷款额' style='width:100px' class='form-control' name='longDebtAmount' >";
    document.getElementById("longDebtForm").innerHTML = txt6;

    var txt7 = "";
    var txt12 = "";
    for(var i = 1; i <= materialNum; i++){
        eval("var strName = ruleMaterial.material" + i + "Name");
        eval("var materialEmPrice = ruleMaterial.material" + i + "Price * ruleParam.paramMaterailBuyRation");
        txt7 += "<label>" + strName + " ： </label><input type='number' min='0' placeholder='采购量' style='width:100px' id='material" + i + "AddNum' name=='material" + i + "AddNum'><br>";
        txt12 += "<label>" + strName + " ： </label>" +
            "<input type='number' min='0' style='width:100px' id='material" + i + "EmergencyAddNum' name=='material" + i + "EmergencyAddNum'>" +
            "<input type='number' id='material" + i + "EmergencyPrice' name=='material" + i + "EmergencyPrice' value='" + materialEmPrice + "' style='display: none'>" +
            "<br>";

    }
    var txt13 = "";
    for(var i = 1; i < productNum + 1; i++){
        eval("var strName = ruleProduct.product" + i + "Name");
        eval("var productEmPrice = ruleProduct.product" + i + "FinalCost * ruleParam.paramProductBuyRation");
        txt13 += "<label>" + strName + " ： </label>" +
            "<input type='number' min='0' style='width:100px' id='product" + i + "EmergencyAddNum' name=='product" + i + "EmergencyAddNum'>" +
            "<input type='number' id='product" + i + "EmergencyPrice' name=='product" + i + "EmergencyPrice' value='" + productEmPrice + "' style='display: none'>" +
            "<br>";
    }
    document.getElementById("addPurchaseForm").innerHTML = txt7;
    document.getElementById("emergencyPurchaseForm1").innerHTML = txt12;
    document.getElementById("emergencyPurchaseForm2").innerHTML = txt13;

    txt8 = "<select name='factoryType'>";
    var ruleFactory = rule.ruleFactory;
    var factoryNum = 0;
    for(var i = 1; i < 6; i++){
        if(eval("ruleFactory.factory" + i + "Name") != ""){
            factoryNum++;
        }
    }
    for(var i = 1; i < factoryNum+1; i++){
        eval("var factoryName = ruleFactory.factory" + i + "Name");
        eval("var factoryBuyPrice = ruleFactory.factory" + i + "BuyPrice");
        eval("var factoryRentPrice = ruleFactory.factory" + i + "RentPrice");
        txt8 += "<option value=" + i + ">" + factoryName + " 购买：" + factoryBuyPrice + "W 租赁：" + factoryRentPrice + "W/年 "+ "</option>";
    }
    txt8 += "</select>" +
        "<br><br>" +
        "<select name='factoryOwning'>" +
        "<option value='1'>购买</option>" +
        "<option value='0'>租赁</option>" +
        "</select>";
    document.getElementById("newFactoryForm").innerHTML = txt8;

    var ruleLine = rule.ruleLine;
    var ruleLineNum = 0;
    var txt9 = "<label>生产线类型 ： </label><select name='lineType'>";
    for(var i = 1; i < 6; i++){
        var lineName = eval("ruleLine.line" + i + "Name");
        if(lineName != ""){
            var linePrice = eval("ruleLine.line" + i + "UnitInvest * ruleLine.line" + i + "InstallTime");
            ruleLineNum++;
            txt9 += "<option value=" + i + ">" + lineName + "</option>"
        }
    }
    txt9 += "</select><br><br>" +
        "<label>所属厂房 ： </label><select name='forFactory'>";
    var factoryStateList = runningState.factoryStateList;
    $.each(factoryStateList,function(n, factoryState) {
        var lineStateList = factoryState.lineStateList;
        var content = factoryState.content;
        var volume = 0;
        var factoryName = "";
        for(var i = 1; i < 6; i++){
            if(factoryState.type == i){
                volume = eval("ruleFactory.factory" + i + "Volume");
                factoryName = eval("ruleFactory.factory" + i + "Name");
            }
        }
        if(content < volume){
            txt9 += "<option value=" + factoryState.id + ">ID: " + factoryState.id + " " + factoryName + "</option>";
        }
    });
    txt9 += "</select><br><br>" +
        "<label>生产产品类型 ： </label><select name='productType'>";
    for(var i = 1; i < productNum+1; i++){
        txt9 += "<option value=" + i + ">" + eval("ruleProduct.product" + i + "Name") + "</option>";
    }
    txt9 += "</select>";

    document.getElementById("newLineForm").innerHTML = txt9;

    var txt10 = "";
    $.each(factoryStateList,function(n, factoryState) {
        var lineStateList = factoryState.lineStateList;
        var content = factoryState.content;
        var volume = 0;
        var factoryName = "";
        var factoryOwning = factoryState.owningState;
        var factoryId = factoryState.id;
        var factoryValue = factoryState.value;
        var factoryTimeQuarter = factoryState.finanPaymentQuarter;
        var factoryTimeYear = factoryState.finalPaymentYear;
        var factoryRentPrice = 0;
        for(var i = 1; i < 6; i++){
            if(factoryState.type == i){
                eval("volume = ruleFactory.factory" + i + "Volume");
                eval("factoryRentPrice = ruleFactory.factory" + i + "RentPrice");
                factoryName = eval("ruleFactory.factory" + i + "Name");
            }
        }
        txt10 += "<section class='panel'>" +
            "<header class='panel-heading no-border'>" +
            "<h3>" + factoryName + " &nbsp;&nbsp;ID: " + factoryId +"</h3>" +
            "<span class='tools pull-right'>" +
            "<a href='javascript:;' class='fa fa-chevron-down'></a>" +
            "</span>" +
            "</header>" +
            "<div class='panel-body'>" +
            "<table class='table table-bordered'>" +
            "<thead>" +
            "<tr>";
        if(factoryOwning == 0){
            txt10 += "<th>厂房状态：租赁中</th>" +
                "<th>厂房租金： " + factoryRentPrice + " W/年</th>" +
                "<th>付租时间： 第 " + factoryTimeYear + " 年 " + factoryTimeQuarter + " 季</th>";
        }else{
            txt10 += "<th>厂房状态：已购买</th>" +
                "<th>厂房价值： " + factoryValue + " W</th>" +
                "<th>购买时间： 第 " + factoryTimeYear + " 年 " + factoryTimeQuarter + " 季</th>";
        }
        txt10 +="<th>厂房容量： " + volume + "</th>" +
            "<th>现有生产线： " + content + " 条</th>" +
            "</tr>" +
            "</thead>" +
            "</table>";
        txt10 += "<br>" +
            "<table class='table table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th>生产线ID</th>" +
            "<th>生产线类型</th>" +
            "<th>产品类型</th>" +
            "<th>生产状态</th>" +
            "<th>拥有状态</th>" +
            "<th>价值</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>";
        $.each(lineStateList,function(n, lineState){
            var lineName = "";
            var productName = "";

            for(var i = 1; i < 6; i++){
                if(lineState.type == i){
                    lineName = eval("ruleLine.line" + i + "Name");
                }
                if(lineState.productType == i){
                    productName = eval("ruleProduct.product" + i + "Name");
                }
            }
            txt10 +="<tr>" +
                "<td>" + lineState.id + "</td>" +
                "<td>" + lineName + "</td>" +
                "<td>" + productName + "</td>";
            if(lineState.owningState > 0){
                if(lineState.produceState < 0){
                    txt10 += "<td>还需 " + (-lineState.produceState) + " 季转产完成</td>";
                }else if(lineState.produceState == 0){
                    txt10 += "<td>空闲</td>";
                }else if (lineState.produceState == null){
                    txt10 += "<td>空闲</td>";
                }else{
                    txt10 += "<td>还需 " + lineState.produceState + " 季生产完成</td>";
                }
            }else {
                txt10 += "<td>暂未建成</td>";
            }

            if(lineState.owningState < 0){
                txt10 += "<td>还需 " + (-lineState.owningState) + " 次投资</td>";
            }else if(lineState.owningState == 0){
                txt10 += "<td>下个季度即可使用</td>";
            }else{
                txt10 += "<td>建成第 " + lineState.owningState + " 年</td>";
            }
            txt10 += "<td>" + lineState.value + " W</td>" +
                "</tr>";
        });
        txt10 += "</tbody>" +
            "</table>" +
            "</div>" +
            "</section>";
    });

    document.getElementById("divFactoryArea").innerHTML = txt10;

    var txt11 = "<label>转产产品 ： </label><select name='changeType'>";
    for(var i = 1; i < productNum+1; i++){
        txt11 += "<option value=" + i + ">" + eval("ruleProduct.product" + i + "Name") + "</option>";
    }
    txt11 += "</select>";
    document.getElementById("divChangeLineProductSelect").innerHTML = txt11;   //输出可转产的产品信息

    var txt14 = "<ul class='nav nav-tabs'>";
    for(var i = 1; i < marketNum + 1; i++){
        var marketName = eval("rule.ruleMarket.market" + i + "Name");
        txt14 += "<li><a href='#tab" + i + "' data-toggle='tab'>" + marketName + "</a></li>";
    }
    txt14 += "</ul>" +
        "<div id='myTabContent' class='tab-content'>";
    for(var i = 1; i < marketNum + 1; i++){
        var marketName = eval("rule.ruleMarket.market" + i + "Name");
        txt14 += "<div class='tab-pane fade' id='tab" + i +"'>" +
            "<div id='divAreaState" + i +"'></div>" +
            "<div id='divOrderInfo" + i + "'></div>" +
            "</div>";
    }
    txt14 += "</div>";
    document.getElementById("divOrderMeeting").innerHTML = txt14;   //


}

function btnController(obj) {
    var txt="";
    if(obj.baseState.state == 0){
        console.log("用户尚未开始运营");
        document.getElementById("ajaxDiv1").innerHTML = getTime() + " 请点击开始运营";
//            $("#btnInit").show();
        txt += "<button class='btn btn-success btn-lg' type='button' onclick='startRunning()' style='' id='btnInit'>开始运营</button>";
    }else if(obj.baseState.state < 0 ){
        console.log("用户已破产");
        document.getElementById("ajaxDiv1").innerHTML = getTime() + " 该用户已经破产";
    }else{

        if(obj.baseState.timeQuarter == 0){
            if(obj.baseState.operateState.report == 0){
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalReport' type='button' style='' id='btnReport'>填写报表</button> ";
            }
            if(obj.baseState.operateState.ad == 0){
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalAdvertising' type='button' style='' id='btnAdvertising'>投放广告</button> ";
            }
            if(obj.baseState.operateState.orderMeeting == 0){
                txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalOrderMeeting' type='button' style='' id='btnOrderMeeting'>订货会</button> ";
            }
            if(obj.baseState.operateState.bidMeeting == 0){
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalBidMeeting' type='button' style='' id='btnBidMeeting'>竞单会</button> ";
            }
            if(obj.baseState.operateState.longLoan == 0){
                txt += "<button class='btn btn-danger btn-lg' data-toggle='modal' href='#modalApplyLongDebt' type='button' id='ApplyLongLoan'>申请长贷</button> ";
            }
            txt += "<button class='btn btn-success btn-lg' data-toggle='modal' href='#modalStartYear' type='button'  id='btnStartYear'>开始本年</button> ";
        }else if(obj.baseState.timeQuarter < 4){
            if(obj.baseState.state == 10){
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalStartQuarter' type='button'  id='btnStartQuarter'>开始本季</button> ";
            }
            if(obj.baseState.state == 11){
                if(obj.baseState.operateState.shortLoan == 0){
                    txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalApplyShortDebt' type='button' id='btnApplyShortLoan'>申请短贷</button> ";
                }
                txt += "<button class='btn btn-danger btn-lg' data-toggle='modal' href='#modalUpdatePurchase' type='button' id='btnUpdatePurchase'>更新原料订单</button>";
            }
            if(obj.baseState.state == 12){
                if(obj.baseState.operateState.addPurchase == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalAddPurchase' type='button' id='btnAddPurchase'>采购原料</button> ";
                }
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalNewFactory' type='button' id='btnAddFactory'>购租厂房</button> ";
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalNewLine' type='button' id='btnAddLine'>新建生产线</button> ";
                if(obj.baseState.operateState.buildLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalBuildLine' type='button' id='btnBuildLine'>在建生产线</button> ";
                }
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalChangeLine' type='button' id='btnChangeLine'>生产线转产</button> ";
                if(obj.baseState.operateState.continueChange == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalContinueChange' type='button' id='btnContinueChange'>继续转产</button> ";
                }
                if(obj.baseState.operateState.saleLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalSaleLine' type='button' id='btnSaleLine'>出售生产线</button> ";
                }
                if(obj.baseState.operateState.beginProduction == 0){
                    txt += "<button class='btn btn-success btn-lg' data-toggle='modal' href='#moadlBeginProduction' type='button' id='btnBeginProduction'>开始生产</button> ";
                }
                txt += "<button class='btn btn-danger btn-lg' data-toggle='modal' href='#modalUpdateReceivable' type='button' id='btnUpdateReceivable'>应收款更新</button>";
            }
            if(obj.baseState.state == 13){
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#moadlDelivery' type='button' id='btnDelivery'>按订单交货</button> ";
                if(obj.baseState.operateState.productDev == 0){
                    txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalProductDev' type='button' id='btnProductDev'>产品研发</button> ";
                }
                txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalFactoryTreatment' type='button' id='btnFactoryTreatment'>厂房处理</button> ";
                txt += "<button class='btn btn-success btn-lg' data-toggle='modal' href='#modalEndQuarter' type='button' id='btnEndQuarter'>当季结束</button> ";
            }

        }else if(obj.baseState.timeQuarter == 4){
            if(obj.baseState.state == 10){
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalStartQuarter' type='button'  id='btnStartQuarter'>开始本季</button>";
            }
            if(obj.baseState.state == 11){
                if(obj.baseState.operateState.shortLoan == 0){
                    txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalApplyShortDebt' type='button' id='btnApplyShortLoan'>申请短贷</button> ";
                }
                txt += "<button class='btn btn-danger btn-lg' data-toggle='modal' href='#modalUpdatePurchase' type='button' id='btnUpdatePurchase'>更新原料订单</button>";
            }
            if(obj.baseState.state == 12){
                if(obj.baseState.operateState.addPurchase == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalAddPurchase' type='button' id='btnAddPurchase'>采购原料</button> ";
                }
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalNewFactory' type='button' id='btnAddFactory'>购租厂房</button> ";
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalNewLine' type='button' id='btnAddLine'>新建生产线</button> ";
                if(obj.baseState.operateState.buildLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalBuildLine' type='button' id='btnBuildLine'>在建生产线</button> ";
                }
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalChangeLine' type='button' id='btnChangeLine'>生产线转产</button> ";
                if(obj.baseState.operateState.continueChange == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalContinueChange' type='button' id='btnContinueChange'>继续转产</button> ";
                }
                if(obj.baseState.operateState.saleLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalSaleLine' type='button' id='btnSaleLine'>出售生产线</button> ";
                }
                if(obj.baseState.operateState.beginProduction == 0){
                    txt += "<button class='btn btn-success btn-lg' data-toggle='modal' href='#moadlBeginProduction' type='button' id='btnBeginProduction'>开始生产</button> ";
                }
                txt += "<button class='btn btn-danger btn-lg' data-toggle='modal' href='#modalUpdateReceivable' type='button' id='btnUpdateReceivable'>应收款更新</button>";
            }
            if(obj.baseState.state == 13){
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#moadlDelivery' type='button' id='btnDelivery'>按订单交货</button> ";
                if(obj.baseState.operateState.productDev == 0){
                    txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalProductDev' type='button' id='btnProductDev'>产品研发</button> ";
                }
                if(obj.baseState.operateState.qualificationDev == 0){
                    txt += "<button class='btn btn-success btn-lg' data-toggle='modal' href='#modalQualificationDev' type='button' id='btnQualificationDev'>资质研发</button> ";
                }
                if(obj.baseState.operateState.marketDev == 0){
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalMarketDev' type='button' id='btnMarketDev'>市场开拓</button> ";
                }
                txt += "<button class='btn btn-danger btn-lg' data-toggle='modal' href='#modalFactoryTreatment' type='button' id='btnFactoryTreatment'>厂房处理</button> ";

                txt += "<button class='btn btn-success btn-lg' data-toggle='modal' href='#modalEndYear' type='button' id='btnEndYear'>当年结束</button>";
            }
        }
    }
    document.getElementById("divTimeAxis").innerHTML = txt;
}

function updateReportDownload() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/getFileInfo/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (list) {
            $.each(list,function(n, fileInfo){
                if (fileInfo.type == 'ad'){
                    if (fileInfo.state == 1){
                        var url = 'reportDownload/' + fileInfo.type + '/' + fileInfo.id;
                        var attr = "window.location.href = '" + url + "'";
                        console.log(attr);
                        $('#downloadAdReport').attr("onclick", attr);
                    } else {
                        console.log('当前广告报表不允许下载 (测试信息)');
                    }
                } else {
                    console.log('当前暂未支持 ' + fileInfo.type + ' 类型报表下载 (测试信息)');
                }
                /**
                 * 在这些补充其他报表的下载URL生成
                 */
            });
        }
    });
}

function reportCalculation() {

    var total1 = 0;
    for(var i = 1; i < 11; i++){
        var temp = 0;
        eval("temp = $('#reprot1Subject" + i + "').val()");
        if(temp != ""){
            total1 += parseInt(temp);
        }
    }
    document.getElementById("reprot1Total").innerHTML = total1;

    var r2s1 = eval("$('#reprot2Subject1').val()");
    var r2s2 = eval("$('#reprot2Subject2').val()");
    if(r2s1 == ""){
        r2s1 = 0;
    }
    if(r2s2 == ""){
        r2s2 = 0;
    }
    var r2s3 = parseInt(r2s1) - parseInt(r2s2); //利润表 - 毛利
    document.getElementById("reprot2Subject3").innerHTML = r2s3;
    var r2s4 = eval("$('#reprot2Subject4').val()");
    if(r2s4 == ""){
        r2s4 = 0;
    }
    var r2s5 = parseInt(r2s3) - parseInt(r2s4); //利润表 - 折旧前利润
    document.getElementById("reprot2Subject5").innerHTML = r2s5;
    var r2s6 = eval("$('#reprot2Subject6').val()");
    if(r2s6 == ""){
        r2s6 = 0;
    }
    var r2s7 = parseInt(r2s5) - parseInt(r2s6); //利润表 - 支付利息前利润
    document.getElementById("reprot2Subject7").innerHTML = r2s7;
    var r2s8 = eval("$('#reprot2Subject8').val()");
    if(r2s8 == ""){
        r2s8 = 0;
    }
    var r2s9 = parseInt(r2s7) - parseInt(r2s8); //利润表 - 税前利润
    document.getElementById("reprot2Subject9").innerHTML = r2s9;
    var r2s10 = eval("$('#reprot2Subject10').val()");
    if(r2s10 == ""){
        r2s10 = 0;
    }
    var r2s11 = parseInt(r2s9) - parseInt(r2s10); //利润表 - 年度净利润
    document.getElementById("reprot2Subject11").innerHTML = r2s11;

    var total2 = 0; //流动资产合计
    for (var i = 1; i < 6; i++){
        var temp = 0;
        eval("temp = $('#reprot3Subject" + i + "').val()");
        if(temp != ""){
            total2 += parseInt(temp);
        }
    }
    document.getElementById("reprot3Subject6").innerHTML = total2;
    var total3 = 0; //固定资产合计
    for (var i = 7; i < 10; i++){
        var temp = 0;
        eval("temp = $('#reprot3Subject" + i + "').val()");
        if(temp != ""){
            total3 += parseInt(temp);
        }
    }
    document.getElementById("reprot3Subject10").innerHTML = total3;
    document.getElementById("reprot3Subject11").innerHTML = total2 + total3;
    var total4 = 0; //负债合计
    for (var i = 12; i < 16; i++){
        var temp = 0;
        eval("temp = $('#reprot3Subject" + i + "').val()");
        if(temp != ""){
            total4 += parseInt(temp);
        }
    }
    document.getElementById("reprot3Subject16").innerHTML = total4;
    var total5 = 0; //所有者权益合计
    for (var i = 17; i < 20; i++){
        var temp = 0;
        eval("temp = $('#reprot3Subject" + i + "').val()");
        if(temp != ""){
            total5 += parseInt(temp);
        }
    }
    document.getElementById("reprot3Subject20").innerHTML = total5;
    document.getElementById("reprot3Subject21").innerHTML = total4 + total5;
}

function operateReport() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/getSubRunningState/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (runningState) {
            var financialStatementList = runningState.financeState.financialStatementList;
            $.each(financialStatementList,function(n,financialStatement) {
                var tempYear = financialStatement.year;
                if(tempYear == runningState.baseState.timeYear - 1){
                    //找到上一年的报表
                    var check = 1;  //报表正常标记
                    //获取填写的综合费用表数值
                    var r1s1 = $('#reprot1Subject1').val();
                    var r1s2 = $('#reprot1Subject2').val();
                    var r1s3 = $('#reprot1Subject3').val();
                    var r1s4 = $('#reprot1Subject4').val();
                    var r1s5 = $('#reprot1Subject5').val();
                    var r1s6 = $('#reprot1Subject6').val();
                    var r1s7 = $('#reprot1Subject7').val();
                    var r1s8 = $('#reprot1Subject8').val();
                    var r1s9 = $('#reprot1Subject9').val();
                    var r1s10 = $('#reprot1Subject10').val();
                    var r1s11 = parseInt($("#reprot1Subject11").html());
                    for (var i = 1; i < 11; i++){
                        if(eval("r1s" + i + "== ''")){
                            eval("r1s" + i + "= 0");
                        }
                    }
                    //开始检查综合费用表各科目
                    if(r1s1 != financialStatement.managementCost){
                        check = 2;
                        console.log("测试 管理费有误");
                    }
                    if(r1s2 != financialStatement.advertisingCost){
                        check = 2;
                        console.log("测试 2费有误");
                    }
                    if(r1s3 != financialStatement.upkeepCost){
                        check = 2;
                        console.log("测试 3费有误");
                    }
                    if(r1s4 != financialStatement.lostCost){
                        check = 2;
                        console.log("测试 4费有误");
                    }
                    if(r1s5 != financialStatement.transferCost){
                        check = 2;
                        console.log("测试 5费有误");
                    }
                    if(r1s6 != financialStatement.factoryRent){
                        check = 2;
                        console.log("测试 6费有误");
                    }
                    if(r1s7 != financialStatement.marketDevCost){
                        check = 2;
                        console.log("测试 7费有误");
                    }
                    if(r1s8 != financialStatement.productDevCost){
                        check = 2;
                        console.log("测试 8费有误");
                    }
                    if(r1s9 != financialStatement.isoDevCost){
                        check = 2;
                        console.log("测试 9费有误");
                    }
                    if(r1s10 != financialStatement.infomationCost){
                        check = 2;
                        console.log("测试 10费有误");
                    }
                    //结束检查综合费用表各科目
                    if (check == 1){
                        console.log("测试 综合费用表检查全部正确");
                        //获取填写的利润表数值
                        var r2s1 = $('#reprot2Subject1').val();
                        var r2s2 = $('#reprot2Subject2').val();
                        var r2s4 = $('#reprot2Subject4').val();
                        var r2s6 = $('#reprot2Subject6').val();
                        var r2s8 = $('#reprot2Subject8').val();
                        var r2s10 = $('#reprot2Subject10').val();
                        if(r2s1 == ""){
                            r2s1 = 0;
                        }
                        if(r2s2 == ""){
                            r2s2 = 0;
                        }
                        if(r2s4 == ""){
                            r2s4 = 0;
                        }
                        if(r2s6 == ""){
                            r2s6 = 0;
                        }
                        if(r2s8 == ""){
                            r2s8 = 0;
                        }
                        if(r2s10 == ""){
                            r2s10 = 0;
                        }
                        //开始检查利润表
                        if(r2s1 != financialStatement.salesIncome){
                            check = 2;
                            console.log("测试 利润表 1 有误");
                        }
                        if(r2s2 != financialStatement.directCost){
                            check = 2;
                            console.log("测试 利润表 2 有误");
                        }
                        if(r2s4 != financialStatement.omnibusCost){
                            check = 2;
                            console.log("测试 利润表 4 有误");
                        }
                        if(r2s6 != financialStatement.depreciation){
                            check = 2;
                            console.log("测试 利润表 6 有误");
                        }
                        if(r2s8 != financialStatement.financialCost){
                            check = 2;
                            console.log("测试 利润表 8 有误");
                        }
                        if(r2s10 != financialStatement.incomeTax){
                            check = 2;
                            console.log("测试 利润表 10 有误");
                        }
                        //结束检查利润表
                        if(check == 1){
                            console.log("测试 利润表检查全部正确");
                            //获取填写的资产负债表 - 流动资产 各科目
                            var r3s1 = $('#reprot3Subject1').val();
                            var r3s2 = $('#reprot3Subject2').val();
                            var r3s3 = $('#reprot3Subject3').val();
                            var r3s4 = $('#reprot3Subject4').val();
                            var r3s5 = $('#reprot3Subject5').val();
                            for (var i = 1; i < 6; i++){
                                if(eval("r3s" + i + "== ''")){
                                    eval("r3s" + i + "= 0");
                                }
                            }
                            //获取填写的资产负债表 - 固定资产 各科目
                            var r3s7 = $('#reprot3Subject7').val();
                            var r3s8 = $('#reprot3Subject8').val();
                            var r3s9 = $('#reprot3Subject9').val();
                            for (var i = 7; i < 10; i++){
                                if(eval("r3s" + i + "== ''")){
                                    eval("r3s" + i + "= 0");
                                }
                            }
                            //获取填写的资产负债表 - 负债 各科目
                            var r3s12 = $('#reprot3Subject12').val();
                            var r3s13 = $('#reprot3Subject13').val();
                            var r3s14 = $('#reprot3Subject14').val();
                            var r3s15 = $('#reprot3Subject15').val();
                            for (var i = 12; i < 16; i++){
                                if(eval("r3s" + i + "== ''")){
                                    eval("r3s" + i + "= 0");
                                }
                            }
                            //获取填写的资产负债表 - 所有者权益 各科目
                            var r3s17 = $('#reprot3Subject17').val();
                            var r3s18 = $('#reprot3Subject18').val();
                            var r3s19 = $('#reprot3Subject19').val();
                            for (var i = 17; i < 20; i++){
                                if(eval("r3s" + i + "== ''")){
                                    eval("r3s" + i + "= 0");
                                }
                            }

                            //开始检查资产负债表
                            if(r3s1 != financialStatement.cashAmount){
                                check = 2;
                            }
                            if(r3s2 != financialStatement.receivableTotal){
                                check = 2;
                            }
                            if(r3s3 != financialStatement.wipValue){
                                check = 2;
                            }
                            if(r3s4 != financialStatement.finishedProductValue){
                                check = 2;
                            }
                            if(r3s5 != financialStatement.materialVaule){
                                check = 2;
                            }
                            if(r3s7 != financialStatement.factoryValue){
                                check = 2;
                            }
                            if(r3s8 != financialStatement.equipmentValue){
                                check = 2;
                            }
                            if(r3s9 != financialStatement.constructionInProgressValue){
                                check = 2;
                            }
                            if(r3s12 != financialStatement.longTermDebt){
                                check = 2;
                            }
                            if(r3s13 != financialStatement.shortTermDebt){
                                check = 2;
                            }
                            if(r3s14 != financialStatement.duesTotal){
                                check = 2;
                            }
                            if(r3s15 != financialStatement.incomeTax){
                                check = 2;
                            }
                            if(r3s17 != financialStatement.equityCapital){
                                check = 2;
                            }
                            if(r3s18 != financialStatement.profitRetention){
                                check = 2;
                            }
                            if(r3s19 != financialStatement.netProfit){
                                check = 2;
                            }
                        }

                    }

                    //发送检查结果
                    $.ajax({
                        type:"POST",
                        url:"/operateReport/" + nowUserName + "/" + check,
                        cache:false,
                        dataType:"json",
                        success:function (runningState) {
                            subOnLoad();
                            if(check == 1){
                                alert("报表填写正确！");
                            }else {
                                alert("报表填写有误！");
                            }
                        },
                        error:function (json) {
                            console.log(json.responseText);
                        }
                    });
                }
            });
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function orderMeetingUpdate() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/getTeachClassRule/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (rule) {
            $.ajax({
                type:"POST",
                url:"/getTeachClassInfo/" + nowUserName,
                cache:false,
                dataType:"json",
                success:function (thc) {
                    if(thc.orderMeetingState == 2){
                        window.location.reload();
                    }
                    var subUserInfoList = thc.subUserInfoList;
                    var runningState;
                    $.each(subUserInfoList,function(n, subUserInfo){
                        if (subUserInfo.subUserName == nowUserName){
                            runningState = subUserInfo.runningState;    //提取出runningState
                        }
                    });
                    var collator = thc.collator;    //提出出排序器
                    var userQualification = 0;
                    if(runningState.devState.qualificationDevStateList.length != 0){
                        //计算用户资质
                        var userQualificationState1 = 0;
                        var userQualificationState2 = 0;
                        $.each(runningState.devState.qualificationDevStateList,function(n, qualificationDevState){
                            if (qualificationDevState.type == 1){
                                userQualificationState1 = qualificationDevState.state;
                            }else if(qualificationDevState.type == 2){
                                userQualificationState2 = qualificationDevState.state;
                            }
                        });
                        if(userQualificationState1 > 0 && userQualificationState2 < 1){
                            userQualification = 1;
                        }else if(userQualificationState2 > 0 && userQualificationState1 < 1){
                            userQualification = 2;
                        }else if(userQualificationState1 > 0 && userQualificationState2 > 0){
                            userQualification = 3;
                        }
                    }
                    var marketNum = 0;
                    for(var j = 1; j <= 5; j++){
                        if(eval("rule.ruleMarket.market" + j + "Name") != ""){
                            marketNum++;
                        }
                    }
                    var openAreaList = collator.openAreaList;
                    var areaCollatorList = collator.areaCollatorList;
                    $.each(openAreaList,function(n, openArea){
                        var marketName = eval("rule.ruleMarket.market" + openArea + "Name");
                        var areaCollator = areaCollatorList[openArea - 1];  //打开区域排序器
                        var openProduct = areaCollator.openProduct;
                        var productName = eval("rule.ruleProduct.product" + openProduct + "Name");
                        var productCollatorList = areaCollator.productCollatorList;
                        if(productCollatorList.length != 0){
                            var productCollator = productCollatorList[openProduct - 1];     //打开产品排序器
                            var orderIdList = productCollator.orderIdList;
                            var sortResultList = productCollator.sortResultList;
                            if(orderIdList.length == 0 || sortResultList == 0){
                                //无订单或无人投广告
                            }else {
                                var openUser = productCollator.openUser;
                                if(openUser == 0){
                                    //当前产品排序器开放用户排位是0
                                }else {
                                    var time = productCollator.time;
                                    var nowTime = new Date();
                                    var timeResult = parseInt((time - nowTime.getTime()) / 1000);
                                    var username = sortResultList[openUser - 1].username;
                                    var txtOrderInfo = "<table class='table table-bordered'>" +
                                        "<thead>" +
                                        "<tr>" +
                                        "<th style='text-align: center'>订单ID</th>" +
                                        "<th style='text-align: center'>数量</th>" +
                                        "<th style='text-align: center'>总价</th>" +
                                        "<th style='text-align: center'>单价</th>" +
                                        "<th style='text-align: center'>交货期</th>" +
                                        "<th style='text-align: center'>账期</th>" +
                                        "<th style='text-align: center'>资质认证</th>" +
                                        "<th style='text-align: center'>操作</th>" +
                                        "</tr>" +
                                        "</thead>" +
                                        "<tbody>";
                                    var orderList;
                                    $.ajax({
                                        type:"POST",
                                        url:"/getOrderList/" + nowUserName + "/" + openArea + "/" + openProduct,
                                        cache:false,
                                        async:false,
                                        dataType:"json",
                                        success:function (list) {
                                            orderList = list;
                                        }
                                    });
                                    $.each(orderIdList,function(n, orderId){
                                        $.each(orderList,function(n, order){
                                            if(order.marketOrderId == orderId){
                                                var unitPriceP = order.orderTotalPrice / order.orderQuantity;
                                                var unitPrice = unitPriceP.toFixed(2);
                                                txtOrderInfo += "<tr>" +
                                                    "<td style='text-align: center'>" + order.marketOrderId + "</td>" +
                                                    "<td style='text-align: center'>" + order.orderQuantity + "</td>" +
                                                    "<td style='text-align: center'>" + order.orderTotalPrice + "</td>" +
                                                    "<td style='text-align: center'>" + unitPrice + "</td>" +
                                                    "<td style='text-align: center'>" + order.orderDeliveryTime + "</td>" +
                                                    "<td style='text-align: center'>" + order.orderAccountPeriod + "</td>" +
                                                    "<td style='text-align: center'>" + order.orderQualificate + "</td>";
                                                var check = 0;
                                                if(username == nowUserName){
                                                    if(order.orderQualificate == 0 || userQualification == 3){
                                                        check = 1;
                                                    }else if(order.orderQualificate == userQualification){
                                                        check = 1;
                                                    }
                                                }
                                                if(check == 1){
                                                    txtOrderInfo += "<td style='text-align: center'><button class='btn btn-info btn-sm' onclick='operateGetOrder(" + openArea + "," + openProduct + "," + order.marketOrderId + ")'>选单</button></td>";
                                                }else{
                                                    txtOrderInfo += "<td style='text-align: center'> -- </td>";
                                                }
                                                txtOrderInfo += "</tr>";
                                            }
                                        });
                                    });
                                    txtOrderInfo += "</tbody></table>";
                                    document.getElementById("divOrderInfo" + openArea).innerHTML = txtOrderInfo;
                                }
                            }
                        }else{
                            //该区域无订单
                        }
                        if(username == nowUserName){
                            var txtAreaState = "<h3 style='text-align: center;color: red'>" + marketName + " " + productName + " 正在选单 选单者：" + username + " 剩余时间：" + timeResult + " 秒</h3>";
                        }else {
                            var txtAreaState = "<h3 style='text-align: center;color: blue'>" + marketName + " " + productName + " 正在选单 选单者：" + username + " 剩余时间：" + timeResult + " 秒</h3>";
                        }
                        document.getElementById("divAreaState" + openArea).innerHTML = txtAreaState;
                    });
                    for(var j = 1; j < areaCollatorList.length + 1; j++){
                        $.each(areaCollatorList,function(n, areaCollator){
                            if(areaCollator.state == 0){
                                var marketType = areaCollator.type;
                                var marketName = eval("rule.ruleMarket.market" + marketType + "Name");
                                var txtAreaState = "<h3>" + marketName + " 还未选单</h3>";
                                document.getElementById("divAreaState" + marketType).innerHTML = txtAreaState;
                            }
                            if(areaCollator.state == 2){
                                var marketType = areaCollator.type;
                                var marketName = eval("rule.ruleMarket.market" + marketType + "Name");
                                var txtAreaState = "<h3>" + marketName + " 已结束选单</h3>";
                                document.getElementById("divAreaState" + marketType).innerHTML = txtAreaState;
                            }
                        });
                    }
                    for(var j = collator.areaQuantity + 1; j < marketNum + 1; j++){
                        var marketName = eval("rule.ruleMarket.market" + j + "Name");
                        var txtAreaState = "<h3>" + marketName + " 本年无订单</h3>";
                        document.getElementById("divAreaState" + j).innerHTML = txtAreaState;
                    }

                }
            });
        }
    });
}

function operateGetOrder(area, product, id) {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/getOrder/" + nowUserName + "/" + area + "/" + product + "/" + id,
        cache:false,
        dataType:"json",
        success:function (runningState) {
            alert("获取订单成功");
        }
    });
}

function operateOrderMeeting() {
    window.setInterval(orderMeetingUpdate, 1000);
}

function oprateAdvertising(form) {
    var nowUserName = $("#nowUserName").val();
    for(var i =1; i < 26; i++){
        eval("var ad =form.ad"+ i + ".value;");

        if(ad < 0){
            alert("广告额不能为负数");
            return false;
        }
        if(ad == ""){
            eval("form.ad"+ i + ".value = 0;");
        }
    }
    $.ajax({
        type:"POST",
        url:"/operateAdvertising/" + nowUserName,
        cache:false,
        dataType:"json",
        data:$('#advertisingForm').serialize(),
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalAdvertising").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function operateApplyLongDebt(form) {
    var nowUserName = $("#nowUserName").val();
    var debtLimit = parseInt($("#valueDebtLimit").html());
    var longDebtAmount = form.longDebtAmount.value;
    var longDebtTime = form.longDebtTime.value;
    if(debtLimit == 0){
        alert("现在已经没有贷款额度了，无法申请贷款");
        return false;
    }
    if(longDebtAmount > debtLimit){
        alert("贷款额度不足，申请失败");
        return false;
    }else{
        var debt = {
            debtType: 2,
            repaymentPeriod: longDebtTime,
            amounts: longDebtAmount
        };
        $.ajax({
            type:"POST",
            url:"/operateApplyDebt/" + nowUserName,
            cache:false,
            dataType:"json",
            data:debt,
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalApplyLongDebt").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateApplyShortDebt() {
    var nowUserName = $("#nowUserName").val();  //从页面读取用户名
    var debtLimit = parseInt($("#valueDebtLimit2").html()); //从页面读取贷款额度
    var shortDebtAmount = $("#shortDebtAmount").val();  //从页面读取贷款申请额
    if(debtLimit == 0){     //判断贷款额度是否为0
        alert("现在已经没有贷款额度了，无法申请贷款");
        return false;
    }
    if(shortDebtAmount > debtLimit){    //判断贷款申请额是否大于贷款额度
        alert("贷款额度不足，申请失败");
        return false;
    }else{
        var debt = {    //贷款对象
            debtType: 1,    //贷款类型,1-短贷 2-长贷
            repaymentPeriod: 4, //还款期
            amounts: shortDebtAmount    //贷款申请额
        };
        $.ajax({
            type:"POST",    //请求类型
            url:"/operateApplyDebt/" + nowUserName, //请求URL
            cache:false,    //是否从缓存中读取
            dataType:"json",    //预期服务器返回的数据类型
            data:debt,  //发送到服务器的数据
            success:function (runningState) {   //请求成功后回调函数
                subOnLoad();    //调用页面载入函数，重新加载页面内容
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg; //输入MSG到页面
                $("#btnCloseModalApplyShortDebt").click();  //关闭贷款申请框
            },
            error:function (json) {     //请求失败后回调函数
                console.log(json.responseText);     //在控制台中打印返回信息
            }
        });
    }
}

function operateUpdatePurchase() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var amounts = parseInt($("#valueUpdatePurchaseAmounts").html());
    if(amounts > cash){
        alert("现金不足");
        return false;
    }else{
        $.ajax({
            type:"POST",
            url:"/operateUpdatePurchase/" + nowUserName,
            cache:false,
            dataType:"json",
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalUpdatePurchase").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateAddPurchase(form) {
    var nowUserName = $("#nowUserName").val();
    var materialNum = form.length;
    var list = new Array();
    for(var i = 1; i <= materialNum; i++){
        eval("var num =form.material" + i + "AddNum.value;");
        if(num <0){
            alert("不能输入负数，请重新输入！");
            return false;
        }
        if(num == ""){
            num = "0";
        }
        list[i-1] = num;
    }
    $.ajax({
        type:"POST",
        url:"/operateAddPurchase/" + nowUserName,
        cache:false,
        dataType:"json",
        data :{array:list},
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalAddPurchase").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });

}

function operateNewFactory(form) {
    var nowUserName = $("#nowUserName").val();
    var factoryType = form.factoryType.value;
    var factoryOwning = form.factoryOwning.value;
    var cash = parseInt($("#valueCash").html());
    if(cash == 0){
        alert("现金不足");
        return false;
    }else{
        var factory = {
            type: factoryType,
            owningState: factoryOwning
        };
        $.ajax({
            type:"POST",
            url:"/operateNewFactory/" + nowUserName,
            cache:false,
            dataType:"json",
            data:factory,
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalNewFactory").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateNewLine(form) {
    var nowUserName = $("#nowUserName").val();
    var lineType = form.lineType.value;
    var productType = form.productType.value;
    var forFactory = form.forFactory.value;
    var cash = parseInt($("#valueCash").html());
    if(cash == 0){
        alert("现金不足");
        return false;
    }else{
        var line = {
            type: lineType,
            productType: productType
        };
        $.ajax({
            type:"POST",
            url:"/operateNewLine/" + nowUserName + "/" + forFactory,
            cache:false,
            dataType:"json",
            data:line,
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalNewLine").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateBuildLine() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('buildLineBox');
    var s = "";
    var list = new Array();
    var amountsTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            amountsTotal += eval("parseInt($('#valueUnitInvest" + box[i].value + "').html())");
        }
    }
    if(cash - amountsTotal < 0){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateBuildLine/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalBuildLine").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }


    // if(amounts > cash){
    //     alert("现金不足");
    //     return false;
    // }else{

    // }

}

function operateChangeLine(form) {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('changeLineBox');
    var changeType = form.changeType.value;
    var s = "";
    var list = new Array();
    var amountsTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            amountsTotal += eval("parseInt($('#valueChangeInvest" + box[i].value + "').html())");
        }
    }
    if(cash - amountsTotal < 0){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateChangeLine/" + nowUserName + "/" + changeType,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalChangeLine").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }


    // if(amounts > cash){
    //     alert("现金不足");
    //     return false;
    // }else{

    // }

}

function operateContinueChange() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('continueChangeBox');
    var s = "";
    var list = new Array();
    var amountsTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            amountsTotal += eval("parseInt($('#valueContinueChangeInvest" + box[i].value + "').html())");
        }
    }
    if(cash - amountsTotal < 0){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateContinueChange/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalChangeLine").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }
}

function operateSaleLine() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('saleLineBox');
    var s = "";
    var list = new Array();
    var scrapValueTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            scrapValueTotal += eval("parseInt($('#valueLineScrapValue" + box[i].value + "').html())");
        }
    }
    if(cash + scrapValueTotal < 0){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateSaleLine/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalSaleLine").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }
}

function operateBeginProduction() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('beginProductionBox');
    var s = "";
    var list = new Array();
    var procCostTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            procCostTotal += eval("parseInt($('#valueProcCost" + box[i].value + "').html())");
        }
    }
    if(cash - procCostTotal < 0){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateBeginProduction/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalBeginProduction").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }
}

function operateUpdateReceivable() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    if(cash >= 0){
        $.ajax({
            type:"POST",
            url:"/operateUpdateReceivable/" + nowUserName,
            cache:false,
            dataType:"json",
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalUpdateReceivable").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }
}

function operateDelivery() {
    var nowUserName = $("#nowUserName").val();
    // var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('deliveryBox');
    var s = "";
    var list = new Array();
    // var procCostTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            // procCostTotal += eval("parseInt($('#valueProcCost" + box[i].value + "').html())");
        }
    }
    $.ajax({
        type:"POST",
        url:"/operateDelivery/" + nowUserName,
        cache:false,
        dataType:"json",
        data :{array:list},
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalDelivery").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function operateProductDev() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('productDevBox');
    var s = "";
    var list = new Array();
    var investTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            investTotal += eval("parseInt($('#valueProductDev" + box[i].value + "').html())");
        }
    }
    if(investTotal > cash){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateProductDev/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalProductDev").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateMarketDev() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('marketDevBox');
    var s = "";
    var list = new Array();
    var investTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            investTotal += eval("parseInt($('#valueMarketDev" + box[i].value + "').html())");
        }
    }
    if(investTotal > cash){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateMarketDev/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalMarketDev").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateQualificationDev() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var box = document.getElementsByName('qualificationDevBox');
    var s = "";
    var list = new Array();
    var investTotal = 0;
    for(var i=0; i<box.length; i++){
        if(box[i].checked){
            s = box[i].value;  //如果选中，将value添加到变量s中
            list[list.length] = s;
            investTotal += eval("parseInt($('#valueeQualificationDev" + box[i].value + "').html())");
        }
    }
    if(investTotal > cash){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateQualificationDev/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalQualificationDev").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateFactoryTreatment() {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var method = $("#changeTreatment").val();
    if(method == 1){
        //出售厂房
        var box = document.getElementsByName('saleFactoryBox');
        var s = "";
        var list = new Array();
        var amountsTotal = 0;
        for(var i=0; i<box.length; i++){
            if(box[i].checked){
                s = box[i].value;  //如果选中，将value添加到变量s中
                list[list.length] = s;
                var content = eval("parseInt($('#valueFactoryContent" + box[i].value + "').html())");
                if(content > 0){
                    amountsTotal += eval("parseInt($('#valueFactoryRentPrice" + box[i].value + "').html())");
                }
            }
        }
        if(cash - amountsTotal < 0){
            alert("现金不足");
            return false;
        }else {
            $.ajax({
                type:"POST",
                url:"/operateSaleFactory/" + nowUserName,
                cache:false,
                dataType:"json",
                data :{array:list},
                success:function (runningState) {
                    subOnLoad();
                    document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                    $("#btnCloseModalFactoryTreatment").click();
                },
                error:function (json) {
                    console.log(json.responseText);
                }
            });
        }


    }
    if(method == 2){
        //厂房退租
        var box = document.getElementsByName('exitRentBox');
        var s = "";
        var list = new Array();
        // var amountsTotal = 0;
        for(var i=0; i<box.length; i++){
            if(box[i].checked){
                s = box[i].value;  //如果选中，将value添加到变量s中
                list[list.length] = s;
                // amountsTotal += eval("parseInt($('#valueFactoryRentPrice" + box[i].value + "').html())");
            }
        }
        $.ajax({
            type:"POST",
            url:"/operateExitRent/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{array:list},
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalFactoryTreatment").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }
    if(method == 3){
        //厂房租转买
        var box = document.getElementsByName('rentToBuyBox');
        var s = "";
        var list = new Array();
        var amountsTotal = 0;
        for(var i=0; i<box.length; i++){
            if(box[i].checked){
                s = box[i].value;  //如果选中，将value添加到变量s中
                list[list.length] = s;
                amountsTotal += eval("parseInt($('#valueFactoryBuyPrice" + box[i].value + "').html())");
            }
        }
        if(cash - amountsTotal < 0){
            alert("现金不足");
            return false;
        }else {
            $.ajax({
                type:"POST",
                url:"/operateRentToBuy/" + nowUserName,
                cache:false,
                dataType:"json",
                data :{array:list},
                success:function (runningState) {
                    subOnLoad();
                    document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                    $("#btnCloseModalFactoryTreatment").click();
                },
                error:function (json) {
                    console.log(json.responseText);
                }
            });
        }


    }

}

function operateEndQuarter() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/operateEndQuarter/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalEndQuarter").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function operateEndYear() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/operateEndYear/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalEndYear").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function operateDiscount(form) {
    var nowUserName = $("#nowUserName").val();
    var list = new Array();
    console.log("收到贴现请求，测试");
    for(var i = 1; i <= 4; i++){
        eval("var num =form.discount" + i + ".value");
        if(num <0){
            alert("不能输入负数，请重新输入！");
            return false;
        }
        if(num == ""){
            num = "0";
        }
        list[i-1] = num;
    }
    console.log("测试节点，下面是LIST对象");
    console.log(list);
    $.ajax({
        type:"POST",
        url:"/operateDiscount/" + nowUserName,
        cache:false,
        dataType:"json",
        data :{array:list},
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalDiscount").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function operateEmergencyPurchase(form1, form2) {
    var nowUserName = $("#nowUserName").val();
    var cash = parseInt($("#valueCash").html());
    var materialNum = form1.length / 2;
    var productNum = form2.length / 2;
    var list1 = new Array();
    var list2 = new Array();
    var totalAmount = 0;
    for(var i = 1; i < materialNum + 1; i++){
        eval("var num =form1.material" + i + "EmergencyAddNum.value;");
        if(num <0){
            alert("不能输入负数，请重新输入！");
            return false;
        }
        eval("totalAmount += form1.material" + i + "EmergencyAddNum.value * form1.material" + i + "EmergencyPrice.value;");
        if(num == ""){
            num = "0";
        }
        list1[i-1] = num;
    }
    for(var i = 1; i < productNum + 1; i++){
        eval("var num =form2.product" + i + "EmergencyAddNum.value");
        if(num <0){
            alert("不能输入负数，请重新输入！");
            return false;
        }
        eval("totalAmount += form2.product" + i + "EmergencyAddNum.value * form2.product" + i + "EmergencyPrice.value");
        if(num == ""){
            num = "0";
        }
        list2[i-1] = num;
    }
    if(totalAmount > cash){
        alert("现金不足");
        return false;
    }else {
        $.ajax({
            type:"POST",
            url:"/operateEmergencyPurchase/" + nowUserName,
            cache:false,
            dataType:"json",
            data :{
                array1:list1,
                array2:list2
            },
            success:function (runningState) {
                subOnLoad();
                document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
                $("#btnCloseModalEmergencyPurchase").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateStartYear() {
    var nowUserName = $("#nowUserName").val();;
    $.ajax({
        type:"POST",
        url:"/operateStartYear/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalStartYear").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function operateStartQuarter() {
    var nowUserName = $("#nowUserName").val();;
    $.ajax({
        type:"POST",
        url:"/operateStartQuarter/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (runningState) {
            subOnLoad();
            document.getElementById("ajaxDiv1").innerHTML = runningState.baseState.msg;
            $("#btnCloseModalStartQuarter").click();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function startRunning() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/subStartRunning/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (obj) {
            console.log("用户已激活");
            document.getElementById("ajaxDiv1").innerHTML = getTime() + " 请开始运营";
            subOnLoad();
            // btnController(obj);
            // infoController(obj);
//                $("#btnInit").hide();
//                $("#btnAdvertising").show();
//                $("#btnStartYear").show();
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });
}

function readMarket(seriesId) {
    var txt1 = "<table class='table table-striped table-hover table-bordered' id='editable-sample'>" +
        "<thead>" +
        "<tr>" +
        "<th>订单ID</th>" +
        "<th>年份</th>" +
        "<th>区域</th>" +
        "<th>产品类型</th>" +
        "<th>数量</th>" +
        "<th>总价</th>" +
        "<th>交货期</th>" +
        "<th>账期</th>" +
        "<th>资质要求</th>" +
        "</tr>" +
        "</thead>" +
        "<tbody>";
    $.ajax({
        type: "GET",
        url: "/findOrderBySeriesId/" + seriesId,
        cache: false,
        async:false,
        dataType: "json",
        success: function (json) {
            for (var i = 0; i < eval(json).length; i++) {

                txt1 += "<tr>" +
                    "<td>" + json[i].marketOrderId + "</td>" +
                    "<td>" + json[i].orderYear + "</td>" +
                    "<td>" + json[i].orderArea + "</td>" +
                    "<td>" + json[i].orderProduct + "</td>" +
                    "<td>" + json[i].orderQuantity + "</td>" +
                    "<td>" + json[i].orderTotalPrice + "</td>" +
                    "<td>" + json[i].orderDeliveryTime + "</td>" +
                    "<td>" + json[i].orderAccountPeriod + "</td>" +
                    "<td>" + json[i].orderQualificate + "</td>" +
                    "</tr>";
            }
            txt1 += "</tbody></table>";

        }
    });
    var txt2 = "<h4 style='text-align: center'>竞 单</h4>" +
        "<table class='table table-striped table-hover table-bordered' id='editable-sample'>" +
        "<thead>" +
        "<tr>" +
        "<th>竞单ID</th>" +
        "<th>年份</th>" +
        "<th>区域</th>" +
        "<th>产品类型</th>" +
        "<th>数量</th>" +
        "<th>资质要求</th>" +
        "</tr>" +
        "</thead>" +
        "<tbody>";
    $.ajax({
        type: "GET",
        url: "/findBidBySeriesId/" + seriesId,
        cache: false,
        async:false,
        dataType: "json",
        success: function (json) {
            for (var i = 0; i < eval(json).length; i++) {
                txt2 += "<tr>" +
                    "<td>" + json[i].marketBidId + "</td>" +
                    "<td>" + json[i].bidYear + "</td>" +
                    "<td>" + json[i].bidArea + "</td>" +
                    "<td>" + json[i].bidProduct + "</td>" +
                    "<td>" + json[i].bidQuantity + "</td>" +
                    "<td>" + json[i].bidQualificate + "</td>" +
                    "</tr>";
            }
            txt2 += "</tbody></table>";
        }
    });

    $.ajax({
        type: "POST",
        url: "/getMarketSeries/" + seriesId,
        cache: false,
        async:false,
        dataType: "json",
        success: function (data) {
            var marketData = data.marketData;
            var timeQuantity = data.timeQuantity;
            var areaQuantity = data.areaQuantity;
            var productQuantity = data.productQuantity;
            var txt3 = "<h4 style='text-align: center'>均 价</h4>" +
                "<table class='table table-striped table-hover table-bordered'>" +
                "<thead>" +
                "<tr>" +
                "<th style='text-align: center'>年份</th>" +
                "<th style='text-align: center'>产品</th>";
            for (var i = 0; i < areaQuantity; i++){
                txt3 += "<th style='text-align: center'>区域" + (i+1) + "</th>"
            }
            txt3 += "</tr>" +
                "</thead>" +
                "<tbody>";
            var txt4 = "<h4 style='text-align: center'>需 求 量</h4>" +
                "<table class='table table-striped table-hover table-bordered'>" +
                "<thead>" +
                "<tr>" +
                "<th style='text-align: center'>年份</th>" +
                "<th style='text-align: center'>产品</th>";
            for (var i = 0; i < areaQuantity; i++){
                txt4 += "<th style='text-align: center'>区域" + (i+1) + "</th>"
            }
            txt4 += "</tr>" +
                "</thead>" +
                "<tbody>";
            var txt5 = "<h4 style='text-align: center'>订 单 数</h4>" +
                "<table class='table table-striped table-hover table-bordered'>" +
                "<thead>" +
                "<tr>" +
                "<th style='text-align: center'>年份</th>" +
                "<th style='text-align: center'>产品</th>";
            for (var i = 0; i < areaQuantity; i++){
                txt5 += "<th style='text-align: center'>区域" + (i+1) + "</th>"
            }
            txt5 += "</tr>" +
                "</thead>" +
                "<tbody>";


            var timeDataList = marketData.timeDataList;
            for (var i = 0; i < timeDataList.length; i++) {
                var productDataList = timeDataList[i].productDataList;
                for (var j = 0; j < productDataList.length; j++) {
                    txt3 += "<tr>" +
                        "<td style='text-align: center'>第" + timeDataList[i].type + "年</td>" +
                        "<td style='text-align: center'>P" + productDataList[j].type + "</td>";    //这里暂时用Pn代替产品名
                    var areaDataList = productDataList[j].areaDataList;
                    for (var k = 0; k < areaDataList.length; k++){
                        txt3 += "<td>" + areaDataList[k].averagePrice + "</td>";
                    }
                    txt3 += "</tr>";

                    txt4 += "<tr>" +
                        "<td style='text-align: center'>第" + timeDataList[i].type + "年</td>" +
                        "<td style='text-align: center'>P" + productDataList[j].type + "</td>";    //这里暂时用Pn代替产品名
                    var areaDataList = productDataList[j].areaDataList;
                    for (var k = 0; k < areaDataList.length; k++){
                        txt4 += "<td>" + areaDataList[k].requirement + "</td>";
                    }
                    txt4 += "</tr>";

                    txt5 += "<tr>" +
                        "<td style='text-align: center'>第" + timeDataList[i].type + "年</td>" +
                        "<td style='text-align: center'>P" + productDataList[j].type + "</td>";    //这里暂时用Pn代替产品名
                    var areaDataList = productDataList[j].areaDataList;
                    for (var k = 0; k < areaDataList.length; k++){
                        txt5 += "<td>" + areaDataList[k].orderQuantity + "</td>";
                    }
                    txt5 += "</tr>";
                }
            }
            txt3 += "</tbody></table><br>";
            txt4 += "</tbody></table><br>";
            txt5 += "</tbody></table><br>";
            document.getElementById("divReadMarket").innerHTML = txt3 + txt4 + txt5 + txt2;
        }
    });
    document.getElementById("ajaxDiv1").innerHTML = "正在查看市场（SeriesID：" + seriesId + "）详细订单";
    $("#btnReadMarket").click();
}

function findMarket() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/operateGetTeachClassInfo/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thcInfo) {
            readMarket(thcInfo.marketSeriesId);
            document.getElementById("ajaxDiv1").innerHTML = "正在查看市场预测";
        }
    });
}



function readRule(ruleId) {
    var txt = "";
    $.ajax({
        type:"GET",
        url:"/ruleFindById/" + ruleId,
        cache:false,
        dataType:"json",
        success:function (rule) {
            txt1 = "<div class='panel panel-warning'>" +
                "<div class='panel-heading'>" +
                "<h3 class='panel-title'>规则名：" +  rule.name + "</h3>" +
                "<h3 class='panel-title'>当前用户：" + $("#nowUserName").val() + "</h3>" +
                "</div>" +
                "<div class='panel-body'>" +
                "</div>" +
                "</div>";

            txt2="<table class='table table-striped table-hover table-bordered' id=''>" +
                "<thead>" +
                "<tr>" +
                "<th>厂房名称</th>" +
                "<th>厂房买价</th>" +
                "<th>厂房租金</th>" +
                "<th>厂房售价</th>" +
                "<th>厂房容量</th>" +
                "<th>数量限制</th>" +
                "<th>潜力分数</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            txt3 = "";
            var factoryQuantity = rule.factoryQuantity;
            for (var i = 1; i <= factoryQuantity; i++){
                var factory = getFactoryInfo(i);
                txt3 += "<tr>" +
                    "<td>" + factory.name + "</td>" +
                    "<td>" + factory.buyPrice + "</td>" +
                    "<td>" + factory.rentPrice + "</td>" +
                    "<td>" + factory.salePrice + "</td>" +
                    "<td>" + factory.volume + "</td>" +
                    "<td>" + factory.quantityLimit + "</td>" +
                    "<td>" + factory.score + "</td>" +
                    "</tr>";
            }
            txt3 += "</tbody></table><br/>";

            txt4 = "<table class='table table-striped table-hover table-bordered' id=''>" +
                "<thead>" +
                "<tr>" +
                "<th>生产线名称</th>" +
                "<th>每周期投资</th>" +
                "<th>投资周期</th>" +
                "<th>生产周期</th>" +
                "<th>每周期转产费</th>" +
                "<th>转产周期</th>" +
                "<th>维护费</th>" +
                "<th>残值</th>" +
                "<th>每周期折旧</th>" +
                "<th>折旧周期</th>" +
                "<th>潜力分数</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";

            var lineQuantity = rule.lineQuantity;
            for (var i = 1; i <= lineQuantity; i++){
                var line = getLineInfo(i);
                txt4 += "<tr>" +
                    "<td>" + line.name + "</td>" +
                    "<td>" + line.unitInvest + "</td>" +
                    "<td>" + line.installTime + "</td>" +
                    "<td>" + line.produceTime + "</td>" +
                    "<td>" + line.changeInvest + "</td>" +
                    "<td>" + line.changeTime + "</td>" +
                    "<td>" + line.upkeep + "</td>" +
                    "<td>" + line.scrapValue + "</td>" +
                    "<td>" + line.depreciation + "</td>" +
                    "<td>" + line.depreTime + "</td>" +
                    "<td>" + line.score + "</td>" +
                    "</tr>";
            }
            txt4 += "</tbody></table><br/>";

            txt5 = "<table class='table table-striped table-hover table-bordered' id=''>" +
                "<thead>" +
                "<tr>" +
                "<th>资质认证名称</th>" +
                "<th>每周期投资</th>" +
                "<th>投资周期</th>" +
                "<th>潜力分数</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            var qualificationQuantity = rule.qualificationQuantity;
            for (var i = 1; i <= qualificationQuantity; i++){
                var qualification = getQualificationInfo(i);
                txt5 += "<tr>" +
                    "<td>" + qualification.name + "</td>" +
                    "<td>" + qualification.unitInvest + "</td>" +
                    "<td>" + qualification.devTime + "</td>" +
                    "<td>" + qualification.score + "</td>" +
                    "</tr>";
            }
            txt5 += "</tbody></table><br/>";

            txt6 = "<table class='table table-striped table-hover table-bordered' id=''>" +
                "<thead>" +
                "<tr>" +
                "<th>市场名称</th>" +
                "<th>每周期投资</th>" +
                "<th>开发周期</th>" +
                "<th>潜力分数</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";

            var areaQuantity = rule.areaQuantity;
            for (var i = 1; i <= areaQuantity; i++){
                var area = getAreaInfo(i);
                txt6 += "<tr>" +
                    "<td>" + area.name + "</td>" +
                    "<td>" + area.unitInvest + "</td>" +
                    "<td>" + area.devTime + "</td>" +
                    "<td>" + area.score + "</td>" +
                    "</tr>";
            }
            txt6 += "</tbody></table><br/>";

            txt7 = "<table class='table table-striped table-hover table-bordered' id=''>" +
                "<thead>" +
                "<tr>" +
                "<th>原料名称</th>" +
                "<th>价格</th>" +
                "<th>采购提前期</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            var materialQuantity = rule.materialQuantity;
            for (var i = 1; i <= materialQuantity; i++){
                var material = getMaterialInfo(i);
                txt7 += "<tr>" +
                    "<td>" + material.name + "</td>" +
                    "<td>" + material.price + "</td>" +
                    "<td>" + material.time + "</td>" +
                    "</tr>";
            }
            txt7 += "</tbody></table><br/>";

            txt8 = "<table class='table table-striped table-hover table-bordered' id=''>" +
                "<thead>" +
                "<tr>" +
                "<th>产品名称</th>" +
                "<th>加工费</th>" +
                "<th>每周期开发费</th>" +
                "<th>开发周期</th>" +
                "<th>直接成本</th>" +
                "<th>潜力分数</th>" +
                "<th>产品组成</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            var productQuantity = rule.productQuantity;
            var materialQuantity = rule.materialQuantity;
            for (var i = 1; i <= productQuantity; i++){
                var product = getProductInfo(i);
                var bom = getProductBomInfo(i);
                var materialBomList = bom.materialBomList;
                var productBomList = bom.productBomList;
                txt8 += "<tr>" +
                    "<td>" + product.name + "</td>" +
                    "<td>" + product.procCost + "</td>" +
                    "<td>" + product.devInvest + "</td>" +
                    "<td>" + product.devTime + "</td>" +
                    "<td>" + product.finalCost + "</td>" +
                    "<td>" + product.score + "</td>" +
                    "<td>";
                for (var j = 0; j < materialQuantity; j++){ //原料组成
                    var bomValue = materialBomList[j].value;
                    if (bomValue > 0){
                        txt8 += bomValue + "*" + getMaterialInfo(j + 1).name + "&nbsp;&nbsp;";
                    }
                }
                for (var j = 0; j < productQuantity; j++){ //半成品组成
                    var bomValue = productBomList[j].value;
                    if (bomValue > 0){
                        txt8 += bomValue + "*" + getProductInfo(j + 1).name + "&nbsp;&nbsp;";
                    }
                }
                txt8 += "</td></tr>";
            }
            txt8 += "</tbody></table><br/>";

            var ruleParam = rule.ruleParam;
            txt9 = "<table class='table table-striped table-hover table-bordered' id=''>" +
                "<thead>" +
                "<tr>" +
                "<th>运行参数</th>" +
                "<tbody>" +
                "<tr>" +
                "<th >违约金</th>" +
                "<td>" + ruleParam.paramPenatly + "</td>" +
                "<th >贷款额倍数</th>" +
                "<td>" + ruleParam.paramLoanRatio + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >产品紧急出售折价率</th>" +
                "<td>" + ruleParam.paramProductSaleRatio + "</td>" +
                "<th >原料紧急出售折价率</th>" +
                "<td>" + ruleParam.paramMaterailSaleRatio + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >长贷利率</th>" +
                "<td>" + ruleParam.paramLongTermLoanRates + "</td>" +
                "<th >短贷利率</th>" +
                "<td>" + ruleParam.paramShortTermLoanRates + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >1、2期贴现利率</th>" +
                "<td>" + ruleParam.paramShortTermDiscountRates + "</td>" +
                "<th >3、4期贴现利率</th>" +
                "<td>" + ruleParam.paramLongTermDiscountRates + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >初始现金</th>" +
                "<td>" + ruleParam.paramInitialCash + "</td>" +
                "<th >管理费</th>" +
                "<td>" + ruleParam.paramManagementCost + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >信息费</th>" +
                "<td>" + ruleParam.paramInfomationCost + "</td>" +
                "<th >所得税率</th>" +
                "<td>" + ruleParam.paramTaxRate + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >最大长贷年份</th>" +
                "<td>" + ruleParam.paramLongTermLoanTimeLimit + "</td>" +
                "<th >最小广告额</th>" +
                "<td>" + ruleParam.paramAdvertisingMinFee + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >产品紧急采购溢价率</th>" +
                "<td>" + ruleParam.paramProductBuyRation + "</td>" +
                "<th >原料紧急采购溢价率</th>" +
                "<td>" + ruleParam.paramMaterailBuyRation + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >选单时间</th>" +
                "<td>" + ruleParam.paramSelectOrderTime + "</td>" +
                "<th >首位选单补时</th>" +
                "<td>" + ruleParam.paramFirstSelectOrderTime + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >市场同开数量</th>" +
                "<td>" + ruleParam.paramMarketSametimeOpenNum + "</td>" +
                "<th >竞单时间</th>" +
                "<td>" + ruleParam.paramBidTime + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th >竞单同竞数</th>" +
                "<td>" + ruleParam.paramBidSametimeNum + "</td>" +
                "<th >最大厂房数量</th>" +
                "<td>" + ruleParam.paramFactoryMaxNum + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th>市场老大</th>";
            if(ruleParam.paramHaveMarketLeader == 0){
                txt9 += "<td> 无 </td>";
            }else{
                txt9 += "<td> 有 </td>";
            }
            txt9 += "<th>贴现方式</th>";
            if(ruleParam.paramDiscountMode == 0){
                txt9 += "<td>独立贴现</td>";
            }else{
                txt9 += "<td>联合贴现</td>";
            }
            txt9 += "</tr>" +
                "<tr>" +
                "<th>用户还原本季</th>";
            if(ruleParam.paramAllowUserReturnSeason == 0){
                txt9 += "<td>不允许</td>";
            }else{
                txt9 += "<td>允许</td>";
            }
            txt9 += "<th>用户还原本年</th>";
            if(ruleParam.paramAllowUserReturnYear == 0){
                txt9 += "<td>不允许</td>";
            }else{
                txt9 += "<td>允许</td>";
            }
            txt9 += "</tr>" +
                "</tbody>" +
                "</table>";
            // document.getElementById("divReadRule").innerHTML = txt1 + txt2 + txt3 +txt4 + txt5 + txt6 + txt7 + txt8 + txt9;
            // document.getElementById("ajaxDiv1").innerHTML = "正在查看规则（ID：" +  ruleId +"）内容";
            document.getElementById("divReadRule").innerHTML = txt1 + txt2 + txt3 +txt4 + txt5 + txt6 + txt7 + txt8 + txt9;
            document.getElementById("ajaxDiv1").innerHTML = "正在查看规则（ID：" +  ruleId +"）内容";
            $("#btnReadRule").click();
        }
    });

}

function findRule() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/operateGetTeachClassInfo/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thcInfo) {
            readRule(thcInfo.ruleId);
            document.getElementById("ajaxDiv1").innerHTML = "正在查看规则";
        }
    });
}

function timeFormat(nS) {
    var date = new Date(parseInt(("/Date("+nS+")/").substr(6, 13))).toLocaleDateString();
    var time = new Date(parseInt(("/Date("+nS+")/").substr(6, 13))).toLocaleTimeString();
    return date +" " +  time;
};

function getTime() {
    var date = new Date();
    var operatorTime = date.toLocaleDateString() +" " +  date.toLocaleTimeString();
    return operatorTime;
}

function ajaxDivHide(id) {
    var date = new Date();
    var operatorTime = date.toLocaleDateString() +" " +  date.toLocaleTimeString();
    if(id==2){
        $("#ajaxDiv2").hide();
        document.getElementById("ajaxDiv1").innerHTML = operatorTime + " : 关闭 查询窗口";
    }
    if(id==3){
        $("#ajaxDiv3").hide();
        document.getElementById("ajaxDiv1").innerHTML = operatorTime + " : 关闭 编辑窗口";
    }
}
function ajaxDivShow(id) {
    var date = new Date();
    var operatorTime = date.toLocaleDateString() +" " +  date.toLocaleTimeString();
    if(id==2){
        $("#ajaxDiv2").show();
        var time = new Date();
        document.getElementById("ajaxDiv1").innerHTML = operatorTime + " : 打开 查询窗口";
    }
    if(id==3){
        $("#ajaxDiv3").show();
        document.getElementById("ajaxDiv1").innerHTML = operatorTime + " : 打开 编辑窗口";
    }
}

function updatePassword() {
//        var username = document.getElementById("username").value;
    var newPassword = document.getElementById("newPassword").value;
    var username;
    var id;
    var userLevel;
    var operatorName;
    var operatorLevel
    if (confirm("确定要修改密码吗?") == false) {
        return;
    }
//            alert("要修改密码的用户ID：" + username);
//            alert("新密码：" + newPassword);
    $.ajax({
        type:"GET",
        url:"getUserInfo",
        cache:false,
        dataType:"json",
        success:function (j1) {

            operatorName = j1[0].username;
            username = operatorName;
            operatorLevel = j1[0].userLevel;
//                    alert("J1:" + operatorName);

            $.ajax({
                type:"GET",
                url:"getUserInfoByUsername/" + username,
                cache:false,
                dataType:"json",
                success:function (json) {
                    id = json[0].id;
                    userLevel = json[0].userLevel;
//                            alert("修改的用户ID：" + id);
//                            alert("修改的用户等级：" + userLevel);
                    var ds = "id=" + id + "&username=" + username + "&password=" + newPassword + "&userLevel=" + userLevel + "&userOperator=" + operatorName;
//                            alert(ds);
                    $.ajax({
                        type:"POST",
                        url:"userChangePassword",
                        cache:false,
//                                dataType:"json",
                        data:ds,
                        async: false,
                        error: function(request) {
                            alert("修改密码失败");
                        },
                        success: function(data) {
                            $("#btnCloseModalUpdatePassword").click();
                            document.getElementById("ajaxDiv1").innerHTML = getTime() + " 成功修改密码";

                        }
                    });

                }
            });

        }
    });

}

function findSubUser() {
    var txt="";
    var nowUserName = $("#nowUserName").val();

    $.ajax({
        type:"GET",
        url:"/userFindByLevel/3",
        cache:false,
        dataType:"json",
        success:function (json) {
            if(json ==null){
                txt = "<h3 style='color: red'>无法查询到子用户信息，请执行初始化操作</h3>";
                document.getElementById("ajaxDiv2").innerHTML = txt;
                document.getElementById("ajaxDiv1").innerHTML = getTime() + "无法查询到子用户信息，因为教学班已被还原至初始状态";
            }else {
                txt="<table class='table table-striped table-hover table-bordered' id='editable-sample'>" +
                    "<thead>" +
                    "<tr>" +
                    "<th>用户ID</th>" +
                    "<th>用户名</th>" +
                    "<th>密码</th>" +
                    "<th>用户等级</th>" +
                    "<th>操作者</th>" +
                    "<th>修改密码</th>" +
                    "<th>删除用户</th>" +
                    "<th style='display: none'>当前操作用户</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody>";
                for(var i=0; i<eval(json).length; i++ ){
                    if(json[i].userOperator == nowUserName){
                        txt = txt + "<tr>" +
                            "<td>" + json[i].id + "</td>" +
                            "<td>" + json[i].username + "</td>" +
                            "<td>" + json[i].password + "</td>" +
                            "<td>" + json[i].userLevel + "</td>" +
                            "<td>" + json[i].userOperator + "</td>" +
                            "<td><a class='edit' href='#modalChangePassword' data-toggle='modal'>修改密码</a></td>" +
                            "<td><a onclick='deleteUser(" + json[i].id + ")'>删除用户</a></td>" +
                            "<td style='display: none'>当前操作用户</td>" +
                            "</tr>";
                    }
                }
                txt = txt + "</tbody></table>";
                document.getElementById("ajaxDiv2").innerHTML = txt;
                document.getElementById("ajaxDiv1").innerHTML = "成功查询用户信息";
            }



        }
    });
}

function addRule() {
    var date = new Date();
    var operatorTime = date.toLocaleDateString() +" " +  date.toLocaleTimeString();
    $("#divAddRule").show();
    document.getElementById("ajaxDiv1").innerHTML = operatorTime + " : 正在添加规则";
}

function validate(f){
    if(!(/^\w{4,20}$/.test(f.username.value))){
        alert("请输入长度为4-20的用户名。") ;
        f.username.focus() ;
        return false ;
    }
    if(!(/^\w{6,20}$/.test(f.password.value))){
        alert("请输入长度为6-20的密码。") ;
        f.password.focus() ;
        return false ;
    }
}

function changeTreatment(obj) {
    if(obj.value == 1){
        $("#treatment1").show();
        $("#treatment2").hide();
        $("#treatment3").hide();
    }
    if(obj.value == 2){
        $("#treatment2").show();
        $("#treatment1").hide();
        $("#treatment3").hide();
    }
    if(obj.value == 3){
        $("#treatment3").show();
        $("#treatment1").hide();
        $("#treatment2").hide();
    }

}

