package com.ken.mysocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mBtn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtn_send = (Button) findViewById(R.id.btn_send);
        mBtn_send.setOnClickListener(this);
        
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                sendq();
                
                
                
                break;
        }
    }

    private void sendq() {
        String mCurrTime = String.valueOf(System.currentTimeMillis() / 1000);
        String pHousrStartTime = DateUtils.pHousrStartTime();
        
        
        String queryData="00001/0004"+ "," +"abcdef"+ "," +"PM1.0"+ "," +mCurrTime+"," +pHousrStartTime;
        SocketClient.getInstance().initSocket(queryData);
    }
}
