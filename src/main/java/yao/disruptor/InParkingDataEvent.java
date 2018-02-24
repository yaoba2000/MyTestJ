package yao.disruptor;


/**
 * @Desc 1. 定义事件
 * @Author Remilia
 * @Create 2017-11-13
 */
public class InParkingDataEvent {
    private String carLicense = "";

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }
}
