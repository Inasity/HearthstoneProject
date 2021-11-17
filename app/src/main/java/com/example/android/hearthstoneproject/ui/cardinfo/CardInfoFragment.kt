package com.example.android.hearthstoneproject.ui.cardinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.hearthstoneproject.R
import com.example.android.hearthstoneproject.databinding.FragmentCardInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardInfoFragment : Fragment() {

    private val cardParamsFragmentArgs by navArgs<CardInfoFragmentArgs>()

    private val viewModel: CardInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.card = cardParamsFragmentArgs.card

        val binding = FragmentCardInfoBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        binding.cardInfoBackArrow.setOnClickListener {
            it.findNavController().popBackStack()
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

        binding.infoFavoritedIcon.setOnClickListener {
            viewModel.card.cardFavorited = !viewModel.card.cardFavorited

            if (viewModel.card.cardFavorited)
            {
                binding.infoFavoritedIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            else
            {
                binding.infoFavoritedIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            viewModel.update()
        }

        return binding.root
    }

}
