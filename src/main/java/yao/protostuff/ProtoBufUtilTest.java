package yao.protostuff;


/**
 * Created by Administrator on 2017-5-10.
 */
public class ProtoBufUtilTest {
    public static void main(String[] args) {

        Student student = new Student();
        student.setName("yao");
        student.setAge(28);
        student.setStudentNo("2007041125");
        student.setSchoolName("BJUT32");

//        byte[] serializerResult = ProtoBufUtil.serializer(student);

        //protostuff 是基于protobuf 的所以能达到其压缩效果
//        System.out.println("protostuff is len:" + serializerResult.length);
//
//        System.out.println("serializer result:" + Arrays.toString(serializerResult));
//
//        Student deSerializerResult = ProtoBufUtil.deserializer(serializerResult, Student.class);
//
//        System.out.println("deSerializerResult:" + deSerializerResult.toString());

        byte[] serializer = ProtoBufUtil.serializer(student);
        System.out.println(serializer.length);
        Student deserializer = ProtoBufUtil.deserializer(serializer, Student.class);
        System.out.println(deserializer.getName());

        String jsonStr = JsonUtils.toJsonStr(student);
        System.out.println(jsonStr.getBytes().length);
        Student p = JsonUtils.toBeanObj(jsonStr, Student.class);
        System.out.println(p.getName());
    }
}
