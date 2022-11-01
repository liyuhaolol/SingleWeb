package spa.lyh.cn.singleweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import spa.lyh.cn.ft_webview.webview.WebViewActivity

class MainActivity: AppCompatActivity(),OnClickListener {
    private lateinit var tv:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tv)
        tv.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        var intent:Intent
        when(p0?.id){
            R.id.tv ->{
                intent = Intent(this,WebViewActivity::class.java)
                intent.putExtra("url","https://news.cctv.com/2022/10/19/ARTI1GmPVo7xLsTqOqsEStTM221019.shtml?spm=C94212.P4YnMod9m2uD.ENPMkWvfnaiV.34")
                intent.putExtra("title","测试标题")
                intent.putExtra("ua","cnsAndroid")
                startActivity(intent)
            }
        }
    }
}