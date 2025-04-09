package com.example.myapplication

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
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

class InicioFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var dbHelper: Connection
    private lateinit var database: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)
        dbHelper = Connection(requireContext())
        database = dbHelper.readableDatabase
        setupRecyclerView(view)
        cargarProductosDesdeBD()
        debugDatabase()
        Log.d("BD_DEBUG", dbHelper.debugProducts())

        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewInicio)
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
                } else {
                    dbHelper.addFavorito(userId, producto.id)
                    Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                }
                cargarProductosDesdeBD()
            }
        )
        recyclerView.adapter = productoAdapter
    }

    private fun cargarProductosDesdeBD() {
        val productos = mutableListOf<Producto>()
        val cursor = database.rawQuery("SELECT * FROM products", null)

        Log.d("BD_DEBUG", "Número de productos: ${cursor.count}")

        try {
            while (cursor.moveToNext()) {
                val producto = Producto(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    image_url = cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                    description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                )
                productos.add(producto)
                Log.d("BD_DEBUG", "Producto cargado: ${producto.name}")
            }
        } catch (e: Exception) {
            Log.e("BD_ERROR", "Error al cargar productos: ${e.message}")
        } finally {
            cursor.close()
        }

        productoAdapter.actualizarLista(productos)
    }

    private fun debugDatabase() {
        val db = dbHelper.readableDatabase
        val cursorTables = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        Log.d("BD_DEBUG", "Tablas existentes:")
        while (cursorTables.moveToNext()) {
            Log.d("BD_DEBUG", cursorTables.getString(0))
        }
        cursorTables.close()

        val cursorProducts = db.rawQuery("SELECT * FROM products", null)
        Log.d("BD_DEBUG", "Número de productos: ${cursorProducts.count}")
        cursorProducts.close()
    }

    override fun onDestroyView() {
        database.close()
        super.onDestroyView()
    }
}