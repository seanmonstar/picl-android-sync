package org.mozilla.gecko.picl.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AccountAuthenticatorService extends Service {
	
	private static AccountAuthenticator accountAuthenticator;
	private static final Object LOCK = new Object();

	@Override
	public IBinder onBind(Intent intent) {
		IBinder ret = null;
		if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)) {
			ret = getAuthenticator().getIBinder();
		}
		return ret;
	}

	private AccountAuthenticator getAuthenticator() {
		synchronized (LOCK) {
			if (accountAuthenticator == null) {
		  		accountAuthenticator = new AccountAuthenticator(this);
			}
		}
  		return accountAuthenticator;
	}

}
