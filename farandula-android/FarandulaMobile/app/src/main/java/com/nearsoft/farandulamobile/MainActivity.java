package com.nearsoft.farandulamobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SabreFlightManager tripManager = new SabreFlightManager();
        Luisa.setSupplier(() -> tripManager);
    }
}
