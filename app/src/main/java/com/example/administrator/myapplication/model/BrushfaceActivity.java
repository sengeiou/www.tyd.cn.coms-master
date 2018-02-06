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
import com.example.administrator.myapplication.utilsx.L;
import com.example.administrator.myapplication.utilsx.SPUtils;

public class BrushfaceActivity extends AppCompatActivity {


    private boolean SwitchBool;//Switch开关状态
    private Switch switchBtn;
    private int brushface;
    private int brushfaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Color1SwitchStyle);
        setContentView(R.layout.activity_brushface);
        brushfaces = SPUtils.getInt(BrushfaceActivity.this, "brushface", brushface);
        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        switchBtn = findViewById(R.id.switch_btn);
        if (brushfaces == 1) {
            switchBtn.setChecked(true);
        } else if (brushfaces == 0) {
            switchBtn.setChecked(false);
        }





        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked && BrushfaceActivity.this.brushface != 1) {
                    Intent intent = new Intent(BrushfaceActivity.this, ZxingfaceActivity.class);
                    intent.putExtra("brushface", BrushfaceActivity.this.brushface);
                    startActivityForResult(intent, BrushfaceActivity.this.brushface);
                } else if (!isChecked) {
                    switchBtn.setChecked(false);
                    SPUtils.putInt(BrushfaceActivity.this,"brushface",brushface);
                }
                L.i("888888888888881", String.valueOf(BrushfaceActivity.this.brushface).toString());


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                brushface = data.getExtras().getInt("brushface");
                L.i("8888888888888882", String.valueOf(brushface).toString());
                // SharedPreferencesUtils.setParam(BrushfaceActivity.this,"brushface",brushface);

                SPUtils.putInt(BrushfaceActivity.this, "brushface", brushface);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if(brushfaces==1){
            switchBtn.setChecked(true);
            L.i("22222222222222222",String.valueOf(BrushfaceActivity.this.brushface).toString());
        }*/

        if (SwitchBool == true) {
            brushface = 1;
        } else {
            brushface = 0;
        }


    }
}
