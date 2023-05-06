package com.example.shopapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.shopapp.adapters.ProductImagesViewPagerAdapter
import com.example.shopapp.databinding.FragmentProductDetailsBinding
import com.example.shopapp.models.Product


class ProductDetailsFragment : Fragment() {


    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private var viewPagerAdapter: ProductImagesViewPagerAdapter? = null
    private lateinit var productImageList: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ProductImagesViewPagerAdapter(requireContext())
        val product = args.product
        displayProductData(product)
        binding!!.fragmentDetailViewPager.adapter = viewPagerAdapter

    }

    private fun displayProductData(product: Product) {
        binding!!.fragmentDetailProgressBar.visibility = View.GONE
        binding!!.fragmentDetailTitle.text = product.name
        binding!!.fragmentDetailTotalPrice.text = product.price.toString() + " LE"
        binding!!.fragmentDetailFeatures.text = product.description
        val productName = product.name?.trim()?.split(" ")
        var name: String = " "
        if (productName != null) {
            if (productName.size >= 3) {
                val thirdIndex = (productName.size  ) / 3
                val firstThirdWords = productName.slice(0 until thirdIndex)
                name = firstThirdWords.joinToString(" ")
            } else if (productName.size == 2)
                name = productName.joinToString("")
            else if (productName.size == 1)
                name = productName[0]
        }
        binding!!.toolbarTitle.text = name


        viewPagerAdapter?.updateList(product.images)
    }

}