package com.example.android.hearthstoneproject.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.android.hearthstoneproject.databinding.FragmentMainScreenBinding
import com.example.android.hearthstoneproject.mainscreen.data.MainscreenDatabase
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import com.example.android.hearthstoneproject.util.createViewModel

class MainScreenFragment : Fragment() {

    //val application = requireNotNull(this.activity).application

    //val dataSource = MainscreenDatabase.getInstance(application).mainScreenDatabaseDao

    private val viewModel: MainScreenViewModel by lazy {
        createViewModel {
            MainScreenViewModel(
                application = this.requireActivity().application,
                HearthStoneRepo.provideHeartStoneRepo(),
                database = MainscreenDatabase.getInstance(this.requireActivity().application).mainScreenDatabaseDao
            )
        }
    }

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
            v.findNavController().navigate(MainScreenFragmentDirections
                .actionMainScreenFragmentToListCardsFragment
                    (viewModel.searchParameter, viewModel.whichFunctionToUse()))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllClasses()
    }

}