package com.future_melody.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.future_melody.R;
import com.future_melody.base.BaseActivity;
import com.future_melody.music.PlayerUitlis;
import com.future_melody.net.HttpSubscriber;
import com.future_melody.net.RxHttpUtil;
import com.future_melody.net.exception.ApiException;
import com.future_melody.net.request.GivePresenterRequest;
import com.future_melody.net.request.InputPasswordRequest;
import com.future_melody.net.respone.FutureHttpResponse;
import com.future_melody.net.respone.GivePresenterRespone;
import com.future_melody.net.respone.InputPasswordRespone;
import com.future_melody.utils.CommonUtils;
import com.future_melody.utils.FileUtil;
import com.future_melody.utils.passwordview.AndroidBug5497Workaround;
import com.future_melody.utils.passwordview.GiveDecibelNullTelDialog;
import com.future_melody.utils.TipLinearUtil;
import com.future_melody.utils.passwordview.MD5Tool;
import com.future_melody.utils.passwordview.PassWordDialog;
import com.future_melody.utils.passwordview.PasswordView;
import com.gyf.barlibrary.ImmersionBar;
import com.lzx.musiclibrary.manager.MusicManager;


/**
 * 赠送分贝
 */
public class GiveDecibelActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView ph_title_right_img;
    private Animation animation;
    private ImageView give_tel;
    private LinearLayout btn_mine_hei;
    private LinearLayout btn_mine_hei2;
    private LinearLayout btn_mine_fenbei;
    private LinearLayout btn_mine_fenbei2;
    private Button give_presenter;
    private EditText et_tel;
    private EditText et_say;
    private EditText fenbei_num;
    private int type = 1;
    private int count;
    private String tel;
    private String password = "";   //最后输入的密码
    private PassWordDialog passWordDialog;
    private String countString;
    private int screenHeight;
    private int keyHeight;
    private LinearLayout mContent;
    private String voice;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_give_decibel;
    }

    @Override
    protected void initView() {
        //修复沉浸式状态下无法监听键盘弹起bug
        AndroidBug5497Workaround.assistActivity(this);

        back = findViewById(R.id.back);
        ph_title_right_img = findViewById(R.id.ph_title_right_img);
        give_tel = findViewById(R.id.give_tel);
        btn_mine_hei = findViewById(R.id.btn_mine_hei);
        btn_mine_hei2 = findViewById(R.id.btn_mine_hei2);
        btn_mine_fenbei = findViewById(R.id.btn_mine_fenbei);
        btn_mine_fenbei2 = findViewById(R.id.btn_mine_fenbei2);
        give_presenter = findViewById(R.id.give_presenter);
        et_tel = findViewById(R.id.et_tel);
        fenbei_num = findViewById(R.id.fenbei_num);
        et_say = findViewById(R.id.et_say);

        btn_mine_hei2.setVisibility(View.GONE);
        btn_mine_hei.setVisibility(View.VISIBLE);
        btn_mine_fenbei2.setVisibility(View.VISIBLE);
        btn_mine_fenbei.setVisibility(View.GONE);
    }


    private void ScrollViewlogin() {
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
        ScrollView scrollView = findViewById(R.id.scrollView);
        mContent = findViewById(R.id.content);
        //禁止滑动
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        scrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (getCurrentFocus() != null && getCurrentFocus().getBottom() < keyHeight)
                    return;
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e("wenzhihao", "up------>" + (oldBottom - bottom));
                    int dist = (mContent.getBottom() - bottom) / 4;
                    Log.e("wenzhihao", "dist------>" + dist);
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                    }
                }
            }
        });
    }


    @Override
    protected void initData() {
        ScrollViewlogin();
        Intent intent = getIntent();
        tel = intent.getStringExtra("tel");
        et_tel.setText(tel);
        et_tel.setSelection(et_tel.getText().toString().length());
        back.setOnClickListener(this);
        ph_title_right_img.setOnClickListener(this);
        give_tel.setOnClickListener(this);
        btn_mine_hei.setOnClickListener(this);
        btn_mine_hei2.setOnClickListener(this);
        btn_mine_fenbei.setOnClickListener(this);
        btn_mine_fenbei2.setOnClickListener(this);
        give_presenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ph_title_right_img:
                PlayerUitlis.player(mActivity);
                break;
            case R.id.back:
                finish();
                hideSoftInputView();
                break;
            case R.id.btn_mine_hei:
                type = 2;
                toast("只能转分贝呦~");
                /*btn_mine_hei.setVisibility(View.GONE);
                btn_mine_hei2.setVisibility(View.VISIBLE);
                btn_mine_fenbei.setVisibility(View.VISIBLE);
                btn_mine_fenbei2.setVisibility(View.GONE);*/
                break;
            case R.id.btn_mine_fenbei:
                type = 1;
                btn_mine_hei2.setVisibility(View.GONE);
                btn_mine_hei.setVisibility(View.VISIBLE);
                btn_mine_fenbei2.setVisibility(View.VISIBLE);
                btn_mine_fenbei.setVisibility(View.GONE);
                break;
            case R.id.give_tel:
                intent = new Intent(mActivity, GiveDecibelTelActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.give_presenter:
                if (type == 0) {
                    TipLinearUtil.create(mActivity).showTipMessage("请选中转出类型");
                    return;
                }
                countString = fenbei_num.getText().toString().trim();
                if (!FileUtil.isWholeNumber(countString)) {
                    TipLinearUtil.create(mActivity).showTipMessage("请输入正整数转出数量");
                    return;
                }
                //请输入手机号
                tel = et_tel.getText().toString().trim();
                //转账的分贝数或者黑珍珠数
                count = Integer.parseInt(countString);
                //赠送话语
                voice = et_say.getText().toString().trim();
                givePresenter(count, tel, userId(), type, voice);
                break;
        }
    }

    //转账
    private void givePresenter(final int count, String user_name, String userid, int type, String explains) {
        addSubscribe(apis.givepresenter(new GivePresenterRequest(count, user_name, userid, type, explains))
                .compose(RxHttpUtil.<FutureHttpResponse<GivePresenterRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<GivePresenterRespone>handleResult())
                .subscribeWith(new HttpSubscriber<GivePresenterRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        GiveDecibelNullTelDialog dialog = new GiveDecibelNullTelDialog(mActivity);
                        dialog.setMsg(exception.getMessage());
                        dialog.setCancelButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setDetermineButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }) {
                    @Override
                    public void onNext(GivePresenterRespone givePresenterRespone) {
                        int types = givePresenterRespone.types;
                        if (types == 1) {
                            Intent intent = new Intent(mActivity, FundPasswordActivity.class);
                            startActivity(intent);
                            return;
                        }
                        //弹出成功的dialog
                        showPasswordDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }


    //输入密码 passwordview
    private void showPasswordDialog() {
        passWordDialog = new PassWordDialog(mActivity);
        Button button = (Button) passWordDialog.findViewById(R.id.button);
        TextView tv_gold = passWordDialog.findViewById(R.id.tv_gold);
        tv_gold.setText(countString);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入资金密码
                String md5password = MD5Tool.encryptMD5ToString(password);
                inputPassword(userId(), md5password, count, tel, type);
                hideSoftInputView();
            }
        });

        passWordDialog.setPasswordListener(new PasswordView.PasswordListener() {
            @Override
            public void passwordChange(String changeText) {
                //改变回调监听
                Log.d("tag", "changeText = " + changeText);
            }

            @Override
            public void passwordComplete(String text) {
                password = text;
                //输入结束监听
                Log.d("tag", "passwordComplete:" + text);
            }

            @Override
            public void keyEnterPress(String password, boolean isComplete) {
                //isComplete 为true  说明按下键盘确认键
                Log.d("tag", "password = " + password + " isComplete = " + isComplete);
            }

        });
        passWordDialog.show();
    }


    //输入资金密码
    private void inputPassword(final String userid, String zijingpassword, int count, String user_name, int type) {
        showPasswordDialog();  //缓冲
        addSubscribe(apis.inputpassword(new InputPasswordRequest(userid, zijingpassword, count, user_name, type))
                .compose(RxHttpUtil.<FutureHttpResponse<InputPasswordRespone>>rxSchedulerHelper())
                .compose(RxHttpUtil.<InputPasswordRespone>handleResult())
                .subscribeWith(new HttpSubscriber<InputPasswordRespone>(new HttpSubscriber.ErrorListener() {
                    @Override
                    public void onError(ApiException exception) {
                        dismissLoadingDialog(); //缓冲
                        GiveDecibelNullTelDialog dialog = new GiveDecibelNullTelDialog(mActivity);
                        dialog.setMsg(exception.getMessage());
                        dialog.setCancelButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setDetermineButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }) {

                    @Override
                    public void onNext(InputPasswordRespone inputPasswordRespone) {
                        dismissLoadingDialog(); //缓冲
                    }

                    @Override
                    public void onComplete() {
                        dismissLoadingDialog();
                        passWordDialog.dismiss(); //缓冲
                        finish();
                    }
                }));
    }


    //如果输入法在窗口上已经显示，则隐藏，反之则显示
    public void toggleKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  //popwindoe  隐藏键盘
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnim();
        if (MusicManager.isPlaying()) {
            startAnmi();
        } else {
            stoptAnmi();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAnmi();
    }

    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.player);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
    }

    private void startAnmi() {
        ph_title_right_img.startAnimation(animation);
    }

    private void stoptAnmi() {
        ph_title_right_img.clearAnimation();
    }

    private void destroyAnmi() {
        animation.cancel();
        animation = null;
    }
}
