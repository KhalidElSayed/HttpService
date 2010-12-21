package novoda.lib.httpservice.request;

import android.os.Handler;
import android.os.Parcel;
import android.os.ResultReceiver;

public class RequestReceiver extends ResultReceiver {
	
	private String contentClassSimpleName;
	
	public RequestReceiver(Handler handler) {
		super(handler);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("String builder");
		return sb.toString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(String.class.getSimpleName());
	}

	public void setContentClassSimpleName(String contentClassSimpleName) {
		this.contentClassSimpleName = contentClassSimpleName;
	}

	public String getContentClassSimpleName() {
		return contentClassSimpleName;
	}
}
