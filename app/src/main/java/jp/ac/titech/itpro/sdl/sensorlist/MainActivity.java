package jp.ac.titech.itpro.sdl.sensorlist;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private ArrayAdapter<Sensor> sensorListAdapter;
    private SensorManager sensorMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        ListView sensorListView = findViewById(R.id.sensor_list_view);
        sensorListAdapter = new ArrayAdapter<Sensor>(this, 0, new ArrayList<Sensor>()) {
            @Override
            public @NonNull
            View getView(int pos, @Nullable View view, @NonNull ViewGroup parent) {
                if (view == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
                }
                Sensor sensor = getItem(pos);
                if (sensor != null) {
                    TextView sensorNameText = view.findViewById(android.R.id.text1);
                    TextView sensorTypeText = view.findViewById(android.R.id.text2);
                    sensorNameText.setText(sensor.getName());
                    sensorTypeText.setText(sensorTypeName(sensor));
                }
                return view;
            }
        };
        sensorListView.setAdapter(sensorListAdapter);
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        sensorListAdapter.clear();
        for (Sensor sensor : sensorMgr.getSensorList(Sensor.TYPE_ALL))
            sensorListAdapter.add(sensor);
    }

    private String sensorTypeName(Sensor sensor) {
        try {
            Class klass = sensor.getClass();
            for (Field field : klass.getFields())
                if (field.getName().startsWith("TYPE_") && field.getInt(klass) == sensor.getType())
                    return field.getName();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
