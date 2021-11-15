package com.example.android.hearthstoneproject.cardinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.hearthstoneproject.R
import com.example.android.hearthstoneproject.databinding.FragmentCardInfoBinding
import com.example.android.hearthstoneproject.listclasscards.data.ListCardsDatabase
import com.example.android.hearthstoneproject.util.createViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardInfoFragment : Fragment() {

    val cardParamsFragmentArgs by navArgs<CardInfoFragmentArgs>()

    private val viewModel: CardInfoViewModel by lazy {
        createViewModel {
            CardInfoViewModel(
                application = this.requireActivity().application,
                database = ListCardsDatabase.getInstance(this.requireActivity().application).listCardsDatabaseDao,
                card = cardParamsFragmentArgs.card
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCardInfoBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        binding.cardInfoBackground.setOnClickListener {
            it.findNavController().navigate(
                CardInfoFragmentDirections.actionCardInfoFragmentToListCardsFragment2()
            )
        }

        if(viewModel.card.cardType == null)
        {
            binding.cardTypeInfo.visibility = View.GONE
        }

        if(viewModel.card.cardRarity == null)
        {
            binding.cardRarityInfo.visibility = View.GONE
        }

        if(viewModel.card.cardSet == null)
        {
            binding.cardSetInfo.visibility = View.GONE
        }

        if(viewModel.card.cardText == null)
        {
            binding.cardTextInfo.visibility = View.GONE
        }

        if(viewModel.card.cardFavorited)
        {
            binding.infoFavoritedIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
        else{
            binding.infoFavoritedIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

        binding.infoFavoritedIcon.setOnClickListener { v: View ->
            viewModel.card.cardFavorited = !viewModel.card.cardFavorited

            if (viewModel.card.cardFavorited)
            {
                binding.infoFavoritedIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            else
            {
                binding.infoFavoritedIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.updateCard(viewModel.card)
            }
        }

        return binding.root
    }

}
