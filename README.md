#  布谷直播Android项目

开发环境：JDK 8 Mac OS  Windows

开发工具：Android Studio 3.0 

注：项目目录路径文件名不要包含中文，不要跑模拟器直接跑真机



#### model说明

- bogoBeauty：美颜库

- bogoLive：直播项目主模块

- bogoLiveGame：游戏，扎金花、牛牛代码模块



#### 第三方相关配置

- APP名称修改文件：bogoLive/src/user/res/values/    app_name

- 修改腾讯云APP_ID：bogoLive/src/user/res/values/    app_id_tencent_live

- 微信APP_ID：bogoLive/src/user/res/values/    wx_app_id

- 微信APP_SECRET：bogoLive/src/user/res/values/    wx_app_secret

- QQ登录qq_auth_scheme：bogoLive/src/user/res/values/    qq_auth_scheme

- QQ登录qq_app_id：bogoLive/src/user/res/values/    qq_app_id

- QQ登录qq_app_key：bogoLive/src/user/res/values/    qq_app_key



#### 域名接口修改

build.gradle：buildConfigField字段



#### 签名文件

build.gradle中包含了签名文件的密钥别名和文件路径




### 类说明
* com.bogokj.live.ILiveInfo：直播间基础集成接口
* com.bogokj.live.room.roomactivity.LiveActivity
* 直播间基类
* com.bogokj.live.room.roomactivity.LiveLayoutActivity
* 直播公共界面
* com.bogokj.live.room.roomactivity.push.LiveLayoutCreaterActivity
* 主播界面布局类
* com.bogokj.live.room.roomactivity.push.LiveLayoutCreaterExtendActivity
* 主播界面扩展
* com.bogokj.live.room.roomactivity.LiveLayoutExtendActivity
* 公共界面扩展
* com.bogokj.live.room.roomactivity.LiveLayoutGameActivity
* 直播游戏扩展布局类
* com.bogokj.live.room.roomactivity.LiveLayoutGameExtendActivity
* 直播游戏扩展类
* com.bogokj.live.room.roomactivity.watch.LiveLayoutViewerActivity
* 直播观众页面类
* com.bogokj.live.room.roomactivity.watch.LiveLayoutViewerExtendActivity
* 直播观众页面扩展类
* com.bogokj.live.room.back.LivePlayActivity
* 直播回放页面基类
* com.bogokj.live.room.back.LivePlaybackActivity
* 直播回放页面类
* com.bogokj.live.room.roomactivity.push.LivePushCreaterActivity
* 推流直播主播界面
* com.bogokj.live.room.roomactivity.watch.LivePushViewerActivity
* 推流直播间观众界面


-------
* LiveAccountCenterActivity
* 绑定账户页面
* LiveAdminActivity
* 管理员列表
* LiveAlipayBindingActivity
* 绑定支付宝
* LiveBlackListActivity
* 黑名单列表
* LiveClubDetailsActivity
* 家族详情页面
* LiveContActivity
* 贡献榜容器页面
* LiveCreaterAgreementActivity
* 主播协议
* LiveCreateRoomActivity
* 创建直播页面
* LiveDoUpdateActivity
* 完善资料页面
* LiveFamilyDetailsActivity
* 家族详情
* LiveFamilyInformationActivity
* 家族信息展示
* LiveFamilyMembersListActivity
* 家族成员列表
* LiveFamilysListActivity
* 搜索加入家族
* LiveFamilyUpdateEditActivity
* 家族个人资料修改编辑
* LiveFocusFollowBaseActivity
* 关注列表基类
* LiveFollowActivity
* 关注的人
* LiveLoginActivity
* 登录页面
* LiveLoginBindMobileActivity
* 登录绑定手机号码
* LiveMainActivity
* 首页
* LiveMobielRegisterActivity
* 手机登录页面
* LiveMobileBindActivity
* 绑定手机号码
* LiveMyFocusActivity
* 粉丝列表
* LiveMySelfContActivity
* 我的贡献榜
* LivePrivateChatActivity
* 私信会话页面
* LivePushManageActivity
* 推送管理
* LiveRankingActivity
* 贡献榜
* LiveRechargeDiamondsActivity
* 个人中心-账户-充值界面
* LiveRechargeVipActivity
* 充值VIP界面
* LiveSearchUserActivity
* 搜索页面
* LiveSongChooseActivity
* 直播歌曲选择
* LiveSongSearchActivity
* 直播歌曲搜索
* LiveTakeRewardActivity
* 提现收入票
* LiveToolsShopActivity
* 道具商城
* LiveUploadImageActivity
* 图片上传
* LiveUpLoadImageOssActivity
* 图片上传
* LiveUploadUserImageActivity
* 头像上传
* LiveUserCenterAuthentActivity
* 用户认证
* LiveUserEditActivity
* 编辑用户资料
* LiveUserHeadImageActivity
* 编辑用户头像
* LiveUserHomeActivity
* 用户主页
* LiveUserHomeReplayActivity
* 回播列表
* LiveUserProfitActivity
* 新提现收入页面
* LiveUserProfitExchangeActivity
* 提现收入兑换页面
* LiveUserProfitRecordActivity
* 提现记录
* LiveUserSettingActivity
* 设置
* LiveWebViewActivity
* H5封装打开页面
* PhotoViewActivity
* 聊天图片查看页面
* SelectPhotoActivity
* 选择图片
* TCVideoEditerMgr
* 短视频编辑管理类
* VideoChooseActivity
* 短视频上传选择类
* WebViewActivity
* H5封装打开页面



-------
* MsgCreaterComebackViewHolder
* 直播间消息列表holder适配器-主播回来消息
* MsgCreaterLeaveViewHolder
* 直播间消息列表holder适配器-主播离开消息
* MsgForbidSendMsgViewHolder
* 直播间消息列表holder适配器-禁言消息
* MsgGiftCreaterViewHolder
* 直播间消息列表holder适配器-主播礼物提示
* MsgGiftViewerViewHolder
* 直播间消息列表holder适配器-观众礼物提示
* MsgLightViewHolder
* 直播间消息列表holder适配器-点亮消息
* MsgLiveMsgViewHolder
* 直播间消息列表holder适配器-直播消息
* MsgPopViewHolder
* 直播间消息列表holder适配器-弹幕消息
* MsgProViewerJoinViewHolder
* 直播间消息列表holder适配器-高级用户加入
* MsgRedEnvelopeViewHolder
* 直播间消息列表holder适配器-红包消息
* MsgTextViewHolder
* 直播间消息列表holder适配器-文字消息
* MsgViewerJoinViewHolder
* 直播间消息列表holder适配器-普通用户加入
* MsgViewHolder
* 直播间消息列表holder适配器-基类

-------
* LiveAdminAdapter
* 用户列表相关UI列表Adapter共用类
* LiveBlacklistAdapter
* 拉黑列表适配器
* LiveContAdapter
* 排行榜列表适配器
* LiveConversationListAdapter
* 私信会话列表适配器
* LiveDistributionAdatper
* 贡献榜列表适配器
* LiveExpressionAdapter
* 私信表情列表适配器
* LiveFamilyApplyAdapter
* 家族成员申请适配器
* LiveFamilyMembersAdapter
* 家族成员适配器
* LiveFamilysListAdapter
* 家族列表适配器
* LiveGiftAdapter
* 礼物列表适配器
* LiveGiftTypelAdapter
* 礼物类型适配器
* LiveGuardListTableAdapter
* 直播间守护列表适配器
* LiveGuardSpecialAdapter
* 守护特权适配器
* LiveGuardTypeAdapter
* 守护类型适配器
* LiveMainTabHotsAdapter
* 首页热门列表适配器
* LiveMsgRecyclerAdapter
* 直播间消息列表适配器
* LivePKEmceeListAdapter
* PK主播列表适配器
* LivePkTimeListAdapter
* PK时间列表适配器
* LivePrivateChatRecyclerAdapter
* 私信消息列表适配器
* LiveRankingListAdapter
* 排行榜适配器
* LiveRechargePayDialogAdapter
* 充值支付台创适配器
* LiveRechargePaymentAdapter
* 支付规则适配器
* LiveRechargeRuleAdapter
* 充值规则适配器
* LiveRechrgeDiamondsPaymentRuleAdapter
* 充值钻石支付规则列表
* LiveRechrgeVipPaymentRuleAdapter
* VIP商品列表购买规则适配器
* LiveSetBeautyFilterAdapter
* 美颜滤镜适配器
* LiveSociatyMembersAdapter
* 家族成员适配器
* LiveSociatySearchJoinAdapter
* 搜索加入公会列表适配器
* LiveSongListAdapter
* 歌曲列表适配器
* LiveTabCategoryAdapter
* 首页直播分类适配器
* LiveTabFollowAdapter
* 首页关注直播适配器
* LiveTabHotAdapter
* 首页热门直播适配器
* LiveTabNearbyAdapter
* 首页附近直播适配器
* LiveUserCenterAuthentAdapter
* 用户认证适配器
* LiveViewerListRecyclerAdapter
* 直播间观众列表适配器
* SelectLabelAdapter
* 性别选择适配器
* TCVideoEditerListAdapter
* 选择短视频列表适配器


-------
* GifAnimatorCar1
* 动画蓝色法拉利
* GiftAnimatorCar2
* 动画红色兰博基尼
* GiftAnimatorPlane1
* 动画轰炸机
* GiftAnimatorPlane2
* 白色客机
* GiftAnimatorRocket1
* 火箭
* GiftAnimatorView
* 动画基类


-------
* LiveMainBottomNavigationView
* 底部菜单栏控件
* LiveMainDynamicView
* 首页动态View
* LiveMainHomeView
* 首页主页View
* LiveMainMeView
* 首页用户中心View
* LiveMainRankingView
* 首页榜单View
* LiveTabBaseView
* 首页View基类
* LiveTabCategoryView
* 首页直播分类
* LiveTabFollowView
* 首页关注直播列表
* LiveTabHotNewView
* 首页热门直播列表
* LiveTabNearbyView
* 首页附近直播列表


-------
* LiveHomeTitleSmallTab
* 首页分类标题Item

-------
* LiveContLocalView
* 贡献榜本次直播
* LiveContTotalView
* 贡献榜累计排行
* LiveRankingContributionView
* 贡献排行View
* LiveRankingListBaseView
* 排行榜列表基类
* LiveRankingListContributionView
* 贡献排行榜列表
* LiveRankingListMeritsView
* 功德排行榜列表
* LiveRankingMeritsView
* 功德排行榜View

-------
* ARoomMusicView
* 直播间播放音乐view基类
* RoomBottomView
* 房间底部控制View基类
* RoomBuyGuardianSuccessSVGAPlayView
* SVGA动画播放View
* RoomCarsSVGAPlayView
* 座驾播放View
* RoomCloseView
* 直播间关闭View
* RoomContributionView
* 直播间贡献榜
* RoomCountDownView
* 主播开播倒计时
* RoomCreaterBottomView
* 主播页面底部控件View
* RoomCreaterFinishView
* 主播直播结束View
* RoomGameBankerView
* 游戏庄家信息控件
* RoomGiftGifView
* gift礼物显示View
* RoomGiftPlayView
* 礼物播放控件View
* RoomHeartView
* 点亮控件
* RoomInfoView
* 直播间顶部view
* RoomInviteFriendsView
* 私密直播邀请好友
* RoomLargeGiftInfoView
* 直播间大型礼物动画通知view
* RoomLargeGiftSVGAPlayView
* 礼物播放
* RoomMenuView
* 直播间菜单
* RoomMsgView
* 聊天消息
* RoomPopMsgView
* 弹幕消息
* RoomPushMusicView
* 直播sdk播放音乐view
* RoomRemoveViewerView
* 私密直播踢出观众
* RoomSelectFriendsView
* 选择好友
* RoomSendGiftView
* 赠送礼物View
* RoomSendMsgView
* 发送消息View
* RoomViewerBottomView
* 观众底部菜单
* RoomViewerFinishView
* 观众直播结束页面
* RoomViewerJoinRoomView
* 房间加入动画


-------
* LiveMainHomeTitleView
* 首页-主页标题栏view

-------
* AMenuView
* 菜单View
* ItemLiveTabNewSingle
* 直播最新列表ItemView
* LiveChatC2CNewView
* 私信会话页面View
* LiveConversationListView
* 会话列表view
* LiveCreaterPluginToolView
* 主播插件基础工具view
* LiveCropImageView
* 图片裁剪View
* LiveExpressionView
* 私聊界面，表情布局
* LiveGameExchangeView
* 直播间充值窗口兑换视图
* LiveGuardTableHeaderView
* 直播守护列表HeaderView
* LiveLargeGiftInfoView
* 大型礼物通知view
* LiveLinkMicGroupView
* 多人连麦view
* LiveLinkMicView
* 连麦view
* LiveLuckGiftInfoView
* 幸运礼物中奖信息控件
* LivePKContentView
* PK相关控件View
* LivePKViewerContentView
* PK相关控件View
* LivePlayMusicView
* 推流类型的直播间播放音乐view
* LivePrivateChatBarView
* 私聊界面底部操作栏布局
* LivePrivateChatMoreView
* 私聊界面，更多里面的布局
* LivePrivateChatRecordView
* 私信录制音频
* LiveRechargeDiamondsView
* 直播间充值窗口充值视图
* LiveRedEnvelopeOpenView
* 打开的红包view
* LiveRedEnvelopeView
* 未打开的红包view
* LiveSearchUserView
* 搜索用户View
* LiveSendGiftView
* 发送礼物view
* LiveTabHotHeaderView
* 首页热门列表HeaderView
* LiveVideoExtendView
* 视频播放扩展view
* LiveVideoView
* 视频播放view
* RankingListBaseHeaderView
* 排行榜头部View
* RoomSdkInfoView
* 直播间上行，下行等sdk信息

-------
* InitBusiness
* 初始化界面业务
* LiveBusiness
* 直播间公共业务类
* LiveCreaterBusiness
* 直播间主播业务类
* LiveMsgBusiness
* 直播间消息业务类
* LivePrivateChatBusiness
* 私聊业务类
* LiveViewerBusiness
* 直播间观众业务类
* MsgBusiness
* 消息业务类

