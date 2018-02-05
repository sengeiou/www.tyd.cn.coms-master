/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.administrator.myapplication.ocr_discern;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.FaceDetectManager;
import com.baidu.idl.face.platform.ui.FileImageSource;
import com.baidu.idl.face.platform.ui.ImageFrame;
import com.baidu.idl.facesdk.FaceInfo;
import com.example.administrator.myapplication.APIService;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.exception.FaceException;
import com.example.administrator.myapplication.ocr_model.OnlineFaceliveResult;
import com.example.administrator.myapplication.ocr_model.RegResult;
import com.example.administrator.myapplication.utils.FaceCropper;
import com.example.administrator.myapplication.utils.ImageUtil;
import com.example.administrator.myapplication.utils.OnResultListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 该类提供人脸注册功能，注册的人脸可以通个自动检测和选自相册两种方式获取。
 */

public class RegActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PICK_IMAGE = 1000;
    private static final int REQUEST_CODE_NO_ACTION_DETECT = 100;
    private static final int REQUEST_CODE_LIVENESS_DETECT = 101;
    private EditText usernameEt;
    private EditText passwordEt;
    private EditText confirmPasswordEt;
    private ImageView avatarIv;
    private Button noDetectDetectBtn;
    private Button faceLivenessRegBtn;
    private Button pickFromAlbumBtn;
    private Button submitBtn;
    private String facePath;

    private FaceDetectManager detectManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg);

        // TODO 实际应用时，为了防止破解app盗取ak，sk（为您在百度的标识，有了ak，sk就能使用您的账户），
        // TODO 建议把ak，sk放在服务端，移动端把相关参数传给您出服务端去调用百度人脸注册和比对服务，
        // TODO 然后再加上您的服务端返回的登录相关的返回参数给移动端进行相应的业务逻辑
        detectManager = new FaceDetectManager(this.getApplicationContext());
        init();
        findView();
        addListener();

    }

    private void findView() {
        usernameEt = (EditText) findViewById(R.id.username_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        confirmPasswordEt = (EditText) findViewById(R.id.confirm_password_et);
        avatarIv = (ImageView) findViewById(R.id.avatar_iv);
        noDetectDetectBtn = (Button) findViewById(R.id.no_action_detect_btn);
        faceLivenessRegBtn = (Button) findViewById(R.id.faceliveness_reg_btn);
        pickFromAlbumBtn = (Button) findViewById(R.id.pick_from_album_btn);
        submitBtn = (Button) findViewById(R.id.submit_btn);

    }

    private void addListener() {

        faceLivenessRegBtn.setOnClickListener(this);
        noDetectDetectBtn.setOnClickListener(this);
        pickFromAlbumBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    private void init() {
        List<LivenessTypeEnum> livenessTypeList = new ArrayList();
        livenessTypeList.add(LivenessTypeEnum.Eye);
        livenessTypeList.add(LivenessTypeEnum.Mouth);
        livenessTypeList.add(LivenessTypeEnum.HeadLeft);
        livenessTypeList.add(LivenessTypeEnum.HeadUp);
        livenessTypeList.add(LivenessTypeEnum.HeadRight);
        // SDK设置活体动作，通过选择设置list LivenessTypeEnum.Eye，LivenessTypeEnum.Mouth，LivenessTypeEnum.HeadUp，
        // LivenessTypeEnum.HeadDown，LivenessTypeEnum.HeadLeft, LivenessTypeEnum.HeadRight,
        // LivenessTypeEnum.HeadLeftOrRight，如果不设置默认将添加所有的动作到动作集
        FaceSDKManager.getInstance().getFaceConfig().setLivenessTypeList(livenessTypeList);
        FaceSDKManager.getInstance().getFaceConfig().setLivenessRandomCount(2);
        // FaceConfig faceConfig = FaceSDKManager.getInstance().getFaceConfig();
        // 如果图片中的人脸小于200*200个像素，将不能检测出人脸，可以根据需求在100-400间调节大小
        FaceSDKManager.getInstance().getFaceTracker().set_min_face_size(200);
        FaceSDKManager.getInstance().getFaceTracker().set_isCheckQuality(true);
        // 该角度为商学，左右，偏头的角度的阀值，大于将无法检测出人脸，为了在1：n的时候分数高，注册尽量使用比较正的人脸，可自行条件角度
        FaceSDKManager.getInstance().getFaceTracker().set_eulur_angle_thr(15, 15, 15);
        FaceSDKManager.getInstance().getFaceTracker().set_isVerifyLive(true);
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
            return;
        }

        if (v == noDetectDetectBtn) {
            Intent intent = new Intent(RegActivity.this, FaceDetectExpActivity.class);
            startActivityForResult(intent, REQUEST_CODE_NO_ACTION_DETECT);
        } else if (v == faceLivenessRegBtn) {
            Intent intent = new Intent(RegActivity.this, FaceLivenessExpActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LIVENESS_DETECT);
        } else if (v == pickFromAlbumBtn) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

        } else if (v == submitBtn) {
            // 人脸注册和1：n属于在线接口，需要通过ak，sk获得token后进行调用，此方法为获取token，为了防止你得ak，sk泄露，
            // 建议把此调用放在您的服务端

            if (TextUtils.isEmpty(facePath)) {
                toast( "请先进行本地活体检测");
            } else {
                // 您可以使用在线活体检测后在进行注册，这样安全性更高，也可以直接注册。（在线活体请在官网控制台提交申请工单）
                reg(facePath);
                // onlineLiveness(facePath);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LIVENESS_DETECT && data != null) {
            facePath = data.getStringExtra("file_path");

            Bitmap bitmap = BitmapFactory.decodeFile(facePath);
            avatarIv.setImageBitmap(bitmap);
        }else if (requestCode == REQUEST_CODE_NO_ACTION_DETECT && data != null) {
            facePath = data.getStringExtra("file_path");

            Bitmap bitmap = BitmapFactory.decodeFile(facePath);
            avatarIv.setImageBitmap(bitmap);

        } else if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            facePath = detect(filePath);
        }
    }

    private String detect(final String filePath) {

        FileImageSource fileImageSource = new FileImageSource();
        fileImageSource.setFilePath(filePath);
        detectManager.setImageSource(fileImageSource);
        detectManager.setOnFaceDetectListener(new FaceDetectManager.OnFaceDetectListener() {
            @Override
            public void onDetectFace(int status, FaceInfo[] faces, ImageFrame frame) {
                if (faces != null) {
                    final Bitmap cropBitmap = FaceCropper.getFace(frame.getArgb(), faces[0], frame.getWidth());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            avatarIv.setImageBitmap(cropBitmap);
                        }
                    });

                    try {
                        File file = File.createTempFile(UUID.randomUUID().toString() + "", ".jpg");
                        ImageUtil.resize(cropBitmap, file, 300, 300);
                        RegActivity.this.facePath = file.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        detectManager.start();

        return "";
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    /**
     * - 在线活体检测接口调用：
     * 接口地址：https://aip.baidubce.com/rest/2.0/face/v2/faceverify
     * 有动作检测后，取bestimage图片缓存，用此接口调用在线活体检测接口；
     * 此处写两个方法，分别为一帧方案和三帧方案（注释中写明，以下两种方案都取建议阈值，对应的活体验证通过率为99.5%）：
     * 一帧验证方案：
     * 入参为bestimage，阈值取0.834963，超过此分数及验证通过，低于则验证不通过；
     * 三帧验证方案：
     * 取活体检测过程中的三帧图像，阈值为0.94834，三帧中任何一帧分数超过阈值，则验证通过，如三帧都不通过，则活体验证不通
     * @param filePath
     */
    private void onlineLiveness(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            toast( "线活体校验图片不存在");
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        //        displayTip("在线活体验证中...");
        APIService.getInstance().onlineLiveness(new OnResultListener<OnlineFaceliveResult>() {
            @Override
            public void onResult(OnlineFaceliveResult result) {

                if (result != null) {
                    List<Double> facelivenessValues = result.getFacelivenessValue();
                    if (facelivenessValues.size() == 1 && facelivenessValues.get(0) > 0.834963) {
                        reg(filePath);
                    } else {
                        toast( "在线活体校验失败:" + result.getJsonRes());
                    }
                } else {
                    toast( "在线活体校验失败:" + result.getJsonRes());
                }
            }

            @Override
            public void onError(FaceException error) {
                // TODO 错误处理
                toast( "在线活体校验失败:" + error.getErrorMessage());
            }


        }, file);
    }


    private void reg(String filePath) {



        String username = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(RegActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(password)) {
//            Toast.makeText(RegActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(confirmPassword)) {
//            Toast.makeText(RegActivity.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }



        final File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(RegActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
            return;
        }
        // TODO 人脸注册说明 https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add
        // 模拟注册，先提交信息注册获取uid，再使用人脸+uid到百度人脸库注册，
        // TODO 实际使用中，建议注册放到您的服务端进行（这样可以有效防止ak，sk泄露） 把注册信息包括人脸一次性提交到您的服务端，
        // TODO 注册获得uid，然后uid+人脸调用百度人脸注册接口，进行注册。

        // 每个开发者账号只能创建一个人脸库；
        // 每个人脸库下，用户组（group）数量没有限制；
        // 每个用户组（group）下，可添加最多300000张人脸，如每个uid注册一张人脸，则最多300000个用户uid；
        // 每个用户（uid）所能注册的最大人脸数量没有限制；
        // 说明：人脸注册完毕后，生效时间最长为35s，之后便可以进行识别或认证操作。
        // 说明：注册的人脸，建议为用户正面人脸。
        // 说明：uid在库中已经存在时，对此uid重复注册时，新注册的图片默认会追加到该uid下，如果手动选择action_type:replace，
        // 则会用新图替换库中该uid下所有图片。
        // uid          是	string	用户id（由数字、字母、下划线组成），长度限制128B
        // user_info    是	string	用户资料，长度限制256B
        // group_id	    是	string	用户组id，标识一组用户（由数字、字母、下划线组成），长度限制128B。
        // 如果需要将一个uid注册到多个group下，group_id,需要用多个逗号分隔，每个group_id长度限制为48个英文字符
        // image	    是	string	图像base64编码，每次仅支持单张图片，图片编码后大小不超过10M
        // action_type	否	string	参数包含append、replace。如果为“replace”，则每次注册时进行替换replace（新增或更新）操作，
        // 默认为append操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                faceReg(file);
            }
        }, 1000);


    }

    private void faceReg(File file) {

        // 用户id（由数字、字母、下划线组成），长度限制128B
        // uid为用户的id,百度对uid不做限制和处理，应该与您的帐号系统中的用户id对应。

        String uid = UUID.randomUUID().toString().substring(0, 8) + "_123";
        // String uid = 修改为自己用户系统中用户的id;
        // 模拟使用username替代
        String username = usernameEt.getText().toString().trim();
      //  String uid = username;


        APIService.getInstance().reg(new OnResultListener<RegResult>() {
            @Override
            public void onResult(RegResult result) {
                Log.i("wtf", "orientation->" + result.getJsonRes());
                toast("注册成功！");
                finish();
            }

            @Override
            public void onError(FaceException error) {
                toast("注册失败");
            }


        }, file, uid, username);
    }

    private void toast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper());
}
