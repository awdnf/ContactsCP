package br.edu.ifspsaocarlos.agenda.activity;

import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.adapter.ContatoArrayAdapter;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.provider.ContactProvider;

public class BaseActivity extends AppCompatActivity {

    public ListView list;
    public ContatoArrayAdapter adapter;
    protected SearchView searchView;
    private Uri contactsUri = ContactProvider.Contacts.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2,
                                    long arg3) {
                Contato contact = (Contato) adapterView.getAdapter().getItem(arg2);
                Intent inte = new Intent(getApplicationContext(), DetalheActivity.class);
                inte.putExtra("contact", contact);
                startActivityForResult(inte, 0);

            }

        });


        registerForContextMenu(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ContatoArrayAdapter adapter = (ContatoArrayAdapter) list.getAdapter();
        Contato contact = adapter.getItem(info.position);


        switch (item.getItemId()) {
            case R.id.delete_item:
                getContentResolver().delete(ContentUris.withAppendedId(contactsUri, contact.getId()), null, null);
                Toast.makeText(getApplicationContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
                buildListView();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    protected void buildListView() {
        List<Contato> contatos = new ArrayList<>();
        Cursor cursor = getContentResolver().query(contactsUri, null, null, null, null);
        if (cursor != null) {
            contatos = cursorTolist(cursor);
        }
        adapter = new ContatoArrayAdapter(this, contatos);
        list.setAdapter(adapter);

    }

    protected void buildSearchListView(String query) {
        List<Contato> contatos = new ArrayList<>();
        String where = ContactProvider.Contacts.KEY_NAME + " like ?";
        String[] whereargs = new String[]{query + "%"};
        Cursor cursor = getContentResolver().query(contactsUri, null, where, whereargs, null, null);
        if (cursor != null) {
            contatos = cursorTolist(cursor);
        }
        adapter = new ContatoArrayAdapter(this, contatos);
        list.setAdapter(adapter);

    }

    public List<Contato> cursorTolist(Cursor cursor) {
        List<Contato> contatos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFone2(cursor.getString(4));
            contato.setAniversario(cursor.getString(5));
            contatos.add(contato);

        }
        return contatos;
    }

}
