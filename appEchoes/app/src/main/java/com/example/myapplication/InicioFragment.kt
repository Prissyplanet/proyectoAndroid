package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.db.Connection

class InicioFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)


        recyclerView = view.findViewById(R.id.recyclerViewInicio)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        val productos = listOf(
            Producto("Producto 1", 99.99, "https://example.com/image1.jpg", "Descripción 1"),
            Producto("Producto 2", 149.99, "https://example.com/image2.jpg", "Descripción 2")
        )


        productoAdapter = ProductoAdapter(productos) { producto ->

            val intent = Intent(requireContext(), DetalleProductoActivity::class.java)
            intent.putExtra("producto", producto)
            startActivity(intent)
        }
        recyclerView.adapter = productoAdapter

        return view
    }
}

fun insertarDatos(context: Context, categories:ArrayList<Categoria>,products:ArrayList<Producto>){
    var conexion= Connection(context,"proyectoComercio", null, 1)
    //conexion.writableDatabase.execSQL("INSERT INTO categories(nombre, imagenUrl) values('Ropa','@drawable/echoes')")
    var db=conexion.writableDatabase
    for(x in categories){
        var registro=ContentValues()
        registro.put("nombre", x.nombre)
        registro.put("imagenUrl", x.imagenUrl)
        db.insert("categories", null, registro)
    }
    for(x in products){
        var registro=ContentValues()
        registro.put("nombre", x.nombre)
        registro.put("precio", x.precio.toString())
        registro.put("imagenUrl", x.imagenUrl)
        registro.put("descripcion", x.descripcion)
        db.insert("products", null, registro)
    }

    db.close()



}