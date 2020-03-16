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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.by.lizhiyoupin.app.LiZhiApplication;
import com.by.lizhiyoupin.app.R;
import com.by.lizhiyoupin.app.common.CommonConst;
import com.by.lizhiyoupin.app.common.ComponentManager;
import com.by.lizhiyoupin.app.common.log.LZLog;
import com.by.lizhiyoupin.app.common.utils.CommonToast;
import com.by.lizhiyoupin.app.common.utils.TimeUtils;
import com.by.lizhiyoupin.app.component_ui.activity.BaseMVPActivity;
import com.by.lizhiyoupin.app.component_ui.impl.SelectAvatarCallback;
import com.by.lizhiyoupin.app.component_ui.web.CommonSchemeJump;
import com.by.lizhiyoupin.app.io.BaseBean;
import com.by.lizhiyoupin.app.io.DefaultRx2Subscribe;
import com.by.lizhiyoupin.app.io.bean.AlipayBean;
import com.by.lizhiyoupin.app.io.bean.UserHomeBean;
import com.by.lizhiyoupin.app.io.bean.UserInfoBean;
import com.by.lizhiyoupin.app.io.manager.IAccountManager;
import com.by.lizhiyoupin.app.manager.DiaLogManager;
import com.by.lizhiyoupin.app.message_box.MessageToast;
import com.by.lizhiyoupin.app.user.SettingRequestManager;
import com.by.lizhiyoupin.app.user.contract.AvatarContract;
import com.by.lizhiyoupin.app.user.presenter.AvatarPresenter;
import com.by.lizhiyoupin.app.utils.BirthdayUtil;
import com.by.lizhiyoupin.app.utils.CacheUtil;
import com.by.lizhiyoupin.app.utils.ImageThumbnail;
import com.by.lizhiyoupin.app.utils.SexUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
 * 设置页面
 *jyx
 * */
@Route(path = "/app/SettingActivity")
public class SettingActivity extends BaseMVPActivity<AvatarContract.AvatarView,
        AvatarContract.AvatarPresenters> implements AvatarContract.AvatarView,
        View.OnClickListener, SelectAvatarCallback {
    private TextView mTitle, actionbar_back_tv, sex, birthday, name, phone, bank, zfb, wx, cache;
    private RelativeLayout setting_nickname, setting_number, setting_alipay, setting_bankcard,
            setting_sex, setting_birthday, setting_wx, clearcache;
    private LinearLayout setting_icon, exit_login;
    private String[] PERMISSIONS_STORAGE = {
            //                Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    public final int REQUEST_TAKE_CODE = 10;
    public final int REQUEST_SELECT_CODE = 20;
    public final int REQUEST_PERMISSION_STORAGE = 50;
    public final int REQUESTCODE_CUTTING = 311;
    private Uri mImageUri;
    private File mFile;
    private ImageView user_logo;
    private View view;
    private String account, bankAccount, fullName, idCard, bankName, bankNickName;
    private int bindStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initImmersionBar(Color.WHITE,true);
        initBar();
        initView();
        checkPermission(this, this);
    }

    @Override
    public AvatarContract.AvatarPresenters getBasePresenter() {
        return new AvatarPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLatestData();
    }

    private void initLatestData() {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        UserInfoBean accountInfo = accountManager.getAccountInfo();
        String sname = accountInfo.getName();
        String sphone = accountInfo.getPhone();
        String sbirthday = accountInfo.getUserBirthday();
        String alipay_number = accountInfo.getAlipayAccount();
        String bank_number = accountInfo.getBankAccount();
        if (sbirthday != null) {
            birthday.setText(sbirthday.substring(0, 10));
        }
        int gender = accountInfo.getGender();
        String swx = accountInfo.getWechat();
        String avatar = accountInfo.getAvatar();
        Glide.with(this).load(avatar).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(user_logo);

        wx.setText(swx);
        name.setText(sname);
        phone.setText(sphone);

        if (gender == 0) {
            sex.setText("");
        } else if (gender == 1) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }


        SettingRequestManager.requestFindInfo()
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
                        bindStatus = userInfoBeanBaseBean.getResult().getBindStatus();//支付宝绑定状态
                        account = userInfoBeanBaseBean.getResult().getAccount();//支付宝号
                        bankAccount = userInfoBeanBaseBean.getResult().getBankAccount();//银行卡号
                        fullName = userInfoBeanBaseBean.getResult().getFullName();//支付宝正式姓名
                        idCard = userInfoBeanBaseBean.getResult().getIdCard();//身份证号
                        bankName = userInfoBeanBaseBean.getResult().getBankName();//银行开户行
                        bankNickName = userInfoBeanBaseBean.getResult().getBankNickName();//银行开户名
                        zfb.setText(account);
                        bank.setText(bankAccount);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        MessageToast.showToastBottom(SettingActivity.this,  throwable.toString(),
                                Gravity.CENTER);

                    }
                });


    }

    private void initView() {
        user_logo = findViewById(R.id.user_logo);
        actionbar_back_tv.setOnClickListener(this);
        setting_nickname = findViewById(R.id.setting_nickname);
        setting_nickname.setOnClickListener(this);//修改昵称
        setting_number = findViewById(R.id.setting_number);
        setting_number.setOnClickListener(this);//修改手机号
        setting_alipay = findViewById(R.id.setting_alipay);
        setting_alipay.setOnClickListener(this);//支付宝绑定
        setting_bankcard = findViewById(R.id.setting_bankcard);
        setting_bankcard.setOnClickListener(this);//银行卡绑定
        setting_icon = findViewById(R.id.setting_icon);
        setting_icon.setOnClickListener(this);//更换头像
        setting_sex = findViewById(R.id.setting_sex);
        setting_sex.setOnClickListener(this);//更换性别
        sex = findViewById(R.id.sex);
        birthday = findViewById(R.id.birthday);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        wx = findViewById(R.id.wx);
        zfb = findViewById(R.id.zfb);
        bank = findViewById(R.id.bank);
        setting_birthday = findViewById(R.id.setting_birthday);
        setting_birthday.setOnClickListener(this);//更换生日
        setting_wx = findViewById(R.id.setting_wx);
        setting_wx.setOnClickListener(this);//修改微信号
        view = findViewById(R.id.view);
        exit_login = findViewById(R.id.exit_login);
        exit_login.setOnClickListener(this);
        clearcache = findViewById(R.id.clearcache);
        clearcache.setOnClickListener(this);//清除缓存
        cache = findViewById(R.id.cache);//缓存大小

        try {
            String size = CacheUtil.getTotalCacheSize(this);
            cache.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String operator = getIntent().getStringExtra(CommonConst.WITHDRAW_OPERATOR);
        if (CommonConst.KEY_OPERATE_NAME.equals(operator)) {
            setting_bankcard.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }

    }

    private void initBar() {
        findViewById(R.id.actionbar).setBackgroundColor(Color.WHITE);
        mTitle = findViewById(R.id.actionbar_title_tv);
        actionbar_back_tv = findViewById(R.id.actionbar_back_tv);
        actionbar_back_tv.setText("");
        mTitle.setText("设置");
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFrequentOperation()) {
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {

            case R.id.actionbar_back_tv:
                finish();
                break;
            case R.id.setting_nickname:

                bundle.putString(CommonConst.WITHDRAW_NAME, "name");
                CommonSchemeJump.showNicknameActivity(this, bundle);
                break;
            case R.id.setting_number:
                CommonSchemeJump.showNumberActivity(this);
                break;
            case R.id.setting_alipay:
                bundle.putString(CommonConst.WITHDRAW_ACCOUNT, account);
                bundle.putString(CommonConst.WITHDRAW_FULLNAME, fullName);
                bundle.putString(CommonConst.WITHDRAW_IDCARD, idCard);
                bundle.putString(CommonConst.WITHDRAW_BINDSTATUS, bindStatus + "");
                CommonSchemeJump.showActivity(this, "/app/AlipayBindingActivity", bundle);
                break;
            case R.id.setting_bankcard:
                bundle.putString(CommonConst.WITHDRAW_BANKACCOUTN, bankAccount);
                bundle.putString(CommonConst.WITHDRAW_BANKNAME, bankName);
                bundle.putString(CommonConst.WITHDRAW_BANKNICKNAME, bankNickName);
                CommonSchemeJump.showActivity(this, "/app/BankcardBindingActivity", bundle);

                break;
            case R.id.setting_icon:
                DiaLogManager.showSelectAvatarDialog(this, getSupportFragmentManager(), this);
                break;
            case R.id.setting_sex:
                SexUtil.getInstance(this).showSex(this, sex);
                break;
            case R.id.setting_birthday:
                BirthdayUtil.getInstance(this).showBirthdayDate(this, birthday, "选择时间", 1);
                break;
            case R.id.setting_wx:
                Bundle bundle2 = new Bundle();
                bundle2.putString(CommonConst.WITHDRAW_NAME, "wx");
                CommonSchemeJump.showNicknameActivity(this, bundle2);
                break;
            case R.id.exit_login:
                LiZhiApplication.getApplication().getAccountManager().onLogout();
                CommonToast.showToast("退出成功");
                finish();
                break;
            case R.id.clearcache://清除缓存
                CacheUtil.clearAllCache(this);
                CommonToast.showToast("缓存清理完成");
                try {
                    String size = CacheUtil.getTotalCacheSize(this);
                    cache.setText(size);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    @Override
    public void selectAvatar(int avatarType) {
        if (avatarType == 0) {
            try {
                //检测是否有写的权限和相机权限
                if (ActivityCompat.checkSelfPermission(SettingActivity.this,
                        "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(SettingActivity.this,
                                "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(SettingActivity.this, PERMISSIONS_STORAGE,
                            REQUEST_PERMISSION_STORAGE);
                } else {
                    //相册
                    choosePic();
                }
            } catch (Exception e) {

            }
        } else {


            try {
                //检测是否有写的权限和相机权限
                if (ActivityCompat.checkSelfPermission(SettingActivity.this,
                        "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(SettingActivity.this,
                                "android.permission.CAMERA") != PackageManager.PERMISSION_GRANTED) {
                    // 没有写的权限，去申请写的权限，会弹出对话框
                    ActivityCompat.requestPermissions(SettingActivity.this, PERMISSIONS_STORAGE,
                            REQUEST_PERMISSION_STORAGE);
                } else {
                    //拍照
                    takePic();
                }
            } catch (Exception e) {

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

                    RoundedCorners roundedCorners = new RoundedCorners(100);
                    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                    RequestOptions options =
                            RequestOptions.bitmapTransform(roundedCorners).override(100, 100);
                    Glide.with(this).load(bb).apply(options).into(user_logo);
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
                    RoundedCorners roundedCorners = new RoundedCorners(100);
                    //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                    RequestOptions options =
                            RequestOptions.bitmapTransform(roundedCorners).override(100, 100);
                    Glide.with(this).load(bb).apply(options).into(user_logo);
                    basePresenter.requestAvatarupdate(imageThumbnail.getFile(bb));

                } catch (Exception e) {

                }
                break;
        }
    }

    private void requestAvatar(String bean) {
        final IAccountManager accountManager = (IAccountManager) ComponentManager.getInstance()
                .getManager(IAccountManager.class.getName());
        String apiToken = accountManager.getAccountInfo().getApiToken();
        String name = accountManager.getAccountInfo().getName();
        int gender = accountManager.getAccountInfo().getGender();
        String avatar = accountManager.getAccountInfo().getAvatar();
        String userBirthday = accountManager.getAccountInfo().getUserBirthday();
        String wechat = accountManager.getAccountInfo().getWechat();
        if (wechat == null) {
            wechat = "";
        }
        if (userBirthday == null) {
            userBirthday = "";
        }
        SettingRequestManager.requestBirth(apiToken, String.valueOf(gender), userBirthday,
                name, bean, wechat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultRx2Subscribe<BaseBean<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> userInfoBeanBaseBean) {
                        super.onNext(userInfoBeanBaseBean);
                        if (!userInfoBeanBaseBean.success()) {
                            onError(new Throwable(userInfoBeanBaseBean.msg));
                            return;
                        }

                        if (accountManager != null && userInfoBeanBaseBean.data != null) {

                            accountManager.saveAccountInfo(userInfoBeanBaseBean.data);
                        }
                        initLatestData();
                        CommonToast.showToast("上传图片成功");
                        LZLog.i(TAG, "修改成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        CommonToast.showToast("上传图片失败");
                    }
                });
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




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(this, "相机权限申请成功", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "拒绝权限", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }


    @Override
    public void requestAvatorSuccess(String bean) {
        Log.e("requestAvatorSuccess", bean + "");
        requestAvatar(bean);
    }

    @Override
    public void requestAvatorError(Throwable throwable) {
        Log.e("requestAvatorSuccess", throwable + "");
    }

    @Override
    public void requestUserHomeSuccess(UserHomeBean bean) {

    }

    @Override
    public void requestUserHomeError(Throwable throwable) {

    }
}
