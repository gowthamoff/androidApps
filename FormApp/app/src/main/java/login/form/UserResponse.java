package login.form;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("username")
    private String username;

    @SerializedName("statusText")
    private String statusText;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
