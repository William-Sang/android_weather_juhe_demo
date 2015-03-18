package com.example.sang.sunshine.testapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAG = "ASYNC_TASK";
    private Context mContext;
    private MyTask mytask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String cityStr = editText.getText().toString();
        mytask = new MyTask();
        mytask.execute("http://op.juhe.cn/onebox/weather/query?key=1af0c976493ea69fcce462d3f5213e35&cityname=",cityStr);
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }
    private class MyTask extends AsyncTask<String, Integer, String>{
        Intent intent;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(mContext, DisplayMessageActivity.class);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // 创建DefaultHttpClient对象
                HttpClient httpclient = new DefaultHttpClient();
                // 创建一个HttpGet对象
                HttpGet get = new HttpGet(params[0]+params[1]);
                // 获取HttpResponse对象
                HttpResponse response = httpclient.execute(get);
                //判断是否链接成功
                if (response.getStatusLine().getStatusCode() == 200) {
                    //实体转换为字符串
                    return EntityUtils.toString(response.getEntity()) + params[1];
                }else{
                    return "网络错误";
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return "最后报错";
        }

        @Override
        protected void onPostExecute(String s) {
            intent.putExtra(EXTRA_MESSAGE, s);
            startActivity(intent);
        }
    }
}
