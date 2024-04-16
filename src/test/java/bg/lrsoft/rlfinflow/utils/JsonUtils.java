package bg.lrsoft.rlfinflow.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {

    public static String toJson(Object obj) {
        return "{" + obj.toString() + "}";
    }
}
