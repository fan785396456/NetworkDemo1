package zf.com.networkdemo1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2;
    Button bt1,bt2,bt3;
    String param1,param2,result,result1;
    Handler handler;
    final int NOTIFYID_1 = 888;
    final int NOTIFYID_2 = 889;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.uname);
        ed2=(EditText)findViewById(R.id.upwd);
        bt1=(Button)findViewById(R.id.login);
        bt2=(Button)findViewById(R.id.regist);
        bt3=(Button)findViewById(R.id.demo1);
        final NotificationManager notificationManager=
                (NotificationManager)getSystemService(MainActivity.NOTIFICATION_SERVICE);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification.Builder builder1 = new Notification.Builder(MainActivity.this);
                builder1.setSmallIcon(R.mipmap.ic_launcher);     //设置图标
                builder1.setTicker("新消息");
                builder1.setWhen(System.currentTimeMillis()); //发送时间
                builder1.setContentTitle("推送消息1"); //设置标题
                builder1.setContentText("每天进步一点点就够了，切忌不思进取"); //消息内容
                builder1.setAutoCancel(true);
                //builder1.setOngoing(true);
                builder1.setDefaults(Notification.DEFAULT_ALL);
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                builder1.setContentIntent(pendingIntent);
                Notification notification1 = builder1.build();
                notificationManager.notify(NOTIFYID_1, notification1);// 通过通知管理器发送通知
               // builder1.setSmallIcon(R.mipmap.ic_launcher);
                Log.d("TAG", "onClick: 消息推送");

            }
        });
//        Thread2 th2=new Thread2();
//        th2.start();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x00){
                    if(result.equals("登录成功")){
                        Intent intent1=new Intent(MainActivity.this,Main2Activity.class);
                        startActivity(intent1);
                    }
                    else {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what==0x01){
                    if (result1.equals("messge")){
                        Notification.Builder builder1 = new Notification.Builder(MainActivity.this);
                        builder1.setSmallIcon(R.mipmap.ic_launcher);     //设置图标
                        builder1.setTicker("新消息");
                        builder1.setWhen(System.currentTimeMillis()); //发送时间
                        builder1.setContentTitle("推送消息1"); //设置标题
                        builder1.setContentText("每天进步一点点就够了，切忌不思进取"); //消息内容
                        builder1.setAutoCancel(true);
                        //builder1.setOngoing(true);
                        builder1.setDefaults(Notification.DEFAULT_ALL);
                        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                        PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                        builder1.setContentIntent(pendingIntent);
                        Notification notification1 = builder1.build();
                        notificationManager.notify(NOTIFYID_1, notification1);// 通过通知管理器发送通知
                        // builder1.setSmallIcon(R.mipmap.ic_launcher);
                        Log.d("TAG", "onClick: 消息推送");
                    }
                }
            }
        };
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread1 t=new Thread1();
                t.start();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Ab = new AlertDialog.Builder(MainActivity.this);
                Ab.setTitle("注册");
                Ab.setMessage("这是注册界面");
                Ab.setNegativeButton("确定",null);
                Ab.setPositiveButton("取消",null);
                Ab.create();
                Ab.show();
            }
        });
    }
    class Thread1 extends Thread{

        @Override
        public void run() {
            super.run();
            String target="http://119.23.181.38:8080/FirstProject/Demo1";
            Log.d("TAG", "线程开启");
            try {
                URL url=new URL(target);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.connect();
                //写参数
                param1= URLEncoder.encode(ed1.getText().toString(),"utf-8");
                param2=URLEncoder.encode(ed2.getText().toString(),"utf-8");
                String param="uname="+param1+"&upwd="+param2;
                //创建输出流
                DataOutputStream dos=new DataOutputStream(urlConnection.getOutputStream());
                dos.write(param.getBytes());
                dos.flush();
                dos.close();
                //   InputStream inStrm = urlConnection.getInputStream();
                Log.d("TAG", "参数写入成功");
                //读响应
                if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    Log.d("TAG", "0响应成功");
                    //创建输入流和缓冲流
                    InputStreamReader reader=new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader br=new BufferedReader(reader);
                    String line;
                    //读取从服务端传入的数据
                    while ((line=br.readLine())!=null){
                        result=line;
                    }
                    br.close();
                    //br.reset();
                    reader.close();
                    urlConnection.disconnect();
                    Log.d("TAG", "1响应成功");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            Message message=handler.obtainMessage();
            message.what=0x00;
            handler.sendMessage(message);

        }
    }
    class  Thread2 extends Thread{
        @Override
        public void run() {
            super.run();
            String target="http://119.23.181.38:8080/FirstProject/Demo1";
            Log.d("TAG", "线程开启");
            try {
                URL url=new URL(target);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.connect();
                //写参数
//                param1= URLEncoder.encode(ed1.getText().toString(),"utf-8");
//                param2=URLEncoder.encode(ed2.getText().toString(),"utf-8");
//                String param="uname="+param1+"&upwd="+param2;
//                //创建输出流
//                DataOutputStream dos=new DataOutputStream(urlConnection.getOutputStream());
//                dos.write(param.getBytes());
//                dos.flush();
//                dos.close();
                //   InputStream inStrm = urlConnection.getInputStream();
                Log.d("TAG", "参数写入成功");
                //读响应
                if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    Log.d("TAG", "0响应成功");
                    //创建输入流和缓冲流
                    InputStreamReader reader=new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader br=new BufferedReader(reader);
                    String line;
                    //读取从服务端传入的数据
                    while ((line=br.readLine())!=null){
                        result1=line;
                    }
                    br.close();
                    //br.reset();
                    reader.close();
                    urlConnection.disconnect();
                    Log.d("TAG", "1响应成功");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            Message message=handler.obtainMessage();
            message.what=0x01;
            handler.sendMessage(message);

        }
    }
}
