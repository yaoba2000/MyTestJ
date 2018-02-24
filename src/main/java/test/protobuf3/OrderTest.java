package test.protobuf3;

import com.googlecode.protobuf.format.JsonFormat;

/**
 * 订单新测试类
 * Created by Administrator on 2017-10-20.
 */
public class OrderTest {
    public static void main(String[] args) throws JsonFormat.ParseException {
        OrderInfo.OrderMsg.Builder msg = OrderInfo.OrderMsg.newBuilder();
        msg.setOrderNo("10001")//订单编号
                .setVehicleNo(17)//车辆编号
                .setPlateCode("津N12345")//车牌号
                .setPassengerAppointedTime("2017-10-23 19:01:11")//计划时间（乘客约车时间）
                .setFlightNo("SH12345")//航班编号
                .setPassengerName("李先生")
                .setDestinationLng(14.123456)//目的地经度
                .setDestinationLat(15.654321)//目的地纬度
                .setOriginLng(11.123456f)//起始点经度
                .setOriginLat(12.654321f)//起始点纬度
                .setOrderStatus(4);//4下发订单、8强制结束订单、9取消订单

//        String buf = new JsonFormat().printToString(msg.build());
//        System.out.println(buf);

        PersonOuter.Person.Builder personOrBuilder = PersonOuter.Person.newBuilder();
        personOrBuilder.setId(100L)
                .setName("yaoyan");
        String buf1 = JsonFormat.printToString(personOrBuilder.build());
        System.out.println(buf1);

        PersonOuter.Person.Builder perMsg = PersonOuter.Person.newBuilder();
        long speed = 0L;
        perMsg.setId(speed)
                .setName("yaoyan2");
        String buf2 = JsonFormat.printToString(perMsg.build());
        System.out.println(buf2);

        String jsonStr = "{\"id\": 100}";
        PersonOuter.Person.Builder pMsg = PersonOuter.Person.newBuilder();
        //将json字符串载入进来
        JsonFormat.merge(jsonStr, pMsg);
        System.out.println(pMsg.toString());
    }

}
