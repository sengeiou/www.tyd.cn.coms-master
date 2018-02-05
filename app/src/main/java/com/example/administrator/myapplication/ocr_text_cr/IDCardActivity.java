/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.administrator.myapplication.ocr_text_cr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.example.administrator.myapplication.Config;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.model.IDCardAmendActivity;
import com.example.administrator.myapplication.model.IDLookActivity;
import com.example.administrator.myapplication.model.PhoneActivity;
import com.example.administrator.myapplication.utils.FileUtil;
import com.example.administrator.myapplication.utils.HttpUtil;
import com.example.administrator.myapplication.utilsx.HttpUtils;
import com.example.administrator.myapplication.utilsx.L;
import com.example.administrator.myapplication.utilsx.SPUtils;
import com.example.administrator.myapplication.utilsx.SharedPreferencesUtils;
import com.example.administrator.myapplication.utilsx.ToastUtils;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;

public class IDCardActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE_FRONT = 201;
    private static final int REQUEST_CODE_PICK_IMAGE_BACK = 202;
    private static final int REQUEST_CODE_CAMERA = 102;

    private AlertDialog.Builder alertDialog;
    private String address;
    private String idNumber;
    private String name;
    private String issueAuthority;

    private ImageView id_card_image_front;
    private ImageView id_card_image_back;
    private String filePath_back;
    private String filePath_front;
    private String filePathls;
    private ImageView bitid_card_front_button_native;
    private ImageView bitid_card_back_button_native;
    private String Neterrot = "";
    private String Fhm = "283504";
    private String iDcardMassg;
    private boolean hasGotToken = false;
    private String signDate;
    private String expiryDate;
    private String iDcardMassgAmend;

    private ImageView id_card_front_button;
    private ImageView id_card_back_button;
    private Map<String, String> Idcard_up = new HashMap<String, String>();
    private String sex;
    private String familyname;
    private String birthday;
    private JSONObject jsonObjectback;
    private JSONObject jsonObjectfront;
    private int uid;


    private HashMap<String, String> idcard_zhuan;
    private int ieGrid;


    private boolean checkGalleryPermission() {
        int ret = ActivityCompat.checkSelfPermission(IDCardActivity.this, Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (ret != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IDCardActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_idcard);
        Intent intent=getIntent();
        if (intent != null) {

               // uid = intent.getStringExtra("uid");
            ieGrid = intent.getExtras().getInt("identity_status");
             //   L.i("111",uid.toString());
            uid=   SPUtils.getInt(IDCardActivity.this,"uid",uid);



        }
        initAccessTokenWithAkSk();
        alertDialog = new AlertDialog.Builder(this);

        ImageView id_card_backimage=findViewById(R.id.id_card_backimage);
        id_card_backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        id_card_front_button = findViewById(R.id.id_card_front_button);

        id_card_back_button = findViewById(R.id.id_card_back_button);
        Button personal_button = findViewById(R.id.personal_button);
        personal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //展示的页面，不可修改
                // Intent intent =new Intent(IDCardActivity.this,IDLookActivity.class);
                // intent.putExtra("IDcardMassageshow", iDcardMassg);
                 //startActivity(intent);
                PostIdCard();
                //可修改的页面
                Intent intent = new Intent(IDCardActivity.this, IDCardAmendActivity.class);

                intent.putExtra("IDcardMassageshow", iDcardMassg);

                intent.putExtra("identity_status",ieGrid);
                intent.putExtra("name", name);
//                L.i("6666",name);
                intent.putExtra("idNumber", idNumber);
                L.i("6666",idNumber);
                intent.putExtra("signDate", signDate);

                intent.putExtra("expiryDate", expiryDate);


                startActivity(intent);


            }
        });


        //身份证正面
        findViewById(R.id.id_card_front_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // PostIdCard();
                Intent intent = new Intent(IDCardActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        //身份证反面
        findViewById(R.id.id_card_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IDCardActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
    }


  /*      //身份证正面扫描
        findViewById(R.id.id_card_front_button_native).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IDCardActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());

                intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN,
                        OCR.getInstance().getLicense());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                        true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });*/



    /*    //身份证反面扫描
        findViewById(R.id.id_card_back_button_native).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IDCardActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN,
                        OCR.getInstance().getLicense());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                        true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
    }*/

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {

                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "9evxCWG1MTN8k7u3XU0qVIqi", "5eesgiqRtSflHYOM5OUZLSsSeMPCC81n");
    }

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

                    ToastUtils.showShort(IDCardActivity.this, "认证成功");

                }
            }

            @Override
            public void onError(OCRError error) {

                //initAccessTokenWithAkSk();
                alertText("", error.getMessage());
                ToastUtils.showShort(IDCardActivity.this, "请检查网络");
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
                                            .with(IDCardActivity.this)
                                            .load(filePathls)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(NONE)
                                            .into(id_card_front_button);

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
                                            .with(IDCardActivity.this)
                                            .load(filePathls)
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(NONE)
                                            .into(id_card_back_button);
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

                address = jsonObjectfront.optString("address");//住址

                idNumber = jsonObjectfront.optString("idNumber");

                name = jsonObjectfront.optString("name");

                sex = jsonObjectfront.optString("gender");

                familyname = jsonObjectfront.optString("ethnic");//名族

                birthday = jsonObjectfront.optString("birthday");//出生日期

                SPUtils.putString(IDCardActivity.this,"name",name);

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

                issueAuthority = jsonObjectback.optString("issueAuthority");//签发机关
                signDate = jsonObjectback.optString("signDate");
                expiryDate = jsonObjectback.optString("expiryDate");



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

    //上传身份证
    private void PostIdCard() {


        idcard_zhuan = new HashMap<String, String>();

        L.i("11111",idcard_zhuan.toString());

        Idcard_up.put("uid", String.valueOf(uid));

        idcard_zhuan.put("truename",name);

        idcard_zhuan.put("sex",sex);

        idcard_zhuan.put("familyname", familyname);

        idcard_zhuan.put("birthday",birthday);

        idcard_zhuan.put("address", address);

        idcard_zhuan.put("identity_card", idNumber);

        idcard_zhuan.put("office", issueAuthority);

        idcard_zhuan.put("start_time", signDate);

        idcard_zhuan.put("end_time", expiryDate);



        Idcard_up.put("data",getGet(idcard_zhuan));





            HttpUtils.doPost(Config.TYD_IDcardMessage_up, Idcard_up, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtils.showShort(IDCardActivity.this,"参数错误");
/*
               /* if(e.hashCode()==-newhomepageone){
                    ToastUtils.showLong(IDCardActivity.this,"参数错误");
                }
                else if(e.hashCode()==newhomepageone){
                    ToastUtils.showLong(IDCardActivity.this,"姓名格式有误");
                }
                else if(e.hashCode()==3){
                    ToastUtils.showLong(IDCardActivity.this,"名字格式有误");
                }
                else if(e.hashCode()==4){
                    ToastUtils.showLong(IDCardActivity.this,"身份证格式有误");
                }
                else if(e.hashCode()==6){
                    ToastUtils.showLong(IDCardActivity.this,"上传失败");
                }*/
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response.code()==200){
                        L.i("身份证上传成功",response.body().string());




                    }
                    //关闭防止内存泄漏
                    if (response.body() != null) {
                        response.body().close();

                        Idcard_up.clear();
                    }

                }

            });

    }
    /**
     * 转换get请求参数   集合转换字符串工具
     *
     * @param map
     * @return
     */
    public static String getGet(HashMap<String, String> map) {
        StringBuffer sb = new StringBuffer();
        String key = null;
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();

        while (it.hasNext()) {
            key = it.next();
            sb.append(key + "=" + map.get(key) + "&");
        }
        String sbss=sb.substring(0,sb.length()-1);//减去最后一个字符号&

        L.i("sbss",sbss.toString());
        return sbss.toString();


    }
}
