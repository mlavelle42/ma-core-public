//>>built
define("dojox/date/islamic/Date",["dojo/_base/lang","dojo/_base/declare","dojo/date"],function(k,h,f){var g=h("dojox.date.islamic.Date",null,{_date:0,_month:0,_year:0,_hours:0,_minutes:0,_seconds:0,_milliseconds:0,_day:0,_GREGORIAN_EPOCH:1721425.5,_ISLAMIC_EPOCH:1948439.5,constructor:function(){var a=arguments.length;a?1==a?(a=arguments[0],"number"==typeof a&&(a=new Date(a)),a instanceof Date?this.fromGregorian(a):""==a?this._date=new Date(""):(this._year=a._year,this._month=a._month,this._date=a._date,
this._hours=a._hours,this._minutes=a._minutes,this._seconds=a._seconds,this._milliseconds=a._milliseconds)):3<=a&&(this._year+=arguments[0],this._month+=arguments[1],this._date+=arguments[2],this._hours+=arguments[3]||0,this._minutes+=arguments[4]||0,this._seconds+=arguments[5]||0,this._milliseconds+=arguments[6]||0):this.fromGregorian(new Date)},getDate:function(){return this._date},getMonth:function(){return this._month},getFullYear:function(){return this._year},getDay:function(){return this.toGregorian().getDay()},
getHours:function(){return this._hours},getMinutes:function(){return this._minutes},getSeconds:function(){return this._seconds},getMilliseconds:function(){return this._milliseconds},setDate:function(a){a=parseInt(a);if(!(0<a&&a<=this.getDaysInIslamicMonth(this._month,this._year))){var b;if(0<a)for(b=this.getDaysInIslamicMonth(this._month,this._year);a>b;a-=b,b=this.getDaysInIslamicMonth(this._month,this._year))this._month++,12<=this._month&&(this._year++,this._month-=12);else for(b=this.getDaysInIslamicMonth(0<=
this._month-1?this._month-1:11,0<=this._month-1?this._year:this._year-1);0>=a;b=this.getDaysInIslamicMonth(0<=this._month-1?this._month-1:11,0<=this._month-1?this._year:this._year-1))this._month--,0>this._month&&(this._year--,this._month+=12),a+=b}this._date=a;return this},setFullYear:function(a){this._year=+a},setMonth:function(a){this._year+=Math.floor(a/12);this._month=0<a?Math.floor(a%12):Math.floor((a%12+12)%12)},setHours:function(){var a=arguments.length,b=0;1<=a&&(b=parseInt(arguments[0]));
2<=a&&(this._minutes=parseInt(arguments[1]));3<=a&&(this._seconds=parseInt(arguments[2]));4==a&&(this._milliseconds=parseInt(arguments[3]));for(;24<=b;)this._date++,a=this.getDaysInIslamicMonth(this._month,this._year),this._date>a&&(this._month++,12<=this._month&&(this._year++,this._month-=12),this._date-=a),b-=24;this._hours=b},_addMinutes:function(a){a+=this._minutes;this.setMinutes(a);this.setHours(this._hours+parseInt(a/60));return this},_addSeconds:function(a){a+=this._seconds;this.setSeconds(a);
this._addMinutes(parseInt(a/60));return this},_addMilliseconds:function(a){a+=this._milliseconds;this.setMilliseconds(a);this._addSeconds(parseInt(a/1E3));return this},setMinutes:function(a){this._minutes=a%60;return this},setSeconds:function(a){this._seconds=a%60;return this},setMilliseconds:function(a){this._milliseconds=a%1E3;return this},toString:function(){if(isNaN(this._date))return"Invalidate Date";var a=new Date;a.setHours(this._hours);a.setMinutes(this._minutes);a.setSeconds(this._seconds);
a.setMilliseconds(this._milliseconds);return this._month+" "+this._date+" "+this._year+" "+a.toTimeString()},toGregorian:function(){var a=this._year,a=this._date+Math.ceil(29.5*this._month)+354*(a-1)+Math.floor((3+11*a)/30)+this._ISLAMIC_EPOCH-1,a=Math.floor(a-0.5)+0.5,b=a-this._GREGORIAN_EPOCH,c=Math.floor(b/146097),d=this._mod(b,146097),b=Math.floor(d/36524),e=this._mod(d,36524),d=Math.floor(e/1461),e=this._mod(e,1461),e=Math.floor(e/365),c=400*c+100*b+4*d+e;4==b||4==e||c++;b=a-(this._GREGORIAN_EPOCH+
365*(c-1)+Math.floor((c-1)/4)-Math.floor((c-1)/100)+Math.floor((c-1)/400));d=this._GREGORIAN_EPOCH-1+365*(c-1)+Math.floor((c-1)/4)-Math.floor((c-1)/100)+Math.floor((c-1)/400)+Math.floor(739/12+(f.isLeapYear(new Date(c,3,1))?-1:-2)+1);d=a<d?0:f.isLeapYear(new Date(c,3,1))?1:2;b=Math.floor((12*(b+d)+373)/367);d=this._GREGORIAN_EPOCH-1+365*(c-1)+Math.floor((c-1)/4)-Math.floor((c-1)/100)+Math.floor((c-1)/400)+Math.floor((367*b-362)/12+(2>=b?0:f.isLeapYear(new Date(c,b,1))?-1:-2)+1);return new Date(c,
b-1,a-d+1,this._hours,this._minutes,this._seconds,this._milliseconds)},fromGregorian:function(a){a=new Date(a);var b=a.getFullYear(),c=a.getMonth(),d=a.getDate(),b=this._GREGORIAN_EPOCH-1+365*(b-1)+Math.floor((b-1)/4)+-Math.floor((b-1)/100)+Math.floor((b-1)/400)+Math.floor((367*(c+1)-362)/12+(2>=c+1?0:f.isLeapYear(a)?-1:-2)+d),b=Math.floor(b)+0.5,b=b-this._ISLAMIC_EPOCH,c=Math.floor((30*b+10646)/10631),d=Math.ceil((b-29-this._yearStart(c))/29.5),d=Math.min(d,11);this._date=Math.ceil(b-this._monthStart(c,
d))+1;this._month=d;this._year=c;this._hours=a.getHours();this._minutes=a.getMinutes();this._seconds=a.getSeconds();this._milliseconds=a.getMilliseconds();this._day=a.getDay();return this},valueOf:function(){return this.toGregorian().valueOf()},_yearStart:function(a){return 354*(a-1)+Math.floor((3+11*a)/30)},_monthStart:function(a,b){return Math.ceil(29.5*b)+354*(a-1)+Math.floor((3+11*a)/30)},_civilLeapYear:function(a){return 11>(14+11*a)%30},getDaysInIslamicMonth:function(a,b){var c=0,c=29+(a+1)%
2;11==a&&this._civilLeapYear(b)&&c++;return c},_mod:function(a,b){return a-b*Math.floor(a/b)}});g.getDaysInIslamicMonth=function(a){return(new g).getDaysInIslamicMonth(a.getMonth(),a.getFullYear())};return g});
//@ sourceMappingURL=Date.js.map