package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categorias, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCategorias)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val categorias = listOf(
            Categoria("Ropa", "https://example.com/ropa.jpg"),
            Categoria("Accesorios", "https://example.com/accesorios.jpg"),
            Categoria("Calzado", "https://example.com/calzado.jpg"),
            Categoria("Decoración", "https://example.com/decoracion.jpg")
        )

        categoriaAdapter = CategoriaAdapter(categorias)
        recyclerView.adapter = categoriaAdapter

        return view
    }
}