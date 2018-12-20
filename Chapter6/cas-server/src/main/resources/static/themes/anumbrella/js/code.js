function changeCode(){
    var node = document.getElementById("captcha_img");
    //修改验证码
    if (node){
        if(url.indexOf("?") >= 0){
            node.src = url.split('?')[0] +'?id='+uuid();
        }else{
            node.src = url +'?id='+uuid();
        }
    }
}


function uuid(){
    //获取系统当前的时间
    var d = new Date().getTime();
    //替换uuid里面的x和y
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        //取余 16进制
        var r = (d + Math.random()*16)%16 | 0;
        //向下去整
        d = Math.floor(d/16);
        //toString 表示编程16进制的数据
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
};
