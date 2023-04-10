package com.example.securitymessenger

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.securitymessenger.Fragments.ChatsFragment
import com.example.securitymessenger.Fragments.SearchFragment
import com.example.securitymessenger.Model.User
import com.example.securitymessenger.RestClientApi.ChatsApi
import com.example.securitymessenger.RestClientApi.SearchApi
import com.example.securitymessenger.Services.NotificationService
import com.example.securitymessenger.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var jwt: String
    private lateinit var userId: String
    private var recyclerView: RecyclerView? = null
    private lateinit var email: String
    private var settings: SharedPreferences? = null
    private lateinit var viewPager: ViewPager
    public var activityIsVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityIsVisible = true
        settings = getSharedPreferences("account", MODE_PRIVATE);
        val arguments = intent.extras
        if (arguments != null) {
            jwt = arguments.get("jwt").toString()
            email = arguments.get("email").toString()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        var searchApi = SearchApi()
        searchApi.findUsers(email, this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun setPage(page: Int) {
        viewPager.setCurrentItem(page, true) //второй параметр для плавного перелистывания
    }

    override fun onDestroy() {
        this.activityIsVisible = false
        super.onDestroy()
    }

    override fun onPause() {
        this.activityIsVisible = false
        super.onPause()
    }

    override fun onStop() {
        this.activityIsVisible = false
        super.onStop()
    }

    override fun onResume() {
        this.activityIsVisible = true
        super.onResume()
    }

    public fun setUser(user: User){
        userId = user.userId.toString()
        var settings = getSharedPreferences("account", MODE_PRIVATE)
        settings.edit().putString("userId", userId).apply()
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(ChatsFragment(email, jwt, userId), "Чаты")
        viewPagerAdapter.addFragment(SearchFragment(), "Поиск")

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        var UserName: TextView = binding.root.findViewById<TextView>(R.id.user_name)
        UserName.text = user.userName.toString()
        var imageView: ImageView = binding.root.findViewById<ImageView>(R.id.profile_image)
        Picasso.get().load(user.userImg).placeholder(R.drawable.ic_profile).into(imageView)

        val prefEditor = settings!!.edit()
        prefEditor.putString("userName", user.userName)
        prefEditor.putString("userImg", user.userImg)
        prefEditor.apply()

        startService(Intent(this, NotificationService::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_exit -> {
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                val prefEditor = settings!!.edit()
                prefEditor.remove("jwt")
                prefEditor.remove("userId")
                prefEditor.remove("useremail")
                prefEditor.apply()
                stopService(Intent(this, NotificationService::class.java))
                startActivity(intent)
                finish()
                return true
            }
            R.id.settings_settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings_blocked_chats -> {
                val intent = Intent(this@MainActivity, BlockedChatsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun dialogBlockChat(chatid: Int) : Boolean {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Заблокировать чат")
            .setMessage("Вы уверены, что хотите заблокировать чат?")
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    val api = ChatsApi()
                    api.BlockChat(userId, chatid, {bool -> (supportFragmentManager.fragments.first() as ChatsFragment).refresh()}, jwt!!)
                })
            .setNegativeButton("Отмена",
                DialogInterface.OnClickListener { dialog, id ->

                })
        builder.create().show()
        return true
    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager) :
            FragmentPagerAdapter(fragmentManager) {

        private val fragments: ArrayList<Fragment>
        private val titles: ArrayList<String>

        init {
            fragments = ArrayList<Fragment>()
            titles = ArrayList<String>()
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment: Fragment, title: String){
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }
}