package co.kr.kwon.deliveryapp.widget.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.kwon.deliveryapp.data.entity.RestaurantFoodEntity
import co.kr.kwon.deliveryapp.databinding.ViewholderMenuListBinding

class MenuListAdapter : RecyclerView.Adapter<MenuListAdapter.MenuItemViewHolder>() {

    private var menuList: List<RestaurantFoodEntity> = listOf()

     class MenuItemViewHolder(
        private val binding: ViewholderMenuListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindViews(data: RestaurantFoodEntity) = with(binding) {
            orderMenu.text = data.title
            Log.e("MenuListAdapter","${data.title}")
            priceText.text = data.price.toString()
            Log.e("MenuListAdapter","${data.price.toString()}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val view = ViewholderMenuListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        holder.bindViews(menuList[position])
    }

    override fun getItemCount(): Int = menuList.size

    fun setPhotoList(menuList: List<RestaurantFoodEntity>) {
        this.menuList = menuList
        notifyDataSetChanged()
    }
}
