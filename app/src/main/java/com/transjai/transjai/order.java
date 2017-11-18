package com.transjai.transjai;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


/**
 * A simple {@link Fragment} subclass.
 */
public class order extends Fragment {

    private EditText name;
    private EditText detail;

    private Button btnminute_s;
    private Button btnminute_m;
    private Button btnminute_l;

    private TextView txt_s;
    private TextView txt_m;
    private TextView txt_l;


    private Button btnplus_s;
    private Button btnplus_m;
    private Button btnplus_l;

    private TextView total_s;
    private TextView total_m;
    private TextView total_l;

    private TextView total_box;
    private TextView total;

    private ImageView imageView47;

    private Button enter;
    private Button bin;


    private static final int GALLERY_REQUEST = 1;
    private Uri mImageUri = null;


    //********
    Button bfecha, bhora;
    EditText efecha, ehora;
    private int dia, mes, ano, hora, minutos;
    //********

    public order() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);


        name = (EditText) v.findViewById(R.id.editText3);
        detail = (EditText) v.findViewById(R.id.editText8);


        enter = (Button) v.findViewById(R.id.btn_enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), order2.class);
                intent.putExtra("name", name.getText().toString());


                intent.putExtra("txt_s", txt_s.getText().toString());
                intent.putExtra("txt_m", txt_m.getText().toString());
                intent.putExtra("txt_l", txt_l.getText().toString());

                intent.putExtra("total_s", total_s.getText().toString());
                intent.putExtra("total_m", total_m.getText().toString());
                intent.putExtra("total_l", total_l.getText().toString());

                intent.putExtra("total_box", total_box.getText().toString());
                intent.putExtra("total", total.getText().toString());

                if (mImageUri != null) {
                    intent.putExtra("type", mImageUri.toString());
                }
                intent.putExtra("detail", detail.getText().toString());

                intent.putExtra("time", bhora.getText().toString());
                intent.putExtra("day", bfecha.getText().toString());

                //Toast.makeText(getActivity(), name.getText().toString(), Toast.LENGTH_LONG).show();


                startActivity(intent);

            }
        });


        bfecha = (Button) v.findViewById(R.id.bfecha);
        bfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oks();
            }
        });
        bhora = (Button) v.findViewById(R.id.bhora);
        bhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okss();
            }
        });


        btnminute_s = (Button) v.findViewById(R.id.btnminute_s);
        btnminute_m = (Button) v.findViewById(R.id.btnminute_m);
        btnminute_l = (Button) v.findViewById(R.id.btnminute_l);

        txt_s = (TextView) v.findViewById(R.id.txt_s);
        txt_m = (TextView) v.findViewById(R.id.txt_m);
        txt_l = (TextView) v.findViewById(R.id.txt_l);

        btnplus_s = (Button) v.findViewById(R.id.btnplus_s);
        btnplus_m = (Button) v.findViewById(R.id.btnplus_m);
        btnplus_l = (Button) v.findViewById(R.id.btnplus_l);

        total_s = (TextView) v.findViewById(R.id.total_s);
        total_m = (TextView) v.findViewById(R.id.total_m);
        total_l = (TextView) v.findViewById(R.id.total_l);

        total_box = (TextView) v.findViewById(R.id.total_box);
        total = (TextView) v.findViewById(R.id.total);


        bin = (Button) v.findViewById(R.id.bin);
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_s.setText("1");
                txt_m.setText("0");
                txt_l.setText("0");
                total_s.setText("100฿");
                total_m.setText("0฿");
                total_l.setText("0฿");
                total_box.setText("1");
                total.setText("100฿");
            }
        });

        btnplus_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_btnplus_s();
            }
        });
        btnminute_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_btnpminute_s();

            }
        });

        btnplus_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_btnplus_m();
            }
        });
        btnminute_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_btnminute_m();

            }
        });

        btnminute_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_btnminute_l();

            }
        });
        btnplus_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_btnplus_l();

            }
        });


        imageView47 = (ImageView) v.findViewById(R.id.imageView47);
        imageView47.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image47();
            }
        });
        return v;
    }


    private void image47() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            imageView47.setImageURI(mImageUri);
            Toast.makeText(getActivity(), mImageUri.toString(), Toast.LENGTH_LONG).show();


        }
    }


    private void okss() {
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                bhora.setText(hourOfDay + ":" + minute);
                //Toast.makeText(getActivity(), mImageUri.toString(), Toast.LENGTH_LONG).show();

            }

        }, hora, minutos, false);
        timePickerDialog.show();
    }

    private void oks() {
        Calendar c = Calendar.getInstance();
        dia = 2017;
        mes = 1;
        ano = 1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                bfecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }
                , dia, mes, ano);
        datePickerDialog.show();
        //Toast.makeText(getContext(),a,Toast.LENGTH_LONG).show();

    }

    private void Onclick_btnplus_l() {
        if (Integer.parseInt(txt_l.getText().toString()) < 9) {

            String txt_s_String = String.valueOf(Integer.parseInt(txt_l.getText().toString()) + 1);
            txt_l.setText(txt_s_String);
            total_l.setText(String.valueOf(Integer.parseInt(total_l.getText().toString().substring(0, total_l.length() - 1)) + 300) + "฿");

            String total_box_String = String.valueOf(Integer.parseInt(total_box.getText().toString()) + 1);
            total_box.setText(total_box_String);

            total.setText(String.valueOf(Integer.parseInt(total.getText().toString().substring(0, total.length() - 1)) + 300) + "฿");


        }
    }

    private void Onclick_btnminute_l() {
        if (Integer.parseInt(txt_l.getText().toString()) > 0) {
            String txt_s_String = String.valueOf(Integer.parseInt(txt_l.getText().toString()) - 1);
            txt_l.setText(txt_s_String);
            total_l.setText(String.valueOf(Integer.parseInt(total_l.getText().toString().substring(0, total_l.length() - 1)) - 300) + "฿");


            String total_box_String = String.valueOf(Integer.parseInt(total_box.getText().toString()) - 1);
            total_box.setText(total_box_String);

            total.setText(String.valueOf(Integer.parseInt(total.getText().toString().substring(0, total.length() - 1)) - 300) + "฿");

        } else {

        }
    }

    private void Onclick_btnminute_m() {

        if (Integer.parseInt(txt_m.getText().toString()) > 0) {
            String txt_s_String = String.valueOf(Integer.parseInt(txt_m.getText().toString()) - 1);
            txt_m.setText(txt_s_String);
            total_m.setText(String.valueOf(Integer.parseInt(total_m.getText().toString().substring(0, total_m.length() - 1)) - 200) + "฿");


            String total_box_String = String.valueOf(Integer.parseInt(total_box.getText().toString()) - 1);
            total_box.setText(total_box_String);

            total.setText(String.valueOf(Integer.parseInt(total.getText().toString().substring(0, total.length() - 1)) - 200) + "฿");

        } else {

        }
    }

    private void Onclick_btnplus_m() {
        if (Integer.parseInt(txt_m.getText().toString()) < 9) {

            String txt_s_String = String.valueOf(Integer.parseInt(txt_m.getText().toString()) + 1);
            txt_m.setText(txt_s_String);
            total_m.setText(String.valueOf(Integer.parseInt(total_m.getText().toString().substring(0, total_m.length() - 1)) + 200) + "฿");

            String total_box_String = String.valueOf(Integer.parseInt(total_box.getText().toString()) + 1);
            total_box.setText(total_box_String);

            total.setText(String.valueOf(Integer.parseInt(total.getText().toString().substring(0, total.length() - 1)) + 200) + "฿");


        }
    }

    private void Onclick_btnpminute_s() {

        if (Integer.parseInt(txt_s.getText().toString()) > 0) {
            String txt_s_String = String.valueOf(Integer.parseInt(txt_s.getText().toString()) - 1);
            txt_s.setText(txt_s_String);
            total_s.setText(String.valueOf(Integer.parseInt(total_s.getText().toString().substring(0, total_s.length() - 1)) - 100) + "฿");


            String total_box_String = String.valueOf(Integer.parseInt(total_box.getText().toString()) - 1);
            total_box.setText(total_box_String);

            total.setText(String.valueOf(Integer.parseInt(total.getText().toString().substring(0, total.length() - 1)) - 100) + "฿");

        } else {

        }


    }

    private void Onclick_btnplus_s() {
        if (Integer.parseInt(txt_s.getText().toString()) < 9) {

            String txt_s_String = String.valueOf(Integer.parseInt(txt_s.getText().toString()) + 1);
            txt_s.setText(txt_s_String);
            total_s.setText(String.valueOf(Integer.parseInt(total_s.getText().toString().substring(0, total_s.length() - 1)) + 100) + "฿");

            String total_box_String = String.valueOf(Integer.parseInt(total_box.getText().toString()) + 1);
            total_box.setText(total_box_String);

            total.setText(String.valueOf(Integer.parseInt(total.getText().toString().substring(0, total.length() - 1)) + 100) + "฿");


        }
    }


}
