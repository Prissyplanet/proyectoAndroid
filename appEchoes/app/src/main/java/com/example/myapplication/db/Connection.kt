package com.example.myapplication.db
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Connection(context: Context) : SQLiteOpenHelper(context, "echoes_db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                email TEXT UNIQUE,
                password TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                image_url TEXT
            )
        """)


        db.execSQL("""
            CREATE TABLE products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                description TEXT,
                price REAL,
                image_url TEXT,
                affiliate_link TEXT,
                category_id INTEGER,
                brand_id INTEGER,
                FOREIGN KEY (category_id) REFERENCES categories(id)
            )
        """)
        Log.d("BD_DEBUG", "Tabla products creada correctamente")

        db.execSQL("""
            CREATE TABLE favorites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                product_id INTEGER,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (product_id) REFERENCES products(id),
                UNIQUE(user_id, product_id)
            )
        """)
        db.execSQL("""
            CREATE TABLE reviews (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                product_id INTEGER,
                rating INTEGER,
                review TEXT,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (product_id) REFERENCES products(id)
            )
        """)
        try{
        db.execSQL("INSERT INTO users(id, email, password) VALUES (1, 'test@echoes.com', '123456')")
        db.execSQL("INSERT INTO categories (name) VALUES ('Ropa'), ('Música')")
        db.execSQL("""INSERT INTO products (name, price, image_url, description, category_id) 
        VALUES 
            ('Camiseta Vintage', 29.99, 'https://www.flannelgo.com/cdn/shop/files/xr810-vintage-corduroy-jacket-front-VB.webp?v=1725259883&width=640', 'Camiseta estilo años 80', 1),
            ('Vinilo Rare', 45.50, 'https://i.etsystatic.com/41704972/r/il/07ecba/4740019213/il_fullxfull.4740019213_a7aq.jpg', 'Edición limitada 1975', 2)
        """)
        Log.d("BD_DEBUG", "Productos insertados correctamente")
        db.execSQL("INSERT INTO favorites (user_id, product_id) VALUES (1, 1), (1, 2)")

        }catch (e: Exception){
           Log.e("BD_ERROR", "Error al insertar datos: ${e.message}")
        }


    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS products")
        db.execSQL("DROP TABLE IF EXISTS favorites")
        db.execSQL("DROP TABLE IF EXISTS reviews")
        onCreate(db)
    }
    fun debugUsers(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)
        val users = StringBuilder("Usuarios en BD:\n")
        with(cursor) {
            while (moveToNext()) {
                users.append("ID: ${getInt(0)}, Email: ${getString(1)}\n")
            }
            close()
        }
        return users.toString()
    }
    fun debugProducts(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products", null)
        return buildString {
            append("Productos en BD:\n")
            while (cursor.moveToNext()) {
                append("ID: ${cursor.getInt(0)}, Nombre: ${cursor.getString(1)}\n")
            }
            cursor.close()
        }
    }
    fun addFavorito(userId: Int, productId: Int):Boolean{
        return try{
            writableDatabase.execSQL(
                "INSERT OR IGNORE INTO favorites (user_id, product_id) VALUES (?, ?)",
                arrayOf(userId, productId)
            )
            true
        }catch (e: Exception){
            Log.e("BD_ERROR", "Error al insertar favorito: ${e.message}")
            false
        }
    }

    fun removeFavorito(userId: Int, productId: Int):Boolean{
        return try{
            writableDatabase.execSQL(
                "DELETE FROM favorites WHERE user_id = ? AND product_id = ?",
                arrayOf(userId, productId)
            )
            true
        }catch (e: Exception){
            Log.e("BD_ERROR", "Error al eliminar favorito: ${e.message}")
            false
        }
    }
    fun isFavorito(userId: Int, productId: Int): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM favorites WHERE user_id = ? AND product_id = ?",
            arrayOf(userId.toString(), productId.toString())
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //NOTA PARA MÍ MISMO, AGREGASTE ESTO PARA PODER CHECAR LOS DATOS SIEMPRE, EL ERROR QUE TENÍAS ERA LA VERSIÓN DE LA BD, ÚSALA CUANDO LA NECESITES
    fun checkDatabase(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM products", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return "Productos en BD: $count"
    }



}