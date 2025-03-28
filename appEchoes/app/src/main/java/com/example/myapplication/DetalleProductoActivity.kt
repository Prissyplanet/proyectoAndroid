package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDetalleProductoBinding

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val producto = intent.getParcelableExtra<Producto>("producto")

        if (producto != null) {

            binding.textViewNombreDetalle.text = producto.nombre
            binding.textViewPrecioDetalle.text = "$${producto.precio}"
            binding.textViewDescripcionDetalle.text = producto.descripcion


            Glide.with(this)
                .load(producto.imagenUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.imageViewDetalle)
        }
    }
}