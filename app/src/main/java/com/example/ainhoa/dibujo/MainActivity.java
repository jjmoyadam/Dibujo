package com.example.ainhoa.dibujo;

import android.app.Activity;
import android.content.ContentResolver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//para los colores
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    private DrawingView drawView;
    private ImageButton currPaint,drawBtn,newBtn,saveBtn,eraseBt;

    //medidas del pincel
    private float smallBrush, mediumBrush, largeBrush;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView) findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors2);

        //pinceles
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //referencia al boton dibujar
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //referencia al boton nuevo
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //referencia al boton guardar
        saveBtn=(ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        //referencia al bonto limpiar
        eraseBt=(ImageButton)findViewById(R.id.erase_btn);
        eraseBt.setOnClickListener(this);

        //escogemos el valor
        currPaint = (ImageButton) paintLayout.getChildAt(5);

        //y cambiamos el boton de creacion con
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));


    }

        //metodo paint clicke
        public void paintClicked(View view){

            if(view!=currPaint){

                ImageButton imgView = (ImageButton)view;
                String color = view.getTag().toString();

                drawView.setColor(color);
                //e implementamos para que el boton vuelva a la normalidad
                imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
                currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
                currPaint=(ImageButton)view;
            }

        }
        //on click  para dibujar

    @Override
    public void onClick(View v) {
        //modificado a v
        if(v.getId()==R.id.draw_btn){
            //para elegir size de pincel
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Tamaño Brocha:");
            //creacion del menu de pincel
            brushDialog.setContentView(R.layout.brush_chooser);
            //completamos el dialogo con
            brushDialog.show();

            //on click del menu de pincel
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });


        }else if(v.getId()==R.id.new_btn){
            //dialogo de nuevo botn para dibujar
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("Nuevo Dibujo");
            newDialog.setMessage("¿Empezamos de Nuevo?");
            newDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();

        }else if(v.getId()==R.id.save_btn){
            //guardar el dibujo
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Guardar Dibujo");
            saveDialog.setMessage("Guardar dibujo");
            saveDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //Guardamos el dibujo
                    drawView.setDrawingCacheEnabled(true);


                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),"foto.png","dibujo");

                    //definimos si se guarda o no la imagen
                    if(imgSaved!=null){
                       Toast.makeText(getApplicationContext(),
                                "Guardamos la imagen!", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "La imagen no se ha guardado", Toast.LENGTH_SHORT).show();

                    }
                    //y limpiamos el cache para evitar reescribir
                    drawView.destroyDrawingCache();


                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();

        }else if(v.getId()==R.id.erase_btn){
            Toast.makeText(getApplicationContext(),"Vamos a Encalar", Toast.LENGTH_SHORT).show();
            //limpiamos la vista
            drawView.startNew();

        }
    }
}

