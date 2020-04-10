package com.ganhuo.app.ui.activity.more

import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.ganhuo.app.R
import com.ganhuo.app.base.BaseActivity
import com.ganhuo.app.base.BaseFragment
import com.ganhuo.app.base.Preference
import com.ganhuo.app.ui.fragment.wan.*
import com.ganhuo.app.utils.LogUtils
import com.ganhuo.app.utils.isDestroy
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_wan.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 *    author : zkk
 *    date   : 2020-03-31 13:35
 *    desc   :玩android
 */
class WanAndroidActivity : BaseActivity() {
    private val urlString: String by Preference("head", "")

    //    //fragment不能一直newInstance创建，保证唯一
    private var fragments: Array<BaseFragment> =
        arrayOf(
            HomeWanFragment.newInstance(),
            ProjectWanFragment.newInstance(),
            PublicWanFragment.newInstance(),
            KnowledgeWanFragment.newInstance(),
            MeWanFragment.newInstance()
        )

    override fun setLayoutId(): Int {
        return R.layout.activity_wan
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(toolbar).init()
    }

    override fun initData() {
        super.initData()
        toolbar.run {
            title = getString(R.string.string_main)
            setSupportActionBar(this)
        }
        //切换fragment
        bottomNavigation.run {
            setOnNavigationItemSelectedListener { item: MenuItem ->
                return@setOnNavigationItemSelectedListener when (item.itemId) {
                    R.id.main -> {
                        toolbar.title = getString(R.string.string_main)
                        switchFragment(fragments[0])
                        true
                    }
                    R.id.search -> {
                        toolbar.title = getString(R.string.string_project)
                        switchFragment(fragments[1])
                        true
                    }
                    R.id.hot -> {
                        toolbar.title = getString(R.string.string_public)
                        switchFragment(fragments[2])
                        true
                    }
                    R.id.knowledge -> {
                        toolbar.title = getString(R.string.string_knowledge)
                        switchFragment(fragments[3])
                        true
                    }
                    R.id.me -> {
                        toolbar.title = getString(R.string.string_me)
                        switchFragment(fragments[4])
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
        //关联
        drawerLayout.run {
            val toggle = ActionBarDrawerToggle(
                this@WanAndroidActivity, this, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            addDrawerListener(toggle)
            toggle.syncState()//该方法会自动和actionBar关联, 将开关的图片显示在了action上.如果不设置，也可以有抽屉的效果，不过是默认的图标
        }
        //抽屉点击
        navigationView.run {
            setNavigationItemSelectedListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.nav_setting -> {
                        toast(item.itemId.toString())
                    }
                    R.id.nav_share -> {
                        toast(item.itemId.toString())
                    }
                    else -> {
                        toast(item.itemId.toString())
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }
        //抽屉顶部头像
        navigationViewHeader()
        //设置fragment到布局
        switchFragment(fragments[0])
    }

    //抽屉顶部头像
    private fun navigationViewHeader() {
        val headerView = navigationView.getHeaderView(0)
        val bgHead = headerView.find<ImageView>(R.id.bg_head)
        val imageHead = headerView.find<ImageView>(R.id.image_head)
        setSaveHead(bgHead, imageHead)
    }

    //设置抽屉顶部头像
    private fun setSaveHead(bgHead: ImageView, imageHead: ImageView) {
        if (!TextUtils.isEmpty(urlString)) {
            LogUtils.d("urlString:$urlString")
            if (!isDestroy(this)) {
                Glide.with(this).load(urlString)
                    .placeholder(R.drawable.head_default)
                    .error(R.drawable.head_default).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    ).apply(RequestOptions.bitmapTransform(CircleCrop()))//设置圆图片
                    .into(imageHead)
                //高斯模糊
                Glide.with(this).load(urlString)
                    .placeholder(R.drawable.image_failed)
                    .error(R.drawable.image_failed).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    ).apply( //高斯模糊
                        RequestOptions.bitmapTransform(
                            BlurTransformation(
                                23,
                                3
                            )
                        )
                    )  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                    .into(bgHead)
            }
        }
    }

    //切换
    var mBeforeFragment: Fragment = Fragment()

    private fun switchFragment(currentFragment: Fragment) {
        if (currentFragment.isAdded) {
            supportFragmentManager.beginTransaction().hide(mBeforeFragment)
                .show(currentFragment).commit()
        } else {
            supportFragmentManager.beginTransaction().hide(mBeforeFragment)
                .add(R.id.container, currentFragment).commit()
        }
        mBeforeFragment = currentFragment
    }

    //右上menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuSearch -> {
                startActivity<HistorySearchActivity>()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}