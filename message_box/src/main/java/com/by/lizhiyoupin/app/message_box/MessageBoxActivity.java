package com.by.lizhiyoupin.app.message_box;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.by.lizhiyoupin.app.message_box.bean.MessageButton;
import com.by.lizhiyoupin.app.message_box.holder.ContentCreater;

import androidx.appcompat.app.AppCompatActivity;


/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/9/21 17:51
 * Summary:MessageBox 使用样例
 */
public class MessageBoxActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        findViewById(R.id.button).setOnClickListener(v -> {
            MessageBox.builder()
                    .contentHolder(ContentCreater.createTips(this, "您的title已经出現", "请耐心等待message", R.drawable.messagebox_icon))
                    .bgColor(Color.TRANSPARENT)
                    .showBottomDivider(false)
                    .build("wait")
                    .show(getSupportFragmentManager());
        });

        findViewById(R.id.button7).setOnClickListener(v -> {
            MessageBox.builder()
                    .contentMessage("圆角，无标题，无按钮情况！\n我是第二行的数据", 10f, 1.5f)
                    .radius(20, 20, 20, 20)
                    .showBottomDivider(false)
                    .build("wait")
                    .show(getSupportFragmentManager());
        });

        findViewById(R.id.button8).setOnClickListener(v -> {
            MessageBox.builder()
                    .title("圆角对话框")
                    .contentMessage("圆角，有标题，无按钮情况！")
                    .radius(20, 20, 20, 20)
                    .showBottomDivider(false)
                    .build("wait")
                    .show(getSupportFragmentManager());
        });

        findViewById(R.id.button9).setOnClickListener(v -> {
            MessageBox.builder()
                    .title("圆角对话框")
                    .contentHolder(ContentCreater.createTips(MessageBoxActivity.this, "提示内容", "提示副内容"))
                    .radius(20, 20, 20, 20)
                    .showBottomDivider(false)
                    .build("wait")
                    .show(getSupportFragmentManager());
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            MessageBox.builder()
                    .title("绑定提示")
                    .contentMessage("检测到您输入的手机号已注册，您是否需要将本账号与该手机号合并？之后您可以使用本账号快速登录。")
                    .contentGravity(Gravity.LEFT)
                    .bgColor(Color.WHITE)
                    .titleHeight(200)
                    .radius(20, 20, 20, 20)
                    .Ok("确定")
                    .Cancel("取消")
                    .addButton(new MessageButton("测试", Color.YELLOW))
                    .dimAmount(0f)
                    .onDismissDialogListener((messagebox, bundle, holder, initiativeDiasmiss) -> Log.d("MainActivity", "====================>>>>>>> initiativeDiasmiss " + initiativeDiasmiss))
                    .build("dialog")
                    .show(getSupportFragmentManager());
        });

        findViewById(R.id.button3).setOnClickListener(v -> {
            Reference.FULL_SCREEN_DIALOG
                    .contentHolder(ContentCreater.createTips(this, "您的材料已经提交", "请耐心等待审核", -1))
                    .bgColor(Color.TRANSPARENT)
                    .bgContentColor(Color.TRANSPARENT)
                    .animationStyleRes(Reference.Animation.Animation_Dialog_Fade)
                    .showBottomDivider(false)
                    .build("wait")
                    .show(getSupportFragmentManager());
        });

        findViewById(R.id.button6).setOnClickListener(v -> {
            MessageBox.builder()
                    .contentMessage("您的材料已经提交")
                    .bgColor(Color.TRANSPARENT)
                    .showBottomDivider(false)
                    .animationStyleRes(R.style.MessageBox_Animations_Dialog_Bottom)
                    .styleRes(R.style.dialog_share_theme)
                    .fullScreen(true)
                    .build("wait")
                    .show(getSupportFragmentManager());
        });

    }
}
