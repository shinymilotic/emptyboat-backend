package overcloud.blog.usecase.test.update_test;


public enum UpdateFlg {
    NO_CHANGE(0), CREATE(1), UPDATE(2), DELETE(3) ;

    private int value;

    UpdateFlg(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
