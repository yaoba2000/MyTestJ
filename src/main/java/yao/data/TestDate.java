package yao.data;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017-9-15.
 */
public class TestDate {
    public static void main(String[] args) throws Exception {
        System.out.println(getHDFSWeekFile());
//        Map<String, String> maps =  getLastWeek(1);
//        for (Object key : maps.keySet()) {
//            System.out.println(key + " : " + maps.get(key));
//        }
        //偏移量+1
        List<String> list = getLastWeekList(1);
        System.out.println(list.toString());

    }

    /**
     * 根据日期字符串判断当月第几周
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static int getWeek(String str) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //第几周
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //第几天，从周日开始
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }

    public static String getHDFSWeekFile() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");

        Calendar cal = Calendar.getInstance();
        int n = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (n == 0) {
            n = 7;
        }
        cal.add(Calendar.DATE, -(7 + (n - 1)));// 上周一的日期
        Date monday = cal.getTime();
        String mondayStr = sdf.format(monday);
        //用来计算结果的
        String mondayStrRes = sdf1.format(monday);
//        map.put("monday", mondayStr);

        Date date = sdf.parse(mondayStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //第几周
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        //第几天，从周日开始
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return mondayStrRes + "-w" + week + ".json";
    }

    public static Map<String, String> getLastWeek(int delay) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        int n = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (n == 0) {
            n = 7;
        }
//        cal.add(Calendar.DATE, -(7 + (n - 1)));// 上周一的日期
        //这里为了错一天 大数据特殊需求
        cal.add(Calendar.DATE, -(7 + (n - 1 - delay)));
        Date monday = cal.getTime();
        String mondayStr = sdf.format(monday);
        map.put("monday", mondayStr);

        cal.add(Calendar.DATE, 1);
        Date tuesday = cal.getTime();
        String tuesdayStr = sdf.format(tuesday);
        map.put("tuesday", tuesdayStr);

        cal.add(Calendar.DATE, 1);
        Date wednesday = cal.getTime();
        String wednesdayStr = sdf.format(wednesday);
        map.put("wednesday", wednesdayStr);

        cal.add(Calendar.DATE, 1);
        Date thursday = cal.getTime();
        String thursdayStr = sdf.format(thursday);
        map.put("thursday", thursdayStr);

        cal.add(Calendar.DATE, 1);
        Date friday = cal.getTime();
        String fridayStr = sdf.format(friday);
        map.put("friday", fridayStr);

        cal.add(Calendar.DATE, 1);
        Date saturday = cal.getTime();
        String saturdayStr = sdf.format(saturday);
        map.put("saturday", saturdayStr);

        cal.add(Calendar.DATE, 1);
        Date sunday = cal.getTime();
        String sundayStr = sdf.format(sunday);
        map.put("sunday", sundayStr);
        return map;
    }


    public static List<String> getLastWeekList(int delay) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // TODO Auto-generated method stub
        List<String> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int n = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (n == 0) {
            n = 7;
        }
//        cal.add(Calendar.DATE, -(7 + (n - 1)));// 上周一的日期
        //这里为了错一天 大数据特殊需求
        cal.add(Calendar.DATE, -(7 + (n - 1 - delay)));
        Date monday = cal.getTime();
        String mondayStr = sdf.format(monday);
        list.add(mondayStr);

        cal.add(Calendar.DATE, 1);
        Date tuesday = cal.getTime();
        String tuesdayStr = sdf.format(tuesday);
        list.add(tuesdayStr);

        cal.add(Calendar.DATE, 1);
        Date wednesday = cal.getTime();
        String wednesdayStr = sdf.format(wednesday);
        list.add(wednesdayStr);

        cal.add(Calendar.DATE, 1);
        Date thursday = cal.getTime();
        String thursdayStr = sdf.format(thursday);
        list.add(thursdayStr);

        cal.add(Calendar.DATE, 1);
        Date friday = cal.getTime();
        String fridayStr = sdf.format(friday);
        list.add(fridayStr);

        cal.add(Calendar.DATE, 1);
        Date saturday = cal.getTime();
        String saturdayStr = sdf.format(saturday);
        list.add(saturdayStr);

        cal.add(Calendar.DATE, 1);
        Date sunday = cal.getTime();
        String sundayStr = sdf.format(sunday);
        list.add(sundayStr);
        return list;
    }


}
