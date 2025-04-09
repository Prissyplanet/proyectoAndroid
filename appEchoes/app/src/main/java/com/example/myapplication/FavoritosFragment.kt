package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.db.Connection

class FavoritosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var dbHelper: Connection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoritos, container, false)
        dbHelper = Connection(requireContext())

        setupRecyclerView(view)
        cargarFavoritos()

        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewFavoritos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        productoAdapter = ProductoAdapter(
            emptyList(),
            { producto ->
                val intent = Intent(requireContext(), DetalleProductoActivity::class.java)
                intent.putExtra("producto", producto)
                startActivity(intent)
            },
            { producto ->
                val userId = 1
                if (dbHelper.isFavorito(userId, producto.id)) {
                    dbHelper.removeFavorito(userId, producto.id)
                    Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                    cargarFavoritos() // Actualizar lista después de eliminar
                }
            }
        )
        recyclerView.adapter = productoAdapter
    }

    private fun cargarFavoritos() {
        val productosFavoritos = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase

        val query = """
            SELECT p.* FROM products p
            JOIN favorites f ON p.id = f.product_id
            WHERE f.user_id = 1
        """.trimIndent()

        db.rawQuery(query, null).use { cursor ->
            try {
                while (cursor.moveToNext()) {
                    productosFavoritos.add(
                        Producto(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                            price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                            image_url = cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                            description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("BD_ERROR", "Error al cargar favoritos: ${e.message}")
            }
        }
        productoAdapter.actualizarLista(productosFavoritos)
        Log.d("BD_DEBUG", "Favoritos cargados: ${productosFavoritos.size}")
    }

    override fun onResume() {
        super.onResume()
        cargarFavoritos()
    }

    override fun onDestroyView() {
        dbHelper.close()
        super.onDestroyView()
    }
}