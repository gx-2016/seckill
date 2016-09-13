//处理seckill的交互逻辑
//javascript 模块化
var seckill = {
    //封装秒杀相关的ajax地址
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer'
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    killPhoneMessage: {
        error: '手机号错误！'
    },
    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    //处理秒杀逻辑
    handlerSeckill: function (seckillId, node) {
        //增加秒杀按钮，暴露秒杀地址，执行秒杀
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">执行秒杀</button>').show();
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //秒杀开启
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:" + killUrl);
                    //防止按钮重复点击
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1.按钮失效
                        $(this).addClass('disabled');
                        //2.执行秒杀请求
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                            else {
                                console.log('result:' + result);
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启秒杀
                    var now = exposer['data'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.cutdown(seckillId, now, start, end);
                }
            } else {
                console.log('result:' + result);
            }
        });


    },
    //倒计时
    cutdown: function (seckillId, nowTime, startTime, endTime) {
        var seckill_box = $('#seckill-box');
        if (nowTime > endTime) {
            seckill_box.html('秒杀结束！');
        } else if (nowTime < startTime) {
            //秒杀未开始,显示倒计时
            var killTime = new Date(startTime + 1000);//秒杀开始时间+系统时间偏移
            seckill_box.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckill_box.html(format);
                //时间执行完的回调
            }).on('finish.countdown', function () {
                //秒杀开始
                seckill.handlerSeckill(seckillId, seckill_box);
            });
        }
        else {
            //秒杀开始
            seckill.handlerSeckill(seckillId, seckill_box);
        }


    },
    //详情页秒杀逻辑
    detail: {
        init: function (params) {
            //验证手机，计时交互，秒杀业务
            //在cookie中查找用户
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //没有登录，绑定phone
                //显示弹出层
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,//显示
                    backdrop: 'static', //关闭拖拽事件
                    keyboard: false//关闭键盘事件
                });
                // 按钮事件
                $('#killPhoneBtn').click(function () {
                    var killPhoneKey = $('#killPhoneKey').val();
                    if (seckill.validatePhone(killPhoneKey)) {
                        //放入cookie
                        $.cookie('killPhone', killPhoneKey, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<lable class="label label-danger">' +
                        seckill.killPhoneMessage.error + '</lable>').show(300);
                    }
                });
            } else {
                //已经登录，
                //获取系统时间，判断时间，倒计时
                $.get(seckill.URL.now(), {}, function (result) {
                    if (result && result['success']) {
                        var nowTime = result['data'];
                        seckill.cutdown(seckillId, nowTime, startTime, endTime);
                    } else {
                        console.log('result:' + result);
                    }
                });

            }

        }

    }
}