package com.example.ecotrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ecotrack.model.Agua;
import com.example.ecotrack.model.Energia;
import com.example.ecotrack.model.Recurso;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ecotrack.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela principal
        String sql = "CREATE TABLE recursos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "data TEXT, " +
                "valor REAL, " +
                "tipo TEXT)"; // Aqui salvamos se é 'Energia' ou 'Água'
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recursos");
        onCreate(db);
    }

    // --- O MÉTODO QUE ESTAVA FALTANDO ---
    public long inserir(Recurso recurso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", recurso.getNome());
        values.put("data", recurso.getData());
        values.put("valor", recurso.getValor());

        // Identifica o tipo para salvar no banco
        String tipo = (recurso instanceof Energia) ? "Energia" : "Água";
        values.put("tipo", tipo);

        return db.insert("recursos", null, values);
    }

    public List<Recurso> listarTodos() {
        List<Recurso> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Garantimos que estamos pegando as colunas certas
        Cursor cursor = db.rawQuery("SELECT id, nome, data, valor, tipo FROM recursos", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nome = cursor.getString(1);
                String data = cursor.getString(2);
                double valor = cursor.getDouble(3);
                String tipo = cursor.getString(4);

                // Criamos o objeto específico baseado no tipo salvo
                if ("Energia".equalsIgnoreCase(tipo)) {
                    lista.add(new Energia(nome, data, valor));
                } else {
                    lista.add(new Agua(nome, data, valor));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return lista;
    }
}