package com.example.kafkatech.clonewhatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.kafkatech.clonewhatsapp.R;
import com.example.kafkatech.clonewhatsapp.config.ConfiguraFirebase;
import com.example.kafkatech.clonewhatsapp.fragment.ContatosFragment;
import com.example.kafkatech.clonewhatsapp.fragment.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = ConfiguraFirebase.getFirebaseAuth();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp Fake");
        setSupportActionBar(toolbar);

        //Configura abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
              getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversasFragment.class)
                        .add("Contatos", ContatosFragment.class)
                        .create()
        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuSair){
            deslogarUser();
            finish();
        }

        if(item.getItemId() == R.id.menuConfiguracoes){
            abrirConfiguracao();
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracao(){
        startActivity(new Intent(MainActivity.this, ConfiguracaoActivity.class));
        finish();
    }

    public void deslogarUser(){
        try {
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}