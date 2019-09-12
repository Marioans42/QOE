package mg.telma.qoe.ui.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.SampleBuffer;
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

    int startMs =10;
    int maxMs = 5000;
    private void test() throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream(1024);
        N=128;
        float totalMs = 0;
        double[] x = new double[N];
        boolean seeking = true;
        File file = new File(getFilesDir()+"/got.mp3");
        //File file1 = new File(getFilesDir()+"/got.mp3");
        System.out.println("the data "+ getFilesDir()+"/mozart.mp3");
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file), 8 * 128);
        Number [] tograph = new Number[N];
        int bufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        AudioTrack audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);

        try {
            Bitstream bitstream = new Bitstream(inputStream);
            Decoder decoder = new Decoder();

            boolean done = false;
            while (! done) {
                javazoom.jl.decoder.Header frameHeader = bitstream.readFrame();
                if (frameHeader == null) {
                    done = true;
                } else {
                    totalMs += frameHeader.ms_per_frame();


                    seeking = false;


                    if (! seeking) {
                        SampleBuffer output = (SampleBuffer) decoder.decodeFrame(frameHeader, bitstream);
                        if (output.getSampleFrequency() != 44100
                                || output.getChannelCount() != 2) {
                            // throw new com.mindtherobot.libs.mpg.DecoderException("mono or non-44100 MP3 not supported");
                        }

                        short[] pcm = output.getBuffer();
                        byte[] bs;
                        int index=0;
                        ByteBuffer buffer;
                        buffer = ByteBuffer.allocate(2*pcm.length);

                        for (short s : pcm) {
//                      outStream.write(s & 0xff);
//                      outStream.write((s >> 8 ) & 0xff);


                            buffer.putShort(s);



                        }

                        byte[] dataaudio = buffer.array();
                        //Complex[] complexData = new Complex[dataaudio.length];

                       /* for (int i = 0; i < complexData.length; i++) {
                            complexData[i] = new Complex(dataaudio[i], 0);
                        }*/

                        //Complex[] fftResult = mg.telma.qoe.utils.FFT.fft(complexData);
                        //return buffer.array();





                        audioTrack.write(dataaudio, 0, dataaudio.length);
                        // audioTrack.play();
                        for (k=0;k<N;k++){

                            //implement fft
                            for (j=0;j<N;j++){
                                xc = new Complex(dataaudio[j],0);
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


//clear existing graph
                        graph.clear();
                        //put data from tograph into a series to be added to the graph
                        gData = new SimpleXYSeries(Arrays.asList(tograph), SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "gData");
                        //add series with line and dot format specified earlier to graph
                        graph.addSeries(gData, gDataFormat);
                        graph.redraw();
                    }

                    if (totalMs >= (startMs + maxMs)) {
                        done = true;
                    }
                }
                bitstream.closeFrame();
            }

            //return outStream.toByteArray();
        } catch (BitstreamException e) {
            throw new IOException("Bitstream error: " + e);
        } catch (DecoderException e) {
            Log.w("data", "Decoder error", e);
            ;
        } finally {
            // IOUtils.safeClose(inputStream);
        }
        /*Plot plot = new Plot( "Spectral Flux", 1024, 512 );
        plot.plot( spectralFlux, 1, Color.red );
        new PlaybackVisualizer( plot, 1024, new MP3Decoder( new FileInputStream( FILE ) ) );*/

    }
}
