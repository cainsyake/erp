window.onload=thcOnload;
function thcOnload() {
    var nowUserName = $("#nowUserName").val();
    $.ajax({
        type:"POST",
        url:"/getTeachClassInfo/" + nowUserName,
        cache:false,
        dataType:"json",
        success:function (thc) {
            if (thc.orderMeetingState == null){
                thc.orderMeetingState = 0;
            }
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
        rule.name = $("#ruleName").val();
        rule.isDeleted = 0;
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
                $("#ruleId").val(rs.msg);   //设置规则ID至页面
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
        $('#quantityData').attr("factory-quantity", $("#factoryQuantity").val());
        $('#quantityData').attr("line-quantity", $("#lineQuantity").val());
        $('#quantityData').attr("qualification-quantity", $("#qualificationQuantity").val());
        $('#quantityData').attr("area-quantity", $("#areaQuantity").val());
        $('#quantityData').attr("material-quantity", $("#materialQuantity").val());
        $('#quantityData').attr("product-quantity", $("#productQuantity").val());

        $("#initAddRuleResult").html("<h4 style='color: blue'>已载入规则,请开始修改。</h4>");
    }
}

function initAllAddRuleArea() {
    initAddFactoryArea();
    initAddLineArea();
    initAddQualificationArea();
    initAddAreaArea();
    initAddMaterialArea();
    initAddProductBomArea();
    initAddProductArea();
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
    var ruleId = $("#ruleId").val();
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
    var ajaxData = {factories: factories, username: username, ruleId: ruleId};
    $.ajax({
        url: '/addRuleFactory',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            // $("#ajaxDiv1").html("成功初始化规则,ID：" + rs.msg);
            $("#addRuleFactoryResult").html("<h4 style='color: blue'>上传厂房规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html("上传厂房规则失败");
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

function addRuleLine() {
    var username = $("#nowUserName").val();
    var ruleId = $("#ruleId").val();
    var lines = [];
    for (var i = 1; i <= $('#quantityData').attr('line-quantity'); i++){
        var line = new Object();
        line.type = i;
        line.name = $('#lname' + i).val();
        line.unitInvest = $('#lunitInvest' + i).val();
        line.installTime = $('#linstallTime' + i).val();
        line.changeInvest = $('#lchangeInvest' + i).val();
        line.changeTime = $('#lchangeTime' + i).val();
        line.produceTime = $('#lproduceTime' + i).val();
        line.upkeep = $('#lupkeep' + i).val();
        line.scrapValue = $('#lscrapValue' + i).val();
        line.depreciation = $('#ldepreciation' + i).val();
        line.depreTime = $('#ldepreTime' + i).val();
        line.score = $('#lscore' + i).val();
        lines.push(line);
    }
    var ajaxData = {lines: lines, username: username, ruleId: ruleId};
    $.ajax({
        url: '/addRuleLine',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            $("#addRuleLineResult").html("<h4 style='color: blue'>上传生产线规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html("上传生产线规则失败");
            console.log(rs);
        }
    });
}

function initAddQualificationArea(){
    for (var i = 1; i <= $('#quantityData').attr('qualification-quantity'); i++) {
        var content = "<div class='form-group'>" +
            "<table class='table table-striped table-hover table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th colspan='4' style='text-align: center'>第" + i + "组资质认证规则</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "<tr>" +
            "<th style='text-align: center'>资质名称</th>" +
            "<td><input class='form-control' type='text' id='qname" + i + "'/></td>" +
            "<th style='text-align: center'>潜力分数</th>" +
            "<td><input class='form-control' type='number' id='qscore" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>每期研发投资</th>" +
            "<td><input class='form-control' type='number' id='qunitInvest" + i + "'/></td>" +
            "<th style='text-align: center'>研发周期</th>" +
            "<td><input class='form-control' type='number' id='qdevTime" + i + "'/></td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</div>";
        $('#addQualificationArea').append(content);
    }
}

function addRuleQualification() {
    var username = $("#nowUserName").val();
    var ruleId = $("#ruleId").val();
    var qualifications = [];
    for (var i = 1; i <= $('#quantityData').attr('qualification-quantity'); i++){
        var qualification = new Object();
        qualification.type = i;
        qualification.name = $('#qname' + i).val();
        qualification.unitInvest = $('#qunitInvest' + i).val();
        qualification.devTime = $('#qdevTime' + i).val();
        qualification.score = $('#qscore' + i).val();
        qualifications.push(qualification);
    }
    var ajaxData = {qualifications: qualifications, username: username, ruleId: ruleId};
    $.ajax({
        url: '/addRuleQualification',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            $("#addRuleQualificationResult").html("<h4 style='color: blue'>上传资质认证规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html("上传生产线规则失败");
            console.log(rs);
        }
    });
}

function initAddAreaArea() {
    for (var i = 1; i <= $('#quantityData').attr('area-quantity'); i++) {
        var content = "<div class='form-group'>" +
            "<table class='table table-striped table-hover table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th colspan='4' style='text-align: center'>第" + i + "组区域规则</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "<tr>" +
            "<th style='text-align: center'>区域名称</th>" +
            "<td><input class='form-control' type='text' id='aname" + i + "'/></td>" +
            "<th style='text-align: center'>潜力分数</th>" +
            "<td><input class='form-control' type='number' id='ascore" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>每期研发投资</th>" +
            "<td><input class='form-control' type='number' id='aunitInvest" + i + "'/></td>" +
            "<th style='text-align: center'>研发周期</th>" +
            "<td><input class='form-control' type='number' id='adevTime" + i + "'/></td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</div>";
        $('#addAreaArea').append(content);
    }
}

function addRuleArea() {
    var username = $("#nowUserName").val();
    var ruleId = $("#ruleId").val();
    var areas = [];
    for (var i = 1; i <= $('#quantityData').attr('area-quantity'); i++){
        var area = new Object();
        area.type = i;
        area.name = $('#aname' + i).val();
        area.unitInvest = $('#aunitInvest' + i).val();
        area.devTime = $('#adevTime' + i).val();
        area.score = $('#ascore' + i).val();
        areas.push(area);
    }
    var ajaxData = {areas: areas, username: username, ruleId: ruleId};
    $.ajax({
        url: '/addRuleArea',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            $("#addRuleAreaResult").html("<h4 style='color: blue'>上传区域规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html("上传区域规则失败");
            console.log(rs);
        }
    });
}

function initAddMaterialArea() {
    for (var i = 1; i <= $('#quantityData').attr('material-quantity'); i++) {
        var content = "<div class='form-group'>" +
            "<table class='table table-striped table-hover table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th colspan='2' style='text-align: center'>第" + i + "组原料规则</th>" +
            "<th style='text-align: center'>原料名称</th>" +
            "<td><input class='form-control' type='text' id='mname" + i + "'/></td>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "<tr>" +
            "<th style='text-align: center'>原料价格</th>" +
            "<td><input class='form-control' type='number' id='mprice" + i + "'/></td>" +
            "<th style='text-align: center'>采购提前期</th>" +
            "<td><input class='form-control' type='number' id='mtime" + i + "'/></td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</div>";
        $('#addMaterialArea').append(content);
    }
}

function addRuleMaterial() {
    var username = $("#nowUserName").val();
    var ruleId = $("#ruleId").val();
    var materials = [];
    for (var i = 1; i <= $('#quantityData').attr('material-quantity'); i++){
        var material = new Object();
        material.type = i;
        material.name = $('#mname' + i).val();
        material.price = $('#mprice' + i).val();
        material.time = $('#mtime' + i).val();
        materials.push(material);
    }
    var ajaxData = {materials: materials, username: username, ruleId: ruleId};
    $.ajax({
        url: '/addRuleMaterial',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            $("#addRuleMaterialResult").html("<h4 style='color: blue'>上传原料规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html("上传原料规则失败");
            console.log(rs);
        }
    });
}

function initAddProductBomArea() {
    var materialQuantity = $('#quantityData').attr('material-quantity');
    var productQuantity = $('#quantityData').attr('product-quantity');
    for (var i = 1; i <= productQuantity; i++) {
        var content = "<div class='form-group'>" +
            "<table class='table table-striped table-hover table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th colspan='2' style='text-align: center'>第" + i + "组产品BOM规则</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>";
        for (var j = 1; j <= materialQuantity; j++){
            content += "<tr>" +
            "<th style='text-align: center'>原料" + j + "的数量</th>" +
            "<td><input class='form-control' type='number' id='bom" + i + "material" + j + "' value='0'/></td>" +
            "</tr>";
        }
        for (var j = 1; j <= productQuantity; j++){
            content += "<tr>" +
                "<th style='text-align: center'>半成品" + j + "的数量</th>" +
                "<td><input class='form-control' type='number' id='bom" + i + "product" + j + "' value='0'/></td>" +
                "</tr>";
        }
        content += "</tbody>" +
            "</table>" +
            "</div>";

        $('#addProductBomArea').append(content);
    }
}

function addRuleProductBom() {
    var materialQuantity = $('#quantityData').attr('material-quantity');
    var productQuantity = $('#quantityData').attr('product-quantity');
    var username = $("#nowUserName").val();
    var ruleId = $("#ruleId").val();
    var productBoms = [];
    for (var i = 1; i <= productQuantity; i++){
        var productBom = new Object();
        productBom.type = i;
        productBom.materialBomList = [];
        productBom.productBomList = [];
        for (var j = 1; j <= materialQuantity; j++){
            var ruleBomValue = new Object();
            var valueId = '#bom' + i + 'material' + j;
            ruleBomValue.value = $(valueId).val();
            productBom.materialBomList.push(ruleBomValue);
        }
        for (var j = 1; j <= productQuantity; j++){
            var ruleBomValue = new Object();
            var valueId = '#bom' + i + 'product' + j;
            ruleBomValue.value = $(valueId).val();
            productBom.productBomList.push(ruleBomValue);
        }
        productBoms.push(productBom);
    }
    var ajaxData = {productBoms: productBoms, username: username, ruleId: ruleId};
    $.ajax({
        url: '/addRuleProductBom',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            $("#addRuleProductBomResult").html("<h4 style='color: blue'>上传产品BOM规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html("上传产品BOM规则失败");
            console.log(rs);
        }
    });
}

function initAddProductArea() {
    for (var i = 1; i <= $('#quantityData').attr('product-quantity'); i++) {
        var content = "<div class='form-group'>" +
            "<table class='table table-striped table-hover table-bordered'>" +
            "<thead>" +
            "<tr>" +
            "<th colspan='4' style='text-align: center'>第" + i + "组产品规则</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>" +
            "<tr>" +
            "<th style='text-align: center'>产品名称</th>" +
            "<td><input class='form-control' type='text' id='pname" + i + "'/></td>" +
            "<th style='text-align: center'>加工费</th>" +
            "<td><input class='form-control' type='number' id='pprocCost" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>每周期研发费用</th>" +
            "<td><input class='form-control' type='text' id='pdevInvest" + i + "'/></td>" +
            "<th style='text-align: center'>研发周期</th>" +
            "<td><input class='form-control' type='number' id='pdevTime" + i + "'/></td>" +
            "</tr>" +
            "<tr>" +
            "<th style='text-align: center'>直接成本</th>" +
            "<td><input class='form-control' type='text' id='pfinalCost" + i + "'/></td>" +
            "<th style='text-align: center'>潜力分数</th>" +
            "<td><input class='form-control' type='number' id='pscore" + i + "'/></td>" +
            "</tr>" +
            "</tbody>" +
            "</table>" +
            "</div>";
        $('#addProductArea').append(content);
    }
}

function addRuleProduct() {
    var username = $("#nowUserName").val();
    var ruleId = $("#ruleId").val();
    var products = [];
    for (var i = 1; i <= $('#quantityData').attr('product-quantity'); i++){
        var product = new Object();
        product.type = i;
        product.name = $('#pname' + i).val();
        product.procCost = $('#pprocCost' + i).val();
        product.devInvest = $('#pdevInvest' + i).val();
        product.devTime = $('#pdevTime' + i).val();
        product.finalCost = $('#pfinalCost' + i).val();
        product.score = $('#pscore' + i).val();
        products.push(product);
    }
    var ajaxData = {products: products, username: username, ruleId: ruleId};
    $.ajax({
        url: '/addRuleProduct',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(ajaxData),
        dataType:'json',
        success:function (rs) {
            $("#addRuleProductResult").html("<h4 style='color: blue'>上传产品规则成功,请继续下一步</h4>");
        },
        error:function (rs) {
            $("#ajaxDiv1").html("上传产品规则失败");
            console.log(rs);
        }
    });
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
            // console.log(json);
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
            var rule = json;
            txt1 = "<div class='panel panel-warning'>" +
                "<div class='panel-heading'>" +
                "<h3 class='panel-title'>规则ID：" +  json.id + " &nbsp;&nbsp;上传者：" + json.ruleUploader + "</h3>" +
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
            var factoryQuantity = rule.factoryQuantity;
            for (var i = 1; i <= factoryQuantity; i++){
                var factory = getFactoryInfo(rule, i);
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
                var line = getLineInfo(rule, i);
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
                var qualification = getQualificationInfo(rule, i);
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
                var area = getAreaInfo(rule, i);
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
                var material = getMaterialInfo(rule, i);
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
                var product = getProductInfo(rule, i);
                var bom = getProductBomInfo(rule, i);
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
                        txt8 += bomValue + "*" + getMaterialInfo(rule, j + 1).name + "&nbsp;&nbsp;";
                    }
                }
                for (var j = 0; j < productQuantity; j++){ //半成品组成
                    var bomValue = productBomList[j].value;
                    if (bomValue > 0){
                        txt8 += bomValue + "*" + getProductInfo(rule, j + 1).name + "&nbsp;&nbsp;";
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
            document.getElementById("divReadRule").innerHTML = txt1 + txt2 + txt3 +txt4 + txt5 + txt6 + txt7 + txt8 + txt9;
            document.getElementById("ajaxDiv1").innerHTML = "正在查看规则（ID：" +  ruleId +"）内容";
        }
    });

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
                "<th style='text-align: center'>规则ID</th>" +
                "<th style='text-align: center'>名称</th>" +
                "<th style='text-align: center'>上传者</th>" +
                "<th style='text-align: center'>上传日期</th>" +
                "<th style='text-align: center'>调用次数</th>" +
                "<th style='text-align: center'>查看内容</th>" +
                "<th style='text-align: center'>删除规则</th>" +
                "<th style='display: none'>当前操作用户</th>" +
                "</tr>" +
                "</thead>" +
                "<tbody>";
            for(var i=0; i<eval(json).length; i++ ){
                txt = txt + "<tr>" +
                    "<td style='text-align: center'>" + json[i].id + "</td>" +
                    "<td style='text-align: center'>" + json[i].name + "</td>" +
                    "<td style='text-align: center'>" + json[i].ruleUploader + "</td>" +
                    "<td style='text-align: center'>" + timeFormat(json[i].ruleAlterTime) + "</td>" +
                    "<td style='text-align: center'>" + json[i].ruleUserCount + "</td>" +
                    "<td style='text-align: center'><a class='edit' href='#modalReadRule' data-toggle='modal' onclick='readRule(" + json[i].id + ")'>查看内容</a></td>" +
                    "<td style='text-align: center'><button class='btn btn-warning' onclick='deleteRule(" + json[i].id + ")'>删除</button></td>" +
                    "<td style='display: none'>当前操作用户</td>" +
                    "</tr>";
            }
            txt = txt + "</tbody></table>";
            $('#ajaxDiv1').html("成功查询规则列表");
            $('#ajaxDiv2').html(txt);
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

/**
 * 安全的删除规则
 * @param id
 */
function deleteRule(id) {
    $.ajax({
        type:"POST",
        url:"/deleteRuleInSafety/" + id,
        cache:false,
        dataType:"json",
        success:function (ur) {
            if (ur.state == '00'){
                alert('成功删除规则：' + id);
                findRuleAll();
            }
        }
    });
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

function getLineInfo(rule, type) {
    var lines = rule.ruleLineList;
    for (var i = 0; i < lines.length; i++){
        if (lines[i].type == type){
            return lines[i];
        }
    }
    return null;    //异常情况
}

function getFactoryInfo(rule, type) {
    var factories = rule.ruleFactoryList;
    for (var i = 0; i < factories.length; i++){
        if (factories[i].type == type){
            return factories[i];
        }
    }
    return null;    //异常情况
}

function getQualificationInfo(rule, type) {
    var qualifications = rule.ruleQualificationList;
    for (var i = 0; i < qualifications.length; i++){
        if (qualifications[i].type == type){
            return qualifications[i];
        }
    }
    return null;    //异常情况
}

function getAreaInfo(rule, type) {
    var areas = rule.ruleAreaList;
    for (var i = 0; i < areas.length; i++){
        if (areas[i].type == type){
            return areas[i];
        }
    }
    return null;    //异常情况
}

function getMaterialInfo(rule, type) {
    var materials = rule.ruleMaterialList;
    for (var i = 0; i < materials.length; i++){
        if (materials[i].type == type){
            return materials[i];
        }
    }
    return null;    //异常情况
}

function getProductInfo(rule, type) {
    var products = rule.ruleProductList;
    for (var i = 0; i < products.length; i++){
        if (products[i].type == type){
            return products[i];
        }
    }
    return null;    //异常情况
}

function getProductBomInfo(rule, type) {
    var productBoms = rule.ruleProductBomList;
    for (var i = 0; i < productBoms.length; i++){
        if (productBoms[i].type == type){
            return productBoms[i];
        }
    }
    return null;    //异常情况
}
