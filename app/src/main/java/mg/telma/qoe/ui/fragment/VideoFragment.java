package mg.telma.qoe.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import mg.telma.qoe.R;

public class VideoFragment extends Fragment implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = VideoFragment.class.getSimpleName();
    private static final String DEVELOPER_KEY = "AIzaSyBthnazm5XRmEj6jNCzyD8kLjGfWmvu_vc";
    private static final String VIDEO_ID = "fDuPxGGt6zo";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerFragment myYouTubePlayerFragment;
    private double startTime;
    private double bufferingTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_video, container, false);
        myYouTubePlayerFragment = (YouTubePlayerFragment) getActivity().getFragmentManager().findFragmentById(R.id.youtubeplayerfragment);
        myYouTubePlayerFragment.initialize(DEVELOPER_KEY, this);
        return view;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.loadVideo(VIDEO_ID);
            //youTubePlayer.play();
            this.startTime = System.currentTimeMillis();
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                    /*double loading = System.currentTimeMillis() - startTime ;
                    Toast.makeText(getActivity(), "loading: " + loading, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "loading: " + loading);*/
                }

                @Override
                public void onLoaded(String s) {
                    double loaded = System.currentTimeMillis() - startTime ;
                    Toast.makeText(getActivity(), "Loaded: " + loaded, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Loaded: " + loaded);
                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {
                    Toast.makeText(getActivity(), "Video Started", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onVideoEnded() {

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });

            youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                @Override
                public void onPlaying() {
                    Toast.makeText(getActivity(), "Playing", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onStopped() {

                }

                @Override
                public void onBuffering(boolean b) {
                    if(!b) {
                        double buffering = System.currentTimeMillis() - startTime ;
                        Toast.makeText(getActivity(), "Buffering: " + buffering, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Buffering: " + buffering);
                    }

                }

                @Override
                public void onSeekTo(int i) {
                    Toast.makeText(getActivity(), "Seeking", Toast.LENGTH_LONG).show();
                }
            });

        }


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer (%1$s)",
                    youTubeInitializationResult.toString());
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
        }
    }
    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView)getActivity().findViewById(R.id.youtubeplayerfragment);
    }
}
