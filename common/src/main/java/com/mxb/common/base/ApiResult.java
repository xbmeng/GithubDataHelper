package com.mxb.common.base;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult {

    private String result = TRUE;

    private String rejectReason = "";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static ApiResult success() {
        return new ApiResult(TRUE, "");
    }

    public static ApiResult fail(String msg) {
        return new ApiResult(FALSE, msg);
    }

    public boolean succeed() {
        return TRUE.equals(result);
    }
}
