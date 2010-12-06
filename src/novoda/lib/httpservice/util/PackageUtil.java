
package novoda.lib.httpservice.util;

import android.content.Context;

public class PackageUtil {
    private static volatile PackageUtil instance = null;
    private Context context;
    private String packageName;

    public PackageUtil(Context context) {
        this.context = context;
        packageName = context.getPackageName();
    }

    public static PackageUtil getInstance(final Context context) {
        if (instance == null) {
            synchronized (PackageUtil.class) {
                if (instance == null)
                    instance = new PackageUtil(context);
            }
        }
        return instance;
    }
    
    public int getString(String name) {
        return context.getResources().getIdentifier(name, "string", packageName);
    }
    
    public int getId(String name) {
        return context.getResources().getIdentifier(name, "id", packageName);
    }
}
