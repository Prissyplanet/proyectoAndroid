package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductoAdapter(
    private val productos: List<Producto>,
    private val onItemClick: (Producto) -> Unit

) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewProducto)
        val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val textViewPrecio: TextView = itemView.findViewById(R.id.textViewPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.textViewNombre.text = producto.nombre
        holder.textViewPrecio.text = "$${producto.precio}"

        Glide.with(holder.itemView.context)
            .load(producto.imagenUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.imageView)

        holder.itemView.setOnClickListener { onItemClick(producto) }
    }

    override fun getItemCount(): Int {
        return productos.size
    }
}