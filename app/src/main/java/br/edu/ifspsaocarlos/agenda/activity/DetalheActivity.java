package br.edu.ifspsaocarlos.agenda.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.Toast;

import br.edu.ifspsaocarlos.agenda.R;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.provider.ContactProvider;

public class DetalheActivity extends AppCompatActivity {

    private Contato c;
    private Uri contactsUri = ContactProvider.Contacts.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().hasExtra("contact")) {

            this.c = (Contato) getIntent().getSerializableExtra("contact");
            EditText nameText = (EditText) findViewById(R.id.editText1);
            nameText.setText(c.getNome());

            EditText foneText = (EditText) findViewById(R.id.editText2);
            foneText.setText(c.getFone());

            EditText emailText = (EditText) findViewById(R.id.editText3);
            emailText.setText(c.getEmail());

            EditText fone2Text = (EditText) findViewById(R.id.editFone2);
            fone2Text.setText(c.getFone2());

            EditText niverText = (EditText) findViewById(R.id.editTextNiver);
            niverText.setText(c.getAniversario());

            int pos = c.getNome().indexOf(" ");
            if (pos == -1)
                pos = c.getNome().length();

            setTitle(c.getNome().substring(0, pos));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("contact")) {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                getContentResolver().delete(ContentUris.withAppendedId(contactsUri, c.getId()), null, null);
                Toast.makeText(getApplicationContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void salvar() {
        String name = ((EditText) findViewById(R.id.editText1)).getText().toString();
        String fone = ((EditText) findViewById(R.id.editText2)).getText().toString();
        String email = ((EditText) findViewById(R.id.editText3)).getText().toString();
        String fone2 = ((EditText) findViewById(R.id.editFone2)).getText().toString();
        String aniversario = ((EditText) findViewById(R.id.editTextNiver)).getText().toString();

        ContentValues values = new ContentValues();
        values.put(ContactProvider.Contacts.KEY_NAME, name);
        values.put(ContactProvider.Contacts.KEY_EMAIL, email);
        values.put(ContactProvider.Contacts.KEY_FONE, fone);
        values.put(ContactProvider.Contacts.KEY_FONE2, fone2);
        values.put(ContactProvider.Contacts.KEY_NIVER, aniversario);

        if (c == null) {
            getContentResolver().insert(contactsUri, values);

            Toast.makeText(this, "Incluído com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            getContentResolver().update(ContentUris.withAppendedId(contactsUri, c.getId()), values, null, null);
            Toast.makeText(this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
        }

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }


}
