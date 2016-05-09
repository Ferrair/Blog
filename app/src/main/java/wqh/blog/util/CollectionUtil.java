package wqh.blog.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WQH on 2016/5/9  19:42.
 */
public class CollectionUtil {

    /**
     * Make Array data to List.(DO NOT use Arrays.asList)
     */
    @SafeVarargs
    public static <T> List<T> asList(T... arrayData) {
        List<T> mList = new ArrayList<>();
        for (T itemData : arrayData)
            mList.add(itemData);
        return mList;
    }
}
