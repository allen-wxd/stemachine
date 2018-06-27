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
<script src="/js/toastr.min.js"></script>
<link href="/css/toastr.css" rel="stylesheet" />
<script src="/loginjs/bootbox.min.js"></script>
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

</style>
<script type="text/javascript">
toastr.options.positionClass = 'toast-top-center';
toastr.options.timeOut = '800';
</script>
</head>
<body>
<div class="container-fluid">
	<nav id="head" class="navbar navbar-inverse navbar-fixed-top" >
       		<div class="navbar-header" style="float:left; margin-left:10px;">
          		<span style="color:#EE2C2C;font-size:36px">STEM</span><span style="color:#fff;font-size:30px;">achine.</span>
       		</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right" style="margin-right:20px;">			            
					<li><a style="color:#fff;"><i class="fa fa-user"></i>  ${student.grade}年级 ${student.cno}班， ${student.sname}</a></li>	
					<li><a style="color:#fff;"><i class="fa fa-question-circle"></i> 帮助</a></li>
					<li><a style="color:#fff;" href="javascript:logout('s');"><i class="fa fa-sign-out"></i> 退出</a></li>	
				</ul>
     		</div>
   	</nav>
    	
    <div id="content" class="row" style="margin-top:60px;">
		<div class="btn-group">
		    <button type="button" class="btn btn-default">导入学生用户</button>
		    <button type="button" class="btn btn-default">导入教师用户</button>
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
	  					<div class="form-group">
	  						<input type="hidden" id="operation" value="">
	  						<label for="txt_sname">项目名</label>
	  						<input type="text" name="txt_pname" class="form-control" id="txt_pname" placeholder="项目名称">
	  					</div>
 					</div>
	 				<div class="modal-footer">
	  					<button type="button" class="btn btn-default" data-dismiss="modal"><span class="fa fa-remove" aria-hidden="true"></span>关闭</button>
	  					<button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal" onclick="submit()"><span class="fa fa-save" aria-hidden="true"></span>保存</button>
	 				</div>
 				</div>
 			</div>
 		</div>
 		

    <div class="row">
   		<div id="footer" class="col-lg-12 col-md-12">CopyRight@2018 Wistron Corporation All Rights Reserved </div>
   	</div>	
</div>   	
</body>
</html>