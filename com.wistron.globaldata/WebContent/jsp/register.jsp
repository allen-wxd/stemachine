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
    </style>
    <script type="text/javascript">
    toastr.options.positionClass = 'toast-top-center';
    </script>
    <body>
        <form action="/user/public/register.action" id="registerForm" method="post">
            <div class="mycenter">
            <div class="mysign">
                <div class="col-lg-11 text-center text-info">
                    <h2>请填写注册相关信息</h2>
                </div>
                <div class="col-lg-10">
                    <input type="text" class="form-control" name="username" placeholder="请输入账户名" required autofocus/>
                </div>
                <div class="col-lg-10"></div>
                <div class="col-lg-10">
                    <input type="password" class="form-control" name="password" placeholder="请输入密码" required autofocus/>
                </div>
                 <div class="col-lg-10"></div>
                <div class="col-lg-10">
                    <input type="text" class="form-control" name="email" placeholder="请输入邮箱" required autofocus/>
                </div>
                 <div class="col-lg-10"></div>
                <div class="col-lg-10">
                    <input type="text" class="form-control" name="tel" placeholder="请输入手机号码" required autofocus/>
                </div>
                 <div class="col-lg-10"></div>
                <div class="col-lg-10">
                    <input type="text" class="form-control" name="realname" placeholder="请输入真实姓名" required autofocus/>
                </div>
                <div class="col-lg-10"></div>
                <div class="col-lg-10">
                    <button type="submit" class="btn btn-warning col-lg-12">注册</button>
                </div>
            </div>
        </div>
        </form>
    </body>
</html>