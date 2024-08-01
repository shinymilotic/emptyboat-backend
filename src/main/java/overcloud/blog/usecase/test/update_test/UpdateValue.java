package overcloud.blog.usecase.test.update_test;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UpdateValue<T> {
    private T value;
    private Integer updateFlg;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Integer getUpdateFlg() {
        return updateFlg;
    }

    public void setUpdateFlg(Integer updateFlg) {
        this.updateFlg = updateFlg;
    }
}
