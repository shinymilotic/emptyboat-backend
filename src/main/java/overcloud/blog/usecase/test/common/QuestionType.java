package overcloud.blog.usecase.test.common;

public enum QuestionType {
    CHOICE(1),
    OPEN(2);

    QuestionType(int value) {
        this.value = value;
    }
    
    private int value;

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
