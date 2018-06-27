<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width" charset="utf-8"/>
<link href="/css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
<link type="image/x-icon"   href="/images/favicon.ico"  rel="icon">
<link rel="stylesheet" href="/css/font-awesome.css">
<link rel="stylesheet" href="/css/bootstrapValidator.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/js/bootstrapValidator.js"></script>
<script src="/js/toastr.min.js"></script>
<link href="/css/toastr.css" rel="stylesheet" />
<script src="/loginjs/bootbox.js"></script>
<script src="/js/auto-line-number.js"></script>
<script src="/loginjs/jquery.i18n.properties.js"></script>
<script src="/loginjs/language_detail.js"></script>
<title>学生项目详情</title>
<style type="text/css">
 #footer {
                height: 40px;
                line-height: 40px;
                position: fixed;
                bottom: 0;
                width: 100%;
                text-align: center;
                background: #333;
                color: #fff;
                font-family: Arial;
                font-size: 12px;
                letter-spacing: 1px;
            }
.pageDiv {
                position: fixed;
                bottom: 0;
                width: 33%;
            }            
.mystyle1{
	width:80px;
	border: 1px solid #8DCC4E; 
	text-align:center;
	color:#8DCC4E;
	border-radius:5px;
	float:right;
}
.mystyle2{
	width:80px;
	border: 1px solid #d9534f; 
	text-align:center;
	color:#d9534f;
	border-radius:5px;
	float:right;
}  
.mystyle3{
	width:80px;
	border: 1px solid #4497EC; 
	text-align:center;
	color:#4497EC;
	border-radius:5px;
	float:right;
} 
.mystyle4{
	width:80px;
	border: 1px solid #BFBFBF; 
	text-align:center;
	color:#BFBFBF;
	border-radius:5px;
	float:right;
} 
.mystyle5{
	width:80px;
	border: 1px solid #d9534f; 
	text-align:center;
	background: #d9534f;
	color:#fff;
	border-radius:5px;
	float:right;
}  
tr.focus{
    background-color:#f07860;
} 
textarea {
  width: 100%;
  background-color: #f0f0f0;
  border:none;
  padding: 5px;
  line-height: 23px;
  overflow: auto;
  color: #666;
  font-size: 16px;
  font-family: "微软雅黑"
}
textarea:focus {
	box-shadow: 0 0 1px 1px #AFEEEE;
}
.bg-td{
	background-color:#f0f0f0;
}
.selected {color:#fff;}
 #video::-webkit-media-controls-fullscreen-button {
            display: none;
        }
</style>
<script type="text/javascript">
toastr.options.positionClass = 'toast-top-center';
toastr.options.timeOut = '800';
var pid="";
var pname = "";
var count = 0;
var pagestr = "";
var language = "";
$(function(){
	language=navigator.language;
    language = language.replace("-","_");
    if(language=="zh_CN" || language=="zh_TW"){
    	bootbox.setLocale(language);
    }
    var stumsg=$.i18n.prop('stumsg').replace("#c",$('#classnum').val()).replace("#g",$('#gradenum').val());
    $('#stumsg').text(stumsg);
    var teamsg = $.i18n.prop('teamsg').replace("#n",$('#teaname').val());
    $('#teamsg').text(teamsg);
	$('#last').text($.i18n.prop('last'));
    $('#next').text($.i18n.prop('next'));
    $('#prev').text($.i18n.prop('previous'));
	pagestr = document.getElementById("pageModel").innerHTML; 
	$("#pageModel").empty();
	getList(1);
	var head = $("#head").height();
	var footer = $("#footer").height();
	var table = ($(window).height()-head-footer-100);
	$("#table").height(table);
	$("#codeDiv").height(table);
	$("#video").height((table-15));
	var wid = $("#left").width();
	var val = (wid-170)+"px";
	$("#codeTab").css("margin-left",val);
	$("#video").width(wid*2-10);
	$("#command").width(wid*2-58);
	$("#command").height(table-34);
	initTextArea();
	$("#p_name").text($.i18n.prop('pname'));
    $("#p_date").text($.i18n.prop('utime'));
});

$(window).resize(function () {          //当浏览器大小变化时
	window.location.reload();
});
//初始还command的格式
function initTextArea(){
	$("#command").setTextareaCount({
		width: "30px",
		bgColor: "#668B8B",
		color: "#FFF",
		display: "inline-block",
		height: $("#command").height()
	});
}
//获取项目列表
function getList(offset){
	var seach_pname = $("#pname").val();
	var sno = $("#sno").val();
	$("#pbody").empty();
    $.ajax({
        type: "post",
        url: "/stemachine/projectList.do",
        data: { "pname": seach_pname ,"limit":"10" ,"offset":offset ,"sno":sno},
        success: function (data) {
        	var total = data.total;
            rows = data.rows;
            var trColor = "row";
            $.each(rows, function(i, n) {
            	var tbody="";
            	var status = "";
            	var style="";
            	var date = "";
            	var type=""
            	if(n.status=="1"){
            		status = $.i18n.prop('status5');
            		style = "mystyle4";
            	}else if(n.status=="2"){
            		status = $.i18n.prop('status1');
            		style = "mystyle2";
            		type="fa fa-spinner fa-spin fa-1x";
            	}else if(n.status=="3"){
            		status = $.i18n.prop('status2');
            		style = "mystyle3";
            	}else if(n.status=="4"){
            		status = $.i18n.prop('status3');
            		style = "mystyle1";
            	}else{
            		status = $.i18n.prop('status4');
            		style = "mystyle5";
            		type="fa fa-exclamation-circle";
            	}
            	if(n.submitDate==null){
            		date = $.i18n.prop('unpublishedDialog');
            	}else{
            		date = n.submitDate
            	}
            	tbBody = "<tr id="+n.pid+"><td nowrap><i class='"+type+"' style='color:#d9534f;'></i>&nbsp;&nbsp;<span>"+ n.pname + "</span><div class='"+style+"'>" + status + "</div></td>" + "<td nowrap style='text-align:center;' class='bg-td'>" + date + "</td></tr>";
                $("#pbody").append(tbBody);
            })
            $("#pageContent").empty();
            var str = pagestr.replace('#uppageno',data.page.upPageNo).replace('#nextpageno',data.page.nextPageNo).replace('#totalpage',data.page.totalCount).replace('#curpage',data.page.curentPageNo);
            $("#pageContent").append(str);
            if(data.page.totalCount==data.page.curentPageNo || data.page.totalCount == 0){
		        $('#nextpage').addClass('disabled');
		        $('#nextpage a').removeAttr("href");
		        $('#lastpage').addClass('disabled');
		        $('#lastpage a').removeAttr("href");
	         }
           if(data.page.curentPageNo==1){
        	   $('#uppage').addClass('disabled');
        	   $('#uppage a').removeAttr("href");
           }
        },
        error: function () {
            toastr.error('error');
        },
        complete: function () {
        	 $("#ptable>tbody>tr").on("click", function () {
        		 	var tdarr = $('#ptable>tbody>tr>td:nth-child(2)');
        		 	for(var i=0;i<tdarr.length;i++){
        		 		tdarr[i].className="bg-td";
        		 	}
        		    $(this).children('td').eq(1).removeClass('bg-td');
        	        $(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
        	        $(this).toggleClass("focus");//设定当前行为选中行
        	        pid = this.id;
        	        pname = this.cells[0].getElementsByTagName('span')[0].innerHTML;
        	        var status = this.cells[0].getElementsByTagName('div')[0].innerHTML;
        	        $("#codeDiv").css('display','block');
        			$("#videoDiv").css('display','none');
        			if(status=="尚未发布"||status=="等待回传"){
        	        	$('#result').removeClass('btn-info').addClass('btn-primary');
        	    		$('#result').attr('disabled', true);
        	        }else{
        	        	$('#result').addClass('btn-info');
        	    		$('#result').attr('disabled', false);
        	        }
        	        getCode(this.id);
       	        	$("#rightTitle").text(pname+" "+$.i18n.prop('codeShow'));
       	        	$('#codemaneger').css("color","#fff");
           		 	$('#codedisplay').css("color","");
           		    getVideoPath(pid);
        	    });
        	 var operate = $("#operation").val();
        	 if(""==pid){
        		 if(count != 0 && operate=="0"){//新增项目获取选中状态
        			 $('#ptable>tbody>tr:first>td:nth-child(2)').removeClass('bg-td');
        		     pname = $('#ptable>tbody>tr:first>td:nth-child(1)>span:first').text()
        			 $("#pbody").children("tr").eq(0).attr("class","focus");
            		 pid = document.getElementById("pbody").getElementsByTagName("tr")[0].id;
        		 }
        	 }else{
        		 //修改项目获取选中状态
        		 $("#"+pid+">td:nth-child(2)").removeClass('bg-td');
        		 $("#"+pid+"").attr("class","focus");
        	 }
        	 count = count+1;
        }

    });
}
//切换代码展示和视屏展示
function changeTab(i){
	 /* $("#rightTitle").text(pname+" 代码展示"); */
	 this.focus();
	 if(i==0){
		 $('#codemaneger').css("color","#fff");
		 $('#codedisplay').css("color","");
		 $("#codeDiv").css('display','block');
		 $("#videoDiv").css('display','none');
	 }else{
		 if(pname==""){
			 toastr.warning($.i18n.prop('selectProject'));
			 return;
		 }
		 $('#codemaneger').css("color","");
		 $('#codedisplay').css("color","#fff");
		 $("#codeDiv").css('display','none');
		 $("#videoDiv").css('display','block');
		 $("#rightTitle").text(pname+" "+$.i18n.prop('resultDisply'));
	 }
}
/* //增加或修改代码
function submit(){
	var operation = $("#operation").val();
	var successMsg = "";
	var errorMsg = "";
	if(operation=="1"){
		successMsg = "修改成功";
		errorMsg = "修改失败"
	}else{
		pid = "";
		successMsg = "添加成功";
		errorMsg = "添加失败"
	}
    pname = $("#txt_pname").val();
    
    $.ajax({
        type: "post",
        url: "/stemachine/addOrUpdate.do",
        data: { "pname": pname ,"pid":pid },
        success: function (data) {
        	var status = data.status;
            if (status == 1) {
            	toastr.success(successMsg);
            }
        },
        error: function () {
            toastr.error(errorMsg);
        },
        complete: function () {
        	initTextArea();
        	if(operation == "1"){
        		getCode(pid);
        	}else{
        		$("#command").val('');
            	$('#command').attr("disabled",false);
        	}
        	getList(1);
        }

    });
 }
 */ //获取代码
 function getCode(pid){
	 $("#command").val('');
 	 $('#command').attr("disabled",false);
	 $.ajax({
	        type: "post",
	        url: "/stemachine/getCommand.do",
	        data: { "pid":pid },
	        success: function (data,status) {
	        	if(status=="success"){
	        		initTextArea();
	        		var code = data.command;
	        		$("#command").val(code);
	        	}
	        },
	        error: function () {
	            toastr.error(errorMsg);
	        },
	        complete: function () {
	        	
	        }

	    });
 }
 //代码检查
 function checkCommand(){
	 alert( $("#command").val());
	 $.ajax({  
 		type: "post",
         url: "/stemachine/checkCommand.do",
         data: { "command": $("#command").val() },
         success: function (data) {
         	 alert(data);
             if (data == "success") {
            	 alert("dsa");
            	 $('#send').removeClass('btn-default').addClass('btn-warning');
	             $('#send').attr('disabled', false);
             }else{
            	 alert(data);
             }
         },
         error: function () {
             toastr.error(errorMsg);
         },
         complete: function () {
         	
         }  
	    });
 }
//退出登录
 function logout(role){
	 bootbox.confirm($.i18n.prop('logoutDilog'), function (result) {
        if (result) {
        	$.ajax({  
        		type: "post",
                url: "/logout.action",
                data: { "role": role },
                success: function (data) {
                	window.location.href="/htm/login.html";  
                },
                error: function () {
                    toastr.error(errorMsg);
                },
    	    });
        }
	 })
 }
 function getVideoPath(id){
	 $.ajax({  
	 		type: "post",
	         url: "/stemachine/getVideoPath.do",
	         data: { "pid": id },
	         success: function (data) {
	             $('#video').attr('src', data.url);
	         },
	         error: function () {
	             toastr.error(errorMsg);
	         },
	         complete: function () {
	         	
	         }  
		    });
 }
 function back(){
	 window.location.href="/teacher/toQueryTeacher.taction";
 }
 function searchenter(event) {
    event = event || window.event;
    if (event.keyCode == 13) {
    	$("#btn_query").trigger("click");     
    }
} 
 
 function help(){
		if(language=="zh_CN"){
			window.location.href="/help/onlinehelp_teacher_SC_20180531A.htm";  
		}else if(language=="zh_TW"){
			window.location.href="/help/onlinehelp_teacher_TC_20180531A.htm"; 
		}else{
			window.location.href="/help/onlinehelp_teacher_EN_20180531A.htm";
		}
	}
</script>
</head>
<body>
<div class="container-fluid">
	<nav id="head" class="navbar navbar-inverse navbar-fixed-top" >
       		<div class="navbar-header" style="float:left; margin-left:10px;">
          		<span style="color:#EE2C2C;font-size:36px">STEM</span><span style="color:#fff;font-size:30px;">achine.</span>
       		</div>
			<div id="navbar" class="navbar-collapse collapse" style="margin-right:20px;">
				<ul class="nav navbar-nav navbar-right">			            
					<li><a style="color:#fff;"><i class="fa fa-user"></i>  <span id="teamsg">${teacher_name} 老师</span></a></li>	
					<li><a style="color:#fff;" href="javascript:help();"><i class="fa fa-question-circle"></i><span id="help"> 帮助</span></a></li>
					<li><a style="color:#fff;" href="javascript:logout('t');"><i class="fa fa-sign-out"></i><span id="exit"> 退出</span></a></li>	
				</ul>
     		</div>
   	</nav>
    <input type="hidden" id="classnum" value="${student.cno}" />
    <input type="hidden" id="gradenum" value="${student.grade}" />
    <input type="hidden" id="teaname" value="${teacher_name}" />	
    <div id="content" class="row" style="margin-top:60px;">
    		<div class="row-fluid">
		    	<div class="col-sm-12 col-md-12" style="margin-bottom:8px;"> <span style="font-weight:bold" id="stumsg">${student.grade}年级 ${student.cno}班</span> <i class="fa fa-angle-right"></i> <span style="color:#f07860;font-weight:bold;">${student.sname}</span></div>
		    </div>
			<div class="col-lg-4 col-md-4" id="left">
				<div class="row">
					<div id="table" class="col-lg-12 col-md-12" style="overflow:auto;">
						<div class="input-group" >  
		       			<input type="text" id="pname" class="form-control" onkeyup="searchenter(event);" placeholder="请输入项目名称" />  
		       			<input type="hidden" id="sno" value="${sno}">
		           		<span class="input-group-btn">  
		               		<button class="btn btn-danger btn-search" id="btn_query" onclick="getList(1)">搜索</button>  
		            	</span>
	 					</div> 
	 				<table class="table table-bordered" style="margin-top:5px;" id="ptable">
					  <thead>
					    <tr>
					      <th width="60%" style="text-align:center;" id="p_name">项目名称</th>
					      <th width="40%" style="text-align:center;" class="bg-td" id="p_date">上传时间</th>
					    </tr>
					  </thead>
					  <tbody id="pbody">
					    
					  </tbody>
					</table> 
					</div>
				</div>
					<div class="row">
						<div class="col-lg-12 col-md-12" style="margin-top:-24px;" align="center" id="pageContent">
							
						</div>
					</div>
					<div id="pageModel" style="display:none">
						<ul class="pagination pagination-sm ">
									<li id="uppage"><a href="javascript:getList('#uppageno');">&laquo;<span id="prev">上一页</span></a></li>
									<li class="active" id="curpage"><a href="#">#curpage</a></li>
									<li id="nextpage"><a href="javascript:getList('#nextpageno');"><span id="next">下一页</span>&raquo;</a></li>
									<li id="lastpage"><a href="javascript:getList('#totalpage');"><span id="last">最后一页</span>&raquo;</a></li>
						</ul>
					</div>
			</div>
			
			<!-- 右侧内容展示==================================================   -->   		
			<div class="col-lg-8 col-md-8 main" id="right" >
			<div class="row">
				<div class="btn-group btn-group-sm col-lg-6 col-md-6">
				    <button type="button" class="btn btn-danger" onclick="back();"><span id="back">返回班级列表</span></button>
				</div>
				<div class="col-lg-6 col-md-6">
					<div style="float:right; margin-right:40px;">
					    <button type="button" id="code" class="btn btn-success btn-sm" onclick="changeTab(0)" ><i class="fa fa-list"></i><span id="codetitle">代码</span></button>
				    	<button type="button" id="result" class="btn btn-primary btn-sm" onclick="changeTab(1)" disabled><i class="fa fa-play-circle-o"></i><span id="resulttitle">成果</span></button>
					</div>
				</div>
			</div>
			<div id="codeDiv">
				<div style="margin-top:5px;">
					<textarea id='command' style="display: table-cell; vertical-align: middle;" disabled>
					</textarea>
				</div>
			</div>
			<div id="videoDiv" style="display:none;">
				<div style="margin-top:5px;">
					<video controls id="video">
					  <source src="/video/test.mp4" type="video/mp4">
					</video>
				</div>
			</div>
			</div>
		</div>

    <div class="row">
   		<div id="footer" class="col-lg-12 col-md-12">Copyright © 2018 Wistron Corporation. All Rights Reserved.</div>
   	</div>	
</div>   	
</body>
</html>