package it.communikein.waveonthego;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OnlineMediaFragment extends android.support.v4.app.Fragment {

    private static final String ARG_MEDIA = "media";

    private StorageReference mediaRef;
    private ImageView mediaImage;

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

        String src = getArguments().getString(ARG_MEDIA);
        mediaRef = FirebaseStorage.getInstance().getReference(src);
    }

    public static OnlineMediaFragment newInstance(StorageReference media) {
        OnlineMediaFragment fragment = new OnlineMediaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MEDIA, media.getPath());
        fragment.setArguments(args);
        return fragment;
    }

    public OnlineMediaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.simple_image, container, false);
        mediaImage = (ImageView) rootView.findViewById(R.id.imageView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(mediaRef)
                .into(mediaImage);

        mediaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPictureDialog(getActivity());
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
                .using(new FirebaseImageLoader())
                .load(mediaRef)
                .into(imageView);

        //now that the dialog is set up, it's time to show it
        dialog.show();
    }
}