package overcloud.blog.infrastructure;


public enum UpdateFlg {
    NO_CHANGE(0),
    NEW(1),
    UPDATE(2),
    DELETE(3);

    private int value;
    UpdateFlg(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int i) {
        this.value = i;
    }

    public static UpdateFlg fromInt(int i) {
        for (UpdateFlg e : UpdateFlg.values()) {
            if (e.value == i) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid integer value: " + i);
    }
}
