window.onload=thcOnload;
function thcOnload() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/getTeachClassInfo/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thc) {
            if(thc.orderMeetingState == 1){
                window.setInterval(orderMeetingUpdate, 500);
            }
            checkTime(thc);
            pageOrderMeeting(thc);
        }
    });
}

function initAddRule() {
    var username = $("#nowUserName").val();
    var ruleId = $("#ruleId").val();
    if (ruleId == 0){
        //新增规则
        var rule = new Object();
        rule.factoryQuantity = $("#factoryQuantity").val();
        rule.lineQuantity = $("#lineQuantity").val();
        rule.qualificationQuantity = $("#qualificationQuantity").val();
        rule.areaQuantity = $("#areaQuantity").val();
        rule.materialQuantity = $("#materialQuantity").val();
        rule.productQuantity = $("#productQuantity").val();
        rule.ruleParam = {
            paramPenatly:$("#paramPenatly").val(),
            paramLoanRatio:$("#paramLoanRatio").val(),
            paramProductSaleRatio:$("#paramProductSaleRatio").val(),
            paramMaterailSaleRatio:$("#paramMaterailSaleRatio").val(),
            paramLongTermLoanRates:$("#paramLongTermLoanRates").val(),
            paramShortTermLoanRates:$("#paramShortTermLoanRates").val(),
            paramShortTermDiscountRates:$("#paramShortTermDiscountRates").val(),
            paramLongTermDiscountRates:$("#paramLongTermDiscountRates").val(),
            paramInitialCash:$("#paramInitialCash").val(),
            paramManagementCost:$("#paramManagementCost").val(),
            paramInfomationCost:$("#paramInfomationCost").val(),
            paramTaxRate:$("#paramTaxRate").val(),
            paramLongTermLoanTimeLimit:$("#paramLongTermLoanTimeLimit").val(),
            paramAdvertisingMinFee:$("#paramAdvertisingMinFee").val(),
            paramProductBuyRation:$("#paramProductBuyRation").val(),
            paramMaterailBuyRation:$("#paramMaterailBuyRation").val(),
            paramSelectOrderTime:$("#paramSelectOrderTime").val(),
            paramFirstSelectOrderTime:$("#paramFirstSelectOrderTime").val(),
            paramMarketSametimeOpenNum:$("#paramMarketSametimeOpenNum").val(),
            paramBidTime:$("#paramBidTime").val(),
            paramBidSametimeNum:$("#paramBidSametimeNum").val(),
            paramFactoryMaxNum:$("#paramFactoryMaxNum").val(),
            paramHaveMarketLeader:$('input:radio[name="paramHaveMarketLeader"]:checked').val(),
            paramDiscountMode:$('input:radio[name="paramDiscountMode"]:checked').val(),
            paramAllowUserReturnSeason:$('input:radio[name="paramAllowUserReturnSeason"]:checked').val(),
            paramAllowUserReturnYear:$('input:radio[name="paramAllowUserReturnYear"]:checked').val(),
        };
        var ajaxData = {rule: rule, username: username};
        $.ajax({
            // url: '/addRule/' + username,
            url: '/addRule',
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(ajaxData),
            dataType:'json',
            success:function (rs) {
                $('#quantityData').attr("factory-quantity", rule.factoryQuantity);
                $('#quantityData').attr("line-quantity", rule.lineQuantity);
                $('#quantityData').attr("qualification-quantity", rule.qualificationQuantity);
                $('#quantityData').attr("area-quantity", rule.areaQuantity);
                $('#quantityData').attr("material-quantity", rule.materialQuantity);
                $('#quantityData').attr("product-quantity", rule.productQuantity);
                $("#ajaxDiv1").html("成功初始化规则,ID：" + rs.msg);
                $("#initAddRuleResult").html("<h4 style='color: blue'>初始化规则,请继续下一步</h4>");
                initAllAddRuleArea();
            },
            error:function (rs) {
                $("#ajaxDiv1").html(operatorTime + " : 初始化规则失败");
                console.log(rs);
            }
        });


    }else{
        //TODO 载入规则 修改
    }
}

function initAllAddRuleArea() {
    initAddFactoryArea();
    initAddLineArea();
}

function initAddFactoryArea() {
    for (var i = 1; i <= $('#quantityData').attr('factory-quantity'); i++){
        var content = "<div class='form-group'>" +
            "<table class='table table-striped table-hover table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th colspan='2' style='text-align: center'>第" + i + "组厂房规则</th>" +
            "<th style='text-align: center'>厂房名称</th>" +
            "<td><input class='form-control' type='text' id='fname" + i + "'/></td>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "<tr>" +
            "<th style='text-align: center'>潜力分数</th>" +
            "<td><input class='form-control' type='number' id='fscore" + i + "'/></td>" +
            "<th style='text-align: center'>厂房购买价</th>" +
            "<td><input class='form-control' type='number' id='fbuyPrice" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>厂房租赁价</th>" +
            "<td><input class='form-control' type='number' id='frentPrice" + i + "'/></td>" +
            "<th style='text-align: center'>厂房出售价</th>" +
            "<td><input class='form-control' type='number' id='fsalePrice" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>生产线容量</th>" +
            "<td><input class='form-control' type='number' id='fvolume" + i + "'/></td>" +
            "<th style='text-align: center'>购买上限</th>" +
            "<td><input class='form-control' type='number' id='fquantityLimit" + i + "'/></td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</div>";
        $('#addFactoryArea').append(content);
    }
}

function addRuleFactory() {
    var username = $("#nowUserName").val();
    var factories = [];
    for (var i = 1; i <= $('#quantityData').attr('factory-quantity'); i++){
        var factory = new Object();
        factory.name = $('#fname' + i).val();
        factory.buyPrice = $('#fbuyPrice' + i).val();
        factory.rentPrice = $('#frentPrice' + i).val();
        factory.salePrice = $('#fsalePrice' + i).val();
        factory.volume = $('#fvolume' + i).val();
        factory.quantityLimit = $('#fquantityLimit' + i).val();
        factory.score = $('#fscore' + i).val();
        factory.type = i;
        factories.push(factory);
    }
    // console.log(factorys);
    var ajaxData = {factories: factories, username: username}
    $.ajax({
        url: '/addRuleFactory',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            // $("#ajaxDiv1").html("成功初始化规则,ID：" + rs.msg);
            $("#initAddRuleResult").html("<h4 style='color: blue'>上传厂房规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html(operatorTime + " : 上传厂房规则失败");
            console.log(rs);
        }
    });
}

function initAddLineArea() {
    for (var i = 1; i <= $('#quantityData').attr('line-quantity'); i++){
        var content = "<div class='form-group'>" +
            "<table class='table table-striped table-hover table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th colspan='2' style='text-align: center'>第" + i + "组生产线规则</th>" +
            "<th style='text-align: center'>生产线名称</th>" +
            "<td><input class='form-control' type='text' id='lname" + i + "'/></td>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "<tr>" +
            "<th style='text-align: center'>单位投资</th>" +
            "<td><input class='form-control' type='number' id='lunitInvest" + i + "'/></td>" +
            "<th style='text-align: center'>安装周期</th>" +
            "<td><input class='form-control' type='number' id='linstallTime" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>转产费用(每周期)</th>" +
            "<td><input class='form-control' type='number' id='lchangeInvest" + i + "'/></td>" +
            "<th style='text-align: center'>转产周期</th>" +
            "<td><input class='form-control' type='number' id='lchangeTime" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>生产周期</th>" +
            "<td><input class='form-control' type='number' id='lproduceTime" + i + "'/></td>" +
            "<th style='text-align: center'>维护费</th>" +
            "<td><input class='form-control' type='number' id='lupkeep" + i + "'/></td>" +
            "</tr>" +
            "<th style='text-align: center'>残值</th>" +
            "<td><input class='form-control' type='number' id='lscrapValue" + i + "'/></td>" +
            "<th style='text-align: center'>单位周期折旧</th>" +
            "<td><input class='form-control' type='number' id='ldepreciation" + i + "'/></td>" +
            "</tr>" +
            "<th style='text-align: center'>折旧周期</th>" +
            "<td><input class='form-control' type='number' id='ldepreTime" + i + "'/></td>" +
            "<th style='text-align: center'>潜力分数</th>" +
            "<td><input class='form-control' type='number' id='lscore" + i + "'/></td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</div>";
        $('#addLineArea').append(content);
    }
}


function adReport() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/adReport/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thc) {
            var txt = getTime() + '已发放广告投放情况';
            $('#ajaxDiv1').html(txt);
        }
    });
}

function cloneRule() {
    var nowUserName = $("#nowUserName").val();
    var host = "http://erp.cainsyake.com.cn";
    var id = 1;
    var url = host + "/ruleFindById/"+ id + "?callback=?";
    $.getJSON(url, function (jsondata) {
        console.log(jsondata);
    });
    $.ajax({
        type:"GET",
        url:host + "/ruleFindById/"+ id,
        cache:false,
        dataType:"json",
        success:function (rule) {
            console.log("测试输出获取到的RULE—REMOTE");
            console.log(rule);
            $.ajax({
                type:"POST",
                url:"/cloneRule",
                cache:false,
                data:rule,
                dataType:"json",
                success:function (rule) {
                console.log("测试 成功克隆RULE");
            },
            error:function (json) {
                console.log(json.responseText);
            }
        });
        },
        error:function (json) {
            console.log("测试失败");
            console.log(json.responseText);
        }
    });

}

function startOrderMeeting() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/startOrderMeeting/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thc) {
            thcOnload();
        }
    });
}

function orderMeetingUpdate() {
    var nowUserName = $("#nowUserName").val();
    var time1 = new Date();
    $.ajax({
        type:"POST",
        url:"/getTeachClassInfo/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thc) {
            if(thc.orderMeetingState == 2){
                window.location.reload();
            }
            var areaCollatorList = thc.collator.areaCollatorList;
            var openAreaList = thc.collator.openAreaList;
            if(openAreaList.length == 0){
                $.ajax({
                    type:"POST",
                    url:"/endOrderMeeting/" + nowUserName,
                    cache:false,
                    dataType:"json",
                    success:function (thc) {
                        // thcOnload();
                    }
                });
            }
            $.each(openAreaList,function(n,openArea){
                //遍历开放区域List
                console.log("测试 当前开放区域ID：" + openArea);
                var areaCollator = areaCollatorList[openArea - 1];  //打开区域排序器
                var productCollatorList = areaCollator.productCollatorList;
                if(productCollatorList.length == 0){
                    //该区域无订单，提交切换区域请求
                    console.log("该区域无订单，提交切换区域请求");
                    $.ajax({
                        type:"POST",
                        url:"/nextArea/" + nowUserName + "/" + openArea,
                        cache:false,
                        dataType:"json",
                        success:function (thc) {
                            // thcOnload();
                        }
                    });
                }else {
                    var openProduct = areaCollator.openProduct;     //获取当前开放产品ID
                    console.log("测试 当前开放产品ID：" + openProduct);
                    if(openProduct == 0){
                        $.ajax({
                            type:"POST",
                            url:"/nextProduct/" + nowUserName + "/" + openArea,
                            cache:false,
                            dataType:"json",
                            success:function (thc) {
                                // thcOnload();
                            }
                        });
                    }else {
                        var productCollator = productCollatorList[openProduct - 1];     //打开产品排序器
                        var orderIdList = productCollator.orderIdList;
                        var sortResultList = productCollator.sortResultList;
                        if(orderIdList.length == 0 || sortResultList == 0){
                            //该产品无订单或无人投广告，提交切换产品请求
                            console.log("该产品无订单或无人投广告，提交切换产品请求");
                            $.ajax({
                                type:"POST",
                                url:"/nextProduct/" + nowUserName + "/" + openArea,
                                cache:false,
                                dataType:"json",
                                success:function (thc) {
                                    // thcOnload();
                                    var time2 = new Date();
                                    var time3 = time2.getTime() - time1.getTime();
                                    console.log("测试 切换产品处理时间：" + time3 + " ms");
                                }
                            });
                        }else {
                            var openUser = productCollator.openUser;
                            console.log("测试 输出当前开放排位:" + openUser);
                            if(openUser == 0){
                                console.log("测试 当前产品排序器开放用户排位是0 执行切换用户操作");
                                $.ajax({
                                    type:"POST",
                                    url:"/nextUser/" + nowUserName + "/" + openArea,
                                    cache:false,
                                    dataType:"json",
                                    success:function (thc) {
                                        // thcOnload();
                                        var time2 = new Date();
                                        var time3 = time2.getTime() - time1.getTime();
                                        console.log("测试 切换用户处理时间：" + time3 + " ms");
                                    }
                                });
                            }else {
                                var time = productCollator.time;
                                var nowTime = new Date();
                                var timeResult = time - nowTime.getTime();
                                console.log("测试 输出剩余选单时间(ms):" + timeResult);
                                if(timeResult < 0){
                                    $.ajax({
                                        type:"POST",
                                        url:"/nextUser/" + nowUserName + "/" + openArea,
                                        cache:false,
                                        dataType:"json",
                                        success:function (thc) {
                                            // thcOnload();
                                        }
                                    });
                                }
                            }

                        }
                    }
                }
            });
            pageOrderMeeting(thc);
        }
    });
}

function endOrderMeeting() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/endOrderMeeting/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thc) {
            thcOnload();
        }
    });
}

function checkTime(thc) {
    //完成竞单模块后需要增加对竞单会状态的检验
    var nowUserName = $("#nowUserName").val();
    if(thc.orderMeetingState == 2){
        $.ajax({
            type:"POST",
            url:"/changeTime/" + nowUserName,
            cache:false,
            dataType:"json",
            success:function (thc) {
                thcOnload();
            }
        });
    }

}

function pageOrderMeeting(thc) {
    $.ajax({
        type:"POST",
        url:"/checkThisYearOrder/" + thc.marketSeriesId + "/" + thc.time,
        cache:false,
        dataType:"json",
        success:function (check) {
            if(check == 0){
                var txt = "<button class='btn btn-info' onclick='endOrderMeeting()' type='button'>结束本年选单</button>";
                document.getElementById("divOrderCheck").innerHTML = txt;
            }else {
                if(thc.orderMeetingState == 0){
                    var txtOrderCheck = "";
                    var txtOrderMeeting = "<hr>" +
                        "<h4 style='text-align: center'>第 " + thc.time + " 年广告投放情况</h4>" +
                        "<table class='table table-striped table-hover table-bordered'>" +
                        "<thead>" +
                        "<th style='text-align: center'>用户名</th>" +
                        "<th style='text-align: center'>用户时间</th>" +
                        "<th style='text-align: center'>报表情况</th>" +
                        "<th style='text-align: center'>广告投放状态</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>";

                    var tabAd = 0;
                    var subUserInfoList = thc.subUserInfoList;
                    $.each(subUserInfoList,function(n, subUserInfo){
                        if(subUserInfo.runningState.baseState.state != -2){
                            //遍历非破产的子用户信息
                            txtOrderMeeting += "<tr>" +
                                "<td style='text-align: center'>" + subUserInfo.subUserName + "</td>" +
                                "<td style='text-align: center'>第" + subUserInfo.runningState.baseState.timeYear + "年 " + subUserInfo.runningState.baseState.timeQuarter + "季</td>";
                            if(subUserInfo.runningState.baseState.operateState.reportResult == null){
                                txtOrderMeeting += "<td style='text-align: center'>未填</td>";
                            }else if(subUserInfo.runningState.baseState.operateState.reportResult == 1){
                                txtOrderMeeting += "<td style='text-align: center;color: blue'>正确</td>";
                            }else if(subUserInfo.runningState.baseState.operateState.reportResult == 2){
                                txtOrderMeeting += "<td style='text-align: center;color: red'>有误</td>";
                            }
                            if(subUserInfo.runningState.baseState.operateState.ad == 0 || subUserInfo.runningState.baseState.operateState.ad == null){
                                txtOrderMeeting += "<td style='text-align: center'>未投</td>";
                                tabAd = 1;
                            }else if(subUserInfo.runningState.baseState.operateState.ad == 1){
                                txtOrderMeeting += "<td style='text-align: center;color: blue'>已投</td>";
                            }
                            txtOrderMeeting += "</tr>";
                        }
                    });
                    txtOrderMeeting += "</tbody>" +
                        "</table>";
                    if(tabAd == 0){
                        txtOrderCheck = "<button class='btn btn-info' onclick='startOrderMeeting()' type='button'>开始本年选单</button>";
                    }else{
                        txtOrderCheck = "<button class='btn btn-warning' type='button'>还不能开始选单</button>";
                    }
                    document.getElementById("divOrderCheck").innerHTML = txtOrderCheck;
                    document.getElementById("divOrderMeeting").innerHTML = txtOrderMeeting;
                }else if(thc.orderMeetingState == 1){
                    txtOrderCheck = "<h3 style='text-align: center'>正 在 选 单</h3>";
                    var txtOrderMeeting = "<hr>" +
                        "<h4 style='text-align: center'>第 " + thc.time + " 年选单情况</h4>" +
                        "<table class='table table-striped table-hover table-bordered'>" +
                        "<thead>" +
                        "<th style='text-align: center'>区域ID</th>" +
                        "<th style='text-align: center'>产品ID</th>" +
                        "<th style='text-align: center'>当前排位</th>" +
                        "<th style='text-align: center'>选单用户</th>" +
                        "<th style='text-align: center'>剩余时间</th>" +
                        "</tr>" +
                        "</thead>" +
                        "<tbody>";
                    var collator = thc.collator;
                    var openAreaList = collator.openAreaList;
                    var areaCollatorList = thc.collator.areaCollatorList;
                    $.each(openAreaList,function(n, openArea){
                        txtOrderMeeting += "<tr>" +
                            "<td style='text-align: center'>" + openArea + "</td>";
                        var areaCollator = areaCollatorList[openArea - 1];  //打开区域排序器
                        var productCollatorList = areaCollator.productCollatorList;
                        if(productCollatorList.length == 0){
                            //该区域无订单，提交切换区域请求
                            txtOrderMeeting += "<td style='text-align: center'>无</td>";
                        }else {
                            var openProduct = areaCollator.openProduct;     //获取当前开放产品ID
                            txtOrderMeeting += "<td style='text-align: center'>" + openProduct + "</td>";
                            if(openProduct == 0){
                                txtOrderMeeting += "<td style='text-align: center'>无</td>" +
                                    "<td style='text-align: center'>无</td>";
                            }else {
                                var productCollator = productCollatorList[openProduct - 1];     //打开产品排序器
                                var orderIdList = productCollator.orderIdList;
                                var sortResultList = productCollator.sortResultList;
                                if(orderIdList.length == 0 || sortResultList == 0){
                                    //该产品无订单或无人投广告，提交切换产品请求
                                    txtOrderMeeting += "<td style='text-align: center'>无</td>" +
                                        "<td style='text-align: center'>无</td>";
                                }else {
                                    var openUser = productCollator.openUser;
                                    console.log("测试 输出当前开放排位:" + openUser);
                                    if(openUser == 0){
                                        txtOrderMeeting += "<td style='text-align: center'>0</td>" +
                                            "<td style='text-align: center'>无</td>";
                                    }else {
                                        var time = productCollator.time;
                                        var nowTime = new Date();
                                        var timeResult = parseInt((time - nowTime.getTime()) / 1000);
                                        var name = sortResultList[openUser - 1].username;
                                        txtOrderMeeting += "<td style='text-align: center'>" + openUser + "</td>" +
                                            "<td style='text-align: center'>" + name + "</td>" +
                                            "<td style='text-align: center'>" + timeResult + "</td>";
                                    }

                                }
                            }
                        }
                    txtOrderMeeting += "</tr>";
                    });
                    txtOrderMeeting += "</tbody></table>";
                    document.getElementById("divOrderCheck").innerHTML = txtOrderCheck;
                    document.getElementById("divOrderMeeting").innerHTML = txtOrderMeeting;
                }else {

                }

            }
        }
    });

}

function readRunningState() {
    var txt="";
    var nowUserName = $("#nowUserName").val();

    $.ajax({
        type:"POST",
        url:"/getTeachClassInfo/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (json) {
            var subUserInfoList = json.subUserInfoList;
            txt += "<table class='table table-striped table-hover table-bordered' id='editable-sample'>" +
                "<thead>" +
                "<tr>" +
                "<th>用户名</th>" +
                "<th>运营状态</th>" +
                "<th>运营年份</th>" +
                "<th>运营季度</th>" +
                "<th>现金</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            $.each(subUserInfoList,function(n, subUserInfo){
                txt += "<tr>" +
                    "<td>" + subUserInfo.subUserName + "</td>" +
                    "<td>" + subUserInfo.runningState.baseState.state + "</td>" +
                    "<td>" + subUserInfo.runningState.baseState.timeYear + "</td>" +
                    "<td>" + subUserInfo.runningState.baseState.timeQuarter + "</td>" +
                    "<td>" + subUserInfo.runningState.financeState.cashAmount + "</td>" +
                    "</tr>"
            });
            txt += "</tbody>" +
                "</table>"
            document.getElementById("ajaxDiv2").innerHTML = txt;
        }
    });
}

function teachClassReturnOriginal() {
    if (confirm("确认要执行还原操作吗?") == false) {
        return;
    }
    $.ajax({
        type:"GET",
        url:"/teachClassReturnOriginal",
        cache:false,
        dataType:"json",
        success:function (json) {
            console.log("success");
            console.log(json.responseText);
            document.getElementById("ajaxDiv1").innerHTML = getTime() + json.responseText;
        },
        error:function (json) {
            document.getElementById("ajaxDiv1").innerHTML = getTime() + json.responseText;
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
            console.log(json);
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
}

function findMarketAll() {
    var txt="";

    $.ajax({
        type:"GET",
        url:"/findMarketSeriesAll",
        cache:false,
        dataType:"json",
        success:function (json) {
            txt="<table class='table table-striped table-hover table-bordered' id='editable-sample'>" +
                "<thead>" +
                "<tr>" +
                "<th>SeriesID</th>" +
                "<th>市场名称</th>" +
                "<th>上传者</th>" +
                "<th>上传日期</th>" +
                "<th>调用次数</th>" +
                "<th>查看内容</th>" +
                "<th>调用规则</th>" +
                "<th>删除规则</th>" +
                "<th style='display: none'>当前操作用户</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            for(var i=0; i<eval(json).length; i++ ){
                txt = txt + "<tr>" +
                    "<td>" + json[i].marketSeriesId + "</td>" +
                    "<td>" + json[i].marketSeriesName + "</td>" +
                    "<td>" + json[i].marketSeriesUploader + "</td>" +
                    "<td>" + timeFormat(json[i].marketSeriesAlterTime) + "</td>" +
                    "<td>" + json[i].marketSeriesUseCount + "</td>" +
                    "<td><a class='edit' href='#modalReadMarket' data-toggle='modal' onclick='readMarket(" + json[i].marketSeriesId + ")'>查看详细订单</a></td>" +
                    "<td><a onclick='useMarket(" + json[i].marketSeriesId + ")'>调用市场</a></td>" +
                    "<td><a onclick='deleteMarket(" + json[i].marketSeriesId + ")'>删除市场</a></td>" +
                    "<td style='display: none'>当前操作用户</td>" +
                    "</tr>";
            }
            txt = txt + "</tbody></table>";
            document.getElementById("ajaxDiv2").innerHTML = txt;
            document.getElementById("ajaxDiv1").innerHTML = "成功查询市场列表";
        }
    });
}

function addMarket() {
    var formData = new FormData($("#marketAddForm")[0]);
    $.ajax({
        type: "POST",
        url:"addMarket",
        data:formData,
        async: true,
        cache: false,
        contentType: false,
        processData: false,
        error: function(request) {
            alert("上传错误");
        },
        success: function(data) {
            document.getElementById("ajaxDiv1").innerHTML = data;
            $("#btnCloseAddMarket").click();
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
        }
    });

}

function useRule(ruleId) {

}

function deleteRule(ruleId) {

}

function addRuleStep(step) {
    //公共变量
    var date = new Date();
    var operatorTime = date.toLocaleDateString() +" " +  date.toLocaleTimeString();

    //根据step选择不同方法提交数据
    switch (step){
        case 1:
            var ruleFactory = {
                factory1Name:$("#factory1Name").val(),
                factory1BuyPrice:$("#factory1BuyPrice").val(),
                factory1RentPrice:$("#factory1RentPrice").val(),
                factory1SalePrice:$("#factory1SalePrice").val(),
                factory1Volume:$("#factory1Volume").val(),
                factory1Limit:$("#factory1Limit").val(),
                factory1Score:$("#factory1Score").val(),
                factory2Name:$("#factory2Name").val(),
                factory2BuyPrice:$("#factory2BuyPrice").val(),
                factory2RentPrice:$("#factory2RentPrice").val(),
                factory2SalePrice:$("#factory2SalePrice").val(),
                factory2Volume:$("#factory2Volume").val(),
                factory2Limit:$("#factory2Limit").val(),
                factory2Score:$("#factory2Score").val(),
                factory3Name:$("#factory3Name").val(),
                factory3BuyPrice:$("#factory3BuyPrice").val(),
                factory3RentPrice:$("#factory3RentPrice").val(),
                factory3SalePrice:$("#factory3SalePrice").val(),
                factory3Volume:$("#factory3Volume").val(),
                factory3Limit:$("#factory3Limit").val(),
                factory3Score:$("#factory3Score").val(),
                factory4Name:$("#factory4Name").val(),
                factory4BuyPrice:$("#factory4BuyPrice").val(),
                factory4RentPrice:$("#factory4RentPrice").val(),
                factory4SalePrice:$("#factory4SalePrice").val(),
                factory4Volume:$("#factory4Volume").val(),
                factory4Limit:$("#factory4Limit").val(),
                factory4Score:$("#factory4Score").val(),
                factory5Name:$("#factory5Name").val(),
                factory5BuyPrice:$("#factory5BuyPrice").val(),
                factory5RentPrice:$("#factory5RentPrice").val(),
                factory5SalePrice:$("#factory5SalePrice").val(),
                factory5Volume:$("#factory5Volume").val(),
                factory5Limit:$("#factory5Limit").val(),
                factory5Score:$("#factory5Score").val()
            };
            $.ajax({
                url:'addRuleFactory',
                type:'POST',
                data:ruleFactory,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 厂房规则");
                    $("#addRuleFactoryResult").html("<h4 style='color: blue'>厂房规则上传成功,请继续下一步</h4>");
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 厂房规则");
                }
            });
            break;
        case 2:
            var ruleIso = {
                iso1Name:$("#iso1Name").val(),
                iso1UnitInvest:$("#iso1UnitInvest").val(),
                iso1DevTime:$("#iso1DevTime").val(),
                iso1Score:$("#iso1Score").val(),
                iso2Name:$("#iso2Name").val(),
                iso2UnitInvest:$("#iso2UnitInvest").val(),
                iso2DevTime:$("#iso2DevTime").val(),
                iso2Score:$("#iso2Score").val()
            };
            $.ajax({
                url:'addRuleIso',
                type:'POST',
                data:ruleIso,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 资质认证规则");
                    $("#addRuleIsoResult").html("<h4 style='color: blue'>资质认证规则上传成功,请继续下一步</h4>");
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 资质认证规则");
                }
            });
            break;
        case 3:
            var ruleLine = {
                line1Name:$("#line1Name").val(),
                line1UnitInvest:$("#line1UnitInvest").val(),
                line1InstallTime:$("#line1InstallTime").val(),
                line1ProduceTime:$("#line1ProduceTime").val(),
                line1ChangeInvest:$("#line1ChangeInvest").val(),
                line1ChangeTime:$("#line1ChangeTime").val(),
                line1Upkeep:$("#line1Upkeep").val(),
                line1ScrapValue:$("#line1ScrapValue").val(),
                line1Depreciation:$("#line1Depreciation").val(),
                line1DepreTime:$("#line1DepreTime").val(),
                line1Score:$("#line1Score").val(),
                line2Name:$("#line2Name").val(),
                line2UnitInvest:$("#line2UnitInvest").val(),
                line2InstallTime:$("#line2InstallTime").val(),
                line2ProduceTime:$("#line2ProduceTime").val(),
                line2ChangeInvest:$("#line2ChangeInvest").val(),
                line2ChangeTime:$("#line2ChangeTime").val(),
                line2Upkeep:$("#line2Upkeep").val(),
                line2ScrapValue:$("#line2ScrapValue").val(),
                line2Depreciation:$("#line2Depreciation").val(),
                line2DepreTime:$("#line2DepreTime").val(),
                line2Score:$("#line2Score").val(),
                line3Name:$("#line3Name").val(),
                line3UnitInvest:$("#line3UnitInvest").val(),
                line3InstallTime:$("#line3InstallTime").val(),
                line3ProduceTime:$("#line3ProduceTime").val(),
                line3ChangeInvest:$("#line3ChangeInvest").val(),
                line3ChangeTime:$("#line3ChangeTime").val(),
                line3Upkeep:$("#line3Upkeep").val(),
                line3ScrapValue:$("#line3ScrapValue").val(),
                line3Depreciation:$("#line3Depreciation").val(),
                line3DepreTime:$("#line3DepreTime").val(),
                line3Score:$("#line3Score").val(),
                line4Name:$("#line4Name").val(),
                line4UnitInvest:$("#line4UnitInvest").val(),
                line4InstallTime:$("#line4InstallTime").val(),
                line4ProduceTime:$("#line4ProduceTime").val(),
                line4ChangeInvest:$("#line4ChangeInvest").val(),
                line4ChangeTime:$("#line4ChangeTime").val(),
                line4Upkeep:$("#line4Upkeep").val(),
                line4ScrapValue:$("#line4ScrapValue").val(),
                line4Depreciation:$("#line4Depreciation").val(),
                line4DepreTime:$("#line4DepreTime").val(),
                line4Score:$("#line4Score").val(),
                line5Name:$("#line5Name").val(),
                line5UnitInvest:$("#line5UnitInvest").val(),
                line5InstallTime:$("#line5InstallTime").val(),
                line5ProduceTime:$("#line5ProduceTime").val(),
                line5ChangeInvest:$("#line5ChangeInvest").val(),
                line5ChangeTime:$("#line5ChangeTime").val(),
                line5Upkeep:$("#line5Upkeep").val(),
                line5ScrapValue:$("#line5ScrapValue").val(),
                line5Depreciation:$("#line5Depreciation").val(),
                line5DepreTime:$("#line5DepreTime").val(),
                line5Score:$("#line5Score").val()
            };
            $.ajax({
                url:'addRuleLine',
                type:'POST',
                data:ruleLine,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 生产线规则");
                    $("#addRuleLineResult").html("<h4 style='color: blue'>生产线规则上传成功,请继续下一步</h4>");
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 生产线规则");
                }
            });
            break;
        case 4:
            var ruleMarket = {
                market1Name:$("#market1Name").val(),
                market1UnitInvest:$("#market1UnitInvest").val(),
                market1DevTime:$("#market1DevTime").val(),
                market1Score:$("#market1Score").val(),
                market2Name:$("#market2Name").val(),
                market2UnitInvest:$("#market2UnitInvest").val(),
                market2DevTime:$("#market2DevTime").val(),
                market2Score:$("#market2Score").val(),
                market3Name:$("#market3Name").val(),
                market3UnitInvest:$("#market3UnitInvest").val(),
                market3DevTime:$("#market3DevTime").val(),
                market3Score:$("#market3Score").val(),
                market4Name:$("#market4Name").val(),
                market4UnitInvest:$("#market4UnitInvest").val(),
                market4DevTime:$("#market4DevTime").val(),
                market4Score:$("#market4Score").val(),
                market5Name:$("#market5Name").val(),
                market5UnitInvest:$("#market5UnitInvest").val(),
                market5DevTime:$("#market5DevTime").val(),
                market5Score:$("#market5Score").val()
            };
            $.ajax({
                url:'addRuleMarket',
                type:'POST',
                data:ruleMarket,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 市场开发规则");
                    $("#addRuleMarketResult").html("<h4 style='color: blue'>市场开发规则上传成功,请继续下一步</h4>");
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 市场开发规则");
                }
            });
            break;
        case 5:
            var ruleMaterial = {
                material1Name:$("#material1Name").val(),
                material1Price:$("#material1Price").val(),
                material1Time:$("#material1Time").val(),
                material2Name:$("#material2Name").val(),
                material2Price:$("#material2Price").val(),
                material2Time:$("#material2Time").val(),
                material3Name:$("#material3Name").val(),
                material3Price:$("#material3Price").val(),
                material3Time:$("#material3Time").val(),
                material4Name:$("#material4Name").val(),
                material4Price:$("#material4Price").val(),
                material4Time:$("#material4Time").val(),
                material5Name:$("#material5Name").val(),
                material5Price:$("#material5Price").val(),
                material5Time:$("#material5Time").val()
            };
            $.ajax({
                url:'addRuleMaterial',
                type:'POST',
                data:ruleMaterial,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 原料规则");
                    $("#addRuleMaterialResult").html("<h4 style='color: blue'>原料规则上传成功,请继续下一步</h4>");
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 原料规则");
                }
            });
            break;
        case 6:
            var ruleProductMix = {
                product1MixR1:$("#product1MixR1").val(),
                product1MixR2:$("#product1MixR2").val(),
                product1MixR3:$("#product1MixR3").val(),
                product1MixR4:$("#product1MixR4").val(),
                product1MixR5:$("#product1MixR5").val(),
                product1MixP1:$("#product1MixP1").val(),
                product1MixP2:$("#product1MixP2").val(),
                product1MixP3:$("#product1MixP3").val(),
                product1MixP4:$("#product1MixP4").val(),
                product1MixP5:$("#product1MixP5").val(),
                product2MixR1:$("#product2MixR1").val(),
                product2MixR2:$("#product2MixR2").val(),
                product2MixR3:$("#product2MixR3").val(),
                product2MixR4:$("#product2MixR4").val(),
                product2MixR5:$("#product2MixR5").val(),
                product2MixP1:$("#product2MixP1").val(),
                product2MixP2:$("#product2MixP2").val(),
                product2MixP3:$("#product2MixP3").val(),
                product2MixP4:$("#product2MixP4").val(),
                product2MixP5:$("#product2MixP5").val(),
                product3MixR1:$("#product3MixR1").val(),
                product3MixR2:$("#product3MixR2").val(),
                product3MixR3:$("#product3MixR3").val(),
                product3MixR4:$("#product3MixR4").val(),
                product3MixR5:$("#product3MixR5").val(),
                product3MixP1:$("#product3MixP1").val(),
                product3MixP2:$("#product3MixP2").val(),
                product3MixP3:$("#product3MixP3").val(),
                product3MixP4:$("#product3MixP4").val(),
                product3MixP5:$("#product3MixP5").val(),
                product4MixR1:$("#product4MixR1").val(),
                product4MixR2:$("#product4MixR2").val(),
                product4MixR3:$("#product4MixR3").val(),
                product4MixR4:$("#product4MixR4").val(),
                product4MixR5:$("#product4MixR5").val(),
                product4MixP1:$("#product4MixP1").val(),
                product4MixP2:$("#product4MixP2").val(),
                product4MixP3:$("#product4MixP3").val(),
                product4MixP4:$("#product4MixP4").val(),
                product4MixP5:$("#product4MixP5").val(),
                product5MixR1:$("#product5MixR1").val(),
                product5MixR2:$("#product5MixR2").val(),
                product5MixR3:$("#product5MixR3").val(),
                product5MixR4:$("#product5MixR4").val(),
                product5MixR5:$("#product5MixR5").val(),
                product5MixP1:$("#product5MixP1").val(),
                product5MixP2:$("#product5MixP2").val(),
                product5MixP3:$("#product5MixP3").val(),
                product5MixP4:$("#product5MixP4").val(),
                product5MixP5:$("#product5MixP5").val()
            };
            $.ajax({
                url:'addRuleProductMix',
                type:'POST',
                data:ruleProductMix,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 产品构成规则");
                    $("#addRuleProductMixResult").html("<h4 style='color: blue'>产品构成规则上传成功,请继续下一步</h4>");
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 产品构成规则");
                }
            });
            break;
        case 7:
            var ruleProduct = {
                product1Name:$("#product1Name").val(),
                product1ProcCost:$("#product1ProcCost").val(),
                product1DevInvest:$("#product1DevInvest").val(),
                product1DevTime:$("#product1DevTime").val(),
                product1FinalCost:$("#product1FinalCost").val(),
                product1Score:$("#product1Score").val(),
                product2Name:$("#product2Name").val(),
                product2ProcCost:$("#product2ProcCost").val(),
                product2DevInvest:$("#product2DevInvest").val(),
                product2DevTime:$("#product2DevTime").val(),
                product2FinalCost:$("#product2FinalCost").val(),
                product2Score:$("#product2Score").val(),
                product3Name:$("#product3Name").val(),
                product3ProcCost:$("#product3ProcCost").val(),
                product3DevInvest:$("#product3DevInvest").val(),
                product3DevTime:$("#product3DevTime").val(),
                product3FinalCost:$("#product3FinalCost").val(),
                product3Score:$("#product3Score").val(),
                product4Name:$("#product4Name").val(),
                product4ProcCost:$("#product4ProcCost").val(),
                product4DevInvest:$("#product4DevInvest").val(),
                product4DevTime:$("#product4DevTime").val(),
                product4FinalCost:$("#product4FinalCost").val(),
                product4Score:$("#product4Score").val(),
                product5Name:$("#product5Name").val(),
                product5ProcCost:$("#product5ProcCost").val(),
                product5DevInvest:$("#product5DevInvest").val(),
                product5DevTime:$("#product5DevTime").val(),
                product5FinalCost:$("#product5FinalCost").val(),
                product5Score:$("#product5Score").val()
            };
            $.ajax({
                url:'addRuleProduct',
                type:'POST',
                data:ruleProduct,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 产品规则");
                    $("#addRuleProductResult").html("<h4 style='color: blue'>产品规则上传成功,请继续下一步</h4>");
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 产品规则");
                }
            });
            break;
        case 8:
            var ruleParam = {
                paramPenatly:$("#paramPenatly").val(),
                paramLoanRatio:$("#paramLoanRatio").val(),
                paramProductSaleRatio:$("#paramProductSaleRatio").val(),
                paramMaterailSaleRatio:$("#paramMaterailSaleRatio").val(),
                paramLongTermLoanRates:$("#paramLongTermLoanRates").val(),
                paramShortTermLoanRates:$("#paramShortTermLoanRates").val(),
                paramShortTermDiscountRates:$("#paramShortTermDiscountRates").val(),
                paramLongTermDiscountRates:$("#paramLongTermDiscountRates").val(),
                paramInitialCash:$("#paramInitialCash").val(),
                paramManagementCost:$("#paramManagementCost").val(),
                paramInfomationCost:$("#paramInfomationCost").val(),
                paramTaxRate:$("#paramTaxRate").val(),
                paramLongTermLoanTimeLimit:$("#paramLongTermLoanTimeLimit").val(),
                paramAdvertisingMinFee:$("#paramAdvertisingMinFee").val(),
                paramProductBuyRation:$("#paramProductBuyRation").val(),
                paramMaterailBuyRation:$("#paramMaterailBuyRation").val(),
                paramSelectOrderTime:$("#paramSelectOrderTime").val(),
                paramFirstSelectOrderTime:$("#paramFirstSelectOrderTime").val(),
                paramMarketSametimeOpenNum:$("#paramMarketSametimeOpenNum").val(),
                paramBidTime:$("#paramBidTime").val(),
                paramBidSametimeNum:$("#paramBidSametimeNum").val(),
                paramFactoryMaxNum:$("#paramFactoryMaxNum").val(),
                paramHaveMarketLeader:$('input:radio[name="paramHaveMarketLeader"]:checked').val(),
                paramDiscountMode:$('input:radio[name="paramDiscountMode"]:checked').val(),
                paramAllowUserReturnSeason:$('input:radio[name="paramAllowUserReturnSeason"]:checked').val(),
                paramAllowUserReturnYear:$('input:radio[name="paramAllowUserReturnYear"]:checked').val(),
            };
            $.ajax({
                url:'addRuleParam',
                type:'POST',
                data:ruleParam,
                dataType:'json',
                success:function (json) {
                    $("#ajaxDiv1").html(operatorTime + " : 上传成功 规则 系列号：" + json.ruleId);
                    $("#addRuleParamResult").html("<h4 style='color: blue'>全部规则已上传成功，如需继续上传请刷新页面</h4>");
                    $("#divAddRule").hide();
                },
                error:function () {
                    $("#ajaxDiv1").html(operatorTime + " : 上传失败 运行参数");
                }
            });
            break;
        default:
            $("#ajaxDiv1").html(operatorTime + " : 系统运行错误");
            break;
    }
}

function findRuleAll() {
    var txt="";

    $.ajax({
        type:"GET",
        url:"/ruleFindAll",
        cache:false,
        dataType:"json",
        success:function (json) {
            txt="<table class='table table-striped table-hover table-bordered' id='editable-sample'>" +
                "<thead>" +
                "<tr>" +
                "<th>规则ID</th>" +
                "<th>上传者</th>" +
                "<th>上传日期</th>" +
                "<th>调用次数</th>" +
                "<th>查看内容</th>" +
                "<th>调用规则</th>" +
                "<th>删除规则</th>" +
                "<th style='display: none'>当前操作用户</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            for(var i=0; i<eval(json).length; i++ ){
//                    var time = json[i].ruleAlterTime;
//                    alert(time.substring(10,0));
                txt = txt + "<tr>" +
                    "<td>" + json[i].ruleId + "</td>" +
                    "<td>" + json[i].ruleUploader + "</td>" +
                    "<td>" + timeFormat(json[i].ruleAlterTime) + "</td>" +
                    "<td>" + json[i].ruleUserCount + "</td>" +
                    "<td><a class='edit' href='#modalReadRule' data-toggle='modal' onclick='readRule(" + json[i].ruleId + ")'>查看内容</a></td>" +
                    "<td><a onclick='useRule(" + json[i].ruleId + ")'>调用规则</a></td>" +
                    "<td><a onclick='deleteRule(" + json[i].ruleId + ")'>删除规则</a></td>" +
                    "<td style='display: none'>当前操作用户</td>" +
                    "</tr>";
            }
            txt = txt + "</tbody></table>";
            document.getElementById("ajaxDiv2").innerHTML = txt;
            document.getElementById("ajaxDiv1").innerHTML = "成功查询规则列表";
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

function deleteUser(id) {
    var operatorName;
    var operatorLevel;
    if (confirm("确定要删除这个用户吗?") == false) {
        return;
    }
    $.ajax({
        type:"GET",
        url:"getUserInfo",
        cache:false,
        dataType:"json",
        success:function (json) {
            operatorName = json[0].username;
            operatorLevel = json[0].userLevel;
            $.ajax({
                type:"DELETE",
                url:"userDelete/" + id + "/" + operatorName,
                cache:false,
//                        dataType:"json",
                success:function () {
                    document.getElementById("ajaxDiv1").innerHTML = "成功删除ID：" + id + "用户";
                }
//                        error:function () {
//                            document.getElementById("ajaxDiv1").innerHTML = "未能删除ID：" + id + "用户";
//                        }
            });
        }
    });
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
                            document.getElementById("ajaxDiv1").innerHTML = getTime() + "成功修改密码";
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
                    "<th style='text-align: center'>用户ID</th>" +
                    "<th style='text-align: center'>用户名</th>" +
                    "<th style='text-align: center'>密码</th>" +
                    "<th style='text-align: center'>用户等级</th>" +
                    "<th style='text-align: center'>操作者</th>" +
                    "<th style='display: none'>当前操作用户</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody>";
                for(var i=0; i<eval(json).length; i++ ){
                    if(json[i].userOperator == nowUserName){
                        txt = txt + "<tr>" +
                            "<td style='text-align: center'>" + json[i].id + "</td>" +
                            "<td style='text-align: center'>" + json[i].username + "</td>" +
                            "<td style='text-align: center'>" + json[i].password + "</td>" +
                            "<td style='text-align: center'>" + json[i].userLevel + "</td>" +
                            "<td style='text-align: center'>" + json[i].userOperator + "</td>" +
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
            $("#btnCloseModalThClassInit").click();
        }
    });
}
