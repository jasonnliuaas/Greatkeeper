package com.newline.housekeeper.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.greatkeeper.keeper.R
import com.lidroid.xutils.ViewUtils
import com.newline.core.BaseActivity
import com.newline.housekeeper.KeeperUrl
import com.newline.housekeeper.WelcomeActivity

class MoreActivity : BaseActivity() {

    private var mTextTopTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.more_layout)
        ViewUtils.inject(this)
        mTextTopTitle = loadControl<TextView>(R.id.textTopTitle)
        mTextTopTitle!!.setText(R.string.more)
    }

    /*@OnClick(R.id.layoutBtnCurrency)
    public void onCurrencyClick(View view){
        startActivity(new Intent(this, CurrencyActivity.class));
    }*/

    fun onClick(view: View) {
        val viewId = view.id
        //关于我们
        if (viewId == R.id.layoutBtnAboutUs) {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", KeeperUrl.AboutUsUrl)
            intent.putExtra("title", getString(R.string.moreAboutUs))
            startActivity(intent)
        } else if (viewId == R.id.layoutBtnPresent) {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", KeeperUrl.FunctionsUrl)
            intent.putExtra("title", getString(R.string.morePresent))
            startActivity(intent)
        } else if (viewId == R.id.layoutBtnWelcome) {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra("isAgain", true)
            startActivity(intent)
        } else if (viewId == R.id.layoutBtnProblem) {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url", KeeperUrl.QuestionUrl)
            intent.putExtra("title", getString(R.string.moreQuestion))
            startActivity(intent)
        }/*//版本更新
        else if(viewId == R.id.layoutBtnUpdate){
            UmengUpdateAgent.setUpdateAutoPopup(false);
            UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                
                @Override
                public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                    if(updateStatus == UpdateStatus.Yes){
                        UmengUpdateAgent.showUpdateDialog(MoreActivity.this, updateInfo);
                    } else if(updateStatus == UpdateStatus.No){
                        TipUtil.showShort(MoreActivity.this, "当前已是最新版");
                    } else if(updateStatus == UpdateStatus.Timeout) {
                        TipUtil.showShort(MoreActivity.this, "网络超时");
                    } else {
                        TipUtil.showShort(MoreActivity.this, "无需更新");
                    }
                }
            });
            UmengUpdateAgent.forceUpdate(this);
        }*///常见问题
        //欢迎页
        //功能介绍
    }

}
