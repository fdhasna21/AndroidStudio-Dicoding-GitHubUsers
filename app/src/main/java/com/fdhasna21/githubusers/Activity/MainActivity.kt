package com.fdhasna21.githubusers.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fdhasna21.githubusers.DataResolver.User
import com.fdhasna21.githubusers.DataResolver.getUserData
import com.fdhasna21.githubusers.R
import com.fdhasna21.githubusers.RowAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rowAdapter = RowAdapter(getUserData(applicationContext), this)
        recycler_view.addItemDecoration(object : DividerItemDecoration(this, DividerItemDecoration.VERTICAL){})
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = rowAdapter

        rowAdapter.setOnItemClickCallback(object : RowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_about -> {
                startActivity(Intent(this, AboutMeActivity::class.java)) //AboutMeActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}