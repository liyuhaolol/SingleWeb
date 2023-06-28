package spa.lyh.cn.singleweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import spa.lyh.cn.ft_webview.webview.WebViewActivity
import spa.lyh.cn.singleweb.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity(),OnClickListener {
    private lateinit var b:ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.tv.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        var intent:Intent
        when(p0?.id){
            R.id.tv ->{
                intent = Intent(this,
                    WebViewActivity::class.java)
                intent.putExtra("url","https://news.cctv.com/2022/10/19/ARTI1GmPVo7xLsTqOqsEStTM221019.shtml?spm=C94212.P4YnMod9m2uD.ENPMkWvfnaiV.34")
                //可以自定义第一次加载web的标题，并作为分享弹窗的触发条件之一。
                //注：分享弹窗是配合其他项目使用的，所以本项目本身并不带有shareDialog。
                //如果想启用这个功能只能按照我给出的shareDialog反射包名完全一致来写
                //所以这个功能本质可能没啥用，对你来说。
                intent.putExtra("title","测试标题")
                intent.putExtra("ua","coolAndroid")
                startActivity(intent)
            }
        }
    }
}