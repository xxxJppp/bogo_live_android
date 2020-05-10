package com.bogokj.xianrou.interfaces;

/**
 * @包名 com.bogokj.xianrou.common
 * @描述
 * @作者 Su
 * @创建时间 2017/3/16 15:47
 **/
public interface XRRefreshableInterface extends XRCommonStateInterface
{
    void startRefreshing();

    void stopRefreshing();
}
