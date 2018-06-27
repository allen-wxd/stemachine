<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>用户登录</title>
        <script src="/loginjs/jquery.min.js"></script>
        <script src="/loginjs/jquery.js"></script>
        <script src="/loginjs/bootstrap.min.js"></script>
        <link rel="stylesheet" href="/logincss/bootstrap.min.css" />
        <script src="/loginjs/toastr.min.js"></script>
    	<link href="/logincss/toastr.css" rel="stylesheet" />
    </head>
    <style>
    	body {
    		background:#f8f6e9;
		}
		.mycenter{
		    margin-top: 100px;
		    margin-left: auto;
		    margin-right: auto;
		    height: 350px;
		    width:500px;
		    padding: 5%;
		    padding-left: 5%;
		    padding-right: 5%;
		}
		.mycenter mysign{
		    width: 440px;
		}
		.mycenter input,checkbox,button{
		    margin-top:2%;
		    margin-left: 10%;
		    margin-right: 10%;
		}
		.mycheckbox{
		    margin-top:10px;
		    margin-left: 40px;
		    margin-bottom: 10px;
		    height: 10px;
		}
    </style>
    <script type="text/javascript">
    toastr.options.positionClass = 'toast-top-center';
    $(function(){ 
    	msg = $("#msg").val();
    	if(msg!=""){
    		toastr.error(msg);  
    	}
    }); 
    
    function register(){
    	location.href="/login/register.jsp";
    }
    </script>
    <body>
    	<input type="hidden" id="msg" value="${msg}"/>
        <form action="/user/public/login.action" id="loginForm" method="post">
            <div class="mycenter">
            <div class="mysign">
                <div class="col-lg-11 text-center text-info">
                    <h2>请输入账户信息</h2>
                </div>
                <div class="col-lg-10">
                    <input type="text" class="form-control" name="username" placeholder="请输入账户名" required autofocus/>
                </div>
                <div class="col-lg-10"></div>
                <div class="col-lg-10">
                    <input type="password" class="form-control" name="password" placeholder="请输入密码" required autofocus/>
                </div>
                <div class="col-lg-10"></div>
                <div class="col-lg-10 mycheckbox checkbox">
                    <input type="checkbox" class="col-lg-1">记住密码</input>
                </div>
                <div class="col-lg-10"></div>
                <div class="col-lg-10">
                    <button type="submit" class="btn btn-success col-lg-12">登录</button>
                    <button type="button" onclick="register()" class="btn btn-warning col-lg-12">注册</button>
                </div>
            </div>
        	</div>
        </form>
    </body>
</html>