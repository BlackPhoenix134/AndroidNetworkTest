package se.concurrent;

import androidx.appcompat.app.AppCompatActivity;

import se.app.MainActivity;

//ToDo: better way for this
public class Dispatcher {
    AppCompatActivity activity;

    public Dispatcher(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Thread dispatch(Runnable runnable) {
        return dispatch(new Thread(runnable));
    }

    public Thread dispatch(Thread thread) {
        thread.start();
        return thread;
    }

    public void onUi(Runnable runnable) {
        activity.runOnUiThread(runnable);
    }

}
