package com.corp.maveri.pchell.maveri.CreateAccount;

import android.support.v7.app.*;
import View.*;

public class ActivityCreateAccount extends AppCompatActivity implements OnClickListener {

	private com.google.firebase.database.FirebaseDatabase database = FirebaseDatabase.getInstance();
	private com.google.firebase.database.DatabaseReference databaseReference = database.getReference("Users");
	private com.google.firebase.auth.FirebaseAuth mAuth;
	private android.widget.EditText etEmail;
	private android.widget.EditText etPassword;

	/**
	 * 
	 * @param savedInstanceState
	 * @return 
	 */
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param view
	 * @return 
	 */
	@Override
	public void onClick(android.view.View view) {
		throw new UnsupportedOperationException();
	}

	/**
	 * -----Реги�?траци�? в базе данных нового пользовател�?
	 * @param email
	 * @param password
	 * @return 
	 */
	public void registration(final String email, String password) {
		throw new UnsupportedOperationException();
	}

}