package com.example.shopapp.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.shopapp.utils.loadImage

class ProductImagesViewPagerAdapter(private val context: Context) : PagerAdapter() {

    val images = ArrayList<String>()

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.loadImage(images[position])
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

    fun updateList(list: List<String>?) {
        list?.let {
            images.clear()
            images.addAll(it)
            notifyDataSetChanged()
        }
    }

}