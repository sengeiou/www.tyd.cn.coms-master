package com.touedian.com.facetyd.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.touedian.com.facetyd.Config;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.bean.PersonalIConBean;
import com.touedian.com.facetyd.camera.FileUtils;
import com.touedian.com.facetyd.camera.ImageTools;
import com.touedian.com.facetyd.camera.UIUtils;
import com.touedian.com.facetyd.loadingdialog.CustomDialog;
import com.touedian.com.facetyd.utils.PermissionListener;
import com.touedian.com.facetyd.utilsx.CircleImageView;
import com.touedian.com.facetyd.utilsx.HttpUtils;
import com.touedian.com.facetyd.utilsx.JsonUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;
import com.touedian.com.facetyd.utilsx.ToastUtils;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Author:hwr
 * Date:2018/newhomepageone/22
 * 佛祖保佑       永无BUG     永不修改
 * 个人信息  编写
 */

public class PersonalActivity extends AppCompatActivity {
    private PermissionListener permissionListener;

    public static final int REQ_TAKE_PHOTO = 100;
    public static final int REQ_ALBUM = 101;
    public static final int REQ_ZOOM = 102;

    private PopupWindow popupWindow;
    private String imgPath;
    private Uri outputUri;
    private String scaleImgPath;

    private String fileName;
    private RelativeLayout userPicturesicon;
    private RelativeLayout relativeLayout_name;
    private CircleImageView userPictureicon;
    private Button userbutton_back;
    private TextView setTextName;
    private String name;
    private String names;
    private TextView personal_user_name;
    private TextView urse_phonenumber;
    private String phonenumber;
    private String phonenumbers;
    private TextView personal_user_bankcard;
    private boolean Yhk;
    private String user_niName;
    private Bitmap bitmap;
    private Map<String, String> params;
    private int uid;
    private int usid;
    private String uname;
    private String person_uname;
    private Button tvCancel;

    private String avatar;
    private String identity_card;
    private String truename;
    private String username;
    private EditText editText1;
    private byte[] byteArray;
    private int scaleImgPathICON;
    private byte[] bytes;
    private PersonalIConBean personalIConBean;
    private String filePath;
    private String path;
    private String cachePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fullScreen(PersonalActivity.this);
        setContentView(R.layout.activity_personal);
        Intent intent = getIntent();
        avatar = intent.getStringExtra("avatar");

        L.d("url",avatar+"-----------PersonalActivity ");
        identity_card = intent.getStringExtra("identity_card");
        truename = intent.getStringExtra("truename");
        username = intent.getStringExtra("username");



        //保存本地
        usid = SPUtils.getInt(PersonalActivity.this, "uid", uid);
        L.i("usid", String.valueOf(usid));

        truename = SPUtils.getString(PersonalActivity.this, "truename", truename);

        //用户昵称
        setTextName = findViewById(R.id.setTextName);
       /* if (username != null) {
            setTextName.setText(username);//从接口调用
        } else {
            String Username = SPUtils.getString(PersonalActivity.this, "username", username);
            setTextName.setText(Username);
        }*/

//       L.i("username", username);


        //头像点击事件触发弹出框
        userPicturesicon = findViewById(R.id.UserPictures);
        userPicturesicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow();
                showPopWindow();
            }
        });


        //昵称点击事件，触发pop
        relativeLayout_name = findViewById(R.id.Personal_nicheng);
        relativeLayout_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                clickDialogIos(view);

            }
        });

        //头像
        userPictureicon = findViewById(R.id.UserPictureicon);
        /*L.i("-------------------------", "88888888888");
        Glide.with(PersonalActivity.this).load(avatar)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {

                        // 可替换成进度条
                        Toast.makeText(PersonalActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {

                        L.i("GlideDrawable", resource.toString());
                        // 图片加载完成，取消进度条
                        Toast.makeText(PersonalActivity.this, "图片加载成功", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }).error(R.mipmap.ic_launcher_round)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(userPictureicon);

*/
        //退出登录
        userbutton_back = findViewById(R.id.Userbutton_back);
        userbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PersonalActivity.this, PhoneActivity.class));
            }
        });


        //用户姓名  ok
        personal_user_name = findViewById(R.id.Personal_user_name);
        personal_user_name.setText(truename);
        L.i("truename", truename);


        //用户手机号
        urse_phonenumber = findViewById(R.id.urse_phonenumber);
        phonenumbers = SPUtils.getString(PersonalActivity.this, "phonenumber", phonenumber);
        urse_phonenumber.setText(phonenumbers);


        //用户银行卡
        personal_user_bankcard = findViewById(R.id.personal_user_bankcard);
        Yhk = SPUtils.getBoolean(PersonalActivity.this, "yhk", Yhk);

        if (Yhk == true) {
            personal_user_bankcard.setText("已经添加");

        } else if (Yhk = false) {
            personal_user_bankcard.setText("未添加");
        }


        personal_user_bankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalActivity.this, BankCardActivity.class);
                startActivity(intent);
            }
        });


        //返回键
        ImageView personal_back = findViewById(R.id.personal_back);
        personal_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void clickDialogIos(View v) {
        final CustomDialog dialog = new CustomDialog(this, R.style.customDialog, R.layout.my_info_dialog);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        editText1 = dialog.findViewById(R.id.EditPersonal_message);
        editText1.requestFocusFromTouch();
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                L.i("888888888888888888", editable.toString());

            }
        });


        tvCancel = dialog.findViewById(R.id.cancel_btn);
        Button tvOk = dialog.findViewById(R.id.ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                L.i("99999999999999999", editText1.getText().toString());

                setTextName.setText(editText1.getText().toString());
                String s = editText1.getText().toString();
                SPUtils.putString(PersonalActivity.this,"EditTexts",s);

                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "恭喜你！更改成功！", Toast.LENGTH_SHORT).show();

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


    private void initPopupWindow() {
        //要在布局中显示的布局
        LinearLayout contentView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.popwindow, null, false);
        //实例化PopupWindow并设置宽高
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失，这里因为PopupWindow填充了整个窗口，所以这句代码就没用了
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.MyPopupMenuAnimaiton);

        TextView open_from_camera = (TextView) contentView.findViewById(R.id.open_from_camera);//打开相机
        TextView open_album = (TextView) contentView.findViewById(R.id.open_album);//打开图库
        TextView cancel = (TextView) contentView.findViewById(R.id.cancel);//取消

        //关闭Pop
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        //打开图库
        open_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    openAlbum(view);
                } else {

                }

            }
        });


        open_from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(view);
            }
        });
    }

    //pop弹出位置
    private void showPopWindow() {
        fitPopupWindowOverStatusBar(true);
        View rootview = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.activity_personal, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    //弹出的窗口是否覆盖状态栏
    public void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                //利用反射重新设置mLayoutInScreen的值，当mLayoutInScreen为true时则PopupWindow覆盖全屏。
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(popupWindow, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭Pop
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }


    /**
     * 拍照,使用存储卡路径（需要申请存储权限），即图片的路径在  存储卡目录下 -> 包名 -> icon文件夹下
     */
    public void takePhoto(View view) {
        imgPath = FileUtils.generateImgePathInStoragePath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0或6.0以上，则要申请运行时权限,这里需要申请拍照的权限
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted() {
                    //开启摄像头，拍照完的图片保存在对应路径下
                    openCamera(imgPath);
                    //DoGet();
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                    UIUtils.showToast("所需权限被拒绝");
                }
            });
            return;
        }

        openCamera(imgPath);
    }

    /**
     * 打开相册
     */
    public void openAlbum(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, REQ_ALBUM);
    }

    /**
     * 开启摄像机
     */
    private void openCamera(String imgPath) {
        // 指定调用相机拍照后照片的储存路径
        File imgFile = new File(imgPath);
        Uri imgUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            //如果是7.0或以上
            imgUri = FileProvider.getUriForFile(this, UIUtils.getPackageName() + ".fileprovider", imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQ_TAKE_PHOTO);
    }

    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK://调用图片选择处理成功
                String zoomImgPath = "";
                Bitmap bm = null;
                File temFile = null;
                File srcFile = null;
                File outPutFile = null;
                switch (requestCode) {
                    case REQ_TAKE_PHOTO:// 拍照后在这里回调
                        srcFile = new File(imgPath);
                        outPutFile = new File(FileUtils.generateImgePathInStoragePath());
                        outputUri = Uri.fromFile(outPutFile);
                        FileUtils.startPhotoZoom(this, srcFile, outPutFile, REQ_ZOOM);// 发起裁剪请求
                        break;

                    case REQ_ALBUM:// 选择相册中的图片
                        if (data != null) {
                            Uri sourceUri = data.getData();
                            String[] proj = {MediaStore.Images.Media.DATA};

                            // 好像是android多媒体数据库的封装接口，具体的看Android文档
                            Cursor cursor = managedQuery(sourceUri, proj, null, null, null);

                            // 按我个人理解 这个是获得用户选择的图片的索引值
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                            cursor.moveToFirst();
                            // 最后根据索引值获取图片路径
                            imgPath = cursor.getString(column_index);

                            srcFile = new File(imgPath);
                            outPutFile = new File(FileUtils.generateImgePathInStoragePath());
                            outputUri = Uri.fromFile(outPutFile);
                            FileUtils.startPhotoZoom(this, srcFile, outPutFile, REQ_ZOOM);// 发起裁剪请求
                        }
                        break;

                    case REQ_ZOOM://裁剪后回调
                        //  Bundle extras = data.getExtras();
                        if (data != null) {
                            //  bm = extras.getParcelable("data");
                            if (outputUri != null) {
                                bm = ImageTools.decodeUriAsBitmap(outputUri);

                                //如果是拍照的,删除临时文件
                                temFile = new File(imgPath);
                                if (temFile.exists()) {
                                    temFile.delete();
                                }
                                //进行上传，上传成功后显示新图片,这里不演示上传的逻辑，上传只需将scaleImgPath路径下的文件上传即可。
                                //复制并压缩到自己的目录并压缩
                                scaleImgPath = FileUtils.saveBitmapByQuality(bm, 80);

                                SPUtils.putString(PersonalActivity.this, "imgurl", scaleImgPath);
                                // ToastUtils.show(PersonalActivity.this,Toast.makeText(PersonalActivity.this,scaleImgPath+"",0)scaleImgPath+"");

                                L.i("7777777777777", scaleImgPath + "888888888");

                                //获取图片名字
                                ImageGName();

                                //保存到SharedPreferences

                                if (setTextName.getText().toString() != null) {
                                    saveBitmapToSharedPreferences(bm);//base64
                                } else {
                                    ToastUtils.showLong(PersonalActivity.this, "请添加昵称");
                                }
                               /* Glide.with(PersonalActivity.this)
                                        .load(scaleImgPath)
                                        .placeholder(R.drawable.launch_logo)
                                        .into(userPictureicon);*/
                                popupWindow.dismiss();

                            }
                        } else {
                            UIUtils.showToast("选择图片发生错误，图片可能已经移位或删除");
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }

    public void ImageGName() {

        // 创建File
        File mFile = new File(scaleImgPath);

        // 取得文件名
        fileName = mFile.getName();

    }


    //保存图片到SharedPreferences
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.NO_WRAP));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();
        //上传头像


        setImgByStr(imageString, "");

        getBitmapFromSharedPreferences();


    }

    private void setImgByStr(String imageString, String imgName) {
        //这里是头像接口，通过Post请求，拼接接口地址和ID，上传数据。
        scaleImgPath = imageString;
        params = new HashMap<String, String>();

        params.put("uname", setTextName.getText().toString());

        params.put("uid", String.valueOf(usid));

        params.put("avatar", scaleImgPath);



        HttpUtils.doPost(Config.TYD_PersonIcon, params, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                L.d("11111111111", "### fileName : " + e.toString());
                L.d("11111111111", "失败");


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.code() == 200) {

                    String s = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        personalIConBean = JsonUtil.parseJsonToBean(s, PersonalIConBean.class);


                        L.i("0000000000000000000000", "成功");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                //   ToastUtils.showShort(PersonalActivity.this, "上传成功");


                L.d("33333333333", response.body().string());
                L.d("33333333333", "成功");


            }
        });

    }


    //从SharedPreferences获取图片

    private void getBitmapFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString = sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byteArray = Base64.decode(imageString, Base64.DEFAULT);
        if (byteArray.length == 0) {

            //userPictureicon.setImageResource(R.mipmap.ic_launcher);
        } else {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

            //第三步:利用ByteArrayInputStream生成Bitmap
            bitmap = BitmapFactory.decodeStream(byteArrayInputStream);


            //userPictureicon.setImageBitmap(bitmap);

            //Bitmap转换Glide
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();


            //Glide 加载图片回调方法
            Glide.with(PersonalActivity.this)
                    .load(bytes)
                    .listener(new RequestListener<byte[], GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, byte[] model, Target<GlideDrawable> target, boolean isFirstResource) {


                            // 可替换成进度条
                            Toast.makeText(PersonalActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, byte[] model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // 图片加载完成，取消进度条
                            L.i("GlideDrawable2", resource.toString());

                            Toast.makeText(PersonalActivity.this, "图片加载成功", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    })
                    .error(R.mipmap.ic_launcher_round)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(userPictureicon);




        }


    }




    @Override
    protected void onResume() {
        super.onResume();

        String avatar = SPUtils.getString(PersonalActivity.this, "avatar", "");
        String imgurl = SPUtils.getString(PersonalActivity.this, "imgurl", "");
        if (imgurl.equals("")) {
            Glide.with(PersonalActivity.this).load(avatar).into(userPictureicon);
            L.d("url",avatar.toString()+"-----------PersonalActivity +avatar");
        }
        else {
            Glide.with(PersonalActivity.this).load(imgurl).into(userPictureicon);
            L.d("url",imgurl.toString()+" -----PersonalActivity+imgurl ");
        }



        if(setTextName==null){
            setTextName.setText(username);

            L.d("url",username.toString()+" -----username+imgurl ");
        }else {
            String editTexts = SPUtils.getString(PersonalActivity.this, "EditTexts", "");
            setTextName.setText(editTexts);

        }



    }


    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }
}



