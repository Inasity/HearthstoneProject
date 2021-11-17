package com.example.android.hearthstoneproject.ui.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.android.hearthstoneproject.databinding.FragmentMainScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private val viewModel: MainScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflate the layout for this fragment

        val binding = FragmentMainScreenBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        binding.classesRecyclerView.adapter = ClassesAdapter()

        binding.searchTextView.setOnClickListener { v: View ->
            v.findNavController().navigate(
                MainScreenFragmentDirections
                    .actionMainScreenFragmentToListCardsFragment
                        (viewModel.searchParameter, viewModel.whichFunctionToUse())
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllClasses()
    }

}
