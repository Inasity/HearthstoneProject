package com.example.android.hearthstoneproject.listclasscards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.hearthstoneproject.R
import com.example.android.hearthstoneproject.databinding.ListItemCardBinding
import com.example.android.hearthstoneproject.listclasscards.data.CardEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction1

class CardAdapter(private val updateCardFavorite: KSuspendFunction1<CardEntity, Unit>)
    : ListAdapter<CardEntity, CardAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private var binding: ListItemCardBinding,
        private var updateCardFavorite: KSuspendFunction1<CardEntity, Unit>)
        : RecyclerView.ViewHolder(binding.root) {

                fun bind(card: CardEntity) {
                    binding.card = card
                    binding.executePendingBindings()

                    if(card.cardFavorited)
                    {
                        binding.cardFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                    else{
                        binding.cardFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }

                    binding.cardImage.setOnClickListener { v: View ->
                        v.findNavController().navigate(
                            ListCardsFragmentDirections.actionListCardsFragmentToCardInfoFragment(
                                card = card
                            )
                        )
                    }

                    binding.cardFavoriteIcon.setOnClickListener {
                        card.cardFavorited = !card.cardFavorited

                        if (card.cardFavorited)
                        {
                            binding.cardFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                        }

                        else
                        {
                            binding.cardFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            updateCardFavorite.invoke(card)
                        }
                    }
                }

        companion object
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemCardBinding.inflate(LayoutInflater.from(parent.context)), updateCardFavorite)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardFeed = getItem(position)
        holder.bind(cardFeed)
    }

    companion object DiffCallback: DiffUtil.ItemCallback<CardEntity>() {
        override fun areItemsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem.cardId == newItem.cardId
        }
    }
}