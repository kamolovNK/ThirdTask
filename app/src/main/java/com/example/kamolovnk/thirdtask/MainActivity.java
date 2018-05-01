package com.example.kamolovnk.thirdtask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://rawgit.com/startandroid/data/master/messages/";
    private int listSize;
    private List<Message> messageList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MessageApi messageApi = retrofit.create(MessageApi.class);

        Call<List<Message>> messages = messageApi.messages();

        final ListView listView = (ListView)findViewById(R.id.listView);

        final CustomAdapter customAdapter = new CustomAdapter();



        messages.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messageList = response.body();
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: response " + + response.body().size());
                    listSize = response.body().size();

                    listView.setAdapter(customAdapter);


                    for(int i = 0; i < messageList.size(); i ++){
                        Log.d(TAG, "onResponse: \n" +
                        "id " + messageList.get(i).getId() + "\n" +
                                "time " + messageList.get(i).getTime() + "\n" +
                                "text " + messageList.get(i).getText() + "\n"

                        );
                    }


                }else{
                    Log.d(TAG, "onResponse: response error " + + response.code());
                }
                toastMessage("Response " + response.body().size());








            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                toastMessage("Something went wrong!!! :( ");
            }
        });



    }

    public void toastMessage(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listSize;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_layout, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            TextView textViewId = (TextView) convertView.findViewById(R.id.tVId);
            TextView textViewTime = (TextView) convertView.findViewById(R.id.tVTime);
            TextView textViewText = (TextView) convertView.findViewById(R.id.tVText);

            imageView.setImageResource(R.drawable.ic_launcher_background);
            textViewText.setText(messageList.get(position).getText());
            textViewId.setText(messageList.get(position).getId());
            textViewTime.setText(messageList.get(position).getTime());

            return convertView;
        }
    }
}
