package overcloud.blog.application.practice.core;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@JsonTypeName("practices")
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class PracticeResponse {
    
}
