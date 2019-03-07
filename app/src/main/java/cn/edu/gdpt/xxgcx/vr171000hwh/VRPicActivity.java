package cn.edu.gdpt.xxgcx.vr171000hwh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.InputStream;

public class VRPicActivity extends AppCompatActivity {

    private VrPanoramaView mVrMainPic;
    AsyncTask<Void,Void,Bitmap> task;//声明异步任务对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrpic);
        initView();
        task=new LoadPicAsyncTask();//创建异步任务对象
        task.execute();//执行异步任务
    }
    //定义异步任务内容
    private class LoadPicAsyncTask extends AsyncTask<Void,Void,Bitmap>{
        //异步处理的准备工作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"开始加载图片",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {//异步处理的主要内容：读图片
            try{
                InputStream is=getAssets().open("andes.jpg");
                Bitmap bitmap=BitmapFactory.decodeStream(is);
                is.close();
                return bitmap;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {//执行完主要的异步内容后调用
            super.onPostExecute(bitmap);
            Toast.makeText(getApplicationContext(),"已经加载完图片",Toast.LENGTH_LONG).show();
            if(bitmap!=null){
                VrPanoramaView.Options options=new VrPanoramaView.Options();
                options.inputType=VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
                mVrMainPic.loadImageFromBitmap(bitmap,options);
            }

        }
    }

    private void initView() {
        mVrMainPic = (VrPanoramaView) findViewById(R.id.vr_main_pic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVrMainPic.resumeRendering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVrMainPic.pauseRendering();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVrMainPic.shutdown();
        if(task!=null){
            task.cancel(true);
            task=null;
        }
    }
}
