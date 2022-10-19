package spa.lyh.cn.ft_webview.webview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SingleWebView extends WebView {
   private static final String CUSTOM_MENU_JS_INTERFACE = "CustomMenuJSInterface";
   private ActionMode mActionMode;
   private ActionSelectListener mActionSelectListener;
   private List<String> mCustomMenuList; //自定义添加的选项


   public SingleWebView(@NonNull Context context) {
      this(context,null);
   }

   public SingleWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
      this(context, attrs,0);
   }

   public SingleWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      //init();
   }

   /**
    * 设置点击回掉
    *
    * @param actionSelectListener
    */

   private void setActionSelectListener(ActionSelectListener actionSelectListener) {
      this.mActionSelectListener = actionSelectListener;
   }

   private interface ActionSelectListener {
      void onClick(String title, String selectText);
   }

/*
   private void init() {
      addJavascriptInterface(new ActionSelectInterface(this), CUSTOM_MENU_JS_INTERFACE);

      mCustomMenuList = new ArrayList<>();
      mCustomMenuList.add("菜单1");
      mCustomMenuList.add("菜单2");
   }

   @Override
   public ActionMode startActionMode(ActionMode.Callback callback) {
      return super.startActionMode(buildCustomCallback(callback));
   }

   @Override
   public ActionMode startActionMode(ActionMode.Callback callback, int type) {
      return super.startActionMode(buildCustomCallback(callback), type);
   }

   private ActionMode.Callback buildCustomCallback(final ActionMode.Callback callback) {
      ActionMode.Callback customCallback;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
         customCallback = new ActionMode.Callback2() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
               return callback.onCreateActionMode(mode, menu);
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
               callback.onPrepareActionMode(mode, menu);
               addCustomMenu(mode);
               for (int i = 0; i < menu.size(); i++) {
                  MenuItem menuItem = menu.getItem(i);
                  if (menuItem.getItemId() == 0) {
                     //自定义或是通过PROCESS_TEXT方案加入到菜单中的选项，item都为0
                     Intent intent = menuItem.getIntent();
                     ComponentName componentName = intent == null ? null : intent.getComponent();
                     //根据包名比较菜单中的选项是否是本app加入的
                     if (componentName != null && getContext().getPackageName().equals(componentName.getPackageName())) {
                        menuItem.setVisible(true);
                     } else {
                        menuItem.setVisible(false);
                     }
                  } else {
                     menuItem.setVisible(true);
                  }
               }
               return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
               if (item == null || TextUtils.isEmpty(item.getTitle())) {
                  return callback.onActionItemClicked(mode, item);
               }
               String title = item.getTitle().toString();
               if (mCustomMenuList != null && mCustomMenuList.contains(title)) {
                  try {
                     getSelectedData(title);
                     //延迟release，防止js获取不到选中的文字
                     postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           releaseAction();
                        }
                     }, 200);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
                  return true;
               } else if (mActionSelectListener != null) {
                  mActionSelectListener.onClick(title, "");
               }
               return callback.onActionItemClicked(mode, item);
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
               callback.onDestroyActionMode(mode);
            }

            @Override
            public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
               if (callback instanceof ActionMode.Callback2) {
                  ActionMode.Callback2 tempCallback2 = (ActionMode.Callback2) callback;
                  tempCallback2.onGetContentRect(mode, view, outRect);
               } else {
                  super.onGetContentRect(mode, view, outRect);
               }
            }
         };
      } else {
         customCallback = callback;
      }
      return customCallback;
   }


   private ActionMode addCustomMenu(ActionMode actionMode) {
      if (actionMode != null && mCustomMenuList != null) {
         Menu menu = actionMode.getMenu();
         //menu.clear();
         //添加自定义选项
         int size = mCustomMenuList.size();
         for (int i = 0; i < size; i++) {
            //intent主要用于过滤菜单时使用
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getContext().getPackageName(), ""));
            String title = mCustomMenuList.get(i);
            //非系统选项，itemId只能为0，否则会崩溃（ Unable to find resource ID）
            //order可以自己选择控制，但是有些rom不行
            menu.add(0, 0, 0, title).setIntent(intent);
         }
         mActionMode = actionMode;
      }
      return actionMode;
   }

   private void releaseAction() {
      if (mActionMode != null) {
         mActionMode.finish();
         mActionMode = null;
      }
   }


   private void getSelectedData(String title) {

      String js = "javascript:(function getSelectedText() {" + "var txt;" + "var title = \"" + title + "\";" + "if (window.getSelection) {" +
              "txt = window.getSelection().toString();" + "} else if (window.document.getSelection) {" + "txt = window.document.getSelection()" + ".toString();" + "} else if (window.document.selection) {" + "txt = window.document.selection.createRange().text;" + "}" + CUSTOM_MENU_JS_INTERFACE + ".callback(txt,title);" + "})()";
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
         evaluateJavascript(js, null);
      } else {
         loadUrl(js);
      }
   }

   private class ActionSelectInterface {

      SingleWebView mContext;

      ActionSelectInterface(SingleWebView context) {
         mContext = context;
      }

      @JavascriptInterface
      public void callback(final String value, final String title) {
         if (mActionSelectListener != null) {
            mActionSelectListener.onClick(title, value);
         }
      }
   }*/


}
