package com.fdhasna21.githubusers.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fdhasna21.githubusers.activity.UserDetailActivity
import com.fdhasna21.githubusers.adapter.AccountRowAdapter
import com.fdhasna21.githubusers.dataclass.Account
import com.fdhasna21.githubusers.dataResolver.getUserData
import com.fdhasna21.githubusers.databinding.FragmentTabLayoutBinding

class TabLayoutFragment() : Fragment() {
    private var _binding : FragmentTabLayoutBinding? = null
    private val binding get() = _binding!!

    enum class Type{
        //dataFragment:ArrayList<*> , type:Type sbg constructor
        ACCOUNT, //call AccountRowAdapter
        REPOSITORY //call RepositoryRowAdapter
    }

    private fun setupRecyclerView(){
        //todo : beda di adapter dan datanya, jadi inputan
        val rowAdapter = AccountRowAdapter(getUserData(requireContext()), requireContext())
        binding.recyclerViewFragment.addItemDecoration(object :
            DividerItemDecoration(requireContext(), VERTICAL) {})
        binding.recyclerViewFragment.setHasFixedSize(true)
        binding.recyclerViewFragment.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFragment.adapter = rowAdapter

        rowAdapter.setOnItemClickCallback(object : AccountRowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Account) {
                val intent = Intent(requireContext(), UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}