package com.bogokj.live.appview.ranking;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bogokj.hybrid.http.AppRequestCallback;
import com.bogokj.live.adapter.LiveRankingListAdapter;
import com.bogokj.live.common.AppRuntimeWorker;
import com.bogokj.live.common.CommonInterface;
import com.bogokj.live.model.App_followActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.bogokj.library.utils.SDToast;
import com.bogokj.library.utils.SDViewUtil;
import com.bogokj.live.R;
import com.bogokj.live.activity.LiveUserHomeActivity;
import com.bogokj.live.adapter.LiveContAdapter;
import com.bogokj.live.appview.BaseAppView;
import com.bogokj.live.appview.RankingListBaseHeaderView;
import com.bogokj.live.model.App_ContActModel;
import com.bogokj.live.model.App_ContModel;
import com.bogokj.live.model.RankUserItemModel;
import com.bogokj.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-6 下午9:18:22 类说明
 */
public abstract class LiveContBaseView extends BaseAppView
{
    public LiveContBaseView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveContBaseView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveContBaseView(Context context)
    {
        super(context);
        init();
    }

    @ViewInject(R.id.list)
    protected ListView list;

    protected List<App_ContModel> listModel = new ArrayList<App_ContModel>();
    protected List<App_ContModel> listHeaderModel = new ArrayList<App_ContModel>();
    protected LiveContAdapter adapter;
    protected App_ContActModel app_ContActModel;

    protected RankingListBaseHeaderView mHeaderView;

    protected int page = 1;

    private void init()
    {
        setContentView(R.layout.frag_cont_list);
        register();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                refreshViewer();
            }

            @Override
            public void onRefreshingFromFooter()
            {
                loadMoreViewer();
            }
        });
    }

    protected void register()
    {
        mHeaderView = new RankingListBaseHeaderView(getActivity());
        mHeaderView.setContDatas(listHeaderModel);
        mHeaderView.setRankingType("贡献");
        mHeaderView.setRankingHeaderOnClick(new RankingListBaseHeaderView.RankingHeaderOnClick()
        {
            @Override
            public void onItemClick(View view, RankUserItemModel model, App_ContModel contModel)
            {
                if (null != contModel)
                {
                    jumpUserHomeActivity(String.valueOf(contModel.getUser_id()));
                } else
                {
                    SDToast.showToast("暂无排行数据！");
                }
            }
        });
        list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                App_ContModel model = adapter.getItem((int) id);
                if (model != null)
                {
                    String userid = String.valueOf(model.getUser_id());
                    if (!TextUtils.isEmpty(userid))
                    {
                        jumpUserHomeActivity(userid);
                    } else
                    {
                        SDToast.showToast("userid为空");
                    }
                }

            }
        });
        adapter = new LiveContAdapter(listModel, getActivity());
        adapter.setRankingType("贡献");
        adapter.setTicketName(AppRuntimeWorker.getTicketName());
        adapter.setOnRankingClickListener(new LiveContAdapter.OnRankingClickListener()
        {
            @Override
            public void clickFollow(View view, int position, App_ContModel model)
            {
                requestFollow(position, model);
            }

            @Override
            public void clickItem(View view, int position, App_ContModel model)
            {
                jumpUserHomeActivity(""+model.getUser_id());
            }
        });
        list.addHeaderView(mHeaderView);
        list.setAdapter(adapter);
    }
    private void requestFollow(final int position, final App_ContModel model)
    {
        showProgressDialog("");
        CommonInterface.requestFollow(model.getUser_id() + "", 0, new AppRequestCallback<App_followActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    if (actModel.getHas_focus() == 1)
                    {
                        model.setIs_focus(1);
                    } else
                    {
                        model.setIs_focus(0);
                    }
                    adapter.updateData(position, model);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }
    /**
     * 跳转到用户详情界面
     *
     * @param userid
     */
    private void jumpUserHomeActivity(String userid)
    {
        Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, userid);
        getActivity().startActivity(intent);
    }

    private void loadMoreViewer()
    {
        if (app_ContActModel != null)
        {
            if (app_ContActModel.getHas_next() == 1)
            {
                page++;
                requestCont(true);
            } else
            {
                onRefreshComplete();
                SDToast.showToast("没有更多数据");
            }
        } else
        {
            refreshViewer();
        }
    }

    protected void fillData(App_ContActModel actModel, boolean isLoadMore)
    {
        app_ContActModel = actModel;
        if (!isLoadMore)
        {
            listModel.clear();
            listHeaderModel.clear();
            listHeaderModel.addAll(actModel.getList());
            mHeaderView.setContDatas(listHeaderModel);
            mHeaderView.setRankingNum(actModel.getTotal_num());
        }
        if (listModel.isEmpty())
        {
            if (actModel.getList().size() > 2)
            {
                actModel.getList().remove(0);
                actModel.getList().remove(0);
                actModel.getList().remove(0);
            } else
            {
                if (actModel.getList().size() == 1)
                {
                    actModel.getList().remove(0);
                }
                if (actModel.getList().size() == 2)
                {
                    actModel.getList().remove(0);
                    actModel.getList().remove(0);
                }
            }
        }
        SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
        if (listHeaderModel.size() < 1)
        {
            getStateLayout().showEmpty();
        } else
        {
            getStateLayout().showContent();
        }
    }

    protected void onRefreshComplete()
    {
        if (list != null)
        {
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    private void refreshViewer()
    {
        page = 1;
        requestCont(false);
    }

    public abstract void requestCont(final boolean isLoadMore);

    private int room_id;
    private String user_id = "";

    public int getRoom_id()
    {
        return room_id;
    }

    public void setRoom_id(int room_id)
    {
        this.room_id = room_id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public void setTipName(String tipName)
    {
        mHeaderView.setRankingType(tipName);
    }
}
