<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>

<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8" />
		<title>文件下载</title>
	</head>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap.min.css">

	<!-- 可选的Bootstrap主题文件（一般不用引入） -->
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/style.css">
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="<%=request.getContextPath() %>/js/jquery.min.js"></script>

	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
	<script type="text/javascript">
	 $(function(){ 
		search(1);
	}); 
	function search(page){
		var tbody=document.getElementById("bookblock");
		var filename = $("#filename").val();
		var model=document.getElementById("bookmodel").innerHTML;
		var pageModel = document.getElementById("pagemodel").innerHTML;
		var pageDiv = document.getElementById("pageDiv");
		$.ajax({  
	           type: "post",  
	           url: "/wistron/list", 
	           data: { "filename": filename,"page":page }, 
	           dataType:'json',   
	           success: function (res) {
	        	   var str = "";
	        	   var block="";
	        	   var title = "";
	        	   var data = res[0].data;
	        	   var pageinfo = "";
	        	   for (var i=0;i<data.length;i++) {
	        		   block = model.replace('#bookid',data[i].fileId);
	        		   if(data[i].fileName.length>20){
	        			   block = block.replace('#bookname',data[i].fileName.substring(0,20)+"...");
	        			   title = data[i].fileName;
	        			   block = block.replace('#title',title);
	        		   }else{
	        			   block = block.replace('#bookname',data[i].fileName);
	        			   block = block.replace('#title',data[i].fileName);
	        		   }
	        		   str+=block;
                    } 
                    tbody.innerHTML = str;
                    pageinfo = pageModel.replace('#uppageno',res[0].page.upPageNo);
                    pageinfo = pageinfo.replace('#curpage',res[0].page.curentPageNo);
                    pageinfo = pageinfo.replace('#nextpageno',res[0].page.nextPageNo);
	               	pageinfo = pageinfo.replace('#totalpage',res[0].page.totalCount);
	               	pageDiv.innerHTML = pageinfo;
	           }  
	       });  
			
		}
	</script>
	<body>
	<div class="container">	
		<h3>文件下载  </h3>
		<%=session.getAttribute("loginname")%>
	</div>
		<div class="container">	
			<div class="input-group col-md-6" style="margin-top:10px;">  
       			<input type="text" id="filename" class="form-control"placeholder="请输入文件名" />  
           		<span class="input-group-btn">  
               		<button class="btn btn-info btn-search" onclick="search('')">查找</button>  
            	</span>  
 			</div> 
 		</div>	
 		<div class="container" id="bookblock" style="margin-top:10px">
		</div>
		<div id="bookmodel" style="display:none">
			<div class="col-md-3" >
				<div style="text-align: center" ><img class="img-rounded" src="../images/book.jpg"></div>
				<div style="text-align: center" title="#title"><h4>#bookname</h4></div>
				<div style="text-align: center"><a href="http://localhost:8080/wistron/download?fileid=#bookid">下载</a></div>
			</div>
		</div>
		<div class="panel-footer" id="pageDiv">
	    </div>
	    <div id="pagemodel" style="display: none">
			<nav style="text-align: center">
				<ul class="pagination">
					<li onclick="search('#uppageno')"><a href="#">&laquo;上一页</a></li>
					<li class="active"><a href="#">#curpage</a></li>
					<li onclick="search('#nextpageno')"><a href="#">下一页&raquo;</a></li>
					<li onclick="search('#totalpage')"><a href="#">最后一页&raquo;</a></li>
				</ul>
			</nav>
		</div>
</body>
</html>