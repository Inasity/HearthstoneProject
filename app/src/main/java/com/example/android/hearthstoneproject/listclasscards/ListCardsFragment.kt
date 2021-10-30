package com.example.android.hearthstoneproject.listclasscards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.hearthstoneproject.databinding.FragmentListCardsBinding
import com.example.android.hearthstoneproject.listclasscards.data.ListCardsDatabase
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import com.example.android.hearthstoneproject.util.createViewModel

class ListCardsFragment : Fragment() {

    private val searchParamsFragmentArgs by navArgs<ListCardsFragmentArgs>()

    private val viewModel: ListCardsViewModel by lazy {
        createViewModel {
            ListCardsViewModel(
                application = this.requireActivity().application,
                HearthStoneRepo.provideHeartStoneRepo(),
                database = ListCardsDatabase.getInstance(this.requireActivity().application).listCardsDatabaseDao,
                searchParameter = searchParamsFragmentArgs.searchParameter,
                useClassSearch = searchParamsFragmentArgs.useClassSearch
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Inflate layout for this fragment

        val binding = FragmentListCardsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        binding.cardListRecyclerview.adapter = CardAdapter(viewModel::updateCard)

        return binding.root
    }

}