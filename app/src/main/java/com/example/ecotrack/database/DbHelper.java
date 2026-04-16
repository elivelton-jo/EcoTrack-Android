package com.example.ecotrack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ecotrack.model.*;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    // Configurações do Banco de Dados
    private static final String NOME_BANCO = "ecotrack.db";
    private static final int VERSAO = 1;

    public DbHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela para armazenar os recursos cadastrados
        db.execSQL("CREATE TABLE recursos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, data TEXT, valor REAL, tipo TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lógica para atualização de versão do banco, se necessário
    }

    // Método para salvar um recurso (Água ou Energia) no banco
    public void inserir(Recurso r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", r.getNome());
        cv.put("data", r.getData());
        cv.put("valor", r.getValor());
        // Identifica o tipo do objeto para salvar no banco (Polimorfismo)
        cv.put("tipo", r instanceof Agua ? "AGUA" : "ENERGIA");
        db.insert("recursos", null, cv);
    }

    // Método para recuperar todos os registros do banco e transformar em objetos Java
    public List<Recurso> listarTodos() {
        List<Recurso> lista = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM recursos", null);

        while (cursor.moveToNext()) {
            String tipo = cursor.getString(4);
            Recurso r;
            // Instanciação dinâmica baseada no tipo salvo (Fábrica de objetos)
            if (tipo.equals("AGUA")) {
                r = new Agua(cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            } else {
                r = new Energia(cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            }
            r.setId(cursor.getInt(0));
            lista.add(r);

        }
        cursor.close();
        return lista;
    }
    /**
     * Método para deletar um registro do banco de dados.
     * @param id O identificador único do recurso que será removido.
     */
    public void deletar(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // O comando delete pede: (nome da tabela, cláusula WHERE, argumentos do WHERE)
        db.delete("recursos", "id = ?", new String[]{String.valueOf(id)});
        db.close(); // Boa prática: fechar a conexão após a operação
    }
}