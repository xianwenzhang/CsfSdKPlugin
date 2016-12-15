package com.csf.csfsdktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jaily.zhang on 2016/8/3.
 */
public class MainActivity extends Activity {
    private EditText et_input;
    private Button bt_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        et_input = (EditText) findViewById(R.id.et_input);
        bt_send = (Button) findViewById(R.id.bt_send);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainVerticalActivity.class);
                intent.putExtra("input", et_input.getText().toString());
                startActivity(intent);
            }
        });
    }
}
