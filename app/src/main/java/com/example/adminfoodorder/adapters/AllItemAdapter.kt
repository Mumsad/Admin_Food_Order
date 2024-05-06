//package com.example.adminfoodorder.adapters
//
//import android.view.LayoutInflater
//import android.view.MenuItem
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.adminfoodorder.databinding.ActivityAddItemBinding
//import com.example.adminfoodorder.databinding.ItemAllItemBinding
//
//class AllItemAdapter( private  val MenuItemName:ArrayList<String>,
//                      private  val MenuItemPrice:ArrayList<String>,
//                      private  val MenuItemImage:ArrayList<Int>): RecyclerView.Adapter<AllItemAdapter.AddItemViewHolder>() {
//     private  val itemQuantities = IntArray(MenuItemName.size){1}
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
//        val binding = ItemAllItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        return AddItemViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int =MenuItemName.size
//
//
//    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
//    holder.bind(position)
//    }
//    inner  class AddItemViewHolder(private val binding: ItemAllItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(position: Int) {
//    binding.apply {
//        val    quantity= itemQuantities[position]
//    itemFoodNameTextView.text=MenuItemName[position]
//    itemPriceTextView.text=MenuItemPrice[position]
//    itemImageView.setImageResource(MenuItemImage[position])
//
//        quantityTextView.text=quantity.toString()
//
//        minusImageButton.setOnClickListener{
//            decreaseQuantity(position)
//
//        }
//        plusImageButton.setOnClickListener{
//            increaseQuantity(position)
//        }
//        deleteImageButton.setOnClickListener{
//            deleteQuantity(position)
//        }
//    }
//        }
//        private fun increaseQuantity(position: Int) {
//            if (itemQuantities[position]<10){
//                itemQuantities[position]++
//                binding.quantityTextView.text=itemQuantities[position].toString()
//            }
//        }
//        private fun decreaseQuantity(position: Int) {
//            if (itemQuantities[position]>1){
//                itemQuantities[position]--
//                binding.quantityTextView.text=itemQuantities[position].toString()
//            }
//        }
//
//        private fun deleteQuantity(position: Int) {
//            MenuItemName.removeAt(position)
//            MenuItemPrice.removeAt(position)
//            MenuItemImage.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position,MenuItemImage.size)
//
//        }
//    }
//}
//
