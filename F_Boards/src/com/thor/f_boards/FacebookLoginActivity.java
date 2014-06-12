package com.thor.f_boards;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;


import com.facebook.model.GraphUser;
import com.thor.entities.Account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class FacebookLoginActivity extends Activity {
	
	private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";
	private Button buttonLoginLogout;
	private TextView textInstructionsOrLink;
	private GraphUser user;
	public Account account;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, com.facebook.SessionState state,
				Exception exception) {
				
			  updateView();
		}
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		account = new Account();

		buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
	}
	
	@Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
            buttonLoginLogout.setText("Logout");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });
        } else {
            textInstructionsOrLink.setText("instructions");
            buttonLoginLogout.setText("Login");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }
    
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            
        	
        	
        	Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
        		
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        // Display the parsed user info
                        buildUserInfoDisplay(user);
                    }
                }
            });
        	
        } else if (state.isClosed()) {
        }
    }
    
    private String buildUserInfoDisplay(GraphUser user) {
        StringBuilder userInfo = new StringBuilder("");
       
        account.setName( user.getName());
       
        userInfo.append(String.format("Birthday: %s\n\n", 
            user.getBirthday()));
       
        userInfo.append(String.format("Birthday: %s\n\n", 
                user.getUsername()));

        return userInfo.toString();
    }
    
    
}
