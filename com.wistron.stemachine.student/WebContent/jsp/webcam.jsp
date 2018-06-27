<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>程式執行</title>
<link rel="stylesheet" href="/css/bootstrap.min.css">
<!--  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>-->
<script src="/js/jquery.min.js">
<script src="/js/bootstrap.min.js"></script>
<style>
body {  
    background-color: #000;
	background-size:     cover;                      
    background-repeat:   no-repeat;
    background-position: center center;
}
#title{
	float: up;
}
#work {
	float: down;
}
#header {
	float: left;
	background-color:transparent;
}
h1 {
	text-align:left;
	font-style: oblique;
	font: bold arial, sans-serif;
	color:white;
}
h2 {
	text-align:left;
	font: bold arial;
	color:white;
}
h3 {
	text-align:left;
	font: bold arial;
	color:green;
}

textarea {
	background-color: #000;
	color:green;
}

#logo {
	float: right;
	
}
#wrapper {
  margin-right: 150px;
  background-color:transparent;
}
#lbarrier {
  float: left;	
  width: 1%;
}
#content {
  float: left;
  width: 25%;
  background-color:transparent;
}
#sidebar {
  float: right;
  width: 20%;
  margin-right: 350px;
  background-color: #001
  background-color:transparent;
}
#cleared {
  clear: both;
}

</style>
	<script language='javascript'>

	
	$(document).ready(function(){
	"use strict";
	var video = document.getElementById('video');
	var canvas = document.getElementById('canvas');
	var videoStream;
	var preLog = document.getElementById('preLog');
	start();
		$('#compile').click(function() {
			checkMe();
			stop();
		})
		$('#execute').click(function() {
			var flag = $('#excute').attr('disabled');
			if (flag) {
				console.log(true);
			} else {
				console.log(false);
				alert("已將您的程式碼送到雲端，請等一會兒再回來看執行成果，謝謝!!");
				$('#execute').attr('disabled', true);
				var cmd = $('#command').val();
				eval(cmd);
				Sleep(1000);
				start();
				$('#export').attr('disabled', false);
				$('#export').removeClass('btn-default').addClass('btn-primary');
				
			}
		})
		$('#view').click(function(){
			$('#sidebar').show();
		})
		$('#export').click(function() {
			var exportText = $('#command').val();
			exportText = exportText+"\n";
			exportText = exportText+"http://192.168.2.101/video/homework_0521.mp4";
			exportText = exportText+"\n";
			var link = document.createElement('a');
			var mimeType = mimeType || 'text/plain';
			link.setAttribute('download', 'export.txt');
			link.setAttribute('href', 'data:' + mimeType  +  ';charset=utf-8,' + encodeURIComponent(exportText));
			link.click(); 
		})
	})

	
function ServoControl(section,parameter) {
		var request;		
		if (window.XMLHttpRequest) {
			request = new XMLHttpRequest();
		} else {
			// code for IE6, IE5
			request = new ActiveXObject("Microsoft.XMLHTTP");
		}				
		request.open("GET", "/teaching/apicall.do?method=D103Q&command=robot123%20"+section+"%20"+parameter);
		request.send();
		request.onreadystatechange = function() {
				// 伺服器回應成功
					if (request.status === 200) {
					var type = request.getResponseHeader("Content-Type");   // 取得回應類型
					// 判斷回應類型，這裡使用 JSON
					if (type.indexOf("application/json; charset=utf-8") === 0) { 
						var data = JSON.parse(request.responseText);
						if (data.rtn_code===0) {
							alert(data.message);
							console.log(data.message);
						} else {
							//alert("wrong!");
							console.log(data.message);
						}
					}
			}
		}

}

function Sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

function checkMe() {
	var cmd = $('#command').val();
	var check = true;
	var array = cmd.split("\n");
	var index;
	var returnHtml = "";
	var mark = false;
	$("#errMsg").html("");
	for (index = 0; index < array.length; ++index) {
		if (mark===true) {
			if (array[index].indexOf("*/")>=0) {
				console.log("第"+(index+1)+"行為註解結束");
				mark = false;
			}
			continue;
		}
		if (array[index].indexOf("//")===0) {
			console.log("第"+(index+1)+"行為註解不處理");
		} else {
			if (array[index].indexOf("/*")===0) {
				if (mark === false) {
					console.log("第"+(index+1)+"行為註解起頭不處理");
					mark = true;
				} 				
			} else {
				if (array[index].indexOf("(")>0) {
					if (array[index].indexOf(")")<0) {
						check = false;
						returnHtml = returnHtml+"<p style='color:red'>"+array[index]+" 語法錯誤</p>";
					} else {
						var parameter = array[index].substring(array[index].indexOf("(")+1,array[index].indexOf(")"));
						console.log(parameter);
						if (parameter.indexOf(",")>0) {
													var parameters = parameter.split(",");
							if (parameters[0] > 6) {
								returnHtml = returnHtml+"<p style='color:red'>"+array[index]+" 馬達不存在</p>";
								check = false;
							} else {
								if (parameters[0] <= 0) {
									returnHtml = returnHtml+"<p style='color:red'>"+array[index]+" 馬達不存在</p>";
									check = false;
								} else {
									if (parameters[1] <0) {
										returnHtml = returnHtml+"<p style='color:red'>"+array[index]+" 馬達參數錯誤</p>";
										check = false;
									} else {
										if (parameters[1] > 3000) {
											returnHtml = returnHtml+"<p style='color:red'>"+array[index]+" 馬達參數錯誤</p>";
											check = false;
										}
									}
								}
							}
						}
					}
					/*if (array[index].indexOf(";")<0) {
						check = false;
						returnHtml = returnHtml+"<p style='color:red'>"+array[index]+"語法錯誤</p>";
					}*/
				} else {
					if (array[index].trim().length>0) {
						check = false;
						returnHtml = returnHtml+"<p style='color:red'>"+array[index]+" 語法錯誤</p>";
					} 					
				}
			}
		}

	}
	if (check) {
		console.log(true);
		$('#compile').removeClass('btn-primary').addClass('btn-success');
		$('#execute').attr('disabled', false);
		$('#execute').removeClass('btn-default').addClass('btn-primary');
		
	} else {
		console.log(returnHtml);
		$("#errMsg").append( returnHtml);
		
	}
}

function Backforward() {
	//復位
	ServoControl(1, 1500);
	Sleep(500);
	ServoControl(2, 1500);
	Sleep(500);
	ServoControl(3, 2000);
	Sleep(500);
	ServoControl(4, 1500);
	Sleep(500);
	ServoControl(5, 1500);
	Sleep(500);
	ServoControl(6, 1500);
	Sleep(500);
}

function start()
{
	if ((typeof window === 'undefined') || (typeof navigator === 'undefined')) log('This page needs a Web browser with the objects window.* and navigator.*!');
	else if (!(video)) log('HTML context error!');
	else
	{
		log('Get user media…');
		if (navigator.getUserMedia) navigator.getUserMedia({video:true}, gotStream, noStream);
		else if (navigator.oGetUserMedia) navigator.oGetUserMedia({video:true}, gotStream, noStream);
		else if (navigator.mozGetUserMedia) navigator.mozGetUserMedia({video:true}, gotStream, noStream);
		else if (navigator.webkitGetUserMedia) navigator.webkitGetUserMedia({video:true}, gotStream, noStream);
		else if (navigator.msGetUserMedia) navigator.msGetUserMedia({video:true, audio:false}, gotStream, noStream);
		else log('getUserMedia() not available from your Web browser!');
	}
}

function noStream()
{
	log('Access to camera was denied!');
}

function stop()
{
	if (videoStream)
	{
		if (videoStream.stop) videoStream.stop();
		else if (videoStream.msStop) videoStream.msStop();
		videoStream.onended = null;
		videoStream = null;
	}
	if (video)
	{
		video.onerror = null;
		video.pause();
		if (video.mozSrcObject)
			video.mozSrcObject = null;
		video.src = "";
	}
}

function gotStream(stream)
{
	videoStream = stream;
	log('Got stream.');
	video.onerror = function ()
	{
		log('video.onerror');
		if (video) stop();
	};
	stream.onended = noStream;
	if (window.webkitURL) video.src = window.webkitURL.createObjectURL(stream);
	else if (video.mozSrcObject !== undefined)
	{//FF18a
		video.mozSrcObject = stream;
		video.play();
	}
	else if (navigator.mozGetUserMedia)
	{//FF16a, 17a
		video.src = stream;
		video.play();
	}
	else if (window.URL) video.src = window.URL.createObjectURL(stream);
	else video.src = stream;
}

function log(text)
{
	if (preLog) preLog.textContent += ('\n' + text);
	else alert(text);
}
	
	</script>
</head>
<body>
<table width="100%">
<tr>
<td align="left"><h1>STEMachine創客工作坊</h1></td>
<td align="center"><h3>欢迎登录${sessionScope.loginname} </h3></td>
<td align="right"><img src="/images/logo.png"></td>
</tr>
</table>
<p/>
<div id="work">
<div id="wrapper">
  <div id="lbarrier">
   &nbsp;&nbsp;
  </div>
  <div id="content">
	<div><h2>輸入程式碼</h2></div>
	<textarea id='command' rows="18" cols="65">
ServoControl(1, 1500);
Sleep(500);
ServoControl(2, 1500);
Sleep(500);
ServoControl(3, 2000);
Sleep(500);
ServoControl(4, 1500);
Sleep(500);
ServoControl(5, 1500);
Sleep(500);
ServoControl(6, 1500);
Sleep(500);			
	</textarea>
	<button type="button" class="btn btn-primary" id='compile'>編譯</button>
	<button type="button" class="btn btn-info" id='execute' disabled>執行</button>
	<button type="button" class="btn default" id='export' disabled>成果匯出</button>
    </div>
	<div id="sidebar">
	 <div><h2>成果展示影片</h2></div>
	<div class="video">
		<!--<video id="video" src="http://192.168.1.32:8080/stem.ogg" type="video/ogg" width="640px" height="400px"></video>-->
		<video id="video" width="800px" height="450px" autoplay="autoplay"></video>
		<canvas id="canvas"></canvas>
		<div id="preLog"></div>
	</div>
	</div>
	<div id="cleared"><div id='errMsg'></div></div>
	<p/>
</div>
</div>
</body>
</html>