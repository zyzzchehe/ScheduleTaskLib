package com.torchmu.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.torchmu.schedule.ScheduleProxy;
import com.torchmu.schedule.ScheduleTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = getClass().getSimpleName();

    TextView textView;
    ScheduleTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = (TextView) findViewById(R.id.text_view);

        findViewById(R.id.button).setOnClickListener(this);

        timerTask = new TestTask(TAG, 6000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (ScheduleProxy.getInstance().containTask(timerTask)) {
                    ScheduleProxy.getInstance().unregister(timerTask);
                    ((Button) v).setText("start");
                } else {
                    ScheduleProxy.getInstance().register(timerTask);
                    ((Button) v).setText("stop");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // step 3 of 3, don't forget to unregister which avoids memory leaks
        ScheduleProxy.getInstance().unregister(timerTask);
    }

    class TestTask extends ScheduleTask {

        public TestTask(String id, long interval) {
            super(id, interval);
        }

        @Override
        public void callback() {
            // step 2 of 3, do your timer task here
            String lastText = textView.getText().toString();
            String currentText = getCurrentLoop() + " intervalTime:" + (System.currentTimeMillis() - getLastCallBackTime());
            textView.setText(lastText + "\n" + currentText);
        }

    }
}
