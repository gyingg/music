package com.future_melody.net;

/**
 * 接口地址
 */
public class CommonURL {
    public final static String BASE_URL = "/futuremelody-api";//正式加测试  future_sound-web
    // public final static String BASE_URL = "";
    /**
     * 登录接口地址 post
     */
    public static final String LOGIN = BASE_URL + "/loginUser";
    /**
     * 注册接口地址 post
     */
    public static final String REGISTER = BASE_URL + "/addUser";
    /**
     * 注册接获取短信验证码 post
     */
    public static final String MSGCODE = BASE_URL + "/send";
    /**
     * 短信验证码登录 post
     */
    public static final String SENGDLOGIN = BASE_URL + "/loginUserByphone";
    /**
     * 短信登录验证码 post
     */
    public static final String SENGDLOGINMSG = BASE_URL + "/sendlogin";
    /**
     * 找回密码 post
     */
    public static final String FINDPSW = BASE_URL + "/updateUserpassword";
    /**
     * 设置个人资料 post
     */
    public static final String SETUSERINFO = BASE_URL + "/updateuser";
    /**
     * 获取个人资料 post
     */
    public static final String GETUSERINFO = BASE_URL + "/queryuserbyid";
    /**
     * 设置头像 post
     */
    public static final String SETUSERIMG = BASE_URL + "/upload";
    /**
     * 编辑音乐信息 post
     */
    public static final String EDITMUSICINFO = BASE_URL + "/addSpecialmusic";
    /**
     * 发布主题 post
     */
    public static final String SENDTHEME = BASE_URL + "/addSpecial";

    /**
     * 删除喜欢item post
     */
    public static final String DELETELIKES = BASE_URL + "/personal/deletelike";
    /**
     * 主题详情 post
     */
    public static final String THEMEDETAILS = BASE_URL + "/recommend/findRecommendSpecialDetail";

    /**
     * 获取推荐主题列表 post
     */
    public static final String RECOMMEND = BASE_URL + "/recommend/getRecommendSpecialList";

    /**
     * 我的界面 post
     */
    public static final String MINEINFO = BASE_URL + "/personal/querypersonal";

    /**
     * 加入星球
     */
    public static final String JOINSTAR = BASE_URL + "/personal/addxingqiu";
    /**
     * 评论列表
     */
    public static final String COMMENTLIST = BASE_URL + "/recommend/getRecommendSpecialComment";
    /**
     * New评论列表
     */
    public static final String COMMENTLIST_NEW = BASE_URL + "/recommend/getRecommendSpecialComment2";
    /**
     * 回复评论
     */
    public static final String REPLYCOMMENT = BASE_URL + "/recommend/addComment";

    /**
     * 修改个人信息
     */
    public static final String UPDATEUSER = BASE_URL + "/updateuser ";

    /**
     * 修改个人信息
     */
    public static final String SHOWUSER = BASE_URL + "/queryuserbyid ";

    /**
     * 0:歌曲点赞 1:评论点赞
     */
    public static final String DOTPRAISE = BASE_URL + "/recommend/addOrCancelLike";


    /**
     * 1今日之星 2一周最佳
     */
    public static final String WEEKSUPERU = BASE_URL + "/recommend/getLeaderboard";

    /**
     * 点亮榜
     */
    public static final String MUSICLEADER = BASE_URL + "/recommend/getMusicLeaderboardVo";

    /**
     * 查询行星信息
     */
    public static final String STARDETAILS = BASE_URL + "/recommend/findPlanetInfo";

    /**
     * 我的：推荐主题
     */
    public static final String MINETHEME = BASE_URL + "/personal/queryspecial";
    /**
     * 我的：推荐音乐
     */
    public static final String MINEMUSIC = BASE_URL + "/personal/queryspecialmusic";
    /**
     * 我的：关注
     */
    public static final String MINEFOLLOW = BASE_URL + "/personal/queryspecialguanzhu";
    /**
     * 我的：粉丝
     */
    public static final String MINEFANS = BASE_URL + "/personal/queryspecialfensi";
    /**
     * 取消关注
     */
    public static final String CANCELFOLLOW = BASE_URL + "/personal/cancelconcern";

    /**
     * 添加关注
     */
    public static final String ADDFOLLOW = BASE_URL + "/personal/addconcern";


    /**
     * 关注
     */
    public static final String FOLLOW = BASE_URL + "/personal/addconcern";
    /**
     * 查询关注人的主题
     */
    public static final String ATTENTIONTHEME = BASE_URL + "/recommend/getAttentionSpecial";
    /**
     * 查询行星推荐音乐
     */
    public static final String PLANETMUSIC = BASE_URL + "/recommend/getPlanetMusic";
    /**
     * 查询行星推荐主题
     */
    public static final String PLANETTHEME = BASE_URL + "/recommend/getPlanetSpecial";
    /**
     * 我的通知
     */
    public static final String MYINFORM = BASE_URL + "/personal/getNotice ";

    /**
     * 反馈
     */
    public static final String FANKUI = BASE_URL + "/personal/addopinion";
    /**
     * 喜欢
     */
    public static final String LIKE = BASE_URL + "/personal/querylikeer";
    /**
     * 管理星球
     */
    public static final String STAR_GUANLI = BASE_URL + "/personal/queryruler";
    /**
     * 管理星球 :更换背景 ,修改介绍
     */
    public static final String STAR_GUANLI_IMG = BASE_URL + "/personal/updateruler";
    /**
     * 任命小行星列表
     */
    public static final String STAR_RENMING_LIST = BASE_URL + "/personal/queryshouhu";
    /**
     * 查看发布人主页
     */
    public static final String USETINFO = BASE_URL + "/recommend/findPublisher";

    /**
     * 获取发布人主页的主题集合
     */
    public static final String RECOMMEND_THEME = BASE_URL + "/recommend/getPublisherSpecialList";

    /**
     * 获取发布人主页的主题集合
     */
    public static final String RECOMMEND_MUSIC = BASE_URL + "/recommend/getPublisherMusicList";
    /**
     * 任命小行星列表 添加和取消
     */
    public static final String STAR_SET_RENMING = BASE_URL + "/personal/updatshouhu";
    /**
     * 主题分享
     */
    public static final String SHAR_THEME = BASE_URL + "/recommend/findShareInfo";
    /**
     * 主题置顶
     */
    public static final String TOP_THEME = BASE_URL + "/recommend/updateTopSpecial";
    /**
     * 星歌
     */
    public static final String XINGMUSIC = BASE_URL + "/star/music/getStarMusicAndActive";
    /**
     * 推荐星歌List
     */
    public static final String XINGTOPLIST = BASE_URL + "/personal/querymusicbyuserid";
    /**
     * 立即邀请
     */
    public static final String SHSRT_FRIENDS = BASE_URL + "/friendsShare/findFriendsShareByType";

    /**
     * 星歌推荐
     */
    public static final String SETXINGTOP = BASE_URL + "/personal/addstartmusic";
    /**
     * 星歌榜
     */
    public static final String XINGTOP = BASE_URL + "/star/music/getStarMusicLeaderboard";
    /**
     * 星歌榜点赞或者取消
     */
    public static final String MUSICZAN = BASE_URL + "/star/music/addOrCancelStarMusic";
    /**
     * 输入邀请码
     */
    public static final String INVITATIONI_Code = BASE_URL + "/personal/updateyaoqingma";
    /**
     * 资产(分贝)
     */
    public static final String FENBEI_URL = BASE_URL + "/personal/queryzichan";

    /**
     * 资产(黑珍珠)
     */
    public static final String BLACKPEARL = BASE_URL + "/personal/queryheizhenzhu";
    /**
     * 分享音乐
     */
    public static final String SHARE_MUSIC = BASE_URL + "/recommend/getShareMusicInfo";
    /**
     * 版本更新
     */
    public static final String VERSION = BASE_URL + "/queryversion";
    /**
     * 听新歌
     */
    public static final String LISTERMUSIC = BASE_URL + "/star/music/addStarMusicListenRecord";
    /**
     * 设置资金密码
     */
    public static final String SETMONEYPSW = BASE_URL + "/addcapital";
    /**
     * 设置资金密码
     */
    public static final String FINDMONEYPSW = BASE_URL + "/updatecapital";
    /**
     * 退出登录
     */
    public static final String EXIT_LOGIN = BASE_URL + "/esituser";

    public static final String TEST = BASE_URL + "/query";
    public static final String BANNER = BASE_URL + "/banner/index";
    /*
     * 上传
     * */
    public static final String UPLOAN = BASE_URL + "/upload";
    /*
     * 上传音乐
     * */
    public static final String UPLOANMUSIC = BASE_URL + "/uploadmusic";
    /*
     * 是否收藏音乐
     * */
    public static final String ISLIKEMUSIC = BASE_URL + "/star/music/checkIsList";
    /*
     * 主题置顶
     * */
    public static final String setThemeTop = BASE_URL + "/recommend/updateTopSpecial";
    /*
     * 新版推荐
     * */
    public static final String GETRECOMMENDList = BASE_URL + "/recommend/getSpecialList";
    /*
     * 新版星歌排行榜
     * */
    public static final String XINGMUSICTOP_NEW = BASE_URL + "/star/music/getStarMusicLeaderboardV2";

    /*
     * 获取活动
     * */
    public static final String MainActivityFragment = BASE_URL + "/active/getActive";

    /*
     * 获取活动
     * */
    public static final String REMCOMMENDSHARE = BASE_URL + "/recommend/getSpecialShare";
    /*
     * 小未扫码
     * */
    public static final String XIAOWEIQRCODE = BASE_URL + "/wxsn/executeScanCode";
    /*
     * 小未显示数据
     * */
    public static final String MINEXIAOWEI = BASE_URL + "/wxsn/getMyXw";
    /*
     * 我的小未列表
     * */
    public static final String MINEXIAOWEILIST = BASE_URL + "/wxsn/getSn";
    /*
     * 小未收益明细
     * */
    public static final String MINEXIAOWEIMONEYINFO = BASE_URL + "/wxsn/getXwBpData";
    /*
     * 听歌曲
     * */
    public static final String LISTER_MUCI = BASE_URL + "/star/music/savePlayRecord";
    /*
     * 分享成功回调
     * */
    public static final String SHARE_SUCCEC = BASE_URL + "/recommend/saveSpecialShare";
    /*
     * 主题点赞
     * */
    public static final String THEME_PICK = BASE_URL + "/recommend/saveSpecialLike";
    /*
     * 主页推荐页
     * */
    public static final String ROMMEND_THEME = BASE_URL + "/recommend/getRecommendSpecialV2";
    /*
     * 小未播放状态
     * */
    public static final String XiaoWeiPlayer = BASE_URL + "/wxsn/getXwPlayState";
    /**
     * 转账
     */
    public static final String GIVEPRESENTER = BASE_URL + "/transferaccounts";

    /**
     * 输入资金密码
     */
    public static final String INPUTPASSWORD = BASE_URL + "/queryzijinpassword";
    /**
     * 搜索用户
     */
    public static final String TELGIVE = BASE_URL + "/queryusermohu";


}
