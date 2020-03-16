package com.by.lizhiyoupin.app.user.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.utils.DeviceUtil;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.impl.SelectAvatarCallback;
import com.by.lizhiyoupin.app.component_ui.web.CommonWebJump;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.WebUrlManager;
import com.by.lizhiyoupin.app.io.bean.AlipayBean;
import com.by.lizhiyoupin.app.io.bean.UserHomeBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.login.LoginRequestManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.user.contract.AvatarContract;
import com.by.lizhiyoupin.app.user.presenter.AvatarPresenter;
import com.by.lizhiyoupin.app.utils.EdittextUtil;
import com.by.lizhiyoupin.app.utils.ImageThumbnail;
import com.hjq.toast.ToastUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static android.os.Environment.DIRECTORY_PICTURES;

/*
 * jyx
 * 支付宝绑定页面
 * */
@Route(path = "/app/AlipayBindingActivity")
public class AlipayBindingActivity extends BaseMVPActivity<AvatarContract.AvatarView,
        AvatarContract.AvatarPresenters> implements AvatarContract.AvatarView, View.OnClickListener,
        Handler.Callback, SelectAvatarCallback {
    private TextView mTitle, actionbar_back_tv, getsms, bind, bind2, freelance_service,
            alipay_phone;
    private LinearLayout alipay_bind, alipay_sub;
    private EditText alipay_name, alipay_number, alipay_sms, alipay_idcard;
    //倒计时60秒
    public static final int TIME_INTERVAL_COUNT = 60;
    public static final int KEY_COUNT_DOWN_CODE = 2001;
    private Handler mHandler;
    private ImageView id_img, id_img2;
    private String[] PERMISSIONS_STORAGE = {
            //                Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    public final int REQUEST_TAKE_CODE = 10;
    public final int REQUEST_SELECT_CODE = 20;
    public final int REQUEST_PERMISSION_STORAGE = 50;
    private Uri mImageUri;
    private File mFile;
    private String userLogoBase64 = "";
    private int type = 0;
    private String imgurl, imgurl2;
    private CheckBox alipay_check_xy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay_binding);
        initImmersionBar(Color.WHITE, true);
        mHandler = new Handler(Looper.getMainLooper(), this);
        initBar();
        initView();
        initListener();
        initRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alipay_name.setSelection(alipay_name.getText().toString().length());
        DeviceUtil.showInputMethodDelay(alipay_name, 500);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(CommonConst.WITHDRAW_IDCARD))) {
            findAlipay();
        }
    }

    private void findAlipay() {


        SettingRequestManager.requestAlipayBindFind(getIntent().getStringExtra(CommonConst.WITHDRAW_IDCARD),
                getIntent().getStringExtra(CommonConst.WITHDRAW_FULLNAME))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AlipayBean>>() {
                    @Override
                    public void onNext(BaseBean<AlipayBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }
                        int bindStatus = userInfoBeanBaseBean.getResult().getBindStatus();
                        if (bindStatus == 0) {//未绑定
                            bind.setText("立即绑定");
                        } else if (bindStatus == 1) {//绑定中
                            bind.setText("绑定中");
                        } else if (bindStatus == 2) {//绑定成功
                            bind.setText("立即修改");
                            bind2.setText("立即绑定");
                        } else if (bindStatus == 3) {//绑定失败
                            ToastUtils.show("绑定失败，请重试");
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public AvatarContract.AvatarPresenters getBasePresenter() {
        return new AvatarPresenter(this);
    }

    private void initRecord() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        alipay_phone.setText(accountManager.getUserPhone());
    }

    private void initListener() {
        // 监听多个输入框
        TextChange textChange = new TextChange();
        alipay_name.addTextChangedListener(textChange);
        alipay_number.addTextChangedListener(textChange);
        alipay_sms.addTextChangedListener(textChange);
        alipay_idcard.addTextChangedListener(textChange);

    }

    private void initView() {
        actionbar_back_tv.setOnClickListener(this);
        alipay_bind = findViewById(R.id.alipay_bind);//绑定
        bind = findViewById(R.id.bind);
        bind2 = findViewById(R.id.bind2);
        alipay_sub = findViewById(R.id.alipay_sub);
        alipay_name = findViewById(R.id.alipay_name);//姓名
        alipay_number = findViewById(R.id.alipay_number);//支付宝号
        alipay_phone = findViewById(R.id.alipay_phone);//手机号
        alipay_sms = findViewById(R.id.alipay_sms);//验证码
        getsms = findViewById(R.id.getsms);//获取验证码
        getsms.setOnClickListener(this);
        alipay_bind.setOnClickListener(this);
        id_img = findViewById(R.id.id_img);//身份证正面
        id_img.setOnClickListener(this);
        id_img2 = findViewById(R.id.id_img2);//身份证反面
        id_img2.setOnClickListener(this);
        alipay_idcard = findViewById(R.id.alipay_idcard);//身份证号
        freelance_service = findViewById(R.id.freelance_service);//自由职业服务协议
        freelance_service.setOnClickListener(this);
        alipay_check_xy = findViewById(R.id.alipay_check_xy);

        EdittextUtil.textChinese(alipay_name);//只能输入中文
        EdittextUtil.StringWatcher(alipay_number);//不能输入中文
        EdittextUtil.StringWatcher(alipay_idcard);//不能输入中文


        String bindStatus = getIntent().getStringExtra("bindStatus");
        alipay_name.setText(getIntent().getStringExtra("fullName"));
        alipay_number.setText(getIntent().getStringExtra("account"));
        alipay_idcard.setText(getIntent().getStringExtra("idCard"));

        if ("0".equals(bindStatus)) {//未绑定
            bind.setText("立即绑定");
        } else if ("1".equals(bindStatus)) {//绑定中
            bind.setText("绑定中");
        } else if ("2".equals(bindStatus)) {//绑定成功
            bind.setText("立即修改");
            bind2.setText("立即绑定");
        }
        if ("3".equals(bindStatus)) {//绑定失败
            bind.setText("立即绑定");
        }
        alipay_check_xy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!TextUtils.isEmpty(alipay_name.getText().toString())
                            && !TextUtils.isEmpty(alipay_number.getText().toString())
                            && !TextUtils.isEmpty(alipay_sms.getText().toString())
                            && !TextUtils.isEmpty(alipay_idcard.getText().toString())
                    ) {
                        alipay_sub.setVisibility(View.GONE);
                        alipay_bind.setVisibility(View.VISIBLE);
                    } else {
                        alipay_bind.setVisibility(View.GONE);
                        alipay_sub.setVisibility(View.VISIBLE);
                    }
                } else {
                    alipay_bind.setVisibility(View.GONE);
                    alipay_sub.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        mTitle.setText("支付宝绑定");
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        switch (v.getId()) {
            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.getsms:
                setTimeCodeText(TIME_INTERVAL_COUNT);
                setNumberSms();
                break;
            case R.id.alipay_bind:
                setSubmit();
                break;
            case R.id.id_img:
                DeviceUtil.hideInputMethod(alipay_name);
                type = 1;
                DiaLogManager.showSelectAvatarDialog(this, getSupportFragmentManager(), this);
                break;
            case R.id.id_img2:
                DeviceUtil.hideInputMethod(alipay_name);
                type = 2;
                DiaLogManager.showSelectAvatarDialog(this, getSupportFragmentManager(), this);
                break;
            case R.id.freelance_service:
                CommonWebJump.showCommonWebActivity(this, WebUrlManager.getFreelanceServiceUrl());
                break;
        }
    }

    private void setSubmit() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        SettingRequestManager.requestAlipay(alipay_idcard.getText().toString(),
                alipay_number.getText().toString(),
                alipay_name.getText().toString(), alipay_sms.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AlipayBean>>() {
                    @Override
                    public void onNext(BaseBean<AlipayBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }


                        findIdcard();
                        finish();

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        MessageToast.showToastBottom(AlipayBindingActivity.this, "绑定失败",
                                Gravity.CENTER);
                    }
                });
    }

    private void findIdcard() {

        SettingRequestManager.requestAlipayBindFind(alipay_idcard.getText().toString(),
                alipay_name.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<AlipayBean>>() {
                    @Override
                    public void onNext(BaseBean<AlipayBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }
                        int bindstatus = userInfoBeanBaseBean.getResult().getBindStatus();
                        if (bindstatus == 0) {//未绑定
                            bind.setText("立即绑定");
                        } else if (bindstatus == 1) {//绑定中
                            bind.setText("绑定中");
                        } else if (bindstatus == 2) {//绑定成功
                            bind.setText("立即修改");
                            bind2.setText("立即绑定");
                        }
                        if (bindstatus == 3) {//绑定失败
                            ToastUtils.show("绑定失败，请重试");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void setNumberSms() {

        LoginRequestManager.requestPutPhoneSms(alipay_phone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<Boolean>>() {
                    @Override
                    public void onNext(BaseBean<Boolean> booleanBaseBean) {
                        super.onNext(booleanBaseBean);
                        if (!booleanBaseBean.success()) {
                            onError(new Throwable(booleanBaseBean.msg));
                            return;
                        }
//                        LZLog.i(TAG, "发送验证码 成功==" + booleanBaseBean.data);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        LZLog.i(TAG, "发送验证码 失败==" + throwable);
                    }
                });

    }

    private void setTimeCodeText(int time) {
        getsms.setEnabled(time <= 0);
        if (time > 0) {
            if (getsms != null) {

                getsms.setText(time + "s");
            }
            Message msg = Message.obtain();
            msg.what = KEY_COUNT_DOWN_CODE;
            msg.obj = time - 1;
            mHandler.sendMessageDelayed(msg, 1000);
        } else {
            setRetryCodeText();
        }
    }


    /**
     * 重新发送
     */
    private void setRetryCodeText() {
        if (getsms != null) {
            getsms.setText(getResources().getString(R.string.login_verification_time_retry_text));
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case KEY_COUNT_DOWN_CODE:
                Integer time = (Integer) msg.obj;
                setTimeCodeText(time);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void selectAvatar(int avatarType) {
        if (avatarType == 0) {
            try {
                //检测是否有写的权限和相机权限
                if (ActivityCompat.checkSelfPermission(AlipayBindingActivity.this,
                        "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(AlipayBindingActivity.this,
                                "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(AlipayBindingActivity.this,
                            PERMISSIONS_STORAGE, REQUEST_PERMISSION_STORAGE);
                } else {
                    //相册
                    choosePic();
                }
            } catch (Exception e) {

            }
        } else {

            try {
                //检测是否有写的权限和相机权限
                if (ActivityCompat.checkSelfPermission(AlipayBindingActivity.this,
                        "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(AlipayBindingActivity.this,
                                "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(AlipayBindingActivity.this,
                            PERMISSIONS_STORAGE, REQUEST_PERMISSION_STORAGE);
                } else {
                    //拍照
                    takePic();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void requestAvatorSuccess(String bean) {
        if (type == 1) {
            this.imgurl = bean;
            Log.e("sss", imgurl + "正面");
        } else {
            this.imgurl2 = bean;
            Log.e("sss", imgurl2 + "反面");
        }

    }

    @Override
    public void requestAvatorError(Throwable throwable) {


    }

    @Override
    public void requestUserHomeSuccess(UserHomeBean bean) {

    }

    @Override
    public void requestUserHomeError(Throwable throwable) {

    }

    // EditText监听器
    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean Sign1 = alipay_name.getText().length() > 0;
            boolean Sign2 = alipay_number.getText().length() > 0;
            boolean Sign4 = alipay_sms.getText().length() > 0;
            boolean Sign5 = alipay_idcard.getText().length() > 0;
            if (Sign1 & Sign2 & Sign4 & Sign5 & alipay_check_xy.isChecked()) {
                alipay_sub.setVisibility(View.GONE);
                alipay_bind.setVisibility(View.VISIBLE);
            } else {
                alipay_bind.setVisibility(View.GONE);
                alipay_sub.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 拍照
     */

    public void takePic() {
        try {
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), "temp.jpg");
                ///storage/emulated/0/Pictures/temp.jpg
                mImageUri = Uri.fromFile(mFile);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    //content://com.example.shinelon.takephotodemo
                    // .provider/camera_photos/Pictures/temp.jpg
                    mImageUri = FileProvider.getUriForFile(this,
                            "com.by.lizhiyoupin.app.fileprovider", mFile);
                }
                //指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(cameraIntent, REQUEST_TAKE_CODE);

            } else {
                Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 相册选择
     */
    public void choosePic() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_SELECT_CODE);
    }


    //android6.0之后要动态获取权限
    private void checkPermission(Activity activity, Context context) {
        // Storage Permissions
        String[] PERMISSIONS_STORAGE = {
                //                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        try {
            //检测是否有写的权限和相机的权限
            if (ActivityCompat.checkSelfPermission(context,
                    "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context,
                            "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        REQUEST_PERMISSION_STORAGE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TAKE_CODE://YYF-拍照OK回调
                try {

                    Bitmap bitmap = getBitmapFormUri(mImageUri);
//                    basePresenter.requestAvatar(mImageUri);
                    //YYF图片工具类
                    ImageThumbnail imageThumbnail = new ImageThumbnail();
                    //读取这张图片的旋转角度
                    int degree =
                            imageThumbnail.readPictureDegree(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/temp.jpg");
                    //矫正图片的旋转
                    Bitmap bm = imageThumbnail.rotaingImageView(degree, bitmap);
                    // 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。
                    int scale = imageThumbnail.reckonThumbnail(bm.getWidth(), bm.getHeight(), 200
                            , 200);
                    Bitmap result = imageThumbnail.PicZoom(bm, bm.getWidth() / scale,
                            bm.getHeight() / scale);
                    bm.recycle();//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常

                    Bitmap bb = imageThumbnail.centerSquareScaleBitmap(result, 200);


                    userLogoBase64 = fileToBase64(imageThumbnail.getFile(bb));
                    /*RoundedCorners roundedCorners = new RoundedCorners(100);
                    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                    RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                    .override(100, 100);*/
                    if (type == 1) {
                        Glide.with(this).load(bb).apply(RequestOptions.bitmapTransform(new RoundedCorners(18))).into(id_img);
                    } else if (type == 2) {
                        Glide.with(this).load(bb).apply(RequestOptions.bitmapTransform(new RoundedCorners(18))).into(id_img2);
                    }
                    basePresenter.requestAvatarupdate(mFile);


                } catch (Exception e) {
                }
                break;
            case REQUEST_SELECT_CODE://YYF-相册OK回调
                try {
                    //                    Toast.makeText(getApplicationContext(), resultCode +
                    //                    "", Toast.LENGTH_SHORT).show();
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    //获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                            null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    cursor.close();

                    // Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    Bitmap bitmap = getBitmapFormUri(selectedImage);
                    //YYF图片工具类
                    ImageThumbnail imageThumbnail = new ImageThumbnail();
                    //读取这张图片的旋转角度
                    int degree = imageThumbnail.readPictureDegree(picturePath);
                    //矫正图片的旋转
                    Bitmap bm = imageThumbnail.rotaingImageView(degree, bitmap);
                    // 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。
                    int scale = imageThumbnail.reckonThumbnail(bm.getWidth(), bm.getHeight(), 200
                            , 200);
                    Bitmap result = imageThumbnail.PicZoom(bm, bm.getWidth() / scale,
                            bm.getHeight() / scale);
                    bm.recycle();//由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常

                    Bitmap bb = imageThumbnail.centerSquareScaleBitmap(result, 200);

                    userLogoBase64 = fileToBase64(imageThumbnail.getFile(bb));
                  /*  RoundedCorners roundedCorners = new RoundedCorners(100);
                    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                    RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                    .override(100, 100);*/
                    if (type == 1) {
                        Glide.with(this).load(bb).apply(RequestOptions.bitmapTransform(new RoundedCorners(18))).into(id_img);
                    } else if (type == 2) {
                        Glide.with(this).load(bb).apply(RequestOptions.bitmapTransform(new RoundedCorners(18))).into(id_img2);
                    }
                    basePresenter.requestAvatarupdate(imageThumbnail.getFile(bb));
                } catch (Exception e) {

                }
                break;
        }
    }

    public Bitmap getBitmapFormUri(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getContentResolver().openInputStream(uri);

        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;

        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options <= 0)
                break;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos
        // 存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 文件转base64字符串
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            int length = in.read(bytes);
            base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "申请成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "拒绝权限", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        DeviceUtil.hideInputMethod(alipay_name);
    }
}
