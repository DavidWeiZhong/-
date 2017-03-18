package com.example.lcit.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import dialog.CustomDialog;
import dialog.WarnDialog;

/**
 * 各种dialog分析
 */
public class MainActivity extends AppCompatActivity {

    private CustomDialog mBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnclick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                WarnDialog.Builder builder = new WarnDialog.Builder(this)
                        .setMessage("确认退出当前账号？")
                        .setTitle("提示")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                WarnDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.btn2:

                CustomDialog.Builder builder1 = new CustomDialog.Builder(this);
                mBuild = builder1.cancelTouchout(false)
                        .view(R.layout.dialog_loginerror)
                        .heightDimenRes(R.dimen.dialog_loginerror_height)
                        .widthDimenRes(R.dimen.dialog_loginerror_width)
                        .style(R.style.Dialog)//一般设置为透明的
                        .addViewOnclick(R.id.btn_cancel,new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mBuild.dismiss();
                                    }
                                })
                        .build();
                mBuild.show();
//                dialog = builder1.cancelTouchout(false)
//                                .view(R.layout.dialog_loginerror)
//                                .heightDimenRes(R.dimen.dialog_loginerror_height)
//                                .widthDimenRes(R.dimen.dialog_loginerror_width)
//                                .style(R.style.Dialog)
//                                .addViewOnclick(R.id.btn_cancel,new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .build();
//                dialog.show();
                break;
        }

    }
}
