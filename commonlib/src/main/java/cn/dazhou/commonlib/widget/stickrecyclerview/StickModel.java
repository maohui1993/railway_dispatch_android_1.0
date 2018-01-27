/*
 * Copyright (c) 2017. Hooyee@dazhou
 *
 * Last modified 17-12-22 上午10:44.
 *
 * author                             bug                             date                            comment
 * Hooyee............................................................................. 2017/12/22..............................init
 */

package cn.dazhou.commonlib.widget.stickrecyclerview;

import java.util.List;

/**
 * @author Hooyee on 2017/12/21.
 */

public class StickModel<K, V> {
    public K parent;
    public List<V> children;
    public boolean selected;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StickModel) {
            StickModel result = (StickModel) obj;
            return parent.equals(result.parent);
        }
        return false;
    }
}
