如今做web开发，jquery 几乎是必不可少的，就连vs神器在2010版本开始将Jquery 及ui 内置web项目里了。至于使用jquery好处这里就不再赘述了，用过的都知道。今天我们来讨论下jquery的插件机制，jquery有着成千上万的第三方插件，有时我们写好了一个独立的功能，也想将其与jquery结合起来，可以用jquery链式调用，这就要扩展jquery，写成插件形式了，如下面就是一个简单扩展Jquery对象的demo：


        //sample:扩展jquery对象的方法，bold()用于加粗字体。
        (function ($) {
            $.fn.extend({
                "bold": function () {
                    ///<summary>
                    /// 加粗字体
                    ///</summary>
                    return this.css({ fontWeight: "bold" });
                }
            });
        })(jQuery);

调用方式：

这是一个非常简单的扩展。接下来我们一步步来解析上面的代码。

一、jquery的插件机制

为了方便用户创建插件，jquery提供了jQuery.extend()和jQuery.fn.extend()方法。

1. jQuery.extend() 方法有一个重载。

 jQuery.extend(object) ,一个参数的用于扩展jQuery类本身，也就是用来在jQuery类/命名空间上增加新函数，或者叫静态方法，例如jQuery内置的 ajax方法都是用jQuery.ajax()这样调用的，有点像 “类名.方法名” 静态方法的调用方式。下面我们也来写个jQuery.extend(object)的例子：


        //扩展jQuery对象本身
        jQuery.extend({
            "minValue": function (a, b) {
                ///<summary>
                /// 比较两个值，返回最小值
                ///</summary>
                return a < b ? a : b;
            },
            "maxValue": function (a, b) {
                ///<summary>
                /// 比较两个值，返回最大值
                ///</summary>
                return a > b ? a : b;
            }
        });
        //调用
        var i = 100; j = 101;
        var min_v = $.minValue(i, j); // min_v 等于 100
        var max_v = $.maxValue(i, j); // max_v 等于 101

重载版本：jQuery.extend([deep], target, object1, [objectN])

   用一个或多个其他对象来扩展一个对象，返回被扩展的对象。
   如果不指定target，则给jQuery命名空间本身进行扩展。这有助于插件作者为jQuery增加新方法。
   如果第一个参数设置为true，则jQuery返回一个深层次的副本，递归地复制找到的任何对象。否则的话，副本会与原对象共享结构。
   未定义的属性将不会被复制，然而从对象的原型继承的属性将会被复制。
参数
   deep:       可选。如果设为true，则递归合并。
   target:     待修改对象。
   object1:   待合并到第一个对象的对象。
   objectN:   可选。待合并到第一个对象的对象。
示例1：
合并 settings 和 options，修改并返回 settings。
var settings = { validate: false, limit: 5, name: "foo" };
var options = { validate: true, name: "bar" };
jQuery.extend(settings, options);
结果：
settings == { validate: true, limit: 5, name: "bar" }

示例2：
合并 defaults 和 options, 不修改 defaults。
var empty = {};
var defaults = { validate: false, limit: 5, name: "foo" };
var options = { validate: true, name: "bar" };
var settings = jQuery.extend(empty, defaults, options);
结果：
settings == { validate: true, limit: 5, name: "bar" }
empty == { validate: true, limit: 5, name: "bar" }
这个重载的方法，我们一般用来在编写插件时用自定义插件参数去覆盖插件的默认参数。

jQuery.fn.extend(object)扩展 jQuery 元素集来提供新的方法（通常用来制作插件）。

首先我们来看fn 是什么东西呢。查看jQuery代码，就不难发现。

jQuery.fn = jQuery.prototype = {

init: function( selector, context ) {.....};
};

原来 jQuery.fn = jQuery.prototype，也就是jQuery对象的原型。那jQuery.fn.extend()方法就是扩展jQuery对象的原型方法。我们知道扩展原型上的方法，就相当于为对象添加”成员方法“，类的”成员方法“要类的对象才能调用，所以使用jQuery.fn.extend(object)扩展的方法， jQuery类的实例可以使用这个“成员函数”。jQuery.fn.extend(object)和jQuery.extend(object)方法一定要区分开来。

二、自执行的匿名函数/闭包

     1. 什么是自执行的匿名函数?
 它是指形如这样的函数: (function {// code})();
    2. 疑问 为什么(function {// code})();可以被执行, 而function {// code}();却会报错?
    3. 分析
       (1). 首先, 要清楚两者的区别:     (function {// code})是表达式, function {// code}是函数声明.
       (2). 其次, js"预编译"的特点:     js在"预编译"阶段, 会解释函数声明, 但却会忽略表式.
       (3). 当js执行到function() {//code}();时, 由于function() {//code}在"预编译"阶段已经被解释过, js会跳过function(){//code}, 试图去执行();, 故会报错;
    当js执行到(function {// code})();时, 由于(function {// code})是表达式, js会去对它求解得到返回值, 由于返回值是一 个函数, 故而遇到();时, 便会被执行.

   另外， 函数转换为表达式的方法并不一定要靠分组操作符()，我们还可以用void操作符，~操作符，!操作符……

  例如：
   bootstrap 框架中的插件写法：
   !function($){
//do something;
   }(jQuery);

   和
   (function($){
//do something;
   })(jQuery); 是一回事。

匿名函数最大的用途是创建闭包（这是JavaScript语言的特性之一），并且还可以构建命名空间，以减少全局变量的使用。
例如：
     var a=1;
     (function()(){
var a=100;
})();
      alert(a); //弹出 1
更多 闭包和匿名函数 可查看 Javascript的匿名函数与自执行 这篇文章。

三、一步一步封装JQuery插件

接下来我们一起来写个高亮的jqury插件
1.定一个闭包区域，防止插件"污染"

//闭包限定命名空间
(function ($) {

})(window.jQuery);
2.jQuery.fn.extend(object)扩展jquery 方法，制作插件


//闭包限定命名空间
(function ($) {
    $.fn.extend({
        "highLight":function(options){
            //do something
        }
    });
})(window.jQuery);

3.给插件默认参数，实现 插件的功能


//闭包限定命名空间
(function ($) {
    $.fn.extend({
        "highLight": function (options) {
            var opts = $.extend({}, defaluts, options); //使用jQuery.extend 覆盖插件默认参数
            this.each(function () {  //这里的this 就是 jQuery对象
                //遍历所有的要高亮的dom,当调用 highLight()插件的是一个集合的时候。
                var $this = $(this); //获取当前dom 的 jQuery对象，这里的this是当前循环的dom
                //根据参数来设置 dom的样式
                $this.css({
                    backgroundColor: opts.background,
                    color: opts.foreground
                });
            });

        }
    });
    //默认参数
    var defaluts = {
        foreground: 'red',
        background: 'yellow'
    };
})(window.jQuery);

到这一步，高亮插件基本功能已经具备了。调用代码如下：

$(function () {
    $("p").highLight(); //调用自定义 高亮插件
});
这里只能 直接调用，不能链式调用。我们知道jQuey是可以链式调用的，就是可以在一个jQuery对象上调用多个方法，如：
$('#id').css({marginTop:'100px'}).addAttr("title","测试“);
但是我们上面的插件，就不能这样链式调用了。比如：$("p").highLight().css({marginTop:'100px'}); //将会报找不到css方法，原因在与我的自定义插件在完成功能后，没有将 jQuery对象给返回出来。接下来，return jQuery对象，让我们的插件也支持链式调用。（其实很简单，就是执行完我们插件代码的时候将jQuery对像return 出来，和上面的代码没啥区别）



 1 //闭包限定命名空间
 2 (function ($) {
 3     $.fn.extend({
 4         "highLight": function (options) {
 5             var opts = $.extend({}, defaluts, options); //使用jQuery.extend 覆盖插件默认参数
 6             return this.each(function () {  //这里的this 就是 jQuery对象。这里return 为了支持链式调用
 7                 //遍历所有的要高亮的dom,当调用 highLight()插件的是一个集合的时候。
 8                 var $this = $(this); //获取当前dom 的 jQuery对象，这里的this是当前循环的dom
 9                 //根据参数来设置 dom的样式
10                 $this.css({
11                     backgroundColor: opts.background,
12                     color: opts.foreground
13                 });
14             });
15
16         }
17     });
18     //默认参数
19     var defaluts = {
20         foreground: 'red',
21         background: 'yellow'
22     };
23 })(window.jQuery);

View Code
4.暴露公共方法 给别人来扩展你的插件（如果有需求的话）
比如的高亮插件有一个format方法来格式话高亮文本，则我们可将它写成公共的，暴露给插件使用者，不同的使用着根据自己的需求来重写该format方法，从而是高亮文本可以呈现不同的格式。

    //公共的格式化 方法. 默认是加粗，用户可以通过覆盖该方法达到不同的格式化效果。
    $.fn.highLight.format = function (str) {
        return "<strong>" + str + "</strong>";
    }
5.插件私有方法
 有些时候，我们的插件需要一些私有方法，不能被外界访问。例如 我们插件里面需要有个方法 来检测用户调用插件时传入的参数是否符合规范。
6.其他的一些设置，如：为你的插件加入元数据插件的支持将使其变得更强大。

完整的高亮插件代码如下：


//闭包限定命名空间
(function ($) {
    $.fn.extend({
        "highLight": function (options) {
            //检测用户传进来的参数是否合法
            if (!isValid(options))
                return this;
            var opts = $.extend({}, defaluts, options); //使用jQuery.extend 覆盖插件默认参数
            return this.each(function () {  //这里的this 就是 jQuery对象。这里return 为了支持链式调用
                //遍历所有的要高亮的dom,当调用 highLight()插件的是一个集合的时候。
                var $this = $(this); //获取当前dom 的 jQuery对象，这里的this是当前循环的dom
                //根据参数来设置 dom的样式
                $this.css({
                    backgroundColor: opts.background,
                    color: opts.foreground
                });
                //格式化高亮文本
                var markup = $this.html();
                markup = $.fn.highLight.format(markup);
                $this.html(markup);
            });

        }
    });
    //默认参数
    var defaluts = {
        foreground: 'red',
        background: 'yellow'
    };
    //公共的格式化 方法. 默认是加粗，用户可以通过覆盖该方法达到不同的格式化效果。
    $.fn.highLight.format = function (str) {
        return "<strong>" + str + "</strong>";
    }
    //私有方法，检测参数是否合法
    function isValid(options) {
        return !options || (options && typeof options === "object") ? true : false;
    }
})(window.jQuery);

调用


        //调用
        //调用者覆盖 插件暴露的共公方法
        $.fn.highLight.format = function (txt) {
            return "<em>" + txt + "</em>"
        }
        $(function () {
            $("p").highLight({ foreground: 'orange', background: '#ccc' }); //调用自定义 高亮插件
        });
