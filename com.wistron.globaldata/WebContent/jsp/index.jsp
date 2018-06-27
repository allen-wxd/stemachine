<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     
<!DOCTYPE html>
<html>
<head>
		<meta name="viewport" content="width=device-width" charset="utf-8"/>
		<!-- 引入各种CSS样式表 -->
		<link rel="stylesheet" href="/logincss/bootstrap.css">
		<link rel="stylesheet" href="/logincss/font-awesome.css">
		<link rel="stylesheet" href="/logincss/index.css">	<!-- 修改自Bootstrap官方Demon，你可以按自己的喜好制定CSS样式 -->
		<link rel="stylesheet" href="/logincss/font-change.css">	<!-- 将默认字体从宋体换成微软雅黑（个人比较喜欢微软雅黑，移动端和桌面端显示效果比较接近） -->		
		
		<script type="text/javascript" src="/loginjs/jquery.min.js"></script>
		<script type="text/javascript" src="/loginjs/bootstrap.min.js"></script>
	
		<title>stemachine后台管理系统</title>
	</head>
	<script type="text/javascript">
		
		$(function() {
			var height=document.documentElement.clientHeight;
			document.getElementById('iframe-page-content').style.height=(height-50)+'px';
			});
		/*
		 * 对选中的标签激活active状态，对先前处于active状态但之后未被选中的标签取消active
		 * （实现左侧菜单中的标签点击后变色的效果）
		 */
		$(document).ready(function () {
			$('ul.nav > li').click(function (e) {
				//e.preventDefault();	加上这句则导航的<a>标签会失效
				$('ul.nav > li').removeClass('active');
				$(this).addClass('active');
			});
		});
		function changeContent(url){
			$("#iframe-page-content").attr("src",url); 
		}
	</script>
	<body>
	<!-- 顶部菜单（来自bootstrap官方Demon）==================================== -->
		<nav class="navbar navbar-inverse navbar-fixed-top">
      		<div class="container">
        		<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" >
		            	<span class="sr-only">Toggle navigation</span>
		            	<span class="icon-bar"></span>
		            	<span class="icon-bar"></span>
		            	<span class="icon-bar"></span>
					</button>
	          		<a class="navbar-brand" href="index.jsp">stemachine后台管理系统</a>
        		</div>
        		
				<div id="navbar" class="navbar-collapse collapse">
					<ul class="nav navbar-nav navbar-right">			            
						<li><a href="###" onclick="showAtRight('userList.jsp')"><i class="fa fa-users"></i> 修改密码</a></li>	
						<li><a href="###" onclick="showAtRight('productList.jsp')"><i class="fa fa-question-circle"></i> 帮助</a></li>
						<li><a href="###" onclick="showAtRight('recordList.jsp')" ><i class="fa fa-power-off"></i> 退出</a></li>	
					</ul>
          			
        		</div>
      		</div>
    	</nav>

	<!-- 左侧菜单选项========================================= -->
		<div class="container-fluid">
			<div class="row-fluie">
				<div class="col-sm-3 col-md-2 sidebar">		
					<ul class="nav nav-sidebar">
						<!-- 一级菜单 -->
						<li class="active" onclick="changeContent('studentList.jsp')"><a href="#userMeun" class="nav-header menu-first collapsed" data-toggle="collapse">
							<i class="fa fa-user"></i>&nbsp; 学生管理 <span class="sr-only">(current)</span></a>
						</li> 
						<!-- 二级菜单 -->
						<!-- 注意一级菜单中<a>标签内的href="#……"里面的内容要与二级菜单中<ul>标签内的id="……"里面的内容一致 -->
						<!-- <ul id="userMeun" class="nav nav-list collapse menu-second">
							<li><a href="###" onclick="showAtRight('studentList.jsp')"><i class="fa fa-users"></i> 学生列表</a></li>
						</ul> -->
						<li onclick="changeContent('test.jsp')"><a href="#productMeun" class="nav-header menu-first collapsed" data-toggle="collapse">
							<i class="fa fa-globe"></i>&nbsp; 教师管理 <span class="sr-only">(current)</span></a>
						</li> 
						<li onclick="changeContent('userList.jsp')"><a href="#recordMeun" class="nav-header menu-first collapsed" data-toggle="collapse">
							<i class="fa fa-file-text"></i>&nbsp; 录播查看 <span class="sr-only">(current)</span></a>
						</li> 
					</ul>
					
				</div>
			</div>
		</div>

<!-- 右侧内容展示==================================================   -->   		
 				<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<h1 class="page-header"><i class="fa fa-spinner fa-spin"></i>&nbsp;控制台<small>&nbsp;&nbsp;&nbsp;欢迎使用stemachine后台管理系统</small></h1>
						<div id="iframeContent"></div>  
						<!-- 载入左侧菜单指向的jsp（或html等）页面内容 -->
          				<iframe id="iframe-page-content" src="" width="100%"  frameborder="no" border="0" marginwidth="0"
						marginheight=" 0" scrolling="no" allowtransparency="yes"></iframe>
				</div> 
	<nav class="navbar navbar-inverse navbar-fixed-bottom">
      		<div class="container">
        			copyright@2013-2017 wistron
        		</div>
    	</nav>			
	</body>
</html>