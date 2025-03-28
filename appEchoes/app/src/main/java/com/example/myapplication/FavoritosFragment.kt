package com.example.myapplication

import android.content.Intent
import com.example.myapplication.ProductoAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoritos, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val productosFavoritos = listOf(
            Producto("Producto Favorito 1", 99.99, "@drawable/placeholder_image", "Wacha wacha"),
            Producto("Producto Favorito 2", 149.99, "@drawable/placeholder_image", "no se qué poner, solo es por mientras")
        )

        productoAdapter = ProductoAdapter(productosFavoritos){ producto ->
            val intent = Intent(requireContext(), DetalleProductoActivity::class.java)
            intent.putExtra("producto", producto)
            startActivity(intent)
        }
        recyclerView.adapter = productoAdapter

        return view
    }
}