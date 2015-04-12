package colorincolorado.ernestoascencio.com.colorincolorado;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class cc_main extends ActionBarActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView img;
    private Bitmap bmp;
    private RelativeLayout rl_noContent;
    private LinearLayout rl_Content;
    private ColorUtil colorUtil;

    private TextView lblColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cc_main);

        //setting widgets
        Button mbtn_noContent = (Button) findViewById(R.id.cc_main_ly_noContentButton);
        ImageButton retryCamera = (ImageButton) findViewById(R.id.retryCamera);

        img = (ImageView) findViewById(R.id.cc_main_ly_skyImageView);
        rl_noContent = (RelativeLayout) findViewById(R.id.rl_noContent);
        rl_Content =  (LinearLayout) findViewById(R.id.rl_content);
        lblColor = (TextView) findViewById(R.id.cc_main_ly_tvColor);

        mbtn_noContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera,CAMERA_REQUEST);
            }
        });

        retryCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera,CAMERA_REQUEST);
            }
        });
    }
    //handle response of activity result.
    public void onActivityResult(int requestCode,int resultCode,Intent d){
        int bitmapRGBPredominant = 0;
        if (resultCode == RESULT_OK && requestCode  == CAMERA_REQUEST){
            this.bmp = (Bitmap)d.getExtras().get("data");
            img.setImageBitmap(this.bmp);
            bitmapRGBPredominant = buidPalette(this.bmp);
            //hide Layouts.
            rl_noContent.setVisibility(View.GONE);
            rl_Content.setVisibility(View.VISIBLE);
            //calculate colors

            int deepBlueColor = colorUtil.argb(61,121,188);
            int blueColor = colorUtil.argb(99,135,194);
            int ligthBlueColor = colorUtil.argb(120,149,202);
            int paleBlueColor = colorUtil.argb(170,184,216);
            int milkyColor = colorUtil.argb(170,188,222);

            List<Integer> colors = new ArrayList<Integer>();

            colors.add(0,deepBlueColor);
            colors.add(1,blueColor);
            colors.add(2,ligthBlueColor);
            colors.add(3,paleBlueColor);
            colors.add(4,milkyColor);

            double percentage = 100d;
            int position = 0;

            for (Integer item: colors){
                Log.i("cc", Double.toString(colorUtil.getColorDifference(bitmapRGBPredominant, item)));
                Log.i("cc-percentage", Double.toString(percentage));
                if (percentage >=  colorUtil.getColorDifference(bitmapRGBPredominant,item) ){
                    percentage = colorUtil.getColorDifference(bitmapRGBPredominant,item);
                    position = colors.indexOf(item);
                    Log.i("cc-position", Integer.toString(position));
                }
            }
            String colorName = "";
            switch (position){
                case 0: colorName  = "Deep Blue";
                    break;
                case 1: colorName  = "Blue";
                    break;
                case 2: colorName  = "Light Blue";
                    break;
                case 3: colorName  = "Pale Blue";
                    break;
                case 4: colorName  = "Milky";
                    break;
                default: colorName = "No color";
                    break;
            }

            Toast.makeText(getApplicationContext(),colorName,Toast.LENGTH_SHORT).show();

        }
    }

    public int buidPalette(Bitmap bitmap){

        int higherPopulation= 0;

        Palette palette = Palette.generate(bitmap);
        Palette.Swatch swatch = null;

        for (int i = 0;i<=palette.getSwatches().size()-1;i++){
            if (higherPopulation <= palette.getSwatches().get(i).getPopulation()){
                swatch = palette.getSwatches().get(i);
            }
        }


        lblColor.setBackgroundColor(swatch.getRgb());

        return swatch.getRgb();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_cc_main, menu);
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
}
