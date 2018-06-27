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
<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/loginjs/bootstrapValidator.js"></script>
<script src="/js/toastr.min.js"></script>
<link href="/css/toastr.css" rel="stylesheet" />
<script src="/loginjs/bootbox.js"></script>
<script src="/js/auto-line-number.js"></script>
<script src="/loginjs/jquery.i18n.properties.js"></script>
<script src="/loginjs/language_project.js"></script>
<title>学生项目管理</title>
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
	border: 1px solid green; 
	text-align:center;
	color:green;
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
	border: 1px solid blue; 
	text-align:center;
	color:blue;
	border-radius:5px;
	float:right;
} 
.mystyle4{
	width:80px;
	border: 1px solid gray; 
	text-align:center;
	color:gray;
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
var status = "";
var initCode = "";
var language = "";
$(function(){
	language=navigator.language;
    language = language.replace("-","_");
    if(language=="zh_CN" || language=="zh_TW"){
    	bootbox.setLocale(language);
    }
    var msg=$.i18n.prop('stumsg').replace("#c",$('#classnum').val()).replace("#g",$('#gradenum').val());
    $('#stumsg').text(msg);
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
	$("#video").width(wid*2);
	$("#command").width(wid*2-58);
	$("#command").height(table-34);
	initCode = $("#command").val();
	initCode = initCode.replace("%str",$.i18n.prop('textInit'));
	$("#command").val(initCode);
	initTextArea();
	$("#proName").text($.i18n.prop('pname'));
    $('#txt_pname').attr('placeholder',$.i18n.prop('pname'));
    $("#p_name").text($.i18n.prop('pname'));
    $("#p_date").text($.i18n.prop('utime'));
    $('.cancle').text($.i18n.prop('close'));
	$('.save').text($.i18n.prop('save'));
	$('.upload_txt').text($.i18n.prop('upload'));
	$("#uploadModalLabel").text($.i18n.prop('codeUpload'));
	$("#onlytxt").text($.i18n.prop('selectTxt'));
	//表单验证
	$('#projectForm').bootstrapValidator({
        message: '输入值不合法',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        excluded: [':disabled'],
        fields: {
        	txt_pname: {
                message: $.i18n.prop('verification2'),
                validators: {
                    notEmpty: {
                        message: $.i18n.prop('verification27')
                    },
                    stringLength: {
                        min: 2,
                        max: 8,
                        message: $.i18n.prop('verification4')
                    }
                }
            }
        }
    });
	
	$('textarea').bind('input propertychange', function() {
		$('#send').removeClass('btn-warning').addClass('btn-primary');
    	$('#send').attr('disabled', true);
    	$('#check').removeClass('btn-primary').addClass('btn-info');
        $('#check').attr('disabled', false);
	});
})
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
	$("#pbody").empty();
    $.ajax({
        type: "post",
        url: "/stemachine/projectList.do",
        data: { "pname": seach_pname ,"limit":"10" ,"offset":offset },
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
            	tbBody = "<tr id="+n.pid+"><td nowrap><i class='"+type+"' style='color:#d9534f;'></i>&nbsp;&nbsp;<span>"+ n.pname + "</span><div class='"+style+"'>" + status + "</div><input type='hidden' value="+n.status+"></td>" + "<td nowrap style='text-align:center;' class='bg-td'>" + date + "</td></tr>";
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
	        		$('#upload').removeClass('btn-default').addClass('btn-success');
	            	$('#upload').attr('disabled', false);
	            	$('#check').removeClass('btn-default').addClass('btn-info');
	            	$('#check').attr('disabled', false);
	            	$('#send').removeClass('btn-warning').addClass('btn-primary');
	            	$('#send').attr('disabled', true);
        		 	var tdarr = $('#ptable>tbody>tr>td:nth-child(2)');
        		 	for(var i=0;i<tdarr.length;i++){
        		 		tdarr[i].className="bg-td";
        		 	}
        		 	
        		    $(this).children('td').eq(1).removeClass('bg-td');
        	        $(this).parent().find("tr.focus").toggleClass("focus");//取消原先选中行
        	        $(this).toggleClass("focus");//设定当前行为选中行
        	        if(this.id == pid){
        				if($("#command").val().trim()=="请新建一个项目 或 上传代码"){
        					$("#command").val('');
        				}
        			 	$('#command').attr("disabled",false);
        			}else{
        				var code = getCmdContent(pid);
        				var command = $("#command").val();
        				if(pid != "" && code!=command){
        					if(command.trim()!=""){
        						saveCode1(pid);
        					}
        				}
        				getCode(this.id);
        			}
        	        pid = this.id;
        	        pname = this.cells[0].getElementsByTagName('span')[0].innerText;
        	        status = this.cells[0].getElementsByTagName('input')[0].value;
        	        $("#codeDiv").css('display','block');
        			$("#videoDiv").css('display','none');
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
            		 status = document.getElementById("pbody").getElementsByTagName("tr")[0].getElementsByTagName('input')[0].value;
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
function changeTab(i,which){
	 /* $("#rightTitle").text(pname+" 代码展示"); */
	 this.focus();
	 if(i==0){
		 $('#codemaneger').css("color","#fff");
		 $('#codedisplay').css("color","");
		 $("#codeDiv").css('display','block');
		 $("#videoDiv").css('display','none');
	 }else{
		 if(status=="1"||status=="2"||status=="5"){
			 toastr.warning($.i18n.prop('noVideo'));
			 return;
		 }
		 if(pname==""){
			 toastr.warning($.i18n.prop('selectProject'));
			 return;
		 }
		 $('#codemaneger').css("color","");
		 $('#codedisplay').css("color","#fff");
		 $("#codeDiv").css('display','none');
		 $("#videoDiv").css('display','block');
		 $("#rightTitle").text(pname+""+$.i18n.prop('codeShow'));
	 }
}
function create(){
	$("#projectForm").data('bootstrapValidator').resetForm();
	$("#myModalLabel").text($.i18n.prop('new'));
	$("#operation").val("0");
    $("#myModal").find(".form-control").val("");
	if(pid != ""){
		var code = getCmdContent(pid);
		var command = $("#command").val();
		if(code!=command){
			if(command.trim()!=""){
				bootbox.confirm($.i18n.prop('saveCode'), function (result) {
			        if (result) {
			        	$.ajax({
			                type: "post",
			                url: "/stemachine/updateCommand.do",
			                data: { "pid": pid ,"code":command },
			                async: false,  
			                success: function (data) {
			                	if(data.status=="1"){
			                		toastr.success($.i18n.prop('saveStatus1'));
			                	}else{
			                		toastr.error($.i18n.prop('saveStatus2'));
			                	}
			                },
			                error: function () {
			                    toastr.error($.i18n.prop('saveStatus2'));
			                },
			                complete: function () {
			                	getList(1);
			                	$('#myModal').modal();
			                }
			            });
			        }
			    })
			}else{
				$('#myModal').modal();
			}
		}else{
			$('#myModal').modal();
		}
	}else{
		$('#myModal').modal();
	}
}
function edit(){
	$("#projectForm").data('bootstrapValidator').resetForm();
	$("#myModalLabel").text($.i18n.prop('editProject'));
	if(pid==""){
		toastr.warning($.i18n.prop('editDilog'));
		return;
	}
	if(pid != ""){
		var code = getCmdContent(pid);
		var command = $("#command").val();
		if(code!=command){
			if(command.trim()!=""){
				bootbox.confirm($.i18n.prop('saveCode'), function (result) {
			        if (result) {
			        	$.ajax({
			                type: "post",
			                url: "/stemachine/updateCommand.do",
			                data: { "pid": pid ,"code":command },
			                async: false,  
			                success: function (data) {
			                	if(data.status=="1"){
			                		toastr.success($.i18n.prop('saveStatus1'));
			                	}else{
			                		toastr.error($.i18n.prop('saveStatus2'));
			                	}
			                },
			                error: function () {
			                    toastr.error($.i18n.prop('saveStatus2'));
			                },
			                complete: function () {
			                	getList(1);
			                	$('#myModal').modal();
			                }
			            });
			        }
			    })
			}else{
				$('#myModal').modal();
			}
		}else{
			$('#myModal').modal();
		}
	}else{
		$('#myModal').modal();
	}
    $("#myModal").find(".form-control").val(pname);
    $("#operation").val("1");
}
//增加或修改代码
function submit(){
	var operation = $("#operation").val();
	var successMsg = "";
	var errorMsg = "";
	if(operation=="1"){
		successMsg = $.i18n.prop('editStatus1');
		errorMsg = $.i18n.prop('editStatus2');
	}else{
		pid = "";
		successMsg = $.i18n.prop('addStatus1');
		errorMsg = $.i18n.prop('addStatus2');
	}
    pname = $("#txt_pname").val();
    $('#projectForm').bootstrapValidator('validate');
    var flag = $("#projectForm").data('bootstrapValidator').isValid();
    if(flag){
    	$.ajax({
            type: "post",
            url: "/stemachine/addOrUpdate.do",
            data: { "pname": pname ,"pid":pid },
            success: function (data) {
            	var status = data.status;
                if (status == 1) {
                	$("#myModal").modal('hide');
                	toastr.success(successMsg);
                }
            },
            error: function () {
                toastr.error(errorMsg);
            },
            complete: function () {
            	initTextArea();
            	if(operation == "1"){
            		$("#rightTitle").text(pname+" "+$.i18n.prop('codeShow'));
            		getCode(pid);
            	}else{
            		$("#rightTitle").text(pname+" "+$.i18n.prop('codeShow'));
            		$("#command").val('');
                	$('#command').attr("disabled",false);
                	$('#upload').removeClass('btn-default').addClass('btn-success');
                	$('#upload').attr('disabled', false);
                	$('#check').removeClass('btn-default').addClass('btn-info');
                	$('#check').attr('disabled', false);
                	$('#send').removeClass('btn-warning').addClass('btn-primary');
                	$('#send').attr('disabled', true);
            	}
            	getList(1);
            }
        });
    }
 }
 //获取代码
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
 function getCmdContent(id){
	 var code = "";
	 $.ajax({
	        type: "post",
	        url: "/stemachine/getCommand.do",
	        data: { "pid":pid },
	        async: false,
	        success: function (data,status) {
	        	if(status=="success"){
	        		code = data.command;
	        	}
	        },
	        error: function () {
	            toastr.error(errorMsg);
	        },
	        complete: function () {
	        	
	        }

	    });
	 return code;
 }
 //删除项目
 function delProject(){
	 if (pid == "") {
         toastr.warning($.i18n.prop('selectDate'));
         return;
     }
     bootbox.confirm($.i18n.prop('delDilog'), function (result) {
         if (result) { 
			 $.ajax({
			        type: "post",
			        url: "/stemachine/del.do",
			        data: { "pid": pid },
			        success: function (data) {
			        	var status = data.status;
			            if (status == 1) {
			            	toastr.success($.i18n.prop('delStatus1'));
			            	pid="";
			            	pname="";
			            	$("#command").val(initCode);
			            	$('#command').attr("disabled",true);
			            }
			        },
			        error: function () {
			            toastr.error($.i18n.prop('delStatus2'));
			        },
			        complete: function () {
			        	$("#rightTitle").text($.i18n.prop('codeView'));
			        	$('#upload').removeClass('btn-success').addClass('btn-default');
		            	$('#upload').attr('disabled', true);
		            	$('#check').removeClass('btn-info').addClass('btn-default');
		            	$('#check').attr('disabled', true);
		            	$('#send').removeClass('btn-primary').addClass('btn-default');
		            	$('#send').attr('disabled', true);
		            	$("#operation").val("2");
			        	getList(1);
			        }
		
			    });
         }
     }) 
  }
 //保存代码
 function saveCode(){
	var code = $("#command").val();
	bootbox.confirm($.i18n.prop('publishDilog'), function (result) {
        if (result) {
        	playing('2');
        	$.ajax({
                type: "post",
                url: "/stemachine/updateCommand.do",
                data: { "pid": pid ,"code":code ,"temp":"1" },
                success: function (data) {
                	if(data.status=="1"){
                		toastr.success($.i18n.prop('saveStatus1'));
                		status="3";
                	}else if(data.status=="0"){
                		toastr.error($.i18n.prop('saveStatus3'));
                		status="1";
                		playing('1');
                	}else{
                		toastr.error($.i18n.prop('saveStatus2'));
                		status="5";
                		playing('5');
                	}
                },
                error: function () {
                    toastr.error($.i18n.prop('saveStatus2'));
                    status="5";
                    playing('5');
                },
                complete: function () {
                	getList(1);
                	getCode(pid);
                	getVideoPath(pid);
                }
            });
        }
    })
 }
 function saveCode1(id){
	 var code = $("#command").val();
		bootbox.confirm($.i18n.prop('saveCode'), function (result) {
	        if (result) {
	        	$.ajax({
	                type: "post",
	                url: "/stemachine/updateCommand.do",
	                data: { "pid": id ,"code":code ,"temp":"0" },
	                success: function (data) {
	                	if(data.status=="1"){
	                		toastr.success($.i18n.prop('saveStatus1'));
	                	}else{
	                		toastr.error($.i18n.prop('saveStatus2'));
	                	}
	                },
	                error: function () {
	                    toastr.error($.i18n.prop('saveStatus2'));
	                },
	                complete: function () {
	                	getList(1);
	                }
	            });
	        }
	    })
 }
 //上传代码
 function uploadCode(){
	 if(pid==""){
		 toastr.warning($.i18n.prop('selectProject'));
		 return;
	 }else{
		 $('#uploadModal').modal();
	 }
 }
 function upload(){
	 var filename = $("#file").val();
	 var index1=filename.lastIndexOf(".");  
	 var index2=filename.length;
	 var postf=filename.substring(index1,index2);//后缀名  
	 if(postf==".py"||postf==".txt"){
		 var formData = new FormData($( "#uploadForm" )[0]);  
 	     $.ajax({  
 	         url: '/stemachine/uploadCommand.do' ,  
 	         type: 'POST',  
 	         data: formData,  
 	         async: false,  
 	         cache: false,  
 	         contentType: false,  
 	         processData: false,  
 	         success: function (returndata) {  
 	           $("#command").val(returndata.command);
 	           $('#send').removeClass('btn-warning').addClass('btn-primary');
 	     	   $('#send').attr('disabled', true);
 	     	   $('#check').removeClass('btn-primary').addClass('btn-info');
 	           $('#check').attr('disabled', false);
 	         },  
 	         error: function (returndata) {  
 	        	 toastr.error('Error');
 	         }  
 	    });
	 }else{
		 toastr.warning($.i18n.prop('selectTxt'));
		 return;
	 }
 }
 //退出登录
 function logout(role){
	 if(pid != ""){
			var code = getCmdContent(pid);
			var command = $("#command").val();
			if(code!=command){
				if(command.trim()!=""){
					bootbox.confirm($.i18n.prop('saveCode'), function (result) {
				        if (result) {
				        	$.ajax({
				                type: "post",
				                url: "/stemachine/updateCommand.do",
				                data: { "pid": pid ,"code":command },
				                async: false,  
				                success: function (data) {
				                	if(data.status=="1"){
				                		toastr.success($.i18n.prop('saveStatus1'));
				                	}else{
				                		toastr.error($.i18n.prop('saveStatus2'));
				                	}
				                },
				                error: function () {
				                    toastr.error($.i18n.prop('saveStatus2'));
				                },
				                complete: function () {
				                	getList(1);
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
				            });
				        }else{
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
				    })
				}else{
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
			}else{
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
		}else{
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
	 
 }
 function checkCommand(){
	 $.ajax({  
 		type: "post",
         url: "/stemachine/checkCommand.do",
         data: { "command": $("#command").val() },
         async: false,
         success: function (data) {
             if (data == "success") {
            	 $('#send').removeClass('btn-default').addClass('btn-warning');
	             $('#send').attr('disabled', false);
	             $('#check').removeClass('btn-info').addClass('btn-primary');
	             $('#check').attr('disabled', true);
             }else{
            	 $('#consoleModal').modal();
            	 $('#errortext').val(data);
             }
         },
         error: function () {
             toastr.error(errorMsg);
         },
         complete: function () {
         	
         }  
	    });
 }
 function getVideoPath(id){
	 $.ajax({  
	 		type: "post",
	         url: "/stemachine/getVideoPath.do",
	         data: { "pid": id },
	         cache: false,  
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
 
 function playing(status){
	 $.ajax({  
	 		type: "post",
	         url: "/stemachine/modifyStatus.do",
	         data: { "pid": pid ,"status": status},
	         success: function (data) {
	             
	         },
	         error: function () {
	             toastr.error(errorMsg);
	         },
	         complete: function () {
	        	 getList(1);
	         }  
		    });
 }
 
 function searchenter(event) {
    event = event || window.event;
    if (event.keyCode == 13) {
    	$("#btn_query").trigger("click");     
    }
}  
 
 function help(){
		if(language=="zh_CN"){
			window.location.href="/help/onlinehelp_student_SC_20180531A.htm";  
		}else if(language=="zh_TW"){
			window.location.href="/help/onlinehelp_student_TC_20180531A.htm"; 
		}else{
			window.location.href="/help/onlinehelp_student_EN_20180531A.htm";
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
       		<input type="hidden" id="classnum" value="${student.cno}" />
       		<input type="hidden" id="gradenum" value="${student.grade}" />
			<div id="navbar" class="navbar-collapse collapse">
				<ul id="codeTab" class="nav navbar-nav navbar-center">			            
					<li><a href="javascript:void(0);" id="codemaneger" style="color:#fff;" onclick="changeTab(0,this)"><i class="fa fa-list"></i><span id="codeManage">代码管理</span></a></li>	
					<li><a href="javascript:void(0);" id="codedisplay" onclick="changeTab(1,this)"><i class="fa fa-play-circle-o"></i><span id="show">成果展示</span></a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right" style="margin-right:20px;">			            
					<li><a style="color:#fff;"><i class="fa fa-user"></i>  <span id="stumsg">${student.grade}年级 ${student.cno}班</span>， ${student.sname}</a></li>	
					<li><a style="color:#fff;" href="javascript:help();"><i class="fa fa-question-circle"></i> <span id="help">帮助</span></a></li>
					<li><a style="color:#fff;" href="javascript:logout('s');"><i class="fa fa-sign-out"></i> <span id="exit">退出</span></a></li>	
				</ul>
     		</div>
   	</nav>
    	
    <div id="content" class="row" style="margin-top:60px;">
			<div class="col-lg-4 col-md-4" id="left">
				<h4 style="border-bottom: 1px solid #eee;" id="ltitle">已上传代码列表</h4>	
				<div class="row">
					<div id="table" class="col-lg-12 col-md-12" style="overflow:auto;">
						<div class="input-group" >  
		       			<input type="text" id="pname" class="form-control" onkeyup="searchenter(event);" placeholder="请输入项目名称" />  
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
			<h4 style="border-bottom: 1px solid #eee;" id="rightTitle">代码显示</h4>
				<div id="codeDiv">
					<div class="row">
						<div class="btn-group btn-group-sm col-lg-6 col-md-6">
						    <button type="button" class="btn btn-success" onclick="create()"><span class="fa fa-plus-circle"></span><span id="newp">新建项目</span></button>
						    <button type="button" class="btn btn-info" onclick="edit()"><span class="fa fa-pencil-square-o"></span><span id="editp">编辑项目</span></button>
						    <button type="button" class="btn btn-danger" onclick="delProject()"><span class="fa fa-minus-circle"></span><span id="delp">删除项目</span></button>
						</div>
						<div class="col-lg-6 col-md-6">
							<div style="float:right; margin-right:40px;">
							    <button type="button" id="upload" class="btn btn-defult btn-sm" onclick="uploadCode()" disabled><span class="fa fa-cloud-upload"></span><span id="upcode">上传代码</span></button>
							    <button type="button" id="check" class="btn btn-defult btn-sm" onclick="checkCommand()" disabled><span class="fa fa-check-square-o"></span><span id="checkcode">语法检查</span></button>
							    <button type="button" id="send" class="btn btn-defult btn-sm" onclick="saveCode()" disabled><span class="fa fa-mail-forward"></span><span id="publishcode">代码发布</span></button>
							</div>
						</div>
					</div>
					
					<div style="margin-top:5px;">
						<textarea id='command' onchange="reCheck();" style="display: table-cell; vertical-align: middle;" disabled>
						
						
						
						
						
						
						
						
						
						
							%str
						</textarea>
					</div>
				</div>
				<div id="videoDiv" style="display:none;">
					<video controls id="video" onplay="playing('4');">
					  <source src="" type="video/mp4">
					</video>
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
 			<div class="modal-dialog" role="document">
 				<div class="modal-content">
 					<div class="modal-header">
  						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
  							<h4 class="modal-title" id="myModalLabel">新建项目</h4>
 					</div>
 					<div class="modal-body">
 					  <form id="projectForm">
	  					<div class="form-group">
	  						<input type="hidden" id="operation" value="">
	  						<label for="txt_sname" id="proName">项目名</label>
	  						<input type="text" name="txt_pname" class="form-control" id="txt_pname" placeholder="项目名称">
	  					</div>
	  				  </form>	
 					</div>
	 				<div class="modal-footer">
	  					<button type="button" class="btn btn-default" data-dismiss="modal"><span class="fa fa-remove" aria-hidden="true"></span><span class="cancle">关闭</span></button>
	  					<button type="button" id="btn_submit" class="btn btn-primary" onclick="submit()"><span class="fa fa-save" aria-hidden="true"></span><span class="save">上传</span></button>
	 				</div>
 				</div>
 			</div>
 		</div>
 		<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="uploadModalLabel">
 			<div class="modal-dialog" role="document">
 				<div class="modal-content">
 					<div class="modal-header">
  						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
  							<h4 class="modal-title" id="uploadModalLabel">上传代码</h4>
 					</div>
 					<div class="modal-body"> 
			  			<form id="uploadForm">
							<label id="onlytxt">选择py文件</label>:<input type="file" name="file" id="file" width="120px">  
					    </form>
		            </div>  
	 				<div class="modal-footer">
	  					<button type="button" class="btn btn-default" data-dismiss="modal"><span class="fa fa-remove" aria-hidden="true"></span><span class="cancle">关闭</span></button>
	  					<button type="button" id="btn_upload" class="btn btn-primary" data-dismiss="modal" onclick="upload()"><span class="fa fa-cloud-upload" aria-hidden="true"></span><span class="upload_txt">上传</span></button>
	 				</div>
 				</div>
 			</div>
 		</div>
 		<div class="modal fade" id="consoleModal" tabindex="-1" role="dialog" aria-labelledby="consoleModalLabel">
 			<div class="modal-dialog" role="document">
 				<div class="modal-content">
 					<div class="modal-header">
  						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
  							<h4 class="modal-title" id="consoleModalLabel">console</h4>
 					</div>
 					<div class="modal-body">
	  					<textarea id="errortext" rows="3" cols="3">
	  					</textarea>
 					</div>
	 				<div class="modal-footer">
	  					<button type="button" class="btn btn-default" data-dismiss="modal"><span class="fa fa-remove" aria-hidden="true"></span><span class="cancle">关闭</span></button>
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