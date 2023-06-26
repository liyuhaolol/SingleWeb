package spa.lyh.cn.ft_webview.webview

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ActionMode
import android.view.ActionMode.Callback2
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView

open class SingleWebView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    WebView(context, attrs, defStyleAttr) {
    constructor(context: Context):this(context,null,0)
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)

    companion object{
        @JvmStatic
        private val CUSTOM_MENU_JS_INTERFACE = "CustomMenuJSInterface"
    }
    private var mActionMode:ActionMode? = null
    private var mActionSelectListener: ActionSelectListener? = null
    private lateinit var mCustomMenuList: MutableList<String> //自定义添加的选项
    init {
        init()
    }

    open fun setActionSelectListener(actionSelectListener:ActionSelectListener){
        this.mActionSelectListener = actionSelectListener
    }

    interface ActionSelectListener{
        fun onClick(title:String,selectText:String)
    }

    fun init(){
        addJavascriptInterface(ActionSelectInterface(this), CUSTOM_MENU_JS_INTERFACE)
        mCustomMenuList = mutableListOf()
        mCustomMenuList.add("菜单1")
        mCustomMenuList.add("菜单2")
    }

    override fun startActionMode(callback: ActionMode.Callback?): ActionMode {
        return super.startActionMode(buildCustomCallback(callback))
    }

    override fun startActionMode(callback: ActionMode.Callback?, type: Int): ActionMode {
        return super.startActionMode(buildCustomCallback(callback), type)
    }

    private fun buildCustomCallback(callback:ActionMode.Callback?):ActionMode.Callback?{
        val customCallback:ActionMode.Callback?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            customCallback = object :ActionMode.Callback2(){
                override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                    return callback!!.onCreateActionMode(p0,p1)
                }

                override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                    callback!!.onPrepareActionMode(p0,p1)
                    addCustomMenu(p0)
                    for (i in 0 until p1!!.size()){
                        val menuItem = p1.getItem(i)
                        if (menuItem.itemId == 0){
                            //自定义或是通过PROCESS_TEXT方案加入到菜单中的选项，item都为0
                            val intent = menuItem.intent
                            val componentName = intent?.component
                            //根据包名比较菜单中的选项是否是本app加入的
                            //根据包名比较菜单中的选项是否是本app加入的
                            menuItem.isVisible = context.packageName == componentName?.packageName
                        }else{
                            menuItem.isVisible = true
                        }
                    }
                    return true
                }

                override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                    if (p1 == null || TextUtils.isEmpty(p1.title)) {
                        return callback!!.onActionItemClicked(p0, p1)
                    }
                    val title: String = p1.title.toString()
                    if (mCustomMenuList.contains(title)) {
                        try {
                            getSelectedData(title)
                            //延迟release，防止js获取不到选中的文字
                            postDelayed({ releaseAction() }, 200)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return true
                    } else if (mActionSelectListener != null) {
                        mActionSelectListener!!.onClick(title, "")
                    }
                    return callback!!.onActionItemClicked(p0, p1)
                }

                override fun onDestroyActionMode(p0: ActionMode?) {
                    callback!!.onDestroyActionMode(p0)
                }

                override fun onGetContentRect(mode: ActionMode?, view: View?, outRect: Rect?) {
                    if(callback is Callback2){
                        callback.onGetContentRect(mode, view, outRect);
                    }else{
                        super.onGetContentRect(mode, view, outRect)
                    }
                }

            }
        }else{
            customCallback = callback
        }
        return customCallback
    }

    private  fun releaseAction() {
        if (mActionMode != null) {
            mActionMode?.finish()
            mActionMode = null
        }
    }


    private fun getSelectedData(title: String) {
        val js = "javascript:(function getSelectedText() {var txt;var title = \"$title\";if (window.getSelection) {txt = window.getSelection().toString();} else if (window.document.getSelection) {txt = window.document.getSelection().toString();} else if (window.document.selection) {txt = window.document.selection.createRange().text;}$CUSTOM_MENU_JS_INTERFACE.callback(txt,title);})()"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(js, null)
        } else {
            loadUrl(js)
        }
    }

    private fun addCustomMenu(actionMode:ActionMode?):ActionMode{
        val menu:Menu = actionMode!!.menu
        //menu.clear()
        //添加自定义选项
        mCustomMenuList.forEach {
            //intent主要用于过滤菜单时使用
            val intent = Intent()
            intent.component = ComponentName(context.packageName,"")
            //非系统选项，itemId只能为0，否则会崩溃（ Unable to find resource ID）
            //order可以自己选择控制，但是有些rom不行
            menu.add(0,0,0,it).intent = intent
        }
        mActionMode = actionMode
        return actionMode
    }

    inner class ActionSelectInterface(val context: SingleWebView){
        @JavascriptInterface
        fun callback(value:String,title:String){
            mActionSelectListener?.onClick(title,value)
        }
    }

}