if (typeof jQuery === 'undefined') {
  throw new Error('syhh.date requires jQuery library.');
}

(function ($) {

  /**
   * 验证日期是否符合格式yyyy/mm/dd、yyyy-mm-dd，
   * 缺点不能判断闰年二月天数
   */
  $.fn.extend(options)
  {
    /**
     * 31天 yyyy/mm/dd、yyyy-mm-dd
     * @type {RegExp}
     */
    var patternLong = /^\d{4}(\-|\/)(0[13578]|(1[02]))(\-|\/)((0[1-9])|([12]\d)|(3[01]))$/;

    /**
     * 30天 yyyy/mm/dd、yyyy-mm-dd
     * @type {RegExp}
     */
    var patternMiddle = /^\d{4}(\-|\/)(0[469]|(11))(\-|\/)((0[1-9])|([12]\d)|(30))$/;

    /**
     * 2月 yyyy/mm/dd、yyyy-mm-dd
     * @type {RegExp}
     */
    var patternShort = /^\d{4}(\-|\/)(02)(\-|\/)((0[1-9])|([12]\d))$/;

    var flag = false;

    if (patternLong.test(options) || patternMiddle.test(options)
        || patternShort.test(options)) {
      flag = true
    }
    return flag;
  }



})(jQuery);