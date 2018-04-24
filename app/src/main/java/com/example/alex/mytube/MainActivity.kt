package com.example.alex.mytube

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, LifecycleOwner {
    private var mPlayListViewModel: PlayListViewModel? = null
    private var videos: List<RoomVideoTable> = ArrayList()
    private lateinit var rvVideoAdapter: RVVideoAdapter

    //Menu var for runtime adding items
    private lateinit var mNaviView: NavigationView
    private lateinit var mPlayLists: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        //Init NavDrawers and menu
        mNaviView = findViewById(R.id.nav_view)
        mNaviView.setNavigationItemSelectedListener(this)
        mPlayLists = mNaviView.menu
        mPlayLists.clear()
        mPlayLists.add(1, 1, 1, "Play list 1")
        mPlayLists.add(1, 2, 1, "Play List 2")
        mPlayLists.add(1, 3, 1, "Play List 3")

        //RecyclerView for display videos
        recyclerView.layoutManager = LinearLayoutManager(this)
        rvVideoAdapter = RVVideoAdapter(videos, this)
        recyclerView.adapter = rvVideoAdapter


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        mPlayListViewModel = ViewModelProviders.of(this).get(PlayListViewModel::class.java)
        mPlayListViewModel!!.getVideos().observe(this,
                Observer<List<RoomVideoTable>> { vid ->
                    rvVideoAdapter.setVideo(vid)
                    rvVideoAdapter.notifyDataSetChanged()

                })

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            1 -> showVideoByPlayList("PLkKunJj_bZefHRpkU-MF5YMfIOwZRRlg8")
            2 -> showVideoByPlayList("PLkKunJj_bZefpZJio9Gh25b5hs0oBCXH_")
            3 -> showVideoByPlayList("PLkKunJj_bZefB1_hhS68092rbF4HFtKjW")
        }



        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showVideoByPlayList(playListId: String) {
        mPlayListViewModel!!.showVideoByPlayList(playListId).observe(this,
                Observer<List<RoomVideoTable>> { t ->

                    if (t != null) {
                        for (e in t) {
                            videos = t!!
                            rvVideoAdapter.setVideo(t)
                            rvVideoAdapter.notifyDataSetChanged()

                        }
                    }
                })
    }

    fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}
