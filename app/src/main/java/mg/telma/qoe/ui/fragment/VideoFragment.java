package mg.telma.qoe.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

import java.text.DecimalFormat;

import mg.telma.qoe.R;

public class VideoFragment extends Fragment{
    private static final String TAG = VideoFragment.class.getSimpleName();
    private static final String DEVELOPER_KEY = "AIzaSyBthnazm5XRmEj6jNCzyD8kLjGfWmvu_vc";
    private static final String VIDEO_ID = "F77dhX-YtDE";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private double startTime;
    private double bufferingTotal;
    final DecimalFormat dec = new DecimalFormat("#.##");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.getPlayerUIController().showUI(false);
        youTubePlayerView.getPlayerUIController().showVideoTitle(false);
        youTubePlayerView.getPlayerUIController().showYouTubeButton(false);
        youTubePlayerView.getPlayerUIController().showMenuButton(false);
        youTubePlayerView.getPlayerUIController().showCustomAction1(false);
        youTubePlayerView.getPlayerUIController().enableLiveVideoUI(false);
        youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady() {
                //initializedYouTubePlayer.loadVideo(VIDEO_ID, 0);
            }
            @Override
            public void onVideoLoadedFraction(float fraction) {
               /* Log.e(TAG, "Buffuring" + fraction);
                Toast.makeText(getActivity(),"Buffuring " + dec.format(fraction) + "%", Toast.LENGTH_SHORT).show();*/
            }
        }), true);
        return view;
    }



}
