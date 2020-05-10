package com.bogokj.xianrou.interfaces;

import android.view.View;

/**
 * @PACKAGE com.bogokj.xianrou.interfaces
 * @DESCRIPTION
 * @AUTHOR Su
 * @DATE 2017/4/4 10:34
 */

public interface XRCommonOnItemUserHeadClickCallack<E> {
    void onUserHeadClick(View view, E entity, int position);
}
