package com.summer.developmodule

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.summer.developmodule.bean.Project
import com.summer.developmodule.bean.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val list = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        val query = App.getDaosession()?.projectDao?.queryBuilder()?.build()
        for (pro in query?.list()!!) {
            list.addAll(pro.userList!!)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(this, list)
    }

    private fun initData() {
        val project = Project()
        val list = arrayListOf<User>()

        for (i in 1..10) {
            var user = User()
            user.age = i
            user.name = "tom"
            list.add(user)
        }
        project.userList = list
        App.getDaosession()?.projectDao?.insert(project)
    }


    class MyAdapter(val context: Context, private val list: ArrayList<User>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvUser?.text = "名字${list[position].name} 年龄${list[position].age}"
        }

        class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
            var tvUser: TextView? = null

            init {
                tvUser = view.findViewById(R.id.tv_user)
            }
        }

    }


}

