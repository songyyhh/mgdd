if (typeof jQuery === 'undefined') {
  throw new Error('syhh.regex requires jQuery library.');
}

(function ($) {

  /**
   * 获取字符串中中括号中间的数据，返回一个数组
   * 例如：入参  "我是[123]一名[456]程序猿[789]" 返回{"123","456","789"}
   * 拓展：圆括号（Parentheses） 大括号{Brackets}
   */
  $.fn.extend({
    "getContentsInMiddleBrackets": function (options) {
      // 获取中括号[]及内容
      if(!options){
        return null;
      }
      var regex = /\[(.+?)\]/g;
      var arr = options.match(regex); //[456]
      if(!arr){
        return null;
      }
      for (var i = 0; i < arr.length; i++) {
        // 去掉前后中括号[]
        arr[i] = arr[i].match(/(?<=\[).*?(?=\])/);
      }
      return arr;
    }
  });






})(jQuery);