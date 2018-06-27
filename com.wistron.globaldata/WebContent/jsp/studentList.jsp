<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width" charset="utf-8"/>
    <title>BootStrap Table使用</title>
    <script src="/loginjs/jquery.js"></script>
    <script src="/loginjs/jquery.min.js"></script>
   

    <script src="/loginjs/bootstrap.js"></script>
    <link href="/logincss/bootstrap.css" rel="stylesheet" />
    
    <script src="/loginjs/bootstrap-table.js"></script>
    <link href="/logincss/bootstrap-table.css" rel="stylesheet" />
    <script src="/loginjs/bootstrap-table-zh-CN.js"></script>
    
    <script src="/loginjs/toastr.min.js"></script>
    <link href="/logincss/toastr.css" rel="stylesheet" />
    
    <script src="/loginjs/bootbox.min.js"></script>
    <script src="/loginjs/bootstrap-table-export.js"></script>
</head>
<script type="text/javascript">
toastr.options.positionClass = 'toast-top-center';
$(function () {
    //1.初始化Table
    var oTable = new TableInit();
    oTable.Init();
    //2.初始化Button的点击事件
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
});
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_student').bootstrapTable({
            url: '/user/private/studentList.action',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [5, 10, 15, 25, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: "上一页",
            paginationNextText: "下一页",
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            showExport: true,                     //是否显示导出
            exportDataType: "basic",              //basic', 'all', 'selected'.
            exportTypes:['excel'],  //导出文件类型  
            columns: [{
                checkbox: true
            }, {
                field: 'sname',
                title: '姓名',
            }, {
                field: 'sno',
                title: '学号'
            }, {
            	field: 'grade', formatter: function (value, row, index) {  
                    return value+"年级";
                }  ,
                title: '年级'
            }, {
                field: 'cno', formatter: function (value, row, index) {  
                    return value+"班";
                }  ,
                title: '班级'
            }]
        });
    };
    
  //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: params.offset,  //页码
            sname: $("#sname").val(),
            grade: $("#grade").val()
        };
        return temp;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    var postdata = {};

    oInit.Init = function () {
        //初始化页面上面的按钮事件
         $("#btn_add").click(function () {
            $("#myModalLabel").text("新增");
            $("#myModal").find(".form-control").val("");
            $('#myModal').modal()

            postdata.id = "";
        });

        $("#btn_edit").click(function () {
            var arrselections = $("#tb_student").bootstrapTable('getSelections');
            if (arrselections.length > 1) {
                toastr.warning('只能选择一行进行编辑');

                return;
            }
            if (arrselections.length <= 0) {
                toastr.warning('请选择有效数据');

                return;
            }
            $("#myModalLabel").text("编辑");
            $("#txt_sname").val(arrselections[0].sname);
            $("#txt_sno").val(arrselections[0].sno);
            $("#txt_grade").val(arrselections[0].grade);
            $("#txt_cno").val(arrselections[0].cno);
            postdata.id = arrselections[0].id;
            $('#myModal').modal();
        });

        $("#btn_delete").click(function () {
            var arrselections = $("#tb_student").bootstrapTable('getSelections');
            var ids="";
            for(var i=0;i<arrselections.length;i++){
            	if(i==arrselections.length-1){
            		ids= ids+arrselections[i].id;
            	}else{
            		ids= ids+arrselections[i].id+",";
            	}
            }
            if (arrselections.length <= 0) {
                toastr.warning('请选择有效数据');
                return;
            }
            bootbox.confirm("您确认删除选定的记录吗？", function (result) {
                if (result) {
                	$.ajax({
                        type: "post",
                        url: "/user/private/studentDel.action",
                        data: { "ids": ids },
                        success: function (data, status) {
                            if (status == "success") {
                                toastr.success('删除成功');
                                $("#tb_student").bootstrapTable('refresh');
                            }
                        },
                        error: function () {
                            toastr.error('Error');
                        },
                        complete: function () {

                        }

                    });
                }
             });
        });

        $("#btn_submit").click(function () {
            postdata.sname = $("#txt_sname").val();
            postdata.sno = $("#txt_sno").val();
            postdata.grade = $("#txt_grade").val();
            postdata.cno = $("#txt_cno").val();
            $.ajax({
                type: "post",
                url: "/user/private/studentAddOrEdit.action",
                data: { "sname": postdata.sname, "sno": postdata.sno, "cno": postdata.cno, "grade": postdata.grade, "id": postdata.id},
                success: function (data, status) {
                    if (status == "success") {
                        toastr.success('提交数据成功');
                        $("#tb_student").bootstrapTable('refresh');
                    }
                },
                error: function () {
                    toastr.error('Error');
                },
                complete: function () {

                }

            });
        });

        $("#btn_query").click(function () {
            $("#tb_student").bootstrapTable('refresh');
        });
        
        $("#btn_import").click(function () {
        	$('#importModal').modal();
        });
        
        $("#btn_importdata").click(function () {
        	 var formData = new FormData($( "#importForm" )[0]);  
        	    $.ajax({  
        	         url: '/user/private/import.action' ,  
        	         type: 'POST',  
        	         data: formData,  
        	         async: false,  
        	         cache: false,  
        	         contentType: false,  
        	         processData: false,  
        	         success: function (returndata) {  
        	             if(returndata.status=="success"){
        	            	 toastr.success('数据导入成功');
                             $("#tb_student").bootstrapTable('refresh');
        	             } 
        	         },  
        	         error: function (returndata) {  
        	        	 toastr.error('Error');
        	         }  
        	    });
        });
    };
    return oInit;
};
</script>
<body>
	<div class="panel-body">
        <div class="panel panel-default">
            <div class="panel-heading">查询条件</div>
            <div class="panel-body" style="margin-bottom:0px;">
                <form id="formSearch" >
                    <div class="form-group" style="margin-top:15px">
                        <label class="control-label col-sm-1" for="sname">姓名</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" id="sname">
                        </div>
                        <label class="control-label col-sm-1" for="grade">年级</label>
                        <div class="col-sm-3">
                            <input type="text" class="form-control" id="grade">
                        </div>
                        <div class="col-sm-4" style="text-align:left;">
                            <button type="button" style="margin-left:50px" id="btn_query" class="btn btn-primary">查询</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>       

        <div id="toolbar" class="btn-group">
            <button id="btn_add" type="button" class="btn btn-success">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增
            </button>
            <button id="btn_edit" type="button" class="btn btn-warning">
                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改
            </button>
            <button id="btn_delete" type="button" class="btn btn-danger">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
            </button> <button id="btn_import" type="button" class="btn btn-info">
                <span class="glyphicon glyphicon-import" aria-hidden="true"></span>导入数据
            </button>
        </div>
        <table id="tb_student"></table>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
 			<div class="modal-dialog" role="document">
 				<div class="modal-content">
 					<div class="modal-header">
  						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
  							<h4 class="modal-title" id="myModalLabel">新增</h4>
 					</div>
 					<div class="modal-body">
 
  					<div class="form-group">
  						<label for="txt_sname">学生姓名</label>
  						<input type="text" name="txt_sname" class="form-control" id="txt_sname" placeholder="学生姓名">
  					</div>
  					<div class="form-group">
  						<label for="txt_sno">学号</label>
  						<input type="text" name="txt_sno" class="form-control" id="txt_sno" placeholder="学号">
  					</div>
  					<div class="form-group">
						<label for="txt_grade">年级</label>
						<input type="text" name="txt_grade" class="form-control" id="txt_grade" placeholder="年级">
  					</div>
  					<div class="form-group">
  						<label for="txt_cno">班级</label>
  						<input type="text" name="txt_cno" class="form-control" id="txt_cno" placeholder="班级">
  					</div>
 				</div>
 				<div class="modal-footer">
  					<button type="button" class="btn btn-default" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭</button>
  					<button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal"><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存</button>
 				</div>
 			</div>
 		</div>
 	</div>
 	<div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="importModalLabel">  
    <div class="modal-dialog" role="document">  
        <div class="modal-content">  
            <div class="modal-header">  
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">  
                    <span aria-hidden="true">×</span>  
                </button>  
                <h4 class="modal-title" id="importModalLabel">导入数据</h4>  
            </div>  
            <div class="modal-body"> 
	  			<form id="importForm">
					选择excel文件:<input type="file" name="file" width="120px">  
			    </form>
            </div>  
            <div class="modal-footer">  
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>  
                <button type="button" id="btn_importdata" class="btn btn-primary" data-dismiss="modal">导入</button>  
            </div>  
        </div>  
    </div>  
	</div>
  </div>
</body>
</html>