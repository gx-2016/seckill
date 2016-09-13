<!--引入jstl-->
<%@include file="common/tag.jsp"%>
<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>秒杀详情</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>${seckill.name}</h2>
        </div>
        <div class="panel-body">
            <h2 class="text-danger text-center">
                <!--显示计时图标-->
                <span class="glyphicon glyphicon-time"></span>
                <span class="glyphicon" id="seckill-box"></span>
                <!--显示倒计时-->
            </h2>
        </div>
    </div>
<!--登陆弹出层 输入号码-->
    <div id="killPhoneModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                  <h3> <span class="glyphicon glyphicon-phone"></span>秒杀电话：</h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhoneKey"
                                   placeholder="填写手机号(ˇˍˇ）" class="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success">
                        <span class="glyphicon glyphicon-iphone"></span>
                        提交
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div>
</body>


<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<!--cdn 倒计时和cookie控件-->
<script src="//cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
<script src="//cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<!--交互逻辑-->
<script type="text/javascript" src="/resources/js/seckill.js"></script>
<script type="text/javascript">
    $(function () {
        //使用EL表达式把参数传入js,参数为json格式
        seckill.detail.init({
            seckillId: ${seckill.seckillId},
            startTime:${seckill.startTime.time},//毫秒方便json解析
            endTime: ${seckill.endTime.time}
        });
    });

</script>
</html>