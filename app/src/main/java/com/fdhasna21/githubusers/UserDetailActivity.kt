package com.fdhasna21.githubusers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.fdhasna21.githubusers.DataResolver.User
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = ""

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val views = arrayListOf(
            detail_image,           //0
            detail_username,        //1
            detail_name,            //2
            detail_company,         //3
            detail_location,        //4
            detail_repositories,    //5
            detail_followers,       //6
            detail_following,       //7
            btn_detail_github)      //8

        views.forEachIndexed{ idx: Int, it: View? ->
            if(idx == 0){
                //todo : glide (user.avatar)
            }
            else{
                if(idx > 2){
                    it?.setOnClickListener(this)
                }
                if(idx != 8){
                    (it as TextView).text = when(idx){
                        1 -> user.username
                        2 -> user.name
                        3 -> user.company
                        4 -> user.location
                        5 -> "${user.repository} repositories"
                        6 -> "${user.follower} followers"
                        7 -> "${user.following} following"
                        else -> ""
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_share -> {
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            //todo : intent
            R.id.detail_company -> Toast.makeText(this, "company", Toast.LENGTH_SHORT).show()
            R.id.detail_location -> Toast.makeText(this, "location", Toast.LENGTH_SHORT).show()
            R.id.detail_repositories -> Toast.makeText(this, "repositories", Toast.LENGTH_SHORT).show()
            R.id.detail_followers -> Toast.makeText(this, "followers", Toast.LENGTH_SHORT).show()
            R.id.detail_following -> Toast.makeText(this, "following", Toast.LENGTH_SHORT).show()
            R.id.btn_detail_github -> Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}