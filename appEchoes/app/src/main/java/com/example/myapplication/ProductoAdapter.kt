package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductoAdapter(
    private var productos: List<Producto>,
    private val onItemClick: (Producto) -> Unit,
    private val onFavClick: (Producto) -> Unit

) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewProducto)
        val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val textViewPrecio: TextView = itemView.findViewById(R.id.textViewPrecio)
        val favIcon: ImageView = itemView.findViewById(R.id.favIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.textViewNombre.text = producto.name
        holder.textViewPrecio.text = "$${producto.price}"
        holder.favIcon.setOnClickListener { onFavClick(producto) }

        Glide.with(holder.itemView.context)
            .load(producto.image_url)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.imageView)

        holder.itemView.setOnClickListener { onItemClick(producto) }
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    fun actualizarLista(nuevaLista: List<Producto>) {
        this.productos = nuevaLista
        notifyDataSetChanged()
    }
}