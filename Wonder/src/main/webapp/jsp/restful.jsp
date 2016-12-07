<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>订单管理${pageContext.request.contextPath}</h1>
    <!--get请求获取订单-->
    <a href="/myspringmvc01/order/1">获取一个订单</a>
    <br>
    <!--post请求添加订单-->
    <form action="/myspringmvc01/order" method="post">
        <input type="submit" value="新增一个订单">
    </form>
    <br>
    <!--PUT请求修改订单-->
    <form action="/myspringmvc01/order/1" method="post">
        <input type="hidden" name="_method" value="PUT">
        <input type="submit" value="修改一个订单">
    </form>
    <br>
    <!--DELETE请求删除订单-->
    <form action="/myspringmvc01/order/1" method="post">
        <input type="hidden" name="_method" value="DELETE">
        <input type="submit" value="删除一个订单">
    </form>
</body>
</html>