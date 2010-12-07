
package novoda.lib.httpservice.auth;

import novoda.lib.httpservice.util.PackageUtil;
import android.content.Context;
import android.content.Intent;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginPreference extends DialogPreference {

    private EditText email;

    private EditText password;

    private OnLoginDismissed callback;

    private Intent registerIntent;

    private Button register;

    private PackageUtil pu;
    

    public LoginPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        pu = PackageUtil.getInstance(context);
    }

    public LoginPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnLoginDismissed(OnLoginDismissed callback) {
        this.callback = callback;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            callback.onSignIn(email.getText().toString().trim(), password.getText().toString()
                    .trim());
        } else {
            callback.onCancel();
        }
        super.onDialogClosed(positiveResult);
    }

    @Override
    protected void onBindDialogView(View view) {
        email = (EditText)view.findViewById(pu.getId("email"));
        password = (EditText)view.findViewById(pu.getId("password"));
        register = (Button)view.findViewById(pu.getId("create_new"));
        register.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                if (registerIntent != null)
                    getContext().startActivity(registerIntent);
            }});
        super.onBindDialogView(view);
    }

    public interface OnLoginDismissed {
        void onSignIn(String username, String password);
        void onCancel();
    }

    public void setOnRegisterClick(Intent intent) {
        this.registerIntent = intent;
    }
}
