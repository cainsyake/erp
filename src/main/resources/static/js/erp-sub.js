window.onload=subOnLoad;
function subOnLoad() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/getTeachClassRule/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (rule) {

            $.ajax({
                type:"POST",
                url:"/getSubRunningState/" + nowUserName,
                cache:false,
                dataType:"json",
                success:function (runningState) {
                    btnController(runningState);
                    pageController(rule, runningState);
                    infoController(runningState, rule);
                },
                error:function (json) {
                    console.log(json.responseText);
                }
            });
        },
        error:function (json) {
            console.log(json.responseText);
        }
    });

}

function infoController(runningState, rule) {
    document.getElementById("valueCash").innerHTML = runningState.financeState.cashAmount;   //输出现金金额
    document.getElementById("valueRunningTime").innerHTML = "第 " + runningState.baseState.timeYear + " 年 第 " + runningState.baseState.timeQuarter + " 季";

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



}

function pageController(rule, runningState) {
    var txt1 ="";
    txt1 += "<table class='table table-bordered'>" +
        "<thead>" +
        "<tr>" +
        "<th></th>";
    var ruleMarket = rule.ruleMarket;
    var marketNum = 0;
    if(ruleMarket.market1Name != ""){
        txt1 += "<th>" + ruleMarket.market1Name + "</th>";
        marketNum++;
    }
    if(ruleMarket.market2Name != ""){
        txt1 += "<th>" + ruleMarket.market2Name + "</th>";
        marketNum++;
    }
    if(ruleMarket.market3Name != ""){
        txt1 += "<th>" + ruleMarket.market3Name + "</th>";
        marketNum++;
    }
    if(ruleMarket.market4Name != ""){
        txt1 += "<th>" + ruleMarket.market4Name + "</th>";
        marketNum++;
    }
    if(ruleMarket.market5Name != ""){
        txt1 += "<th>" + ruleMarket.market5Name + "</th>";
        marketNum++;
    }
    txt1 += "</tr>" +
        "</thead>" +
        "<tbody>";
    var ruleProduct = rule.ruleProduct;
    if(ruleProduct.product1Name != ""){
        txt1 += "<tr>" +
            "<th>" + ruleProduct.product1Name + "</th>";
        for(var i =1; i < marketNum+1; i++){
            var strName = "ad" + i;
            txt1 += "<td><input type='number' min='0' style='width:80px' class='form-control' name='" + strName + "'/></td>";
        }
        txt1 += "</tr>";
    }
    if(ruleProduct.product2Name != ""){
        txt1 += "<tr>" +
            "<th>" + ruleProduct.product2Name + "</th>";
        for(var i =6; i < marketNum+6; i++){
            var strName = "ad" + i;
            txt1 += "<td><input type='number' min='0' style='width:80px' class='form-control' name='" + strName + "'/></td>";
        }
        txt1 += "</tr>";
    }
    if(ruleProduct.product3Name != ""){
        txt1 += "<tr>" +
            "<th>" + ruleProduct.product3Name + "</th>";
        for(var i =11; i < marketNum+11; i++){
            var strName = "ad" + i;
            txt1 += "<td><input type='number' min='0' style='width:80px' class='form-control' name='" + strName + "'/></td>";
        }
        txt1 += "</tr>";
    }
    if(ruleProduct.product4Name != ""){
        txt1 += "<tr>" +
            "<th>" + ruleProduct.product4Name + "</th>";
        for(var i =16; i < marketNum+16; i++){
            var strName = "ad" + i;
            txt1 += "<td><input type='number' min='0' style='width:80px' class='form-control' name='" + strName + "'/></td>";
        }
        txt1 += "</tr>";
    }
    if(ruleProduct.product5Name != ""){
        txt1 += "<tr>" +
            "<th>" + ruleProduct.product5Name + "</th>";
        for(var i =21; i < marketNum+21; i++){
            var strName = "ad" + i;
            txt1 += "<td><input type='number' min='0' style='width:80px' class='form-control' name='" + strName + "'/></td>";
        }
        txt1 += "</tr>";
    }
    txt1 += "</tbody>" +
        "</table>";
    document.getElementById("advertisingForm").innerHTML = txt1;

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
    for(var i = 1; i <= materialNum; i++){
        eval("var strName = ruleMaterial.material" + i + "Name");
        txt7 += "<label>" + strName + " ： </label><input type='number' min='0' placeholder='采购量' style='width:100px' id='material" + i + "AddNum' name=='material" + i + "AddNum'><br>";
    }
    document.getElementById("addPurchaseForm").innerHTML = txt7;

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
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnApplyShortLoan'>填写报表</button>";
            }
            if(obj.baseState.operateState.ad == 0){
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalAdvertising' type='button' style='' id='btnAdvertising'>投放广告</button>";
            }
            if(obj.baseState.operateState.orderMeeting == 0){
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnOrderMeeting'>订货会</button>";
            }
            if(obj.baseState.operateState.bidMeeting == 0){
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnBidMeeting'>竞单会</button>";
            }
            if(obj.baseState.operateState.longLoan == 0){
                txt += "<button class='btn btn-danger btn-lg' data-toggle='modal' href='#modalApplyLongDebt' type='button' id='ApplyLongLoan'>申请长贷</button>";
            }
            txt += "<button class='btn btn-success btn-lg' data-toggle='modal' href='#modalStartYear' type='button'  id='btnStartYear'>开始本年</button>";
        }else if(obj.baseState.timeQuarter < 4){
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
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalAddPurchase' type='button' id='btnAddPurchase'>采购原料</button>";
                }
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalNewFactory' type='button' id='btnAddFactory'>购租厂房</button>";
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalNewLine' type='button' id='btnAddLine'>新建生产线</button>";
                if(obj.baseState.operateState.buildLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' type='button' onclick='' style='' id='btnBuildLine'>在建生产线</button>";
                }
                txt += "<button class='btn btn-primary btn-lg' type='button' onclick='' style='' id='btnChangeLine'>生产线转产</button>";
                if(obj.baseState.operateState.continueChange == 0){
                    txt += "<button class='btn btn-warning btn-lg' type='button' onclick='' style='' id='btnContinueChange'>继续转产</button>";
                }
                if(obj.baseState.operateState.saleLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' type='button' onclick='' style='' id='btnSaleLine'>出售生产线</button>";
                }
                if(obj.baseState.operateState.beginProduction == 0){
                    txt += "<button class='btn btn-success btn-lg' type='button' onclick='' style='' id='btnBeginProduction'>开始生产</button>";
                }
                txt += "<button class='btn btn-danger btn-lg' type='button' onclick='' style='' id='btnUpdateReceivable'>应收款更新</button>";
            }
            if(obj.baseState.state == 13){
                if(obj.baseState.operateState.productDev == 0){
                    txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnProductDev'>产品研发</button>";
                }
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnDelivery'>按订单交货</button>";
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnFactoryTreatment'>厂房处理</button>";
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnEndQuarter'>当季结束</button>";
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
                    txt += "<button class='btn btn-warning btn-lg' data-toggle='modal' href='#modalAddPurchase' type='button' id='btnAddPurchase'>采购原料</button>";
                }
                txt += "<button class='btn btn-info btn-lg' data-toggle='modal' href='#modalNewFactory' type='button' id='btnAddFactory'>购租厂房</button>";
                txt += "<button class='btn btn-primary btn-lg' data-toggle='modal' href='#modalNewLine' type='button' id='btnAddLine'>新建生产线</button>";
                if(obj.baseState.operateState.buildLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' type='button' onclick='' style='' id='btnBuildLine'>在建生产线</button>";
                }
                txt += "<button class='btn btn-primary btn-lg' type='button' onclick='' style='' id='btnChangeLine'>生产线转产</button>";
                if(obj.baseState.operateState.continueChange == 0){
                    txt += "<button class='btn btn-warning btn-lg' type='button' onclick='' style='' id='btnContinueChange'>继续转产</button>";
                }
                if(obj.baseState.operateState.saleLine == 0){
                    txt += "<button class='btn btn-warning btn-lg' type='button' onclick='' style='' id='btnSaleLine'>出售生产线</button>";
                }
                if(obj.baseState.operateState.beginProduction == 0){
                    txt += "<button class='btn btn-success btn-lg' type='button' onclick='' style='' id='btnBeginProduction'>开始生产</button>";
                }
                txt += "<button class='btn btn-danger btn-lg' type='button' onclick='' style='' id='btnUpdateReceivable'>应收款更新</button>";

            }
            if(obj.baseState.state == 13){
                if(obj.baseState.operateState.productDev == 0){
                    txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnProductDev'>产品研发</button>";
                }
                if(obj.baseState.operateState.qualificationDev == 0){
                    txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnQualificationDev'>资质研发</button>";
                }
                if(obj.baseState.operateState.marketDev == 0){
                    txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnMarketDev'>市场开拓</button>";
                }
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnDelivery'>按订单交货</button>";
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnFactoryTreatment'>厂房处理</button>";
                txt += "<button class='btn btn-default btn-lg' type='button' onclick='' style='' id='btnEndYear'>当年结束</button>";
            }
        }
    }
    document.getElementById("divTimeAxis").innerHTML = txt;
}

function oprateAdvertising(form) {
    var nowUserName = $("#nowUserName").val();;
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
    var nowUserName = $("#nowUserName").val();
    var debtLimit = parseInt($("#valueDebtLimit2").html());
    var shortDebtAmount = $("#shortDebtAmount").val();
    if(debtLimit == 0){
        alert("现在已经没有贷款额度了，无法申请贷款");
        return false;
    }
    if(shortDebtAmount > debtLimit){
        alert("贷款额度不足，申请失败");
        return false;
    }else{
        var debt = {
            debtType: 1,
            repaymentPeriod: 4,
            amounts: shortDebtAmount
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
                $("#btnCloseModalApplyShortDebt").click();
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
    }

}

function operateUpdatePurchase() {
    var nowUserName = $("#nowUserName").val();
    var amounts = $("#valueUpdatePurchaseAmounts").val();
    var cash = $("#valueCash").val();
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
    console.log(list);
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
    var txt2 = "<table class='table table-striped table-hover table-bordered' id='editable-sample'>" +
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

    document.getElementById("divReadMarket").innerHTML = txt1 + txt2 ;
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
        success:function (json) {
            txt1 = "<div class='panel panel-warning'>" +
                "<div class='panel-heading'>" +
                "<h3 class='panel-title'>规则ID：" +  json.ruleId + " &nbsp;&nbsp;上传者：" + json.ruleUploader + "</h3>" +
                "<h3 class='panel-title'>上传时间：" + timeFormat(json.ruleAlterTime) + " &nbsp;&nbsp;&nbsp;&nbsp;调用次数：" + json.ruleUserCount + "</h3>" +
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
            var ruleFactory = json.ruleFactory;
            if(ruleFactory.factory1Name != ""){
                txt3 += "<tr>" +
                    "<td>" + ruleFactory.factory1Name + "</td>" +
                    "<td>" + ruleFactory.factory1BuyPrice + "</td>" +
                    "<td>" + ruleFactory.factory1RentPrice + "</td>" +
                    "<td>" + ruleFactory.factory1SalePrice + "</td>" +
                    "<td>" + ruleFactory.factory1Volume + "</td>" +
                    "<td>" + ruleFactory.factory1Limit + "</td>" +
                    "<td>" + ruleFactory.factory1Score + "</td>" +
                    "</tr>"
            }
            if(ruleFactory.factory2Name != ""){
                txt3 += "<tr>" +
                    "<td>" + ruleFactory.factory2Name + "</td>" +
                    "<td>" + ruleFactory.factory2BuyPrice + "</td>" +
                    "<td>" + ruleFactory.factory2RentPrice + "</td>" +
                    "<td>" + ruleFactory.factory2SalePrice + "</td>" +
                    "<td>" + ruleFactory.factory2Volume + "</td>" +
                    "<td>" + ruleFactory.factory2Limit + "</td>" +
                    "<td>" + ruleFactory.factory2Score + "</td>" +
                    "</tr>"
            }
            if(ruleFactory.factory3Name != ""){
                txt3 += "<tr>" +
                    "<td>" + ruleFactory.factory3Name + "</td>" +
                    "<td>" + ruleFactory.factory3BuyPrice + "</td>" +
                    "<td>" + ruleFactory.factory3RentPrice + "</td>" +
                    "<td>" + ruleFactory.factory3SalePrice + "</td>" +
                    "<td>" + ruleFactory.factory3Volume + "</td>" +
                    "<td>" + ruleFactory.factory3Limit + "</td>" +
                    "<td>" + ruleFactory.factory3Score + "</td>" +
                    "</tr>"
            }
            if(ruleFactory.factory4Name != ""){
                txt3 += "<tr>" +
                    "<td>" + ruleFactory.factory4Name + "</td>" +
                    "<td>" + ruleFactory.factory4BuyPrice + "</td>" +
                    "<td>" + ruleFactory.factory4RentPrice + "</td>" +
                    "<td>" + ruleFactory.factory4SalePrice + "</td>" +
                    "<td>" + ruleFactory.factory4Volume + "</td>" +
                    "<td>" + ruleFactory.factory4Limit + "</td>" +
                    "<td>" + ruleFactory.factory4Score + "</td>" +
                    "</tr>"
            }
            if(ruleFactory.factory5Name != ""){
                txt3 += "<tr>" +
                    "<td>" + ruleFactory.factory5Name + "</td>" +
                    "<td>" + ruleFactory.factory5BuyPrice + "</td>" +
                    "<td>" + ruleFactory.factory5RentPrice + "</td>" +
                    "<td>" + ruleFactory.factory5SalePrice + "</td>" +
                    "<td>" + ruleFactory.factory5Volume + "</td>" +
                    "<td>" + ruleFactory.factory5Limit + "</td>" +
                    "<td>" + ruleFactory.factory5Score + "</td>" +
                    "</tr>"
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
            var ruleLine = json.ruleLine;
            if(ruleLine.line1Name != ""){
                txt4 += "<tr>" +
                    "<td>" + ruleLine.line1Name + "</td>" +
                    "<td>" + ruleLine.line1UnitInvest + "</td>" +
                    "<td>" + ruleLine.line1InstallTime + "</td>" +
                    "<td>" + ruleLine.line1ProduceTime + "</td>" +
                    "<td>" + ruleLine.line1ChangeInvest + "</td>" +
                    "<td>" + ruleLine.line1ChangeTime + "</td>" +
                    "<td>" + ruleLine.line1Upkeep + "</td>" +
                    "<td>" + ruleLine.line1ScrapValue + "</td>" +
                    "<td>" + ruleLine.line1Depreciation + "</td>" +
                    "<td>" + ruleLine.line1DepreTime + "</td>" +
                    "<td>" + ruleLine.line1Score + "</td>" +
                    "</tr>"
            }
            if(ruleLine.line2Name != ""){
                txt4 += "<tr>" +
                    "<td>" + ruleLine.line2Name + "</td>" +
                    "<td>" + ruleLine.line2UnitInvest + "</td>" +
                    "<td>" + ruleLine.line2InstallTime + "</td>" +
                    "<td>" + ruleLine.line2ProduceTime + "</td>" +
                    "<td>" + ruleLine.line2ChangeInvest + "</td>" +
                    "<td>" + ruleLine.line2ChangeTime + "</td>" +
                    "<td>" + ruleLine.line2Upkeep + "</td>" +
                    "<td>" + ruleLine.line2ScrapValue + "</td>" +
                    "<td>" + ruleLine.line2Depreciation + "</td>" +
                    "<td>" + ruleLine.line2DepreTime + "</td>" +
                    "<td>" + ruleLine.line2Score + "</td>" +
                    "</tr>"
            }
            if(ruleLine.line3Name != ""){
                txt4 += "<tr>" +
                    "<td>" + ruleLine.line3Name + "</td>" +
                    "<td>" + ruleLine.line3UnitInvest + "</td>" +
                    "<td>" + ruleLine.line3InstallTime + "</td>" +
                    "<td>" + ruleLine.line3ProduceTime + "</td>" +
                    "<td>" + ruleLine.line3ChangeInvest + "</td>" +
                    "<td>" + ruleLine.line3ChangeTime + "</td>" +
                    "<td>" + ruleLine.line3Upkeep + "</td>" +
                    "<td>" + ruleLine.line3ScrapValue + "</td>" +
                    "<td>" + ruleLine.line3Depreciation + "</td>" +
                    "<td>" + ruleLine.line3DepreTime + "</td>" +
                    "<td>" + ruleLine.line3Score + "</td>" +
                    "</tr>"
            }
            if(ruleLine.line4Name != ""){
                txt4 += "<tr>" +
                    "<td>" + ruleLine.line4Name + "</td>" +
                    "<td>" + ruleLine.line4UnitInvest + "</td>" +
                    "<td>" + ruleLine.line4InstallTime + "</td>" +
                    "<td>" + ruleLine.line4ProduceTime + "</td>" +
                    "<td>" + ruleLine.line4ChangeInvest + "</td>" +
                    "<td>" + ruleLine.line4ChangeTime + "</td>" +
                    "<td>" + ruleLine.line4Upkeep + "</td>" +
                    "<td>" + ruleLine.line4ScrapValue + "</td>" +
                    "<td>" + ruleLine.line4Depreciation + "</td>" +
                    "<td>" + ruleLine.line4DepreTime + "</td>" +
                    "<td>" + ruleLine.line4Score + "</td>" +
                    "</tr>"
            }
            if(ruleLine.line5Name != ""){
                txt4 += "<tr>" +
                    "<td>" + ruleLine.line5Name + "</td>" +
                    "<td>" + ruleLine.line5UnitInvest + "</td>" +
                    "<td>" + ruleLine.line5InstallTime + "</td>" +
                    "<td>" + ruleLine.line5ProduceTime + "</td>" +
                    "<td>" + ruleLine.line5ChangeInvest + "</td>" +
                    "<td>" + ruleLine.line5ChangeTime + "</td>" +
                    "<td>" + ruleLine.line5Upkeep + "</td>" +
                    "<td>" + ruleLine.line5ScrapValue + "</td>" +
                    "<td>" + ruleLine.line5Depreciation + "</td>" +
                    "<td>" + ruleLine.line5DepreTime + "</td>" +
                    "<td>" + ruleLine.line5Score + "</td>" +
                    "</tr>"
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
            var ruleIso = json.ruleIso;
            if(ruleIso.iso1Name != ""){
                txt5 += "<tr>" +
                    "<td>" + ruleIso.iso1Name + "</td>" +
                    "<td>" + ruleIso.iso1UnitInvest + "</td>" +
                    "<td>" + ruleIso.iso1DevTime + "</td>" +
                    "<td>" + ruleIso.iso1Score + "</td>" +
                    "</tr>"
            }
            if(ruleIso.iso2Name != ""){
                txt5 += "<tr>" +
                    "<td>" + ruleIso.iso2Name + "</td>" +
                    "<td>" + ruleIso.iso2UnitInvest + "</td>" +
                    "<td>" + ruleIso.iso2DevTime + "</td>" +
                    "<td>" + ruleIso.iso2Score + "</td>" +
                    "</tr>"
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
            var ruleMarket = json.ruleMarket;
            if(ruleMarket.market1Name != ""){
                txt6 += "<tr>" +
                    "<td>" + ruleMarket.market1Name + "</td>" +
                    "<td>" + ruleMarket.market1UnitInvest + "</td>" +
                    "<td>" + ruleMarket.market1DevTime + "</td>" +
                    "<td>" + ruleMarket.market1Score + "</td>" +
                    "</tr>"
            }
            if(ruleMarket.market2Name != ""){
                txt6 += "<tr>" +
                    "<td>" + ruleMarket.market2Name + "</td>" +
                    "<td>" + ruleMarket.market2UnitInvest + "</td>" +
                    "<td>" + ruleMarket.market2DevTime + "</td>" +
                    "<td>" + ruleMarket.market2Score + "</td>" +
                    "</tr>"
            }
            if(ruleMarket.market3Name != ""){
                txt6 += "<tr>" +
                    "<td>" + ruleMarket.market3Name + "</td>" +
                    "<td>" + ruleMarket.market3UnitInvest + "</td>" +
                    "<td>" + ruleMarket.market3DevTime + "</td>" +
                    "<td>" + ruleMarket.market3Score + "</td>" +
                    "</tr>"
            }
            if(ruleMarket.market4Name != ""){
                txt6 += "<tr>" +
                    "<td>" + ruleMarket.market4Name + "</td>" +
                    "<td>" + ruleMarket.market4UnitInvest + "</td>" +
                    "<td>" + ruleMarket.market4DevTime + "</td>" +
                    "<td>" + ruleMarket.market4Score + "</td>" +
                    "</tr>"
            }
            if(ruleMarket.market5Name != ""){
                txt6 += "<tr>" +
                    "<td>" + ruleMarket.market5Name + "</td>" +
                    "<td>" + ruleMarket.market5UnitInvest + "</td>" +
                    "<td>" + ruleMarket.market5DevTime + "</td>" +
                    "<td>" + ruleMarket.market5Score + "</td>" +
                    "</tr>"
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
            var ruleMaterial = json.ruleMaterial;
            if(ruleMaterial.material1Name != ""){
                txt7 += "<tr>" +
                    "<td>" + ruleMaterial.material1Name + "</td>" +
                    "<td>" + ruleMaterial.material1Price + "</td>" +
                    "<td>" + ruleMaterial.material1Time + "</td>" +
                    "</tr>"
            }
            if(ruleMaterial.material2Name != ""){
                txt7 += "<tr>" +
                    "<td>" + ruleMaterial.material2Name + "</td>" +
                    "<td>" + ruleMaterial.material2Price + "</td>" +
                    "<td>" + ruleMaterial.material2Time + "</td>" +
                    "</tr>"
            }
            if(ruleMaterial.material3Name != ""){
                txt7 += "<tr>" +
                    "<td>" + ruleMaterial.material3Name + "</td>" +
                    "<td>" + ruleMaterial.material3Price + "</td>" +
                    "<td>" + ruleMaterial.material3Time + "</td>" +
                    "</tr>"
            }
            if(ruleMaterial.material4Name != ""){
                txt7 += "<tr>" +
                    "<td>" + ruleMaterial.material4Name + "</td>" +
                    "<td>" + ruleMaterial.material4Price + "</td>" +
                    "<td>" + ruleMaterial.material4Time + "</td>" +
                    "</tr>"
            }
            if(ruleMaterial.material5Name != ""){
                txt7 += "<tr>" +
                    "<td>" + ruleMaterial.material5Name + "</td>" +
                    "<td>" + ruleMaterial.material5Price + "</td>" +
                    "<td>" + ruleMaterial.material5Time + "</td>" +
                    "</tr>"
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
            var ruleProduct = json.ruleProduct;
            var ruleMaterial = json.ruleMaterial;
            var ruleProductMix = json.ruleProductMix;
            if(ruleProduct.product1Name != ""){
                txt8 += "<tr>" +
                    "<td>" + ruleProduct.product1Name + "</td>" +
                    "<td>" + ruleProduct.product1ProcCost + "</td>" +
                    "<td>" + ruleProduct.product1DevInvest + "</td>" +
                    "<td>" + ruleProduct.product1DevTime + "</td>" +
                    "<td>" + ruleProduct.product1FinalCost + "</td>" +
                    "<td>" + ruleProduct.product1Score + "</td>" +
                    "<td>";
                if((ruleProductMix.product1MixR1 != null) && (ruleProductMix.product1MixR1 != 0)){
                    txt8 += ruleProductMix.product1MixR1 + "*" + ruleMaterial.material1Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixR2 != null) && (ruleProductMix.product1MixR2 != 0)){
                    txt8 += ruleProductMix.product1MixR2 + "*" + ruleMaterial.material2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixR3 != null) && (ruleProductMix.product1MixR3 != 0)){
                    txt8 += ruleProductMix.product1MixR3 + "*" + ruleMaterial.material3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixR4 != null) && (ruleProductMix.product1MixR4 != 0)){
                    txt8 += ruleProductMix.product1MixR4 + "*" + ruleMaterial.material4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixR5 != null) && (ruleProductMix.product1MixR5 != 0)){
                    txt8 += ruleProductMix.product1MixR5 + "*" + ruleMaterial.material5Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixP1 != null) && (ruleProductMix.product1MixP1 != 0)){
                    txt8 += ruleProductMix.product1MixP1 + "*" + ruleProduct.product1Name + "&nbsp;&nbsp;"; //此为MixP1*产品1的名称
                }
                if((ruleProductMix.product1MixP2 != null) && (ruleProductMix.product1MixP2 != 0)){
                    txt8 += ruleProductMix.product1MixP2 + "*" + ruleProduct.product2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixP3 != null) && (ruleProductMix.product1MixP3 != 0)){
                    txt8 += ruleProductMix.product1MixP3 + "*" + ruleProduct.product3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixP4 != null) && (ruleProductMix.product1MixP4 != 0)){
                    txt8 += ruleProductMix.product1MixP4 + "*" + ruleProduct.product4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product1MixP5 != null) && (ruleProductMix.product1MixP5 != 0)){
                    txt8 += ruleProductMix.product1MixP5 + "*" + ruleProduct.product5Name + "&nbsp;&nbsp;";
                }
                txt8 += "</td></tr>";
            }
            if(ruleProduct.product2Name != ""){
                txt8 += "<tr>" +
                    "<td>" + ruleProduct.product2Name + "</td>" +
                    "<td>" + ruleProduct.product2ProcCost + "</td>" +
                    "<td>" + ruleProduct.product2DevInvest + "</td>" +
                    "<td>" + ruleProduct.product2DevTime + "</td>" +
                    "<td>" + ruleProduct.product2FinalCost + "</td>" +
                    "<td>" + ruleProduct.product2Score + "</td>" +
                    "<td>";
                if((ruleProductMix.product2MixR1 != null) && (ruleProductMix.product2MixR1 != 0)){
                    txt8 += ruleProductMix.product2MixR1 + "*" + ruleMaterial.material1Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixR2 != null) && (ruleProductMix.product2MixR2 != 0)){
                    txt8 += ruleProductMix.product2MixR2 + "*" + ruleMaterial.material2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixR3 != null) && (ruleProductMix.product2MixR3 != 0)){
                    txt8 += ruleProductMix.product2MixR3 + "*" + ruleMaterial.material3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixR4 != null) && (ruleProductMix.product2MixR4 != 0)){
                    txt8 += ruleProductMix.product2MixR4 + "*" + ruleMaterial.material4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixR5 != null) && (ruleProductMix.product2MixR5 != 0)){
                    txt8 += ruleProductMix.product2MixR5 + "*" + ruleMaterial.material5Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixP1 != null) && (ruleProductMix.product2MixP1 != 0)){
                    txt8 += ruleProductMix.product2MixP1 + "*" + ruleProduct.product1Name + "&nbsp;&nbsp;"; //此为MixP1*产品1的名称
                }
                if((ruleProductMix.product2MixP2 != null) && (ruleProductMix.product2MixP2 != 0)){
                    txt8 += ruleProductMix.product2MixP2 + "*" + ruleProduct.product2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixP3 != null) && (ruleProductMix.product2MixP3 != 0)){
                    txt8 += ruleProductMix.product2MixP3 + "*" + ruleProduct.product3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixP4 != null) && (ruleProductMix.product2MixP4 != 0)){
                    txt8 += ruleProductMix.product2MixP4 + "*" + ruleProduct.product4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product2MixP5 != null) && (ruleProductMix.product2MixP5 != 0)){
                    txt8 += ruleProductMix.product2MixP5 + "*" + ruleProduct.product5Name + "&nbsp;&nbsp;";
                }
                txt8 += "</td></tr>";
            }
            if(ruleProduct.product3Name != ""){
                txt8 += "<tr>" +
                    "<td>" + ruleProduct.product3Name + "</td>" +
                    "<td>" + ruleProduct.product3ProcCost + "</td>" +
                    "<td>" + ruleProduct.product3DevInvest + "</td>" +
                    "<td>" + ruleProduct.product3DevTime + "</td>" +
                    "<td>" + ruleProduct.product3FinalCost + "</td>" +
                    "<td>" + ruleProduct.product3Score + "</td>" +
                    "<td>";
                if((ruleProductMix.product3MixR1 != null) && (ruleProductMix.product3MixR1 != 0)){
                    txt8 += ruleProductMix.product3MixR1 + "*" + ruleMaterial.material1Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixR2 != null) && (ruleProductMix.product3MixR2 != 0)){
                    txt8 += ruleProductMix.product3MixR2 + "*" + ruleMaterial.material2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixR3 != null) && (ruleProductMix.product3MixR3 != 0)){
                    txt8 += ruleProductMix.product3MixR3 + "*" + ruleMaterial.material3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixR4 != null) && (ruleProductMix.product3MixR4 != 0)){
                    txt8 += ruleProductMix.product3MixR4 + "*" + ruleMaterial.material4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixR5 != null) && (ruleProductMix.product3MixR5 != 0)){
                    txt8 += ruleProductMix.product3MixR5 + "*" + ruleMaterial.material5Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixP1 != null) && (ruleProductMix.product3MixP1 != 0)){
                    txt8 += ruleProductMix.product3MixP1 + "*" + ruleProduct.product1Name + "&nbsp;&nbsp;"; //此为MixP1*产品1的名称
                }
                if((ruleProductMix.product3MixP2 != null) && (ruleProductMix.product3MixP2 != 0)){
                    txt8 += ruleProductMix.product3MixP2 + "*" + ruleProduct.product2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixP3 != null) && (ruleProductMix.product3MixP3 != 0)){
                    txt8 += ruleProductMix.product3MixP3 + "*" + ruleProduct.product3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixP4 != null) && (ruleProductMix.product3MixP4 != 0)){
                    txt8 += ruleProductMix.product3MixP4 + "*" + ruleProduct.product4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product3MixP5 != null) && (ruleProductMix.product3MixP5 != 0)){
                    txt8 += ruleProductMix.product3MixP5 + "*" + ruleProduct.product5Name + "&nbsp;&nbsp;";
                }
                txt8 += "</td></tr>";
            }
            if(ruleProduct.product4Name != ""){
                txt8 += "<tr>" +
                    "<td>" + ruleProduct.product4Name + "</td>" +
                    "<td>" + ruleProduct.product4ProcCost + "</td>" +
                    "<td>" + ruleProduct.product4DevInvest + "</td>" +
                    "<td>" + ruleProduct.product4DevTime + "</td>" +
                    "<td>" + ruleProduct.product4FinalCost + "</td>" +
                    "<td>" + ruleProduct.product4Score + "</td>" +
                    "<td>";
                if((ruleProductMix.product4MixR1 != null) && (ruleProductMix.product4MixR1 != 0)){
                    txt8 += ruleProductMix.product4MixR1 + "*" + ruleMaterial.material1Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixR2 != null) && (ruleProductMix.product4MixR2 != 0)){
                    txt8 += ruleProductMix.product4MixR2 + "*" + ruleMaterial.material2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixR3 != null) && (ruleProductMix.product4MixR3 != 0)){
                    txt8 += ruleProductMix.product4MixR3 + "*" + ruleMaterial.material3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixR4 != null) && (ruleProductMix.product4MixR4 != 0)){
                    txt8 += ruleProductMix.product4MixR4 + "*" + ruleMaterial.material4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixR5 != null) && (ruleProductMix.product4MixR5 != 0)){
                    txt8 += ruleProductMix.product4MixR5 + "*" + ruleMaterial.material5Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixP1 != null) && (ruleProductMix.product4MixP1 != 0)){
                    txt8 += ruleProductMix.product4MixP1 + "*" + ruleProduct.product1Name + "&nbsp;&nbsp;"; //此为MixP1*产品1的名称
                }
                if((ruleProductMix.product4MixP2 != null) && (ruleProductMix.product4MixP2 != 0)){
                    txt8 += ruleProductMix.product4MixP2 + "*" + ruleProduct.product2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixP3 != null) && (ruleProductMix.product4MixP3 != 0)){
                    txt8 += ruleProductMix.product4MixP3 + "*" + ruleProduct.product3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixP4 != null) && (ruleProductMix.product4MixP4 != 0)){
                    txt8 += ruleProductMix.product4MixP4 + "*" + ruleProduct.product4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product4MixP5 != null) && (ruleProductMix.product4MixP5 != 0)){
                    txt8 += ruleProductMix.product4MixP5 + "*" + ruleProduct.product5Name + "&nbsp;&nbsp;";
                }
                txt8 += "</td></tr>";
            }
            if(ruleProduct.product5Name != ""){
                txt8 += "<tr>" +
                    "<td>" + ruleProduct.product5Name + "</td>" +
                    "<td>" + ruleProduct.product5ProcCost + "</td>" +
                    "<td>" + ruleProduct.product5DevInvest + "</td>" +
                    "<td>" + ruleProduct.product5DevTime + "</td>" +
                    "<td>" + ruleProduct.product5FinalCost + "</td>" +
                    "<td>" + ruleProduct.product5Score + "</td>" +
                    "<td>";
                if((ruleProductMix.product5MixR1 != null) && (ruleProductMix.product5MixR1 != 0)){
                    txt8 += ruleProductMix.product5MixR1 + "*" + ruleMaterial.material1Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixR2 != null) && (ruleProductMix.product5MixR2 != 0)){
                    txt8 += ruleProductMix.product5MixR2 + "*" + ruleMaterial.material2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixR3 != null) && (ruleProductMix.product5MixR3 != 0)){
                    txt8 += ruleProductMix.product5MixR3 + "*" + ruleMaterial.material3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixR4 != null) && (ruleProductMix.product5MixR4 != 0)){
                    txt8 += ruleProductMix.product5MixR4 + "*" + ruleMaterial.material4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixR5 != null) && (ruleProductMix.product5MixR5 != 0)){
                    txt8 += ruleProductMix.product5MixR5 + "*" + ruleMaterial.material5Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixP1 != null) && (ruleProductMix.product5MixP1 != 0)){
                    txt8 += ruleProductMix.product5MixP1 + "*" + ruleProduct.product1Name + "&nbsp;&nbsp;"; //此为MixP1*产品1的名称
                }
                if((ruleProductMix.product5MixP2 != null) && (ruleProductMix.product5MixP2 != 0)){
                    txt8 += ruleProductMix.product5MixP2 + "*" + ruleProduct.product2Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixP3 != null) && (ruleProductMix.product5MixP3 != 0)){
                    txt8 += ruleProductMix.product5MixP3 + "*" + ruleProduct.product3Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixP4 != null) && (ruleProductMix.product5MixP4 != 0)){
                    txt8 += ruleProductMix.product5MixP4 + "*" + ruleProduct.product4Name + "&nbsp;&nbsp;";
                }
                if((ruleProductMix.product5MixP5 != null) && (ruleProductMix.product5MixP5 != 0)){
                    txt8 += ruleProductMix.product5MixP5 + "*" + ruleProduct.product5Name + "&nbsp;&nbsp;";
                }
                txt8 += "</td></tr>";
            }
            txt8 += "</tbody></table><br/>";

            var ruleParam = json.ruleParam;
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

function thClassInit() {
    $.ajax({
        cache: true,
        type: "POST",
        url:"teachClassInit",
        data:$('#thClassInitForm').serialize(),
        async: false,
        success: function(data) {
            console.log(data);
            document.getElementById("ajaxDiv1").innerHTML = getTime() + " " + data;
        }
    });
}

function addTeacher() {
    $.ajax({
        cache: true,
        type: "POST",
        url:"userAdd",
        data:$('#teacherAddForm').serialize(),
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) {
            document.getElementById("ajaxDiv1").innerHTML = "添加教学班老师成功";
        }
    });
}
