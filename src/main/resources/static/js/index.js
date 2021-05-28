
$(function () {
    //input 清空
    $("#xxm-qk").click(function () {
        $(".xxm-head-result-com").remove();

    })
    $(".xxm-head-com p").click(function (e) {
        let value = e.currentTarget.innerText;
        $(".index-input").val(value);
        window.open('../Netease Cloud Classroom/web/result.html');
    });
    //input回车
    $(".index-input").bind('keydown',function (event) {
        if(event.keyCode == "13")
        {
            let value = $(".index-input").val();
            window.location.href='../Netease Cloud Classroom/web/result.html?value='+value;
        }
    });

    //课程分类点击事件传递参数
    $(".xxm-class-left li").click(function (e) {
        let value = e.currentTarget.innerText;
        console.log(value);
        window.location.href='../Netease Cloud Classroom/web/Specificclass.html?value='+value;
    })

});