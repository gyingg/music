package com.future_melody.utils;

/**
 * .
 * Created by Administrator on 2018/5/28/028.
 */

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
/*  android:inputType=none--输入普通字符
        android:inputType=text--输入普通字符
        android:inputType=textCapCharacters--输入普通字符
        android:inputType=textCapWords--单词首字母大小
        android:inputType=textCapSentences--仅第一个字母大小
        android:inputType=textAutoCorrect--前两个自动完成
        android:inputType=textAutoComplete--前两个自动完成
        android:inputType=textMultiLine--多行输入
        android:inputType=textImeMultiLine--输入法多行（不一定支持）
        android:inputType=textNoSuggestions--不提示
        android:inputType=textUri--URI格式
        android:inputType=textEmailAddress--电子邮件地址格式
        android:inputType=textEmailSubject--邮件主题格式
        android:inputType=textShortMessage--短消息格式
        android:inputType=textLongMessage--长消息格式
        android:inputType=textPersonName--人名格式
        android:inputType=textPostalAddress--邮政格式
        android:inputType=textPassword--密码格式
        android:inputType=textVisiblePassword--密码可见格式
        android:inputType=textWebEditText--作为网页表单的文本格式
        android:inputType=textFilter--文本筛选格式
        android:inputType=textPhonetic--拼音输入格式
        android:inputType=number--数字格式
        android:inputType=numberSigned--有符号数字格式
        android:inputType=numberDecimal--可以带小数点的浮点格式
        android:inputType=phone--拨号键盘
        android:inputType=datetime
        android:inputType=date--日期键盘

        android:inputType=time--时间键盘*/

public class InputManager {
    private Context context;
    public static InputManager inputManager;
    private InputMethodManager imm;

    private InputManager(Context context) {
        this.context = context; // 得到InputMethodManager的实例
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        totleShowSoftInput();
    }

    public static InputManager getInstances(Context context) {
        if (inputManager == null) {
            inputManager = new InputManager(context);
        }
        return inputManager;
    }

    /**
     * 切换软键盘的显示与隐藏
     */
    public void totleShowSoftInput() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * 关闭键盘
     */
    public void hideALlSoftInput() {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
