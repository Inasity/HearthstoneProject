package com.example.android.hearthstoneproject.ui.listclasscards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.hearthstoneproject.databinding.FragmentListCardsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListCardsFragment : Fragment() {

    private val searchParamsFragmentArgs by navArgs<ListCardsFragmentArgs>()

    private val viewModel: ListCardsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Inflate layout for this fragment

        viewModel.searchParameter = searchParamsFragmentArgs.searchParameter
        viewModel.useClassSearch = searchParamsFragmentArgs.useClassSearch
        viewModel.initializeTheViewModel()

        val binding = FragmentListCardsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.listCardsBackArrow.setOnClickListener {
            it.findNavController().navigate(
                ListCardsFragmentDirections.actionListCardsFragmentToMainScreenFragment()
            )
        }

        setHasOptionsMenu(true)

        binding.cardListRecyclerview.adapter = CardAdapter(viewModel::updateCard)

        return binding.root
    }
}
