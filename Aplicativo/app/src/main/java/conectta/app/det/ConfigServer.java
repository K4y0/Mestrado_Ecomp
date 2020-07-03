package conectta.app.det;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class ConfigServer extends Activity {

    private Button btPlay;
    private EditText etNtp;
    private EditText etFog;
    private EditText etPort;
    private EditText etRoute;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        etNtp = findViewById(R.id.etNtp);
        etFog = findViewById(R.id.etFog);
        etPort = findViewById(R.id.etPort);
        etRoute = findViewById(R.id.etRoute);
        btPlay = findViewById(R.id.btPlay);



        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ConfigServer.this, MainActvity.class);
                it.putExtra("param", etNtp.getText().toString());
                it.putExtra("param1", etFog.getText().toString());
                it.putExtra("param2", etPort.getText().toString());
                it.putExtra("param3", etRoute.getText().toString());
                startActivity(it);
            }
        });
    }
}
