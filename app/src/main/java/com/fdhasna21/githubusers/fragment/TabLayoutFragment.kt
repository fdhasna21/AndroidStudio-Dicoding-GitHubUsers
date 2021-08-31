package com.fdhasna21.githubusers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fdhasna21.githubusers.databinding.FragmentTabLayoutBinding

class TabLayoutFragment(private val adapter : RecyclerView.Adapter<*>) : Fragment() {
    private var _binding : FragmentTabLayoutBinding? = null
    val binding get() = _binding!!

    private fun setupRecyclerView(){
        binding.tablayoutRecyclerView.addItemDecoration(object :
            DividerItemDecoration(requireContext(), VERTICAL) {})
        binding.tablayoutRecyclerView.setHasFixedSize(true)
        binding.tablayoutRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tablayoutRecyclerView.adapter = adapter
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