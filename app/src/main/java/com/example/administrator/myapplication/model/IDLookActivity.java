package com.example.administrator.myapplication.model;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utilsx.L;
import com.example.administrator.myapplication.utilsx.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class IDLookActivity extends AppCompatActivity {

    private String toIDcardMassageshow;
    private TextView idcard_look_name;
    private String name;
    private String idNumber;
    private String issueAuthority;
    private TextView idcard_look_idcard;
    private String signDate;
    private String expiryDate;
    private TextView idcard_idSignDate;
    private TextView idcard_idExpiryDate;
    private JSONObject jsonObjectback;
    private String id_name;
    private String id_expiryDate;
    private String nameMassageAmendShow;
    private String nameMessage;
    private String idNumberMassageAmendShow;
    private String idNumberMassage;
    private String signDateMassageAmendShow;
    private String signDateMassage;
    private String expiryDateMassageAmendShow;
    private String expiryDateMassage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idlook);
        Intent intent = getIntent();
        if (intent != null) {
            toIDcardMassageshow = intent.getStringExtra("IDcardMassageshow");
            ObjetIDcardMassage();

           // id_expiryDate = (String) SharedPreferencesUtils.getParam(IDLookActivity.this, "expiryDate", expiryDate);
        }
        nameMessage = SPUtils.getString(IDLookActivity.this,"name",nameMassageAmendShow);

        idNumberMassage = SPUtils.getString(IDLookActivity.this,"idNumber",idNumberMassageAmendShow);

        signDateMassage = SPUtils.getString(IDLookActivity.this,"signDate",signDateMassageAmendShow);

        expiryDateMassage = SPUtils.getString(IDLookActivity.this,"expiryDate",expiryDateMassageAmendShow);

        ImageView LookBankback=findViewById(R.id.LookBankback);
        LookBankback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //姓名
        idcard_look_name = findViewById(R.id.Idcard_look_name);
        idcard_look_name.setText(nameMessage);


        //身份证号
        idcard_look_idcard = findViewById(R.id.Idcard_look_idcard_show);


        idcard_look_idcard.setText(idNumberMassage);



        //身份证有效期限
        idcard_idSignDate = findViewById(R.id.Idcard_IdSignDate);


        idcard_idSignDate.setText(signDateMassage);


        //身份证到期时间
        idcard_idExpiryDate = findViewById(R.id.Idcard_IdExpiryDate);


        idcard_idExpiryDate.setText(expiryDateMassage);

    }

    private void ObjetIDcardMassage() {
        if (toIDcardMassageshow != null) {


            if (toIDcardMassageshow.contains("IDCardResult front")) {

                L.d("2222222", "身份证正面");
                String IDCardResultFront = toIDcardMassageshow.replace("IDCardResult front", "");

                //转换成功的集合
                String resultfront = IDCardResultFront.replaceAll("=", ":");

                try {
                    JSONObject jsonObjectfront = new JSONObject(resultfront);

                    toIDcardMassageshow = jsonObjectfront.optString("address");


                    idNumber = jsonObjectfront.optString("idNumber");
                    SPUtils.putString(IDLookActivity.this,"idNumber",idNumber);


                    name = jsonObjectfront.optString("name");
                    SPUtils.putString(IDLookActivity.this,"name",name);

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } else if (toIDcardMassageshow.contains("IDCardResult back")) {


                String IDCardResultBack = toIDcardMassageshow.replace("IDCardResult back", "");
                String IDCardResultBacks = "{" + IDCardResultBack.substring(2);


                //转换成功的集合
                String resultback = IDCardResultBacks.replaceAll("=", ":");

                try {
                    jsonObjectback = new JSONObject(resultback);


                    L.d("2222222", "身份证反面" + jsonObjectback.toString());

                    issueAuthority = jsonObjectback.optString("issueAuthority");

                    //有效期限
                    signDate = jsonObjectback.optString("signDate");
                    SPUtils.putString(IDLookActivity.this,"signDate",signDate);
                    //到期时间
                    expiryDate = jsonObjectback.optString("expiryDate");
                    SPUtils.putString(IDLookActivity.this,"expiryDate",expiryDate);


                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }
    }

}
