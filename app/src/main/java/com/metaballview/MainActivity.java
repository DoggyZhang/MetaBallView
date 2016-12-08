package com.metaballview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.view.MetaBallView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MetaBallView bezierDragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_red).setOnClickListener(this);
        findViewById(R.id.btn_blue).setOnClickListener(this);
        findViewById(R.id.btn_green).setOnClickListener(this);
        findViewById(R.id.btn_pink).setOnClickListener(this);
        findViewById(R.id.btn_yellow).setOnClickListener(this);

        bezierDragView = (MetaBallView) findViewById(R.id.bezierDragView);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        bezierDragView.setDragView(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_red:
                bezierDragView.setColor(getResources().getColor(R.color.red));
                break;
            case R.id.btn_blue:
                bezierDragView.setColor(getResources().getColor(R.color.blue));
                break;
            case R.id.btn_green:
                bezierDragView.setColor(getResources().getColor(R.color.green));
                break;
            case R.id.btn_pink:
                bezierDragView.setColor(getResources().getColor(R.color.pink));
                break;
            case R.id.btn_yellow:
                bezierDragView.setColor(getResources().getColor(R.color.yellow));
                break;

        }
    }
}
