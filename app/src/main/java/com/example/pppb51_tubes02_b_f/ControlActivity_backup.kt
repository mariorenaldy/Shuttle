package com.example.pppb51_tubes02_b_f

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pppb51_tubes02_b_f.databinding.ActivityControlBinding
import com.google.android.material.navigation.NavigationView
import java.io.File

class ControlActivity_backup : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerController, IControlActivity, IMultipleRequest{
    private lateinit var binding: ActivityControlBinding
    private lateinit var drawer:DrawerLayout
    private lateinit var presenter: ControlPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawer = binding.drawerLayout
        var navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        var toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(applicationContext, R.color.black));
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //get token if exist in cache
        val tokenFile = File(this.getCacheDir(), "token.txt")
        var token: String? = null
        if(tokenFile.exists()){
            val bufferedReader = tokenFile.bufferedReader()
            token = bufferedReader.readLine()
        }

        if(token.equals(null) || token.equals("")){
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(binding.fragmentContainer.id, LoginFragment()).commit()
            }
        } else {
            LoginFragment.ACCESS_TOKEN = token
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, HomeFragment()).commit()
        }

        presenter = ControlPresenter(this, this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, HomeFragment()).commit()
            R.id.nav_book -> {
                presenter.getRoutes(this)
            }
            R.id.nav_history -> {
                presenter.getOrders(this)
            }
            R.id.nav_logout -> {
                val tokenFile = File(this.getCacheDir(), "token.txt")
                tokenFile.delete()
                LoginFragment.ACCESS_TOKEN = ""
                supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, LoginFragment()).commit()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else{
            super.onBackPressed()
        }
    }

    override fun lockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
    override fun unlockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    override fun onResponse(response: Any?, type: String) {
        if(type.equals("getroutes")){
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, BookFragment.newInstance(
                response as String?
            )).commit()
        }
        else{
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, HistoryFragment.newInstance(
                response as String?
            )).commit()
        }
    }

    override fun onError(error: String?) {
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, ErrorFragment.newInstance(error)).commit()
    }

    override fun showErrorPage(message: String?) {
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, ErrorFragment.newInstance(message)).commit()
    }
}