package com.example.tictactoe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.tictactoe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegistro;
    private ScrollView formLogin;
    private ProgressBar pbLogin;
    private FirebaseAuth firebaseAuth;
    private String email, password;
    boolean tryLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        formLogin = findViewById(R.id.formLogin);
        pbLogin = findViewById(R.id.progressBarLogin);
        btnRegistro = findViewById(R.id.buttonRegistro);

        firebaseAuth = FirebaseAuth.getInstance();

        changeLoginFormVisibility(true);
        eventos();
    }

    private void eventos() {
        btnLogin.setOnClickListener((v) -> {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();

            if (email.isEmpty()) {
                etEmail.setError("El email es obligatorio");
            } else if (password.isEmpty()) {
                etPassword.setError("La contraseña es necesaria");
            } else {
                //TODO: realizar login en Firebese Auth
                changeLoginFormVisibility(false);
                loginUser();
            }
        });

        btnRegistro.setOnClickListener((v) -> {
            Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(i);

        });
    }

    private void loginUser() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        tryLogin = true;
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("TAG", "SignInError", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void changeLoginFormVisibility(boolean showForm) {
        pbLogin.setVisibility(showForm ? View.GONE : View.VISIBLE);
        formLogin.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            //Almacenar la informacion del usario en FireStore
            //TODO


            //Navegar hacia la siguiente pantalla de la aplicación
            Intent i = new Intent(LoginActivity.this, FindGamesActivity.class);
            startActivity(i);

        } else {
            changeLoginFormVisibility(true);
            if (tryLogin) {
                etPassword.setError("Email y/o contraseña incorrecta");
                etPassword.requestFocus();
            }
        }
    }


        @Override
        protected void onStart () {
            super.onStart();

            //comprobamos si previamente el usuario ya ha iniciado
            //sesión en este dispositivo
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }


