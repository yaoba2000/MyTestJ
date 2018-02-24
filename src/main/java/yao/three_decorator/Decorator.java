package yao.three_decorator;

/**
 * Created by Administrator on 2017-6-9.
 */
public abstract class Decorator extends Bird {
    protected Bird bird;

    public Decorator() {

    }

    public Decorator(Bird bird) {
        this.bird = bird;
    }
}