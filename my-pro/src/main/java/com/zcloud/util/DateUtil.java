package com.zcloud.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 说明：日期处理
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public class DateUtil {
	
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getSdfTimes() {
		return sdfTimes.format(new Date());
	}
	
	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author fh
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			return false; // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
		}
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			return 0;	// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
		}
	}
	 
	/**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        return dateStr;
    }
    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    
	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date){
		return date2Str(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date){
		if(Tools.notEmpty(date)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		}else{
			return null;
		}
	}
	
	/**
	 * 把时间根据时、分、秒转换为时间段
	 * @param StrDate
	 */
	public static String getTimes(String StrDate){
		String resultTimes = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    java.util.Date now;
	    try {
	    	now = new Date();
	    	java.util.Date date=df.parse(StrDate);
	    	long times = now.getTime()-date.getTime();
	    	long day  =  times/(24*60*60*1000);
	    	long hour = (times/(60*60*1000)-day*24);
	    	long min  = ((times/(60*1000))-day*24*60-hour*60);
	    	long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);
	        
	    	StringBuffer sb = new StringBuffer();
	    	//sb.append("发表于：");
	    	if(hour>0 ){
	    		sb.append(hour+"小时前");
	    	} else if(min>0){
	    		sb.append(min+"分钟前");
	    	} else{
	    		sb.append(sec+"秒前");
	    	}
	    	resultTimes = sb.toString();
	    } catch (ParseException e) {
	    	e.printStackTrace();
	    }
	    return resultTimes;
	}
	
	/**
	 * 按照参数format的格式，日期转字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date,String format){
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}
	
   /**
     * 获取当年的第一天
     * @param year
     * @author zhangyue
     * @return
     */
    public static String  getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        Date currYearFirst = calendar.getTime();
        return sdfDay.format(currYearFirst);
    }
     
    /**
     * 获取当年的最后一天
     * @param year
     * @author zhangyue
     * @return
     */
    public static String getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return sdfDay.format(currYearLast);
    }
     

 
    /**
     * 获取当前季度第一天
     * @author zhangyue
     * @return
     */
    public static String quarterStart() {
        Date dBegin = new Date();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        int remainder = calBegin.get(Calendar.MONTH)  % 3;
        int month = remainder != 0 ? calBegin.get(Calendar.MONTH) - remainder: calBegin.get(Calendar.MONTH);
 
        calBegin.set(Calendar.MONTH, month);
        calBegin.set(Calendar.DAY_OF_MONTH, calBegin.getActualMinimum(Calendar.DAY_OF_MONTH));
 
        calBegin.setTime(calBegin.getTime());
        return sdfDay.format(calBegin.getTime());
    }
 
    /**
     * 获取当前季度最后一天
     * @author zhangyue
     * @return
     */
    public static String quarterEnd() {
        Date dEnd = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dEnd);
        int remainder = (calendar.get(Calendar.MONTH) + 1) % 3;
        int month = remainder != 0 ? calendar.get(Calendar.MONTH) + (3 - remainder) : calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.setTime(calendar.getTime());
        return sdfDay.format(calendar.getTime());
    }

    /**
     * 获取本月第一天日期
     * @return
     */
    public static String getMonthFirstDay() {
        Calendar thisMonthFirstDateCal = Calendar.getInstance();
        thisMonthFirstDateCal.set(Calendar.DAY_OF_MONTH, 1);
        String thisMonthFirstTime = sdfDay.format(thisMonthFirstDateCal.getTime());
        return thisMonthFirstTime;
    }
    
    /**
     * 获取本月最后一天日期
     * @return
     */
    public static String getMonthEndDay() {
        Calendar thisMonthEndDateCal = Calendar.getInstance();
        thisMonthEndDateCal.set(Calendar.DAY_OF_MONTH, thisMonthEndDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String thisMonthEndTime = sdfDay.format(thisMonthEndDateCal.getTime());
        return thisMonthEndTime;
    }
    /**
     * 获取明天的日期
     *
     * @param date
     * @return
     */
    public static Date getTomorrow(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        date = calendar.getTime();
        return date;
    }

    /**
     * 判断是否是周末
     *
     * @param sdate
     * @return
     * @throws ParseException
     */
    public static boolean isWeekend(String sdate) throws ParseException {
        Date date = sdfDay.parse(sdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        } else{
            return false;
        }

    }
    //获取i天工作日集合（包括今天）
    public static List<String> getScheduleActiveDate(int i) throws ParseException {
//        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());//获取当前日期2018-08-26
    	List<String> dayList = new ArrayList<String>();
        Date today = new Date();//Mon Aug 27 00:09:29 CST 2018
        Date tomorrow = null;
        int delay = 1;
        int num = i;//根据需要设置,这个值就是业务需要的n个工作日
        while(delay <= num){
        	if(delay == 1) {
        		tomorrow = today;
        	}else {

                tomorrow = getTomorrow(today);
        	}
            //当前日期+1即tomorrow,判断是否是节假日,同时要判断是否是周末,都不是则将scheduleActiveDate日期+1,直到循环num次即可
            if(!isWeekend(sdfDay.format(tomorrow))){
                delay++;
                today = tomorrow;
                dayList.add(sdfDay.format(tomorrow));
            } else if (isWeekend(sdfDay.format(tomorrow))){
//                tomorrow = getTomorrow(tomorrow);
                today = tomorrow;
            }
//            else if (isHoliday(sdf.format(tomorrow),list)){
////                tomorrow = getTomorrow(tomorrow);
//                today = tomorrow;
//                System.out.println(sdf.format(tomorrow) + "::是节假日");
//            }
        }
        return dayList;
    }
    public static void main(String[] args) {
    	System.out.println(getDays());
    	System.out.println(getAfterDayWeek("3"));

    }

}
