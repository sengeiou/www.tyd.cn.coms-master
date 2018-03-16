package com.touedian.com.facetyd.model;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;


import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.touedian.com.facetyd.R;
import com.touedian.com.facetyd.ocr_face.FaceOnlineVerifyActivity;
import com.touedian.com.facetyd.ocr_text_cr.DrivingActivity;
import com.touedian.com.facetyd.utils.FileUtil;
import com.touedian.com.facetyd.utilsx.L;
import com.touedian.com.facetyd.utilsx.SPUtils;
import com.touedian.com.facetyd.utilsx.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;

public class IDCardProveActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;

    private ImageView idcard_front_image;
    private ImageView idcard_back_image;
    private JSONObject jsonObjectback;
    private JSONObject jsonObjectfront;
    private String iDcardMassg;
    private String signDate;
    private String expiryDate;
    private String sex;
    private String familyname;
    private String birthday;
    private String filePath_back;
    private String filePath_front;
    private String filePathls;
    private String address;
    private String idNumber;
    private String name;
    private String issueAuthority;
    private String Neterrot = "";
    private String Fhm = "283504";
    private Button btn;
    private String names;
    private String name_duibi;
    public static final String ID_CARD_SIDE_FRONT = "front";
    public static final String ID_CARD_SIDE_BACK = "back";

    public boolean IdcardCode  ;

    private boolean IdcardCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen(IDCardProveActivity.this);
        setContentView(R.layout.activity_idcardprove);
        ButterKnife.bind(this);


        // idcard_front_image = findViewById(R.id.id_card_front_image);
        // idcard_back_image = findViewById(R.id.id_card_back_image);


        btn = findViewById(R.id.personal_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(IdcardCode==false){

                    Intent intent=new Intent(getApplication(), FaceOnlineVerifyActivity.class);
                    startActivity(intent);
                    IdcardCode=true;
                    SPUtils.putBoolean(getApplication(),"IdcardCode",IdcardCode);
                    L.i(String.valueOf(IdcardCode));

                }else {

                    ToastUtils.showLong(IDCardProveActivity.this, "核实成功");

                }

            }
        });
        ImageView backimage = findViewById(R.id.backimage);
        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
/*
        //身份证正面
        findViewById(R.id.id_card_front_image).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IDCardProveActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        //身份证反面
        findViewById(R.id.id_card_back_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IDCardProveActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
    }*/


    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    alertText("", result.toString());

                    ToastUtils.showShort(IDCardProveActivity.this, "认证成功");
                }
            }

            @Override
            public void onError(OCRError error) {


                alertText("", error.getMessage());
                ToastUtils.showShort(IDCardProveActivity.this, "请检查网络,重新拍摄");
                if (error.toString().equals(283504)) {


                } else {

                    alertText("", error.getMessage());

                }

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_PICK_IMAGE_FRONT && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            filePath_front = getRealPathFromURI(uri);
            recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath_front);
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE_BACK && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            filePath_back = getRealPathFromURI(uri);
            recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath_back);
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);

                // 通过临时文件获取拍摄的图片
                filePathls = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();

                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePathls);

                        //身份证正面照片展示
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (Neterrot != Fhm) {//283504  无网络错误码

                                    Glide
                                            .with(IDCardProveActivity.this)
                                            .load(filePathls)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(NONE)
                                            .into(idcard_front_image);

                                }
                            }
                        });
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePathls);
                        //身份证反面照片展示
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (Neterrot != Fhm) {
                                    Glide
                                            .with(IDCardProveActivity.this)
                                            .load(filePathls)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(NONE)
                                            .into(idcard_back_image);
                                }

                            }
                        });
                    }
                }
            }
        }


    }
    private void alertText(final String title, String message) {


        iDcardMassg = message;

        L.i("11111", "" + iDcardMassg);

        if (message.contains("IDCardResult front")) {


            String IDCardResultFront = message.replace("IDCardResult front", "");

            //转换成功的集合
            String resultfront = IDCardResultFront.replaceAll("=", ":");

            try {
                jsonObjectfront = new JSONObject(resultfront);

               // address = jsonObjectfront.optString("address");//住址

                idNumber = jsonObjectfront.optString("idNumber");

                name = jsonObjectfront.optString("name");

              //  sex = jsonObjectfront.optString("gender");

              //  familyname = jsonObjectfront.optString("ethnic");//名族

               // birthday = jsonObjectfront.optString("birthday");//出生日期


            } catch (JSONException e) {
                e.printStackTrace();

            }

        } else if (message.contains("IDCardResult back")) {

            String IDCardResultBack = message.replace("IDCardResult back", "");
            String IDCardResultBacks = "{" + IDCardResultBack.substring(2);


            //转换成功的集合
            String resultback = IDCardResultBacks.replaceAll("=", ":");

            try {
                jsonObjectback = new JSONObject(resultback);

              //  issueAuthority = jsonObjectback.optString("issueAuthority");//签发机关
              //  signDate = jsonObjectback.optString("signDate");
              //  expiryDate = jsonObjectback.optString("expiryDate");


            } catch (JSONException e) {
                e.printStackTrace();

            }
        }


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

    @Override
    protected void onResume() {
        super.onResume();

        IdcardCode = SPUtils.getBoolean(getApplication(), "IdcardCode", IdcardCodes);
        L.i(String.valueOf(IdcardCode));
    }
}
