package site.metacoding.junitproject.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonRespDto<T> {
    private Integer code;
    private String msg;
    private T data;

    @Builder
    public CommonRespDto(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
