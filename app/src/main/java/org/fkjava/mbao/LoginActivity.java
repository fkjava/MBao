package org.fkjava.mbao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void doLogin(View view){
        Intent registerIntent = new Intent(this, MainActivity.class);
        super.startActivity(registerIntent);
        // finish()把当前页面销毁
        this.finish();
    }


    public void showRegisterActivity(View view){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        super.startActivity(registerIntent);
        this.finish();
    }
}
