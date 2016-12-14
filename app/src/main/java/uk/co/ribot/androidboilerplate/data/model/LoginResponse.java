package uk.co.ribot.androidboilerplate.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by raul on 10/12/2016.
 */
public class LoginResponse implements Parcelable {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    /**
     *
     * @return
     * The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     *
     * @param mensaje
     * The mensaje
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    protected LoginResponse(Parcel in) {
        user = (User) in.readValue(User.class.getClassLoader());
        byte successVal = in.readByte();
        success = successVal == 0x02 ? null : successVal != 0x00;
        mensaje = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user);
        if (success == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (success ? 0x01 : 0x00));
        }
        dest.writeString(mensaje);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoginResponse> CREATOR = new Parcelable.Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };
}