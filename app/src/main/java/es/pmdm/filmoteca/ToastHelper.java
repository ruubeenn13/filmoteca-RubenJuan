package es.pmdm.filmoteca;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
public class ToastHelper {
    public static void showCustomToast(Context context, String message, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 150);
        toast.setDuration(duration);
        toast.setView(layout);

        toast.show();
    }

    public static void showCustomToast(Context context, String message) {
        showCustomToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showCustomToast(Context context, int messageResId) {
        showCustomToast(context, context.getString(messageResId), Toast.LENGTH_SHORT);
    }

    public static void showCustomToast(Context context, int messageResId, int duration) {
        showCustomToast(context, context.getString(messageResId), duration);
    }
}