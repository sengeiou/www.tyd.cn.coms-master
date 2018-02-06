package com.example.administrator.myapplication.model;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.ocr_face.OcrFaceActivity;
import com.example.administrator.myapplication.utilsx.L;
import com.example.administrator.myapplication.utilsx.SPUtils;
import com.example.administrator.myapplication.utilsx.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class IDCardAmendActivity extends AppCompatActivity {

    private EditText idcard_amend_name;
    private EditText idcard_amend_idnumber;
    private EditText idcard_amend_signDate;
    private EditText idcard_amend_expiryDate;
    private String toIDcardMassageAmendShow;
    private JSONObject jsonObjectback;
    private String signDate;
    private String expiryDate;
    private String issueAuthority;
    private String name;
    private String idNumber;
    private Button idcard_amend_btn;

    private String nameMassageAmendShow;
    private String idNumberMassageAmendShow;
    private String signDateMassageAmendShow;
    private String expiryDateMassageAmendShow;
    private int IeGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_amend);
        Intent intent = getIntent();

        if (intent != null) {
            toIDcardMassageAmendShow = intent.getStringExtra("IDcardMassageshow");

            ObjetIDcardMassage();

            nameMassageAmendShow = intent.getStringExtra("name");
            L.i("33333", "" + nameMassageAmendShow);
            idcard_amend_name = findViewById(R.id.Idcard_amend_name);
            idcard_amend_name.setText(nameMassageAmendShow);
            SPUtils.putString(IDCardAmendActivity.this,"name",nameMassageAmendShow);


            idNumberMassageAmendShow = intent.getStringExtra("idNumber");
            L.i("33333", "" + idNumberMassageAmendShow);
            idcard_amend_idnumber = findViewById(R.id.Idcard_amend_Idnumber);
            idcard_amend_idnumber.setText(idNumberMassageAmendShow);
            SPUtils.putString(IDCardAmendActivity.this,"idNumber",idNumberMassageAmendShow);


            signDateMassageAmendShow = intent.getStringExtra("signDate");

            idcard_amend_signDate = findViewById(R.id.Idcard_amend_signDate);
            idcard_amend_signDate.setText(signDateMassageAmendShow);
            SPUtils.putString(IDCardAmendActivity.this,"signDate",signDateMassageAmendShow);


            expiryDateMassageAmendShow = intent.getStringExtra("expiryDate");

            idcard_amend_expiryDate = findViewById(R.id.Idcard_amend_expiryDate);
            idcard_amend_expiryDate.setText(expiryDateMassageAmendShow);
            SPUtils.putString(IDCardAmendActivity.this,"expiryDate",expiryDateMassageAmendShow);




            L.i("33333", "" + toIDcardMassageAmendShow);
        } else {

            ToastUtils.showShort(getApplicationContext(), "nulls");
        }




        idcard_amend_btn = findViewById(R.id.Idcard_amend_btn);
        idcard_amend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ToastUtils.showShort(IDCardAmendActivity.this, "确认成功");
                //身份证认证标识

                Intent intt= new Intent(IDCardAmendActivity.this, OcrFaceActivity.class);
                IeGrid = 1;
                intt.putExtra("identity_status",IeGrid);
                SPUtils.putInt(IDCardAmendActivity.this,"identity_status",IeGrid);

                startActivity(intt);
            }
        });

        ImageView idcard_amend_back = findViewById(R.id.idcard_amend_back);
        idcard_amend_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    //手动处理返回数据
    private void ObjetIDcardMassage() {
        JSONObject jsonObjectfront;
        if (toIDcardMassageAmendShow != null) {


            if (toIDcardMassageAmendShow.contains("IDCardResult front")) {

                L.d("2222222", "身份证正面");
                String IDCardResultFront = toIDcardMassageAmendShow.replace("IDCardResult front", "");

                //转换成功的集合
                String resultfront = IDCardResultFront.replaceAll("=", ":");

                try {
                    jsonObjectfront = new JSONObject(resultfront);

                    toIDcardMassageAmendShow = jsonObjectfront.optString("address");

                    idNumber = jsonObjectfront.optString("idNumber");

                    name = jsonObjectfront.optString("name");



                    SPUtils.putString(this,"name",name);

                    SPUtils.putString(this,"idNumber",idNumber);


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } else if (toIDcardMassageAmendShow.contains("IDCardResult back")) {


                String IDCardResultBack = toIDcardMassageAmendShow.replace("IDCardResult back", "");
                String IDCardResultBacks = "{" + IDCardResultBack.substring(2);


                //转换成功的集合
                String resultback = IDCardResultBacks.replaceAll("=", ":");

                try {
                    jsonObjectback = new JSONObject(resultback);


                    L.d("2222222", "身份证反面" + jsonObjectback.toString());

                    issueAuthority = jsonObjectback.optString("issueAuthority");

                    //有效期限
                    signDate = jsonObjectback.optString("signDate");

                    //到期时间
                    expiryDate = jsonObjectback.optString("expiryDate");


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }


    }
}
