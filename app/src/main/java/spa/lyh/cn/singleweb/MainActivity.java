package spa.lyh.cn.singleweb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import spa.lyh.cn.ft_webview.webview.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url","https://news.cctv.com/2022/10/19/ARTI1GmPVo7xLsTqOqsEStTM221019.shtml?spm=C94212.P4YnMod9m2uD.ENPMkWvfnaiV.34");
                //intent.putExtra("url","https://www.xindb.com/static/content/TGGJ/2021-10-26/app_902595663328849920.html");
                //intent.putExtra("url","https://umsweb.offshoremedia.net/login");
                intent.putExtra("title","测试标题");
                intent.putExtra("ua","cnsAndroid");
                startActivity(intent);
            }
        });
    }
}
