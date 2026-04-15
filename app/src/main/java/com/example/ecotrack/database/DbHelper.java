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

public class
DbHelper extends SQLiteOpenHelper {

    // Configurações do Banco de Dados
    private static final String DATABASE_NAME = "ecotrack.db";
    private static final int DATABASE_VERSION = 1;

    // Nome da Tabela e Colunas
    private static final String TABLE_RECURSOS = "recursos";
    private static final String COL_ID = "id";
    private static final String COL_NOME = "nome";
    private static final String COL_DATA = "data";
    private static final String COL_TIPO = "tipo"; // "agua" ou "energia"
    private static final String COL_VALOR = "valor";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criando a tabela (SQL)
        String createTable = "CREATE TABLE " + TABLE_RECURSOS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME + " TEXT, " +
                COL_DATA + " TEXT, " +
                COL_TIPO + " TEXT, " +
                COL_VALOR + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECURSOS);
        onCreate(db);
    }

    // --- OPERAÇÕES CRUD ---

    // 1. CREATE (Salvar)
    public boolean salvarRecurso(String nome, String data, String tipo, double valor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME, nome);
        values.put(COL_DATA, data);
        values.put(COL_TIPO, tipo);
        values.put(COL_VALOR, valor);

        long result = db.insert(TABLE_RECURSOS, null, values);
        return result != -1; // Retorna true se salvou corretamente
    }

    // 2. READ (Listar todos)
    public List<Recurso> listarTodos() {
        List<Recurso> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECURSOS, null);

        if (cursor.moveToFirst()) {
            do {
                String tipo = cursor.getString(3);
                Recurso recurso;

                // Aqui aplicamos a lógica para decidir qual classe filha criar
                if (tipo.equals("agua")) {
                    Agua a = new Agua();
                    a.setMetrosCubicos(cursor.getDouble(4));
                    recurso = a;
                } else {
                    Energia e = new Energia();
                    e.setKwh(cursor.getDouble(4));
                    recurso = e;
                }

                recurso.setId(cursor.getInt(0));
                recurso.setNome(cursor.getString(1));
                recurso.setData(cursor.getString(2));
                lista.add(recurso);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // 3. DELETE (Excluir)
    public void deletarRecurso(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // O delete recebe: nome da tabela, cláusula WHERE e o valor do parâmetro
        db.delete("recursos", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}