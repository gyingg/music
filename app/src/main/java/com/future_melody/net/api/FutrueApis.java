package com.future_melody.net.api;

import com.future_melody.mode.RecommendSpecialVoListBean;
import com.future_melody.net.CommonURL;
import com.future_melody.net.request.AddFollow;
import com.future_melody.net.request.AddFollowRequest;
import com.future_melody.net.request.AttentionThemeRequest;
import com.future_melody.net.request.BlackPearRequest;
import com.future_melody.net.request.CancelFollow;
import com.future_melody.net.request.CommentList;
import com.future_melody.net.request.CommentListRequest;
import com.future_melody.net.request.DecibelRequest;
import com.future_melody.net.request.DeletelikeRequest;
import com.future_melody.net.request.DotPraiseRequest;
import com.future_melody.net.request.EditMusicInfo;
import com.future_melody.net.request.ExitLoginRequest;
import com.future_melody.net.request.FeedBackRequest;
import com.future_melody.net.request.FindMoneyPswRequest;
import com.future_melody.net.request.FindPassword;
import com.future_melody.net.request.GetMusicLeaderReqest;
import com.future_melody.net.request.GetUserInfo;
import com.future_melody.net.request.GivePresenterRequest;
import com.future_melody.net.request.InputPasswordRequest;
import com.future_melody.net.request.InvitationCodeRequest;
import com.future_melody.net.request.JoinStarRequest;
import com.future_melody.net.request.LikeRequest;
import com.future_melody.net.request.ListenMusic;
import com.future_melody.net.request.ListerMusicRequest;
import com.future_melody.net.request.Login;
import com.future_melody.net.request.LoginCode;
import com.future_melody.net.request.MainActivityFragmentRequest;
import com.future_melody.net.request.MineInfo;
import com.future_melody.net.request.MineReconmendFans;
import com.future_melody.net.request.MineReconmendFollow;
import com.future_melody.net.request.MineReconmendMusic;
import com.future_melody.net.request.MineReconmendTheme;
import com.future_melody.net.request.MineXiaoWeiListRequest;
import com.future_melody.net.request.MineXiaoWeiRequest;
import com.future_melody.net.request.MsgCode;
import com.future_melody.net.request.MusicZanRequest;
import com.future_melody.net.request.MyInformRequest;
import com.future_melody.net.request.PlanetMusic;
import com.future_melody.net.request.PlanetTheme;
import com.future_melody.net.request.PlayerIsLikeRequest;
import com.future_melody.net.request.RecommendRequest;
import com.future_melody.net.request.Recommend_music_Request;
import com.future_melody.net.request.Recommend_theme_Request;
import com.future_melody.net.request.Register;
import com.future_melody.net.request.RemendThemeShare;
import com.future_melody.net.request.RemmendNewThemeRequest;
import com.future_melody.net.request.ReplyCommentRequest;
import com.future_melody.net.request.SendLogin;
import com.future_melody.net.request.SendTheme;
import com.future_melody.net.request.SetAppointment;
import com.future_melody.net.request.SetMoneyPswRequest;
import com.future_melody.net.request.SetMusicTop;
import com.future_melody.net.request.SetUserImage;
import com.future_melody.net.request.SetUserInfo;
import com.future_melody.net.request.SharFriendsRequest;
import com.future_melody.net.request.ShareMusicRequest;
import com.future_melody.net.request.ShareSuccsecRequest;
import com.future_melody.net.request.ShareTheme;
import com.future_melody.net.request.ShowUserRequest;
import com.future_melody.net.request.StarAdministration;
import com.future_melody.net.request.StarAppointment;
import com.future_melody.net.request.StarDetailsRequest;
import com.future_melody.net.request.StarIntroduce;
import com.future_melody.net.request.TelGiveRequest;
import com.future_melody.net.request.Test;
import com.future_melody.net.request.ThemeDetails;
import com.future_melody.net.request.ThemePickRequest;
import com.future_melody.net.request.ThemeRecommendRequest;
import com.future_melody.net.request.ThemeSetTop;
import com.future_melody.net.request.TopTheme;
import com.future_melody.net.request.UpdataUserRequest;
import com.future_melody.net.request.UserInforRequet;
import com.future_melody.net.request.VersionRequest;
import com.future_melody.net.request.WeekSuperuRequest;
import com.future_melody.net.request.XiaoWeiInfoRequest;
import com.future_melody.net.request.XiaoWeiPlayerRequest;
import com.future_melody.net.request.XiaoWeiQRcodeRequest;
import com.future_melody.net.request.XingMusicRequest;
import com.future_melody.net.request.XingMusicSetTop;
import com.future_melody.net.request.XingMusicTopRequest;
import com.future_melody.net.request.XingTopMusicRequest;
import com.future_melody.net.respone.AddFollowRespone;
import com.future_melody.net.respone.AttentionThemeRespone;
import com.future_melody.net.respone.BlackPearRespone;
import com.future_melody.net.respone.CancelFollowRespone;
import com.future_melody.net.respone.CommentListNewRespone;
import com.future_melody.net.respone.CommentListRespone;
import com.future_melody.net.respone.DecibelRespone;
import com.future_melody.net.respone.DeletelikeRespone;
import com.future_melody.net.respone.DotPraiseResponse;
import com.future_melody.net.respone.EditMusicInfoRespone;
import com.future_melody.net.respone.ExitLoginRespone;
import com.future_melody.net.respone.FeedBackRespone;
import com.future_melody.net.respone.FindMoneyPswRespone;
import com.future_melody.net.respone.FindPswResponse;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.GetMusicLeaderRespone;
import com.future_melody.net.respone.GetUserInfoRespone;
import com.future_melody.net.respone.GivePresenterRespone;
import com.future_melody.net.respone.InputPasswordRespone;
import com.future_melody.net.respone.InvitationCodeRespone;
import com.future_melody.net.respone.JoinStarRespone;
import com.future_melody.net.respone.LikeRespone;
import com.future_melody.net.respone.ListenMusicRespone;
import com.future_melody.net.respone.ListerMusicRespone;
import com.future_melody.net.respone.LoginCodeResponse;
import com.future_melody.net.respone.LoginResponse;
import com.future_melody.net.respone.MainActivityFragmentRespone;
import com.future_melody.net.respone.MineInfoRespone;
import com.future_melody.net.respone.MineReconmendFansRespone;
import com.future_melody.net.respone.MineReconmendFollowRespone;
import com.future_melody.net.respone.MineReconmendMusicRespone;
import com.future_melody.net.respone.MineReconmendThemeRespone;
import com.future_melody.net.respone.MineXiaoWeiListRespone;
import com.future_melody.net.respone.MineXiaoWeiRespone;
import com.future_melody.net.respone.MsgCodeResponse;
import com.future_melody.net.respone.MusicZanRespone;
import com.future_melody.net.respone.MyInformRespone1;
import com.future_melody.net.respone.MyInformRespone2;
import com.future_melody.net.respone.MyInformRespone3;
import com.future_melody.net.respone.PlanetMusicResone;
import com.future_melody.net.respone.PlayerIsLikeRepone;
import com.future_melody.net.respone.RecommendRespone;
import com.future_melody.net.respone.Recommend_music_Respne;
import com.future_melody.net.respone.Recommend_theme_Respne;
import com.future_melody.net.respone.RegisterResponse;
import com.future_melody.net.respone.RemendThemeRespone;
import com.future_melody.net.respone.RemmendNewThemeRespone;
import com.future_melody.net.respone.ReplyCommentResponse;
import com.future_melody.net.respone.SendLoginResponse;
import com.future_melody.net.respone.SendThemeRespone;
import com.future_melody.net.respone.SetAdministrationRespone;
import com.future_melody.net.respone.SetMoneyPswRespone;
import com.future_melody.net.respone.SetMusicTopResopone;
import com.future_melody.net.respone.SetUserImageRespone;
import com.future_melody.net.respone.SetUserInfoResponse;
import com.future_melody.net.respone.SharFriendsRespone;
import com.future_melody.net.respone.ShareMusicRespone;
import com.future_melody.net.respone.ShareSuccsecRespone;
import com.future_melody.net.respone.ShareThemeRespone;
import com.future_melody.net.respone.ShowUserRespone;
import com.future_melody.net.respone.StarAdministrationRespone;
import com.future_melody.net.respone.StarAppointmentRespone;
import com.future_melody.net.respone.StarDetailsRespone;
import com.future_melody.net.respone.StarIntroduceRespone;
import com.future_melody.net.respone.TelGiveRespone;
import com.future_melody.net.respone.TestResponse;
import com.future_melody.net.respone.ThemeDetailsRespone;
import com.future_melody.net.respone.ThemePickRespone;
import com.future_melody.net.respone.ThemeRecommendRespone;
import com.future_melody.net.respone.ThemeSetTopRespone;
import com.future_melody.net.respone.TopThemeRespone;
import com.future_melody.net.respone.UpdataUserResponse;
import com.future_melody.net.respone.UserInforRespne;
import com.future_melody.net.respone.VersionRespone;
import com.future_melody.net.respone.WeekSuperuRespone;
import com.future_melody.net.respone.XiaoWeiInfoRespone;
import com.future_melody.net.respone.XiaoWeiPlayerRespone;
import com.future_melody.net.respone.XiaoWeiQRcodeRespone;
import com.future_melody.net.respone.XingMusicRespone;
import com.future_melody.net.respone.XingMusicSetTopResopone;
import com.future_melody.net.respone.XingMusicTopRespone;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface FutrueApis {
    //  String HOST = "http://192.168.2.8:8080";  //赵强
//    String HOST = "http://172.16.66.38:8080"; //赵晨旭
      String HOST = "http://app.futuremelody.cn"; //正式
//  String HOST = "http://test.futuremelody.cn:8080"; //测试

    //百度音乐
    // String HOST = "http://tingapi.ting.baidu.com";仿
    //登录
    @POST(CommonURL.LOGIN)
    Flowable<FutureHttpResponse<LoginResponse>> login(@Body Login login);

    //注册
    @POST(CommonURL.REGISTER)
    Flowable<FutureHttpResponse<RegisterResponse>> register(@Body Register register);


    //获取注册验证码
    @POST(CommonURL.MSGCODE)
    Flowable<FutureHttpResponse<MsgCodeResponse>> msgCode(@Body MsgCode msgCode);

    //短信验证码登录
    @POST(CommonURL.SENGDLOGIN)
    Flowable<FutureHttpResponse<SendLoginResponse>> sendLogin(@Body SendLogin sendPassword);

    //忘记密码
    @POST(CommonURL.FINDPSW)
    Flowable<FutureHttpResponse<FindPswResponse>> findPsw(@Body FindPassword findPassword);

    //获取登录验证码
    @POST(CommonURL.SENGDLOGINMSG)
    Flowable<FutureHttpResponse<LoginCodeResponse>> logincode(@Body LoginCode loginCode);

    //设置个人资料
    @POST(CommonURL.SETUSERINFO)
    Flowable<FutureHttpResponse<SetUserInfoResponse>> setInfo(@Body SetUserInfo setUserInfo);

    //获取个人资料
    @POST(CommonURL.GETUSERINFO)
    Flowable<FutureHttpResponse<GetUserInfoRespone>> getUserInfo(@Body GetUserInfo getUserInfo);

    //上传头像
    @POST(CommonURL.SETUSERIMG)
    Flowable<FutureHttpResponse<SetUserImageRespone>> setUserImg(@Body SetUserImage setUserImage);

    //编辑音乐详情
    @POST(CommonURL.EDITMUSICINFO)
    Flowable<FutureHttpResponse<EditMusicInfoRespone>> editmusic(@Body EditMusicInfo editMusicInfo);

    //获取推荐主题列表
    @POST(CommonURL.RECOMMEND)
    Flowable<FutureHttpResponse<RecommendRespone>> recommend(@Body RecommendRequest recommendRequest);

    /**
     * 加入星球
     */
    @POST(CommonURL.JOINSTAR)
    Flowable<FutureHttpResponse<JoinStarRespone>> joinstart(@Body JoinStarRequest joinStarRespone);

    //主题详情
    @POST(CommonURL.THEMEDETAILS)
    Flowable<FutureHttpResponse<ThemeDetailsRespone>> themeDetails(@Body ThemeDetails themeDetails);

    //我的 VCC  W
    @POST(CommonURL.MINEINFO)
    Flowable<FutureHttpResponse<MineInfoRespone>> mineInfo(@Body MineInfo mineInfo);

    //评论列表
    @POST(CommonURL.COMMENTLIST)
    Flowable<FutureHttpResponse<List<CommentListRespone>>> commentList(@Body CommentList commentList);

    //新评论列表
    @POST(CommonURL.COMMENTLIST_NEW)
    Flowable<FutureHttpResponse<CommentListNewRespone>> commenNewtList(@Body CommentListRequest commentList);

    //回复评论
    @POST(CommonURL.REPLYCOMMENT)
    Flowable<FutureHttpResponse<ReplyCommentResponse>> replycomment(@Body ReplyCommentRequest ReplyCommentRequest);

    //1今日之星 2一周最佳 ,
    @POST(CommonURL.WEEKSUPERU)
    Flowable<FutureHttpResponse<List<WeekSuperuRespone>>> weeksuperu(@Body WeekSuperuRequest WeekSuperuRequest);


    //查询行星信息
    @POST(CommonURL.STARDETAILS)
    Flowable<FutureHttpResponse<StarDetailsRespone>> stardetails(@Body StarDetailsRequest StarDetailsRequest);

    //查询关注人的主题
    @POST(CommonURL.ATTENTIONTHEME)
    Flowable<FutureHttpResponse<List<AttentionThemeRespone>>> atttheme(@Body AttentionThemeRequest AttentionThemeRequest);

    //点赞
    @POST(CommonURL.DOTPRAISE)
    Flowable<FutureHttpResponse<DotPraiseResponse>> dotpraise(@Body DotPraiseRequest DotPraiseRequest);

    //获取歌曲点亮排行榜
    @POST(CommonURL.MUSICLEADER)
    Flowable<FutureHttpResponse<List<GetMusicLeaderRespone>>> musicleader(@Body GetMusicLeaderReqest GetMusicLeaderReqest);

    //发布主题
    @POST(CommonURL.SENDTHEME)
    Flowable<FutureHttpResponse<SendThemeRespone>> sendTheme(@Body SendTheme sendTheme);

    //删除喜欢item
    @POST(CommonURL.DELETELIKES)
    Flowable<FutureHttpResponse<List<DeletelikeRespone>>> deletelike(@Body DeletelikeRequest DeletelikeRequest);

    //我的：推荐主题
    @POST(CommonURL.MINETHEME)
    Flowable<FutureHttpResponse<List<MineReconmendThemeRespone>>> mineTheme(@Body MineReconmendTheme mineReconmendTheme);

    //我的：推荐音乐
    @POST(CommonURL.MINEMUSIC)
    Flowable<FutureHttpResponse<List<MineReconmendMusicRespone>>> mineMusic(@Body MineReconmendMusic mineReconmendMusic);

    //我的：关注
    @POST(CommonURL.MINEFOLLOW)
    Flowable<FutureHttpResponse<List<MineReconmendFollowRespone>>> mineFollow(@Body MineReconmendFollow mineReconmendFollow);

    //我的：粉丝
    @POST(CommonURL.MINEFANS)
    Flowable<FutureHttpResponse<List<MineReconmendFansRespone>>> mineFans(@Body MineReconmendFans mineReconmendFans);

    //关注
    @POST(CommonURL.FOLLOW)
    Flowable<FutureHttpResponse<AddFollowRespone>> addFollow(@Body AddFollow addFollow);

    //取消关注
    @POST(CommonURL.CANCELFOLLOW)
    Flowable<FutureHttpResponse<CancelFollowRespone>> cancelFollow(@Body CancelFollow cancelFollow);

    //添加关注
    @POST(CommonURL.ADDFOLLOW)
    Flowable<FutureHttpResponse<AddFollowRespone>> addlFollow(@Body AddFollowRequest AddFollowRespone);


    //查看行星推荐音乐
    @POST(CommonURL.PLANETMUSIC)
    Flowable<FutureHttpResponse<List<PlanetMusicResone>>> planeMusic(@Body PlanetMusic planetMusic);

    //查看行星推荐主题
    @POST(CommonURL.PLANETTHEME)
    Flowable<FutureHttpResponse<List<RecommendSpecialVoListBean>>> planeTheme(@Body PlanetTheme planetTheme);

    // 我的通知
    @POST(CommonURL.MYINFORM)
    Flowable<FutureHttpResponse<List<MyInformRespone1>>> myinform(@Body MyInformRequest MyInformRequest);

    //系统信息
    @POST(CommonURL.MYINFORM)
    Flowable<FutureHttpResponse<List<MyInformRespone2>>> pinglun(@Body MyInformRequest MyInformRequest);

    //点赞
    @POST(CommonURL.MYINFORM)
    Flowable<FutureHttpResponse<List<MyInformRespone3>>> zan(@Body MyInformRequest MyInformRequest);

    //意见反馈
    @POST(CommonURL.FANKUI)
    Flowable<FutureHttpResponse<FeedBackRespone>> myinform(@Body FeedBackRequest FeedBackRequest);

    //喜欢列表
    @POST(CommonURL.LIKE)
    Flowable<FutureHttpResponse<LikeRespone>> like(@Body LikeRequest LikeRequest);

    //修改个人信息
    @POST(CommonURL.SHOWUSER)
    Flowable<FutureHttpResponse<ShowUserRespone>> showuser(@Body ShowUserRequest BodyShowUserRequest);

    //保存个人信息
    @POST(CommonURL.UPDATEUSER)
    Flowable<FutureHttpResponse<UpdataUserResponse>> updatauser(@Body UpdataUserRequest UpdataUserRequest);

    //管理星球
    @POST(CommonURL.STAR_GUANLI)
    Flowable<FutureHttpResponse<StarAdministrationRespone>> satrAdministration(@Body StarAdministration starAdministration);

    //任命List
    @POST(CommonURL.STAR_RENMING_LIST)
    Flowable<FutureHttpResponse<List<StarAppointmentRespone>>> appointmentList(@Body StarAppointment starAppointment);

    //任命:添加和取消
    @POST(CommonURL.STAR_SET_RENMING)
    Flowable<FutureHttpResponse<SetAdministrationRespone>> setAppointment(@Body SetAppointment setAppointment);

    //星球管理：星球介绍
    @POST(CommonURL.STAR_GUANLI_IMG)
    Flowable<FutureHttpResponse<StarIntroduceRespone>> starIntroduce(@Body StarIntroduce starIntroduce);

    //查看发布人主页
    @POST(CommonURL.USETINFO)
    Flowable<FutureHttpResponse<UserInforRespne>> userInfo(@Body UserInforRequet UserInforRequet);

    //获取发布人主页的主题集合
    @POST(CommonURL.RECOMMEND_THEME)
    Flowable<FutureHttpResponse<Recommend_theme_Respne>> recommendTheme(@Body Recommend_theme_Request Recommend_theme_Request);

    //获取发布人主页的音乐集合
    @POST(CommonURL.RECOMMEND_MUSIC)
    Flowable<FutureHttpResponse<Recommend_music_Respne>> recommendMusic(@Body Recommend_music_Request Recommend_music_Request);

    //主题分享
    @POST(CommonURL.SHAR_THEME)
    Flowable<FutureHttpResponse<ShareThemeRespone>> sharTheme(@Body ShareTheme shareTheme);

    //主题置顶
    @POST(CommonURL.TOP_THEME)
    Flowable<FutureHttpResponse<TopThemeRespone>> sharTop(@Body TopTheme topTheme);


    //星歌
    @POST(CommonURL.XINGMUSIC)
    Flowable<FutureHttpResponse<XingMusicRespone>> xingMusic(@Body XingMusicRequest xingMusicRequest);


    //立即邀请
    @POST(CommonURL.SHSRT_FRIENDS)
    Flowable<FutureHttpResponse<SharFriendsRespone>> sharFriends(@Body SharFriendsRequest SharFriendsRequest);

    //星歌推荐List
    @POST(CommonURL.XINGTOPLIST)
    Flowable<FutureHttpResponse<XingMusicSetTopResopone>> topList(@Body XingMusicSetTop xingMusicSetTop);

    //星歌推荐
    @POST(CommonURL.SETXINGTOP)
    Flowable<FutureHttpResponse<SetMusicTopResopone>> setMusicTop(@Body SetMusicTop setMusicTop);

    //排行榜：星歌
    @POST(CommonURL.XINGTOP)
    Flowable<FutureHttpResponse<List<GetMusicLeaderRespone>>> xingTop(@Body XingTopMusicRequest xingTopMusicRequest);

    //排行榜：星歌点赞/取消
    @POST(CommonURL.MUSICZAN)
    Flowable<FutureHttpResponse<MusicZanRespone>> musicZan(@Body MusicZanRequest musicZanRequest);

    //输入邀请码
    @POST(CommonURL.INVITATIONI_Code)
    Flowable<FutureHttpResponse<InvitationCodeRespone>> invitationCode(@Body InvitationCodeRequest invitationCodeRequest);

    //分贝
    @POST(CommonURL.FENBEI_URL)
    Flowable<FutureHttpResponse<List<DecibelRespone>>> decibel(@Body DecibelRequest decibelRequest);

    //黑珍珠
    @POST(CommonURL.BLACKPEARL)
    Flowable<FutureHttpResponse<List<BlackPearRespone>>> blackpear(@Body BlackPearRequest blackPearRequest);

    //分享音乐
    @POST(CommonURL.SHARE_MUSIC)
    Flowable<FutureHttpResponse<ShareMusicRespone>> shareMusic(@Body ShareMusicRequest shareMusicRequest);

    //版本更新
    @POST(CommonURL.VERSION)
    Flowable<FutureHttpResponse<VersionRespone>> version(@Body VersionRequest versionRequest);

    //星歌听歌
    @POST(CommonURL.LISTERMUSIC)
    Flowable<FutureHttpResponse<ListenMusicRespone>> listen(@Body ListenMusic listenMusic);

    //设置资金密码
    @POST(CommonURL.SETMONEYPSW)
    Flowable<FutureHttpResponse<SetMoneyPswRespone>> setMoney(@Body SetMoneyPswRequest setMoneyPswRequest);


    //获取活动
    @POST(CommonURL.MainActivityFragment)
    Flowable<FutureHttpResponse<List<MainActivityFragmentRespone>>> MainActivityFragment(@Body MainActivityFragmentRequest MainActivityFragmentRequest);


    //找回资金密码
    @POST(CommonURL.FINDMONEYPSW)
    Flowable<FutureHttpResponse<FindMoneyPswRespone>> findMoney(@Body FindMoneyPswRequest findMoneyPswRequest);

    //退出登录
    @POST(CommonURL.EXIT_LOGIN)
    Flowable<FutureHttpResponse<ExitLoginRespone>> exitLogin(@Body ExitLoginRequest exitLoginRequest);

    //是否收藏音乐
    @POST(CommonURL.ISLIKEMUSIC)
    Flowable<FutureHttpResponse<PlayerIsLikeRepone>> islikeMusic(@Body PlayerIsLikeRequest playerIsLikeRequest);

    //主题置顶
    @POST(CommonURL.setThemeTop)
    Flowable<FutureHttpResponse<ThemeSetTopRespone>> setTop(@Body ThemeSetTop themeSetTop);

    //新版推荐
    @POST(CommonURL.GETRECOMMENDList)
    Flowable<FutureHttpResponse<ThemeRecommendRespone>> getRecommendTheme(@Body ThemeRecommendRequest themeRecommendRequest);

    //新版星歌排行榜
    @POST(CommonURL.XINGMUSICTOP_NEW)
    Flowable<FutureHttpResponse<XingMusicTopRespone>> getNewXingTop(@Body XingMusicTopRequest xingMusicTopRequest);

    //新版星歌排行榜
    @POST(CommonURL.REMCOMMENDSHARE)
    Flowable<FutureHttpResponse<RemendThemeRespone>> getThemeShare(@Body RemendThemeShare remendThemeShare);

    //小未小码
    @POST(CommonURL.XIAOWEIQRCODE)
    Flowable<FutureHttpResponse<XiaoWeiQRcodeRespone>> getXiaoWeiInfo(@Body XiaoWeiQRcodeRequest xiaoWeiQRcodeRequest);

    //我的小未
    @POST(CommonURL.MINEXIAOWEI)
    Flowable<FutureHttpResponse<MineXiaoWeiRespone>> getMineXiaoWei(@Body MineXiaoWeiRequest mineXiaoWeiRequest);

    //我的小未List
    @POST(CommonURL.MINEXIAOWEILIST)
    Flowable<FutureHttpResponse<List<MineXiaoWeiListRespone>>> getMineXiaoWeiList(@Body MineXiaoWeiListRequest mineXiaoWeiListRequest);

    //我的小未List
    @POST(CommonURL.MINEXIAOWEIMONEYINFO)
    Flowable<FutureHttpResponse<XiaoWeiInfoRespone>> getXiaoWeiInfo(@Body XiaoWeiInfoRequest xiaoWeiInfoRequest);

    //歌曲播放回调
    @POST(CommonURL.LISTER_MUCI)
    Flowable<FutureHttpResponse<ListerMusicRespone>> listermusic(@Body ListerMusicRequest listerMusicRequest);

    //分享成功回调
    @POST(CommonURL.SHARE_SUCCEC)
    Flowable<FutureHttpResponse<ShareSuccsecRespone>> shareRespone(@Body ShareSuccsecRequest shareSuccsecRequest);

    //主题点赞
    @POST(CommonURL.THEME_PICK)
    Flowable<FutureHttpResponse<ThemePickRespone>> theme_pick(@Body ThemePickRequest themePickRequest);

    //新版本推荐主题9。20
    @POST(CommonURL.ROMMEND_THEME)
    Flowable<FutureHttpResponse<List<RemmendNewThemeRespone>>> remend_new_theme(@Body RemmendNewThemeRequest remmendNewThemeRequest);

    //小未播放状态
    @POST(CommonURL.XiaoWeiPlayer)
    Flowable<FutureHttpResponse<List<XiaoWeiPlayerRespone>>> xiaoweiType(@Body XiaoWeiPlayerRequest xiaoWeiPlayerRequest);

    @POST(CommonURL.TEST)
    Flowable<FutureHttpResponse<TestResponse>> test(@Body Test test);


    //转账
    @POST(CommonURL.GIVEPRESENTER)
    Flowable<FutureHttpResponse<GivePresenterRespone>> givepresenter(@Body GivePresenterRequest givePresenterRequest);

    //输入资金密码
    @POST(CommonURL.INPUTPASSWORD)
    Flowable<FutureHttpResponse<InputPasswordRespone>> inputpassword(@Body InputPasswordRequest inputPasswordRequest);

    //搜索用户
    @POST(CommonURL.TELGIVE)
    Flowable<FutureHttpResponse<List<TelGiveRespone>>> inputpassword(@Body TelGiveRequest telGiveRequest);


    //单张图片上传f
    @Multipart
    @POST("custom/custom")
    Flowable<ResponseBody> upLoadFile(@Part MultipartBody.Part file);

    //单张图片上传
    @Multipart
    @POST("custom/custom")
    Flowable<ResponseBody> upLoadFile(@Part MultipartBody.Part part, @Part RequestBody paramsBody);

    //多张图片上传
    @Multipart
    @POST("custom/custom")
    Flowable<ResponseBody> upLoadFile(@Part MultipartBody.Part[] file);

    //图文上传
    @Multipart
    @POST("custom/custom")
    Flowable<ResponseBody> upLoadFile(@Part MultipartBody.Part[] parts, @QueryMap Map<String, String> maps);

    //文件下载
    @GET
    @Streaming
    Flowable<ResponseBody> downloadFile(@Url String url);

    //文件上传
    /*上传文件*/
    @Multipart
    @POST("AppYuFaKu/uploadHeadImg")
    Observable<FutureHttpResponse<Test>> uploadImage(@Part("uid") RequestBody uid, @Part("auth_key") RequestBody auth_key, @Part MultipartBody.Part file);

}
