package com.example.administrator.myapplication.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.ocr_discern.FaceDetectExpActivity;
import com.example.administrator.myapplication.utilsx.L;
import com.example.administrator.myapplication.utilsx.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrushfaceActivity extends AppCompatActivity {



    private Switch switchBtn;
    private int brushface ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Color1SwitchStyle);
        setContentView(R.layout.activity_brushface);

        ImageView imageView2=findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchBtn = findViewById(R.id.switch_btn);

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked&&brushface!=1){
                    Intent intent=new Intent(BrushfaceActivity.this, FaceDetectExpActivity.class);
                    intent.putExtra("brushface",brushface);
                    startActivityForResult(intent, brushface);
                }else if(brushface==1){
                    L.i("face",String.valueOf(brushface).toString());
                }
                 L.i("888888888888881", String.valueOf(brushface).toString());


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                 brushface= data.getExtras().getInt("brushface");
                L.i("8888888888888882", String.valueOf(brushface).toString());
               // SharedPreferencesUtils.setParam(BrushfaceActivity.this,"brushface",brushface);

               SPUtils.putInt(BrushfaceActivity.this,"brushface",brushface);
            }
        }

    }

}
