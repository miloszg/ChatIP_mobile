package pl.milosz.chatip;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    EditText messageEditText;
    private static PrintWriter writer;
    String message = "";
    LinearLayout messageLayout;
    ArrayList<Message> messageArrayList;
    private static String ip = "192.168.0.52";

    private static ServerSocket serverSocket;
    private static Socket socketReceived;
    private static Socket socketSend;
    private static BufferedReader reader;
    private static InputStreamReader inputStreamReader;
    private static String messageReceived ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageArrayList = new ArrayList<>();
        messageEditText = findViewById(R.id.messageEditText);
        messageLayout = findViewById(R.id.messageLayout);
        ImageView buttonSend = findViewById(R.id.sendButton);
        buttonSend.setOnClickListener(v -> sendMessage(v));


        Thread thread = new Thread(() -> {
            try {
                Log.i("info","running");
                while (true) {
                    serverSocket = new ServerSocket(6000);
                    socketReceived = serverSocket.accept();
                    inputStreamReader = new InputStreamReader(socketReceived.getInputStream());
                    reader = new BufferedReader(inputStreamReader);
                    messageReceived = reader.readLine();
                    Log.i("info", messageReceived);
                    runOnUiThread(() -> addMessageView(messageReceived,false));
                    inputStreamReader.close();
                    reader.close();
                    socketReceived.close();
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void sendMessage(View view) {
        if(messageEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Prosze wpisać tekst wiadomości!", Toast.LENGTH_SHORT).show();
        } else {
            message = messageEditText.getText().toString();
            addMessageView(message,true);
            SendMessageTask sendTask = new SendMessageTask();
            sendTask.execute();
            Log.i(TAG, "message sent");
            messageEditText.setText("");
        }
    }

    public void addMessageView(String messageV,boolean userFlag) {
        int value10dp=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10.0f,this.getResources().getDisplayMetrics());
        int value5dp=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5.0f,this.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lpRIGHT=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lpRIGHT.gravity= Gravity.RIGHT;
        lpRIGHT.setMargins(value5dp,value5dp,value5dp,value5dp);
        LinearLayout.LayoutParams lpLEFT=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lpLEFT.gravity= Gravity.LEFT;
        lpLEFT.setMargins(value5dp,value5dp,value5dp,value5dp);
        TextView messageTextView = new TextView(this);
        if(userFlag){
            messageTextView.setLayoutParams(lpLEFT);
            messageTextView.setBackgroundResource(R.drawable.roundshape_green);
        } else {
            messageTextView.setLayoutParams(lpRIGHT);
            messageTextView.setBackgroundResource(R.drawable.roundshape);
        }
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        messageTextView.setText(messageV);
        messageTextView.setTextColor(Color.BLACK);
        messageTextView.setPadding(value10dp,value10dp,value10dp,value10dp);
        messageLayout.addView(messageTextView);


        Calendar c = Calendar.getInstance();
        Message m = new Message(message, true, c);
        messageArrayList.add(m);

    }

    class SendMessageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socketSend = new Socket(ip, 4200);
                writer = new PrintWriter(socketSend.getOutputStream());
                writer.write(message);
                writer.flush();
                writer.close();
                socketSend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
