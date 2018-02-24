package yao.design.pattern.comman.observe;

/**
 * Created by Administrator on 2017-7-5.
 */

import java.util.Observable;

public class SubjectForSSQ extends Observable {
    private String msg;


    public String getMsg() {
        return msg;
    }


    /**
     * 主题更新消息
     *
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
        setChanged();
        notifyObservers();
    }
}
