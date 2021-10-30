package com.example.android.hearthstoneproject

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.hearthstoneproject.listclasscards.CardAdapter
import com.example.android.hearthstoneproject.listclasscards.data.CardEntity
import com.example.android.hearthstoneproject.mainscreen.ClassesAdapter
import com.example.android.hearthstoneproject.mainscreen.data.ClassEntity

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    if(imgUrl != null) {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https")?.build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .override(550, 550)
                .fitCenter()
                .placeholder(R.drawable.loading_animation))
            .into(imgView)
    }
    else {
        Glide.with(imgView.context)
            .load(R.drawable.hearthstone_error_image)
            .apply(RequestOptions()
                .override(450, 450)
                .fitCenter())
            .into(imgView)
    }

}

@BindingAdapter("bigImageUrl")
fun bindBigImage(imgView: ImageView, imgUrl: String?) {
    if(imgUrl != null) {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https")?.build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .override(750, 750)
                .fitCenter()
                .placeholder(R.drawable.loading_animation))
            .into(imgView)
    }
    else {
        Glide.with(imgView.context)
            .load(R.drawable.hearthstone_error_image)
            .apply(RequestOptions()
                .override(650, 650)
                .fitCenter())
            .into(imgView)
    }
}

@BindingAdapter("littleImageUrl")
fun bindLittleImage(imgView: ImageView, imgUrl: String?) {
    if(imgUrl != null) {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https")?.build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .override(175, 175)
                .fitCenter()
                .placeholder(R.drawable.loading_animation))
            .into(imgView)
    }
    else {
        Glide.with(imgView.context)
            .load(R.drawable.hearthstone_error_image)
            .apply(RequestOptions()
                .override(450, 450)
                .fitCenter())
            .into(imgView)
    }

}

@BindingAdapter("listCardEntities")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<CardEntity>?) {
    val adapter = recyclerView.adapter as CardAdapter
    adapter.submitList(data)
}

@BindingAdapter("listHearthStoneClasses")
fun bindRecyclerViewClasses(recyclerView: RecyclerView, data: List<ClassEntity>?) {
    val adapter = recyclerView.adapter as ClassesAdapter
    adapter.submitList(data)
}

@BindingAdapter("typeCard")
fun bindCardTypeName(textView: TextView, type: String?)
{
    type?.let {
        val cardType = "<b>Type: </b>$type"
        textView.text = Html.fromHtml(cardType)
    }
}

@BindingAdapter("rarityCard")
fun bindCardRarityName(textView: TextView, rarity: String?)
{
    rarity?.let {
        val cardRarity = "<b>Rarity: </b>$rarity"
        textView.text = Html.fromHtml(cardRarity)
    }
}

@BindingAdapter("setCard")
fun bindCardSetName(textView: TextView, set: String?)
{
    set?.let {
        val cardSet = "<b>Set: </b>$set"
        textView.text = Html.fromHtml(cardSet)
    }
}

@BindingAdapter("effectCard")
fun bindCardEffectName(textView: TextView, effect: String?)
{
    effect?.let {
        val cardEffect = "<b>Effect: </b>$effect"


        textView.text = Html.fromHtml(cardEffect.replace("\\n", "<br>"))
    }
}