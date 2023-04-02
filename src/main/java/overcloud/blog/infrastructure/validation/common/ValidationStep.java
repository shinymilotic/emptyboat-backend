package overcloud.blog.infrastructure.validation.common;

import overcloud.blog.infrastructure.exceptionhandling.dto.ApiValidationError;


public abstract class ValidationStep<T> {

    private ApiValidationError apiValidationError;

    private ValidationStep<T> next;

    public ValidationStep<T> linkWith(ValidationStep<T> next) {
        if (this.next == null) {
            this.next = next;
            return null;
        }
        ValidationStep<T> lastStep = this.next;
        while (lastStep.next != null) {
            lastStep = lastStep.next;
        }
        lastStep.next = next;
        return next;
    }

    public abstract ValidationStep<T> validate(T toValidate);

    protected ValidationStep<T> checkNext(T toValidate) {
        if (next == null) {
            return this;
        }

        return next.validate(toValidate);
    }

    public ApiValidationError getApiValidationError() {
        return apiValidationError;
    }

    public void setApiValidationError(ApiValidationError apiValidationError) {
        this.apiValidationError = apiValidationError;
    }

    public ValidationStep<T> getNext() {
        return next;
    }

    public void setNext(ValidationStep<T> next) {
        this.next = next;
    }
}

