<%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>程式執行</title>
<link rel="stylesheet" href="/css/bootstrap.min.css">
<!--  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>-->
<script src="/js/jquery.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<style>
#wrapper {
  margin-right: 200px;
}
#content {
  float: left;
  width: 60%;
  background-color: #CCC;
  background-image: url("/images/luca-bravo-198061.jpg");
}
#sidebar {
  float: right;
  width: 500px;
  margin-right: -200px;
  background-color: #FFF;
}
#cleared {
  clear: both;
}
</style>
	<script language='javascript'>
	$(document).ready(function(){
		//视频效果刚开始是隐藏的
		$('#sidebar').hide();
		
		$('#compile').click(function() {
			checkMe();
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
				Sleep(20000);
				$('#view').attr('disabled', false);
				//??Class什么意思，是样式？
				$('#view').removeClass('btn-default').addClass('btn-primary');
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
			//不太懂，感觉在拼一些文本
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
		request.open("GET", "apicall.jsp?method=D103Q&command=robot123%20"+section+"%20"+parameter);
		request.send();
		request.onreadystatechange = function() {
				// 伺服器回應成功
					if (request.status === 200) {
					var type = request.getResponseHeader("Content-Type");   // 取得回應類型
					// 判斷回應類型，這裡使用 JSON
					if (type.indexOf("application/json") === 0) {               
						var data = JSON.parse(request.responseText);
						if (data.rtn_code===0) {
							//alert(data.message);
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

	
	</script>
</head>
<body>
<div id="wrapper">
  <div id="content">
	<div class='special-testimony'><h2>輸入機器碼</h2></div>
	<textarea class='special-testimony' id='command' rows="20" cols="70">
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
	<p/>
	<button type="button" class="btn btn-primary" id='compile'>編譯</button>
	<button type="button" class="btn default" id='execute' disabled>執行</button>
	<button type="button" class="btn default" id='view' disabled>檢視成果</button>
	<button type="button" class="btn default" id='export' disabled>成果匯出</button>
	
  </div>
	<div id="sidebar">
	<div class="video">
		<video id="video" src="" type="video/mp4" width="400" height="600" controls ></video>
	</div>
	</div>
	<div id="cleared"><div id='errMsg'></div></div>
</div>
</body>
</html>