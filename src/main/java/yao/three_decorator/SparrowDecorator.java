package yao.three_decorator;

/**
 * Created by Administrator on 2017-6-9.
 */
public class SparrowDecorator extends Decorator {
    public final int DISTANCE = 50;  //eleFly方法能飞50米

    SparrowDecorator(Bird bird) {
        super(bird);                 //表示调用父类(Decorator)的构造函数
    }

    public int fly() {
        int distance = 0;
        distance = bird.fly() + eleFly();   //委托被装饰者bird调用fly()，然后再调用eleFly()
        return distance;

    }

    /*
     * eleFly()方法访问权限设置为private，其目的是使得客户程序只有调用fly方法才可以使用eleFly方法
     */
    private int eleFly() {             //装饰者新添加的方法
        return DISTANCE;
    }
}
