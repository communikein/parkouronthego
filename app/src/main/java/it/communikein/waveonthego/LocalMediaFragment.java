package it.communikein.waveonthego;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;

/**
 *
 * Created by Elia Maracani on 16/04/2017.
 */
public class LocalMediaFragment extends android.support.v4.app.Fragment {

    private static final String ARG_MEDIA = "media";
    private File media;

    private ImageView mediaImage;

    private OnMediaRemoved mCallBack;

    public interface OnMediaRemoved {
        void onMediaRemoved(File media);
    }

    private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            FirebaseCrash.report(ex);
            ex.getMessage();
        }
    };

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Thread.setDefaultUncaughtExceptionHandler(handler);

        if (getArguments() != null && getArguments().getString(ARG_MEDIA) != null)
            media = new File(getArguments().getString(ARG_MEDIA));

        try {
            mCallBack = (OnMediaRemoved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMediaRemoved");
        }
    }

    public static LocalMediaFragment newInstance(File media) {
        LocalMediaFragment fragment = new LocalMediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEDIA, media.getAbsolutePath());
        fragment.setArguments(args);
        return fragment;
    }

    public LocalMediaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.simple_image, container, false);
        mediaImage = (ImageView) rootView.findViewById(R.id.imageView);

        Glide.with(this)
                .load(media)
                .into(mediaImage);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mediaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPictureDialog(getActivity());
            }
        });

        view.findViewById(R.id.imageView).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.delete_media))
                        .setMessage(getString(R.string.confirm_remove_media))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mCallBack.onMediaRemoved(media);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });
    }

    private void callPictureDialog(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_picture_fullscreen);
        dialog.setCancelable(true);

        dialog.findViewById(R.id.imageFull).setVisibility(View.VISIBLE);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imageFull);

        Glide.with(this)
                .load(media)
                .into(imageView);

        //now that the dialog is set up, it's time to show it
        dialog.show();
    }
}