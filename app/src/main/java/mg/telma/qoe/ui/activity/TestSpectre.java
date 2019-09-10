package mg.telma.qoe.ui.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import analysis.FFT;
import io.MP3Decoder;
import mg.telma.qoe.R;
import mg.telma.qoe.utils.Complex;

public class TestSpectre extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //declare data types/objects to be used later in the program
    int k,j,N,fs,freq,xformchoice,inputchoice;
    double Xre=0;
    double Xim=0;
    private XYPlot graph;
    EditText editF,editFs,editN;
    Button buttonTime,buttonFFT;
    CheckBox checkBoxAUX;
    Spinner spinnerChoice, spinnerIpType;
    Button buttonGo;
    Complex Xc;
    Complex xc;
    Complex F;
    XYSeries gData;
    //set colour of lines and points
    LineAndPointFormatter gDataFormat;
    public static final String FILE = Uri.parse("android.resource://mg.telma.qoe/res/raw/mozart").toString();

    //onCreate is done on the creation of the class (when the app starts up)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        //set screen to be landscape only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //use activity_signal_analyser.xml for layout
        setContentView(R.layout.activity_test_spectre);
        System.out.println(" FILE DIR"+ getFilesDir().getAbsolutePath());
        File file = new File(getFilesDir().getAbsolutePath());
        for(String f : file.list()) {
            System.out.println(" FILE "+ f);
        }
        gDataFormat = new LineAndPointFormatter(Color.rgb(0, 200, 0), Color.rgb(0, 100, 0), null, null);
        //map objects to their counterparts in xml file
        graph = findViewById(R.id.graph);
        editF = findViewById(R.id.editF);
        editFs = findViewById(R.id.editFs);
        editN = findViewById(R.id.editN);
        spinnerChoice = findViewById(R.id.spinnerChoice);
        spinnerIpType = findViewById(R.id.spinnerIpType);
        buttonGo = findViewById(R.id.buttonGo);

        //create arroy adapters that will get their contents from the string.xml fle
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinnerchoice, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.spinnerIpType, android.R.layout.simple_spinner_item);

        //specify layout of spinner items
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set the spinners' adapters
        spinnerChoice.setAdapter(adapter);
        spinnerIpType.setAdapter(adapter1);

        //set the Listeners for the spinners and button
        spinnerChoice.setOnItemSelectedListener(this);
        spinnerIpType.setOnItemSelectedListener(this);
        buttonGo.setOnClickListener(this);
    }//end of oncreate class

    //class is called everytime a spinner choice is made
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
        //if the spinnerChoice spinner was used
        if (parent.getId()==R.id.spinnerChoice){
            xformchoice = pos;//store the choice's index in array to global variable
        }
        //if the spinnerIpType spinner was used
        else if (parent.getId()==R.id.spinnerIpType){
            inputchoice = pos;//store the choice's index in array to global variable
        }
    }//end onItemSelected class

    //since the main class implements OnItemSelectedListener this must be
    //included as it's one of OnItemSelectedListener's methods
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    //class is called everytime the button is pressed
    public void onClick(View v) {
        //if either the second or third choice in the spinnerChoice i.e. time or absfft
/*        if (xformchoice>0){

            //get fs from edittext box, if user hasnt ented anything set fs to 1000
            try{
                fs=Integer.parseInt(editFs.getText().toString());
            }catch (NumberFormatException e){
                fs=1000;
            }

            //get N from edittext box, if user hasnt ented anything set N to 1000
            try{
                N=Integer.parseInt(editN.getText().toString());
            }catch (NumberFormatException e){
                N=1000;
            }

            double[] x = new double[N];//will store time domain signal
            Number [] tograph = new Number[N];//will store the data to be graphed

            //if third choice in spinnerIpType i.e. AUX source
            if (inputchoice==2){
                //this will be for aux option - not implemented yet
                //just doubling amplitude for now...

                //get freq from edittext box, if user hasnt entered anything set freq to 0
                try{
                    freq=Integer.parseInt(editF.getText().toString());
                }catch (NumberFormatException e){
                    freq=0;
                }

                //create sine wave
                for (k=0;k<N;k++){
                    x[k] = 2*Math.sin(2*Math.PI*freq*k/fs);

                }
            }

            //otherwise... i.e. if device of the default is selected...
            else{

                //get freq from edittext box, if user hasnt entered anything set freq to 0
                try{
                    freq=Integer.parseInt(editF.getText().toString());
                }catch (NumberFormatException e){
                    freq=0;
                }

                //create sine wave
                for (k=0;k<N;k++){
                    x[k] = Math.sin(2*Math.PI*freq*k/fs);
                }
            }

            //if third choice in the spinnerChoice i.e. absfft
            if (xformchoice == 2){

                for (k=0;k<N;k++){

                    //implement fft
                    for (j=0;j<N;j++){
                        xc = new Complex(x[j],0);
                        F = new Complex(0,(2*Math.PI*(j-1)*(k-1))/N);
                        Xc =xc.times(F.exp());
                        Xre = Xre + Xc.re();
                        Xim = Xim + Xc.im();
                    }
                    //send result to tograph to be displayed later
                    tograph[k] = Math.sqrt(Math.pow(Xre,2)+Math.pow(Xim,2));
                    //reset Xim & Xre
                    Xim=0;
                    Xre=0;
                }

                //set graph title & labels
                graph.setTitle("Absolute FFT");
                graph.setRangeLabel("Amplitude");
                graph.setDomainLabel("Frequency (Hz*N/Fs)");
            }

            //otherwise... i.e. if time selected
            else{

                //send elements of time domain signal to tograph
                for(k=0;k<N;k++){
                    tograph[k]= x[k];
                }

                //set graph title & labels
                graph.setTitle("Time Domain");
                graph.setRangeLabel("Amplitude");
                graph.setDomainLabel("Time (Sec*N)");
            }

            //clear existing graph
            graph.clear();
            //put data from tograph into a series to be added to the graph
            gData = new SimpleXYSeries(Arrays.asList(tograph), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "gData");
            //add series with line and dot format specified earlier to graph
            graph.addSeries(gData, gDataFormat);
            graph.redraw();
        }*///end onclick class
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end main class

    private void test() throws Exception {

        MP3Decoder decoder = new MP3Decoder( new FileInputStream( getFilesDir()+"/mozart.mp3") );
        FFT fft = new FFT( 1024, 44100 );
        fft.window( FFT.HAMMING );
        float[] samples = new float[1024];
        float[] spectrum = new float[1024 / 2 + 1];
        float[] lastSpectrum = new float[1024 / 2 + 1];
        Number [] tograph = new Number[512];

        while( decoder.readSamples( samples ) > 0 )
        {
            fft.forward( samples );
            System.arraycopy( spectrum, 0, lastSpectrum, 0, spectrum.length );
            System.arraycopy( fft.getSpectrum(), 0, spectrum, 0, spectrum.length );

            float flux = 0;
            for( int i = 0; i < spectrum.length; i++ )
            {
                float value = (spectrum[i] - lastSpectrum[i]);
                tograph[i] = value < 0? 0: value;
            }
            //tograph.add( flux );
        }

        graph.clear();
        //put data from tograph into a series to be added to the graph
        gData = new SimpleXYSeries(Arrays.asList(tograph), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "gData");
        //add series with line and dot format specified earlier to graph
        graph.addSeries(gData, gDataFormat);
        graph.redraw();


        /*Plot plot = new Plot( "Spectral Flux", 1024, 512 );
        plot.plot( spectralFlux, 1, Color.red );
        new PlaybackVisualizer( plot, 1024, new MP3Decoder( new FileInputStream( FILE ) ) );*/

    }
}
