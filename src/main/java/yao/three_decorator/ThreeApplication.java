package yao.three_decorator;

/**
 * Created by Administrator on 2017-6-9.
 */
public class ThreeApplication {
    public void needBird(Bird bird) {
        int flyDistance = bird.fly();
        System.out.println("这只鸟能飞行" + flyDistance + "米");
    }

    public static void main(String[] args) {
        ThreeApplication client = new ThreeApplication();
        Bird sparrow = new Sparrow();          //sparrow只能飞行100米
        Bird sparrowDecorator1 = new SparrowDecorator(sparrow);   //sparrowDecorator1能飞行150米
        Bird sparrowDecorator2 = new SparrowDecorator(sparrowDecorator1); //sparrowDecorator2能飞行200米
        client.needBird(sparrowDecorator1);
        client.needBird(sparrowDecorator2);
    }
}
