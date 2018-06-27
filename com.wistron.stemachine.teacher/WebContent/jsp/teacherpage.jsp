<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title id="ttitle">教师</title>
        <link rel="stylesheet" type="text/css" href="/tcss/bootstrap.min.css"/>
		<link rel="stylesheet" type="text/css" href="/tcss/style.css"/>
		<link rel="stylesheet" href="/tcss/font-awesome.css">
		<link rel="stylesheet" type="text/css" href="/logincss/toastr.css"/>
		<link type="image/x-icon"   href="/timages/favicon.ico"  rel="icon">
		<script src="/tjs/jquery.min.js"></script>
		<script src="/loginjs/toastr.min.js"></script>
		<script src="/loginjs/jquery.i18n.properties.js"></script>
		<script src="/loginjs/language-lnt.js"></script>
		<script src="/tjs/bootstrap.min.js"></script>
		<script src="/tjs/bootbox.min.js" ></script>
		<style type="text/css">
		/* 班级列表div原始样式 */
			.classdiv {
			    background: #fffeff;
			    color: black;
			    margin:1.5px 1.5px;
				border-bottom:dashed;
				border-width:1px;
				border-color:gray;
				text-align: center;
				font-size:20px;
				height:10%;
				line-height:77px;
			}
			/*学生列表div原始的样式  */
			.studentdiv {
			    background: #fffeff;
			    color: black;
				width:9.56%;
				border:dashed;
				border-width:1px;
				border-color:#dedede;
				float:left;
				margin-top:5px;
				margin-left:0.4%;
				word-wrap:break-word; 
				overflow:hidden;
			}
			.btn-my{
			    width:300px;
			}
			</style>
    </head>
  <body>
    <div>
        <div id="firstDiv" class="fistDivClass" style="background: #373737; height: 40px;color: white;">
			<span style="color: #ec5944;font-size: 30px;margin-left: 5px;">STEM</span><span style="font-size: 27px;">achine.</span>
			
			<sapn id="outa" class="label" style="color:white;"><a style="color:white;" href="javascript:void(0);" onclick="logout()"><i class="fa fa-sign-out fa-lg">退出</i></a></sapn>
			<sapn id="helpa" class="label" style="color:white;margin-top:10px;"><a href="javascript:void(0);" onclick="helpa()" style="color:white;"><i class="fa fa-question-circle fa-lg">帮助</i></a></sapn>
			
			<span id="teatspan" class="cssa"  style="margin-left: 20px;margin-right:30px;">
				老师
			</span>
			<span id="namespan" style="display: block;float: right;margin-top: 8px;">
				姓名
			</span>
			<sapn class="label" style="color:white;"><i class="fa fa-user fa-lg"></i></sapn>
		</div>
    	<div style="margin-bottom:4%;">
    	<div class="sigma-content">
		     <div class="sigma-middle-line">
		        <span class="sigma-line-text" id="tclasslist">班级列表</span>
		         <span class="sigma-line-text" id="tstudentlist" style="margin-left: 14%;">学生列表</span>
		    </div>
		</div>
		
		<div id="classlist" style="overflow-y: auto; border:0.1px solid darkgray;width: 23.5%;height: 770px;float: left;margin-top: 5px;margin-left:5px">
		</div>
    	
    	<div id="" style="border:0.1px solid #fffeff;width: 74%;height: 770px;margin-top: 5px;margin-left: 25%;margin-right:5px;">
    		<div>
    			<div id="searchdiv" class="input-group col-md-3" style="margin-top:0px positon:relative;float: left;border:1px solid #ec5944;">  
                    <input id="searchinput" type="text" class="form-control" placeholder="请输入姓名" style="background: #fffeff; border:0.3px solid #ec5944;"/>  
                    <span class="input-group-btn">  
                      <button id="searchbutton" onclick="search()" class="btn btn-info btn-search" style="background: #ec5944;color: white;border:1px solid #ec5944;">查找</button>   
                    </span>  
                  </div>  
	    		<span id="sidspan" class="span1" style="margin-left: 3px;" onclick="orderbyid(this.id)">
    				学号
    			</span>
    			<span id="snamespan" class="span1" onclick="orderbyname(this.id)">
    				姓名
    			</span>
    		</div>
    		<div id="studentlist" style="overflow-y: auto;text-align: center;margin-top: 36px;width:100%;height:95.5%;border:0.1px solid darkgray;">
    		</div>
    	</div>
    	</div>
    	<div id="thirdDiv" align="center" class="thirdDivClass" style="position: fixed;">
			<span style="color: white;display: block;line-height: 40px;">Copyright &copy; 2018.Wistron Corporation All Rights Peserved.</span>
		</div>	
	</div>
	 <input type="hidden" id="teaname" value="${name}">
  </body>
<script type="text/javascript">
//1.页面加载时，根据id查询数据库，查出老师的姓名和所负责的班级
  //获取请求路径中的id参数
  var number=$("#teaname").val();
  var sgrade=null;
  var sclass=null;
  var lan = navigator.language
  var lans=['zh','zh-CN','zh-TW'];
  //页面加载时，将根据url传来的参数，查询数据库，并在页面显示
  $(window).load(function(){
    //如果是中文，显示中，老师与姓名不换位置
    if(IsInArray(lans,lan)){ 
		 $('#teatspan').html($.i18n.prop('tteacher'));
		 $("#namespan").text(number);
		 }else{
			 $('#teatspan').html(number);
			 $("#namespan").text($.i18n.prop('tteacher'));
		 }
	  
	  toastr.options.positionClass = 'toast-top-center';
	  toastr.options.timeOut = '1000';
	  
	   var url="/teacher/queryTeacher.taction";
      $.ajax({
    	  type:"get",
    	  url:url,
    	  datdType:"json",
    	  success: function (teachers) {
    		for(var i=0;i<teachers.length;i++){
                  var tc = teachers[i];
				   if(IsInArray(lans,lan)){
				     var newdiv=$("<div id='div" +i
                		  + "' class='classdiv' onclick='divclick(this.id,this.innerHTML)'><font>"
                		  +tc.grade+"</font> "+$.i18n.prop('teagrad')+" <font>"+tc.cno+"</font> "+$.i18n.prop('teaclass')+"</div>");
				   }else{
				       var newdiv=$("<div id='div" +i
                		  + "' class='classdiv' onclick='divclick(this.id,this.innerHTML)'>"
                		  +$.i18n.prop('teaclass')+" <font>"+tc.cno+"</font> "+$.i18n.prop('teagrad')+" <font>"+tc.grade+"</font>");
				   }
                  
                  $("#classlist").append(newdiv);
              }; 
    	  },
    	  error : function() {
    		  toastr.warning($.i18n.prop('terrortip2'));
              
           },
           complete : function(){
        	  recoverData();  
           }
      });
  });
//2.点击退出时
  function logout(){
      bootbox.confirm({
          message: $.i18n.prop('ttip1'), 
          buttons: {
              confirm: {
                label: $.i18n.prop('ttip4')
              },
              cancel:{
                  label:$.i18n.prop('ttip5')
              }
            },        
          callback: function(result) {
              if(result) {
            	  $.ajax({
        			  type:"get",
        	    	  url:"/logout.action", 
        	    	  success: function (msg){
        	    		  location.href="/htm/login.html"; 
        	    	  },
        	    	  error : function() {
        	    		 bootbox.alert($.i18n.prop('terrortip'));
        	           },
        		 });
              }
          }
      });      
  }
//3.点击班级列表动态生成的，班级div时，将动态加载本班的学生们的姓名和编号
  //动态生成学生div，添加预设样式，并显示在学生列表中
   function divclick(id){
//保存班级div的id
	localStorage.setItem("cdivid",id);
	   $("#snamespan").text($.i18n.prop('ttip9'));
	   $("#snamespan").css({"background":"#fffeff","color":"black"});
      
	   $("#sidspan").text($.i18n.prop('ttip8'));
	   $("#sidspan").css({"background":"#fffeff","color":"black"});
 //3.1触发点击事件后首先修改自身的样式
	  $("#classlist div").css({"background":"#fffeff","color":"black"});
	  $("#"+id).css({"background":"#ec5944","color":"white"});
	//获取年级和班级信息备用
	if(IsInArray(lans,lan)){
	 var grade=$("#"+id).children('font').eq(0).text();
	 var cno=$("#"+id).children('font').eq(1).text();
	}else{
	 var grade=$("#"+id).children('font').eq(1).text();
	 var cno=$("#"+id).children('font').eq(0).text();
	}
	 sgrade=grade;
	 sclass=cno;
 //3.2发送ajax请求本班级学生的信息，并显示在学生列表中
	  var url="/student/queryStudent.taction?grade="+grade;
	  var tail="&cno="+cno;
	  url=url+tail;
	  $.ajax({
   	  type:"GET",
   	  url:url,
   	  cache:true,
   	  datdType:"json",
   	  async: false,
   	  success: function (students) {
   		$("#studentlist").html("");
   		 for(var i=0;i<students.length;i++){
                 var st = students[i];
                 var newdiv=$("<div id='" +st.sno
               		  + "' class='studentdiv' onclick='sdivclick(this.id)'><img src='/timages/2.png'><br/>"
               		  +st.sname+"<br/>"+st.sno+"</div>");
                 $("#studentlist").append(newdiv);
             };  
   	  },
   	  error : function() {
   		toastr.warning($.i18n.prop('terrortip2'));
             
          },
     });
   };
//4,点击学生头像的点击事件
   function sdivclick(id){
	  // var id=id;
	 //保存选中的学生div的id
	localStorage.setItem("sdivid",id);
 //4.1点击学生头像，首先修改样式
   //4.1.1将所有的学生样式还原包括图片
	    $("#studentlist div").css({"background":"#fffeff","color":"black"});
	    $("#studentlist img").prop("src","/timages/2.png");
   //4.1.2将点击的学生的头像样式改为预设样式
		$("#"+id).css({"background":"#ec5944","color":"white"});
		$("#"+id).children('img').eq(0).prop("src","/timages/3.png");
 //4.2转发到学生查看界面
	selectThis(id);
	 
   }
   
//5.按名字查询某班学生功能
   function search(){
	      var text=$("#searchdiv input").val().trim();
		  localStorage.setItem("textname",text);
		  //此处存储text是用来做状态恢复函数判断的
		  var url="/student/searchStudent.taction?sname="+text;
		  var mtail="&grade="+sgrade;
		  var ltail="&cno="+sclass;
			  url+=mtail;
			  url+=ltail;
			 $.ajax({
			   	  type:"get",
			   	  url:url,
			   	  datdType:"json",
			   	  async: false,
			   	  success: function (students) {
			   		$("#studentlist").html("");
			   		 for(var i=0;i<students.length;i++){
			                 var st = students[i];
			                 var newdiv=$("<div id='" +st.sno
			               		  + "' class='studentdiv' onclick='sdivclick(this.id)'><img src='/timages/2.png'><br/>"
			               		  +st.sname+"<br/>"+st.sno+"</div>");
			                 $("#studentlist").append(newdiv);
			             };  
			   	  },
			   	  error : function() {
			   		toastr.warning($.i18n.prop('terrortip2'));
			             
			          },
			     });
	  
   }
//6.按学号排序
   function orderbyid(){
	//按学号是0,此处的0是用来状态恢复函数的判断
	   localStorage.setItem("orderid",0);
	   localStorage.removeItem("textname");
   //在进行按学号排序时，可能是排序已经按search查找的数据，
   //此处的解决方案是：传入输入框的值text再次查询，如果text不为空
      var text=$("#searchdiv input").val().trim();
   
	   $("#snamespan").text($.i18n.prop('ttip9'));
	   $("#snamespan").css({"background":"#fffeff","color":"black"});
	   
	   $("#sidspan").text($.i18n.prop('ttip2'));
	   $("#sidspan").css({"background":"#ec5944","color":"white"});
		 
	      var url="/student/orderStudent.taction?span=idspan&name="+text+"&grade="+sgrade;
		  var tail="&cno="+sclass;
		  url+=tail;
		  if(sgrade!==null){
			  $.ajax({
			   	  type:"get",
			   	  url:url,
			   	  datdType:"json",
			   	  async: false,
			   	  success: function (students) {
			   		$("#studentlist").html("");
			   		 for(var i=0;i<students.length;i++){
			                 var st = students[i];
			                 var newdiv=$("<div id='" +st.sno
			               		  + "' class='studentdiv' onclick='sdivclick(this.id)'><img src='/timages/2.png'><br/>"
			               		  +st.sname+"<br/>"+st.sno+"</div>");
			                 $("#studentlist").append(newdiv);
			             };  
			   	  },
			   	  error : function() {
			   		toastr.warning($.i18n.prop('terrortip2'));
			             
			          },
			     });
			  }else{
				  toastr.warning($.i18n.prop('terrortip1'));
			  }
	   
   }
//7.按姓名排序
   function orderbyname(){
	//按姓名是1，此处的1是用来状态恢复函数的判断
	  localStorage.setItem("orderid",1);
	  localStorage.removeItem("textname");
	  
	//在进行按姓名排序时，可能是排序已经按search查找的数据，
	//此处的解决方案是：传入输入框的值text再次查询，如果text不为空
	  var text=$("#searchdiv input").val().trim();
	
	
	  $("#sidspan").text($.i18n.prop('ttip8'));
	  $("#sidspan").css({"background":"#fffeff","color":"black"});
	  
	  $("#snamespan").text($.i18n.prop('ttip3'));
	  $("#snamespan").css({"background":"#ec5944","color":"white"});
	 
	      var url="/student/orderStudent.taction?span=namespam&name="+text+"&grade="+sgrade;
	      var tail="&cno="+sclass;
		  url+=tail;
		  if(sgrade!==null){
			  $.ajax({
			   	  type:"get",
			   	  url:url,
			   	  datdType:"json",
			      async: false,
			   	  success: function (students) {
			   		$("#studentlist").html("");
			   		 for(var i=0;i<students.length;i++){
			                 var st = students[i];
			                 var newdiv=$("<div id='" +st.sno
			               		  + "' class='studentdiv' onclick='sdivclick(this.id)'><img src='/timages/2.png'><br/>"
			               		  +st.sname+"<br/>"+st.sno+"</div>");
			                 $("#studentlist").append(newdiv);
			             };  
			   	  },
			   	  error : function() {
			   		toastr.warning($.i18n.prop('terrortip2'));
			          },
			     });
		  }else{
			  toastr.warning($.i18n.prop('terrortip1'));
		  }
}
//封装成from交给后台，跳转页面
   function selectThis(sno) {
			    document.write("<form action=/stemachine/studentdetail.do method=post name=form1 style='display:none'>");
			    document.write("<input type=hidden name=sno value='"+sno+"'/>");
			    document.write("</form>");
			    document.form1.submit();
   }
 //状态恢复函数
    function recoverData(){
	 var cdivid=localStorage.getItem("cdivid");
	 var orderid=localStorage.getItem("orderid");
	 var sdivid=localStorage.getItem("sdivid");
	 var textname=localStorage.getItem("textname");
     var val=$("#"+cdivid).text();
	 if(cdivid!=null&&sdivid!=null){
		if(textname==null){
			 if(orderid!=null&&orderid==0){
				 divclick(cdivid,val);
				 orderbyid();
				 $("#"+sdivid).css({"background":"#ec5944","color":"white"});
				 $("#"+sdivid).children('img').eq(0).prop("src","/timages/3.png");
			 }else if(orderid!=null&&orderid==1){
				 divclick(cdivid,val);
				 orderbyname();
				 $("#"+sdivid).css({"background":"#ec5944","color":"white"});
				 $("#"+sdivid).children('img').eq(0).prop("src","/timages/3.png");
			 }else{
				  divclick(cdivid,val);
				  $("#"+sdivid).css({"background":"#ec5944","color":"white"});
				  $("#"+sdivid).children('img').eq(0).prop("src","/timages/3.png");
			 }
		}else{
			$("#searchdiv input").val(textname);
			divclick(cdivid,val);
			search();
			$("#"+sdivid).css({"background":"#ec5944","color":"white"});
			$("#"+sdivid).children('img').eq(0).prop("src","/timages/3.png");
		} 
	}
    }
 //enter键
    $("#searchinput").keyup(function(event){
        if(event.keyCode ==13){
         $("#searchbutton").trigger("click");
         }
       });
    function helpa(){
	    if(lan=='zh'||lan=='zh-CN'){
		//中简
		  location.href="/help/onlinehelp_teacher_SC_20180531A.htm";
		}else if(lan=='zh-TW'){
		//中繁
		 location.href="/help/onlinehelp_teacher_TC_20180531A.htm";
		}else{
		 location.href="/help/onlinehelp_teacher_EN_20180531A.htm";
		//en
		}
    	
    }
 
   //背景白色#fffeff
   //背景红色#ec5944
   //背景黑色#373737
</script>
</html>