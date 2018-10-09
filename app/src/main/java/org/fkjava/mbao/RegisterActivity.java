package org.fkjava.mbao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void showLoginActivity(View view) {
        Intent loginIntent = new Intent(this
                , LoginActivity.class);
        super.startActivity(loginIntent);
        super.finish();
    }
}
